package miniExpansion.patches;

import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.GameActionManager;
import com.megacrit.cardcrawl.cards.CardQueueItem;
import com.megacrit.cardcrawl.cards.status.Burn;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.events.AbstractEvent;
import com.megacrit.cardcrawl.events.shrines.Nloth;
import com.megacrit.cardcrawl.neow.NeowReward;
import com.megacrit.cardcrawl.random.Random;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import miniExpansion.patches.relics.RelicListUpdatePatch;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


public class DebugPatch {
    private static final Logger logger = LogManager.getLogger(DebugPatch.class.getName());

    // Generate certain relic for debugging

//    @SpirePatch(clz = AbstractDungeon.class, method = "returnRandomRelicKey")
//    public static class MyRelicDebugPatch {
//        public static String Postfix(String __result, AbstractRelic.RelicTier tier){
////            for (String s : AbstractDungeon.bossRelicPool) {
////                logger.info(s);
////            }
//            __result = "HornCleat";
//            return __result;
//        }
//    }

//    // Generate certain event room for debugging
//
//    @SpirePatch(clz = AbstractDungeon.class, method = "generateEvent")
//    public static class MyEventRoomDebugPatch {
//        public static AbstractEvent Postfix(AbstractEvent __result, Random rng) {
//            __result = new Nloth();
//            return __result;
//        }
//    }

//    @SpirePatch(clz = NeowReward.class, method = SpirePatch.CONSTRUCTOR, paramtypez = {boolean.class})
//    public static class MyNeowRewardDebugPatch {
//        public static void Postfix(NeowReward __instance, boolean firstMini) {
//        }
//    }
//
//    @SpirePatch(clz = CardCrawlGame.class, method = "loadPostCombat")
//    public static class MyExceptionPatch {
//        public static void Prefix(CardCrawlGame __instance) {
//            throw new RuntimeException("Stop here!");
//        }
//    }





    // Debugging end of turn burn auto use

//    @SpirePatch(clz = Burn.class, method = "triggerOnEndOfTurnForPlayingCard")
//    public static class MyBurnDebugPatch {
//        public static void Postfix(Burn __instance) {
//            logger.info("!!!");
//            logger.info("Burn on end of turn triggered!");
//        }
//    }

//    @SpirePatch(clz = GameActionManager.class, method = "getNextAction")
//    public static class MyActionDebugPatch {
//        public static void Prefix(GameActionManager __instance) {
//            if (!__instance.actions.isEmpty() || !__instance.cardQueue.isEmpty()) {
//                logger.info("----");
//                logger.info("Current game action status:");
//                for (AbstractGameAction a : __instance.actions) {
//                    if (a != null) {
//                        logger.info(a.getClass().getName());
//                    }
//                }
//                logger.info("Current card queue:");
//                for (CardQueueItem c : __instance.cardQueue) {
//                    if (c != null && c.card != null) {
//                        logger.info(c.card.name);
//                    }
//                }
//            }
//        }
//    }

}
