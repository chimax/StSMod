package miniExpansion.patches.neow;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.evacipated.cardcrawl.modthespire.lib.*;
import com.google.gson.Gson;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.neow.NeowEvent;
import com.megacrit.cardcrawl.neow.NeowRoom;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import com.megacrit.cardcrawl.saveAndContinue.SaveAndContinue;
import com.megacrit.cardcrawl.saveAndContinue.SaveFile;
import com.megacrit.cardcrawl.saveAndContinue.SaveFileObfuscator;
import javassist.CtBehavior;
import miniExpansion.neow.AbstractNeowScreen;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.regex.Pattern;

@SpirePatch(clz = SaveFile.class, method = SpirePatch.CLASS)
public class NeowSavePatch {
    private static final Logger logger = LogManager.getLogger(NeowSavePatch.class.getName());

    public static SpireField<Integer> neow_screen_num = new SpireField<>(() -> 99);

    // Save Neow screen number to SaveFile
    @SpirePatch(clz = SaveFile.class, method = SpirePatch.CONSTRUCTOR, paramtypez = {SaveFile.SaveType.class})
    public static class MySaveFileConstructorPatch {
        public static void Postfix(SaveFile __instance, SaveFile.SaveType type) {
            int screenNum = 99;
            AbstractRoom currRoom = AbstractDungeon.currMapNode.room;
            if (currRoom instanceof NeowRoom) {
                screenNum = NeowEventPatch.screenNumPublic.get(currRoom.event);
            }
            NeowSavePatch.neow_screen_num.set(__instance, screenNum);
        }
    }

    // Write Neow screen number from SaveFile to params to put into json
    @SpirePatch(clz = SaveAndContinue.class, method = "save")
    public static class MySavePatch {
        @SpireInsertPatch(locator = NeowSavePatch.GameSaverLocator.class, localvars = {"params"})
        public static void MySaveParamsPutNeowScreenNum(SaveFile save, @ByRef HashMap<Object, Object>[] params) {
            params[0].put("neow_screen_num", NeowSavePatch.neow_screen_num.get(save));
        }
    }

    private static class GameSaverLocator extends SpireInsertLocator {
        @Override
        public int[] Locate(CtBehavior ctMethodToPatch) throws Exception {
            Matcher finalMatcher = new Matcher.FieldAccessMatcher(SaveFile.class, "metric_campfire_rested");
            return LineFinder.findInOrder(ctMethodToPatch, finalMatcher);
        }
    }

}
