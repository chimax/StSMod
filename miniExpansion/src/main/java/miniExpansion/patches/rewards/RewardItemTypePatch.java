package miniExpansion.patches.rewards;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.evacipated.cardcrawl.modthespire.lib.*;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.rewards.RewardItem;
import javassist.CtBehavior;

public class RewardItemTypePatch {
    // Handle CUSTOM_GOLD claim reward logic
    @SpirePatch(clz = RewardItem.class, method = "claimReward")
    public static class MyClaimRewardPatch {
        public static SpireReturn<Boolean> Prefix(RewardItem __instance) {
            if (__instance.type == CustomGold.CUSTOM_GOLD) {
                CardCrawlGame.sound.play("GOLD_GAIN");
                if (__instance.bonusGold == 0) {
                    AbstractDungeon.player.gainGold(__instance.goldAmt);
                } else {
                    AbstractDungeon.player.gainGold(__instance.goldAmt + __instance.bonusGold);
                }
                return SpireReturn.Return(true);
            }
            return SpireReturn.Continue();
        }
    }

    // Handle CUSTOM_GOLD image rendering
    @SpirePatch(clz = RewardItem.class, method = "render")
    public static class MyRenderPatch {
        @SpireInsertPatch(locator = RewardItemTypePatchLocator.class)
        public static void MyRenderInsert(RewardItem __instance, SpriteBatch sb) {
            if (__instance.type == CustomGold.CUSTOM_GOLD) {
                sb.draw(ImageMaster.UI_GOLD, RewardItem.REWARD_ITEM_X - 32.0F, __instance.y - 32.0F - 2.0F * Settings.scale, 32.0F, 32.0F, 64.0F, 64.0F, Settings.scale, Settings.scale, 0.0F, 0, 0, 64, 64, false, false);
            }
        }
    }

    private static class RewardItemTypePatchLocator extends SpireInsertLocator {
        @Override
        public int[] Locate(CtBehavior ctMethodToPatch) throws Exception {
            Matcher finalMatcher = new Matcher.FieldAccessMatcher(RewardItem.class, "type");
            return LineFinder.findInOrder(ctMethodToPatch, finalMatcher);
        }
    }
}
