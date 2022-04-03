package miniExpansion.patches.relicsets;

import com.evacipated.cardcrawl.modthespire.lib.*;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.events.GenericEventDialog;
import com.megacrit.cardcrawl.events.shrines.Nloth;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import javassist.CtBehavior;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

public class RelicSetRemovalPreventionPatch {
    /*
     *  Prevent random relic removals to remove set rewards
     */

    // AbstractPlayer lose relic functions
    @SpirePatch(clz = AbstractPlayer.class, method = "loseRandomRelics")
    public static class MyLossRandomRelicsPatch {
        // Temporarily remove all set rewards before random number generates
        // Don't worry. They will be added back by postfix patch
        public static void Prefix(AbstractPlayer __p, int amount) {
            if (RelicSetManager.relicSets == null) { RelicSetManager.initManager(); }
            __p.relics.removeIf(r -> RelicSetManager.isSetReward(r.relicId));
        }
    }

    // Random relics for Nloth event to remove
    @SpirePatch(clz = Nloth.class, method = SpirePatch.CONSTRUCTOR)
    public static class MyNlothPatch {
        private static final Logger logger = LogManager.getLogger(MyNlothPatch.class.getName());

        @SpireInsertPatch(locator = Locator.class)
        public static void rerollRelicPatchMethod(Nloth __instance, @ByRef AbstractRelic[] ___choice1, @ByRef AbstractRelic[] ___choice2){
            if (RelicSetManager.relicSets == null) { RelicSetManager.initManager(); }
            ArrayList<AbstractRelic> relics = new ArrayList(AbstractDungeon.player.relics);
            Collections.shuffle(relics, new Random(AbstractDungeon.miscRng.randomLong()));
            int nextChoice = 2;
            if (RelicSetManager.isSetReward(___choice1[0])) {
                while (nextChoice < relics.size() && RelicSetManager.isSetReward(relics.get(nextChoice))) {
                    nextChoice++;
                }
                if (nextChoice >= relics.size()) {
                    logger.info("WHY ARE ALL RELICS SET REWARDS???");
                } else {
                    ___choice1[0] = relics.get(nextChoice);
                    nextChoice++;
                }
            }
            if (RelicSetManager.isSetReward(___choice2[0])) {
                while (nextChoice < relics.size() && RelicSetManager.isSetReward(relics.get(nextChoice))) {
                    nextChoice++;
                }
                if (nextChoice >= relics.size()) {
                    logger.info("WHY ARE ALL RELICS SET REWARDS???");
                } else {
                    ___choice2[0] = relics.get(nextChoice);
                }
            }
        }
    }

    private static class Locator extends SpireInsertLocator { // Hey welcome to our SpireInsertLocator class!
        @Override
        public int[] Locate(CtBehavior ctMethodToPatch) throws Exception {
            Matcher finalMatcher = new Matcher.MethodCallMatcher(GenericEventDialog.class, "setDialogOption");
            return LineFinder.findInOrder(ctMethodToPatch, finalMatcher);
        }
    }
}
