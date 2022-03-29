package miniExpansion.patches.rewards;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.evacipated.cardcrawl.modthespire.lib.*;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.rewards.RewardItem;
import com.megacrit.cardcrawl.rewards.RewardSave;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import com.megacrit.cardcrawl.saveAndContinue.SaveFile;
import javassist.CtBehavior;
import miniExpansion.patches.relics.RelicListUpdatePatch;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class RewardSavePatch {
    private static final Logger logger = LogManager.getLogger(RewardSavePatch.class.getName());

    // Save CUSTOM_GOLD
    @SpirePatch(clz = SaveFile.class, method = SpirePatch.CONSTRUCTOR, paramtypez = {SaveFile.SaveType.class})
    public static class MySaveFilePatch {
        @SpireInsertPatch(locator = RewardSavePatch.RewardSavePatchLocator.class)
        public static void MySaveFileInsert(SaveFile __instance, SaveFile.SaveType type) {
            for (RewardItem r : AbstractDungeon.getCurrRoom().rewards) {
                if (r.type == CustomGold.CUSTOM_GOLD) {
                    __instance.combat_rewards.add(new RewardSave(r.type.toString(), (String) null, r.goldAmt, r.bonusGold));
                }
            }
        }
    }

    private static class RewardSavePatchLocator extends SpireInsertLocator {
        @Override
        public int[] Locate(CtBehavior ctMethodToPatch) throws Exception {
            Matcher finalMatcher = new Matcher.FieldAccessMatcher(AbstractRoom.class, "rewards");
            return LineFinder.findInOrder(ctMethodToPatch, finalMatcher);
        }
    }

    // Load CUSTOM_GOLD
    @SpirePatch(clz = CardCrawlGame.class, method = "loadPostCombat")
    public static class MyLoadPostCombatPatch {
        public static void Prefix(CardCrawlGame __instance, SaveFile saveFile) {
            if (saveFile.post_combat) {
                for (RewardSave r : saveFile.combat_rewards) {
                    if (r.type.equals(CustomGold.CUSTOM_GOLD.toString())) {
                        RewardItem ri = new RewardItem(r.amount);
                        ri.type = CustomGold.CUSTOM_GOLD;
                        AbstractDungeon.getCurrRoom().rewards.add(ri);
                    }
                }
            }
        }
    }


    // Debug
//    @SpirePatch(clz = SaveFile.class, method = SpirePatch.CONSTRUCTOR, paramtypez = {SaveFile.SaveType.class})
//    public static class MySaveFilePatch2 {
//        public static void Postfix(SaveFile __instance, SaveFile.SaveType type) {
//            logger.info("--");
//            if (__instance.combat_rewards != null) {
//                for (RewardSave r : __instance.combat_rewards) {
//                    logger.info(r.type);
//                }
//            }
//        }
//    }
}
