package miniExpansion.patches.saveAndContinue;

import com.evacipated.cardcrawl.modthespire.lib.*;
import com.evacipated.cardcrawl.modthespire.lib.SpireReturn;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.neow.NeowEvent;
import com.megacrit.cardcrawl.neow.NeowRoom;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import com.megacrit.cardcrawl.saveAndContinue.SaveFile;
import com.megacrit.cardcrawl.vfx.AbstractGameEffect;
import com.megacrit.cardcrawl.vfx.InfiniteSpeechBubble;
import miniExpansion.neow.AbstractNeowScreen;
import miniExpansion.neow.NeowScreenManager;
import miniExpansion.patches.neow.NeowEventPatch;

public class NeowEventSaveLoad {
    // Neow event saving logic
    public static Void save(Void unused) {
        int screenNum = 99;
        AbstractRoom currRoom = AbstractDungeon.currMapNode.room;
        if (currRoom instanceof NeowRoom) {
            screenNum = NeowEventPatch.screenNumPublic.get(currRoom.event);
        }
        SaveLoadManager.saveField("neow_screen_num", screenNum);
        return unused;
    }


    // Neow event loading logic
    // SpirePatch: Overwrite screen number and set up the right screen before actual Neow event happens
    @SpirePatch(clz = AbstractDungeon.class, method = "populatePathTaken")
    public static class MyPopulatePathTakenPatch {
        public static void Postfix(AbstractDungeon __instance, SaveFile saveFile) {
            if (AbstractDungeon.floorNum == 0 || saveFile.current_room.equals(NeowRoom.class.getName())){
                NeowEvent event = (NeowEvent)AbstractDungeon.currMapNode.room.event;
                int screenNum = (Integer) SaveLoadManager.loadField("neow_screen_num");
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
                // Init Neow screens - first time doing it after game starts so init without check
                NeowScreenManager.initManager();
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


}
