package miniExpansion.patches.relicsets;

import com.evacipated.cardcrawl.modthespire.lib.SpireField;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;


@SpirePatch(clz = AbstractPlayer.class, method = SpirePatch.CLASS)
public class RelicListUpdatePatch {
    /*
     *  Add two fields "relics2add" and "relics2remove" to AbstractPlayer and
     *  add extra logics to relic obtaining and loss
     *  to enable more flexible change of player's relic list
     */
    public static SpireField<ArrayList<AbstractRelic>> relics2add = new SpireField<>(() -> new ArrayList());
    public static SpireField<ArrayList<AbstractRelic>> relics2remove = new SpireField<>(() -> new ArrayList());
    private static final Logger logger = LogManager.getLogger(RelicListUpdatePatch.class.getName());

    // AbstractPlayer lose relic functions
    @SpirePatch(clz = AbstractPlayer.class, method = "loseRandomRelics")
    public static class MyLossRandomRelicsPatch {
        public static void Postfix(AbstractPlayer __p, int amount) {
            RelicSetManager.updateRelicObtainLoss();
        }
    }

    @SpirePatch(clz = AbstractPlayer.class, method = "loseRelic")
    public static class MyLossRelicPatch {
        public static void Postfix(AbstractPlayer __p, String targetID) {
            RelicSetManager.updateRelicObtainLoss();
        }
    }

    // AbstractRelic obtain relic functions
    @SpirePatch(clz = AbstractRelic.class, method = "instantObtain", paramtypez = {AbstractPlayer.class, int.class, boolean.class})
    public static class MyInstantObtainPatch {
        public static void Postfix(AbstractRelic __relic, AbstractPlayer p, int slot, boolean callOnEquip) {
            RelicSetManager.updateRelicObtainLoss();
        }
    }

    @SpirePatch(clz = AbstractRelic.class, method = "instantObtain", paramtypez = {})
    public static class MyInstantObtainPatch2 {
        public static void Postfix(AbstractRelic __relic) {
            RelicSetManager.updateRelicObtainLoss();
        }
    }

    @SpirePatch(clz = AbstractRelic.class, method = "obtain")
    public static class MyObtainPatch {
        public static void Postfix(AbstractRelic __relic) {
            RelicSetManager.updateRelicObtainLoss();
        }
    }

    @SpirePatch(clz = AbstractRelic.class, method = "bossObtainLogic")
    public static class MyBossObtainLogicPatch {
        public static void Postfix(AbstractRelic __relic) {
            RelicSetManager.updateRelicObtainLoss();
        }
    }


    // AbstractRoom obtain relic functions
    @SpirePatch(clz = AbstractRoom.class, method = "spawnRelicAndObtain")
    public static class MySpawnRelicAndObtainPatch {
        public static void Postfix(AbstractRoom __room, float x, float y, AbstractRelic relic) {
            RelicSetManager.updateRelicObtainLoss();
        }
    }

}

