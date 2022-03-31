package miniExpansion.patches.neow;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.evacipated.cardcrawl.modthespire.lib.ByRef;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpireReturn;
import com.google.gson.Gson;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.neow.NeowEvent;
import com.megacrit.cardcrawl.neow.NeowRoom;
import com.megacrit.cardcrawl.saveAndContinue.SaveAndContinue;
import com.megacrit.cardcrawl.saveAndContinue.SaveFile;
import com.megacrit.cardcrawl.saveAndContinue.SaveFileObfuscator;
import com.megacrit.cardcrawl.vfx.AbstractGameEffect;
import com.megacrit.cardcrawl.vfx.InfiniteSpeechBubble;
import miniExpansion.neow.AbstractNeowScreen;
import miniExpansion.neow.NeowScreenManager;

import java.util.ArrayList;
import java.util.regex.Pattern;

public class NeowLoadPatch {

    // Load Neow screen number when load from SaveFile
    @SpirePatch(clz = SaveAndContinue.class, method = "loadSaveFile", paramtypez = {String.class})
    public static class MyLoadSaveFilePatch {
        public static SpireReturn<SaveFile> Prefix(String filePath) {
            SaveFile saveFile;
            String savestr;
            Gson gson = new Gson();
            try {
                FileHandle file = Gdx.files.local(filePath);
                String data = file.readString();
                savestr = SaveFileObfuscator.isObfuscated(data) ? SaveFileObfuscator.decode(data, "key") : data;
                saveFile = (SaveFile)gson.fromJson(savestr, SaveFile.class);
            } catch (Exception tmp) {
                return SpireReturn.Continue();
            }
            int neow_screen_num = loadInt(savestr, "neow_screen_num");
            NeowSavePatch.neow_screen_num.set(saveFile, neow_screen_num);
            return SpireReturn.Return(saveFile);
        }
    }

    @SpirePatch(clz = AbstractDungeon.class, method = "populatePathTaken")
    public static class MyPopulatePathTakenPatch {
        public static void Postfix(AbstractDungeon __instance, SaveFile saveFile) {
            if (AbstractDungeon.floorNum == 0 || saveFile.current_room.equals(NeowRoom.class.getName())){
                NeowEvent event = (NeowEvent)AbstractDungeon.currMapNode.room.event;
                int screenNum = NeowSavePatch.neow_screen_num.get(saveFile);
                NeowEventPatch.screenNumPublic.set(event, screenNum);
                event.logMetric(null);
            }
        }
    }

    @SpirePatch(clz = NeowEvent.class, method = "logMetric")
    public static class MyLogMetricPatch {
        private static final float DIALOG_X = 1100.0F * Settings.xScale;
        private static final float DIALOG_Y = AbstractDungeon.floorY + 60.0F * Settings.yScale;

        public static SpireReturn<Void> Prefix(NeowEvent __instance, String actionTaken, @ByRef int[] ___screenNum) {
            if (actionTaken == null) {
                // Load current Neow screen number
                ___screenNum[0] = NeowEventPatch.screenNumPublic.get(__instance);
                // The rest is similar to loading the first screen in list
                // Prepare Neow screens
                if (NeowScreenManager.customScreens == null) { NeowScreenManager.initManager(); }
                // Set custom screen
                AbstractNeowScreen activeScreen = NeowScreenManager.getScreen(___screenNum[0]);
                if (activeScreen == null) { return SpireReturn.Return(); }
                activeScreen.drawRewards();
                if (activeScreen.rewards.size() == 0) { return SpireReturn.Return(); }
                // Dismiss bubble and previous options
                for (AbstractGameEffect e : AbstractDungeon.effectList) {
                    if (e instanceof InfiniteSpeechBubble) {
                        ((InfiniteSpeechBubble) e).dismiss();
                    }
                }
                __instance.roomEventText.clearRemainingOptions();
                // Set speech
                AbstractDungeon.effectList.add(new InfiniteSpeechBubble(DIALOG_X, DIALOG_Y, activeScreen.getSpeech()));
                // Set options
                NeowEventPatch.neowRewards.set(__instance, activeScreen.rewards);
                __instance.roomEventText.updateDialogOption(0, activeScreen.rewards.get(0).optionLabel);
                for (int i = 1; i < activeScreen.rewards.size(); i++) {
                    __instance.roomEventText.addDialogOption(activeScreen.rewards.get(i).optionLabel);
                }
                return SpireReturn.Return();
            }
            return SpireReturn.Continue();
        }
    }


    // TODO: Create a helper class for JSON managing
    // Loading fields from json formatted String
    public static String loadStr(String str, String field) {
        Pattern p = Pattern.compile("\"" + field + "\": [a-zA-Z0-9]*,");
        java.util.regex.Matcher m = p.matcher(str);
        String regexResult;
        if (m.find()) {
            regexResult = m.group();
        } else {
            return null;
        }
        return regexResult.substring(regexResult.indexOf(" ")+1, regexResult.length()-1);
    }

    public static int loadInt(String str, String field) {
        String regexResult = loadStr(str, field);
        if (regexResult == null) { return -1; }
        return Integer.parseInt(regexResult);
    }

}
