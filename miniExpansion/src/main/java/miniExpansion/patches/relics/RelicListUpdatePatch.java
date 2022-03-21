package miniExpansion.patches.relics;

import com.evacipated.cardcrawl.modthespire.lib.SpireField;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
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


    @SpirePatch(clz = AbstractPlayer.class, method = "loseRandomRelics")
    public static class MyLossRandomRelicsPatch {
        public static void Postfix(AbstractPlayer __p, int amount) {
            // Add relics
            ArrayList<AbstractRelic> relicListA = RelicListUpdatePatch.relics2add.get(__p);
            for (AbstractRelic r : relicListA) {
                r.obtain();
            }
            // Remove relics
            ArrayList<AbstractRelic> relicListR = RelicListUpdatePatch.relics2remove.get(__p);
            for (AbstractRelic r : relicListR) {
                r.onUnequip();
            }
            __p.relics.removeAll(relicListR);
            // Update relic list
            __p.reorganizeRelics();
            for (AbstractRelic r : relicListA) {
                r.onEquip();
                r.flash();
            }
            if (AbstractDungeon.topPanel != null) {
                AbstractDungeon.topPanel.adjustRelicHbs();
            }
            // Reset relics2add and relics2remove lists
            ArrayList<AbstractRelic> emptyListA = new ArrayList<>();
            ArrayList<AbstractRelic> emptyListR = new ArrayList<AbstractRelic>();
            RelicListUpdatePatch.relics2add.set(__p, emptyListA);
            RelicListUpdatePatch.relics2remove.set(__p, emptyListR);
        }
    }

    @SpirePatch(clz = AbstractPlayer.class, method = "loseRelic")
    public static class MyLossRelicPatch {
        public static void Postfix(AbstractPlayer __p, String targetID) {
            // Add relics
            ArrayList<AbstractRelic> relicListA = RelicListUpdatePatch.relics2add.get(__p);
            for (AbstractRelic r : relicListA) {
                r.obtain();
            }
            // Remove relics
            ArrayList<AbstractRelic> relicListR = RelicListUpdatePatch.relics2remove.get(__p);
            for (AbstractRelic r : relicListR) {
                r.onUnequip();
            }
            __p.relics.removeAll(relicListR);
            // Update relic list
            __p.reorganizeRelics();
            for (AbstractRelic r : relicListA) {
                r.onEquip();
                r.flash();
            }
            if (AbstractDungeon.topPanel != null) {
                AbstractDungeon.topPanel.adjustRelicHbs();
            }
            // Reset relics2add and relics2remove lists
            ArrayList<AbstractRelic> emptyListA = new ArrayList<AbstractRelic>();
            ArrayList<AbstractRelic> emptyListR = new ArrayList<AbstractRelic>();
            RelicListUpdatePatch.relics2add.set(__p, emptyListA);
            RelicListUpdatePatch.relics2remove.set(__p, emptyListR);
        }
    }


    @SpirePatch(clz = AbstractRelic.class, method = "instantObtain", paramtypez = {AbstractPlayer.class, int.class, boolean.class})
    public static class MyInstantObtainPatch {
        public static void Postfix(AbstractRelic __relic, AbstractPlayer p, int slot, boolean callOnEquip) {
            // Add relics
            ArrayList<AbstractRelic> relicListA = RelicListUpdatePatch.relics2add.get(p);
            for (AbstractRelic r : relicListA) {
                r.obtain();
            }
            // Remove relics
            ArrayList<AbstractRelic> relicListR = RelicListUpdatePatch.relics2remove.get(p);
            for (AbstractRelic r : relicListR) {
                r.onUnequip();
            }
            p.relics.removeAll(relicListR);
            // Update relic list
            p.reorganizeRelics();
            for (AbstractRelic r : relicListA) {
                r.onEquip();
                r.flash();
            }
            if (AbstractDungeon.topPanel != null) {
                AbstractDungeon.topPanel.adjustRelicHbs();
            }
            // Reset relics2add and relics2remove lists
            ArrayList<AbstractRelic> emptyListA = new ArrayList<AbstractRelic>();
            ArrayList<AbstractRelic> emptyListR = new ArrayList<AbstractRelic>();
            RelicListUpdatePatch.relics2add.set(p, emptyListA);
            RelicListUpdatePatch.relics2remove.set(p, emptyListR);
        }
    }

    @SpirePatch(clz = AbstractRelic.class, method = "instantObtain", paramtypez = {})
    public static class MyInstantObtainPatch2 {
        public static void Postfix(AbstractRelic __relic) {
            AbstractPlayer p = AbstractDungeon.player;
            // Add relics
            ArrayList<AbstractRelic> relicListA = RelicListUpdatePatch.relics2add.get(p);
            for (AbstractRelic r : relicListA) {
                r.obtain();
            }
            // Remove relics
            ArrayList<AbstractRelic> relicListR = RelicListUpdatePatch.relics2remove.get(p);
            for (AbstractRelic r : relicListR) {
                r.onUnequip();
            }
            p.relics.removeAll(relicListR);
            // Update relic list
            p.reorganizeRelics();
            for (AbstractRelic r : relicListA) {
                r.onEquip();
                r.flash();
            }
            if (AbstractDungeon.topPanel != null) {
                AbstractDungeon.topPanel.adjustRelicHbs();
            }
            // Reset relics2add and relics2remove lists
            ArrayList<AbstractRelic> emptyListA = new ArrayList<AbstractRelic>();
            ArrayList<AbstractRelic> emptyListR = new ArrayList<AbstractRelic>();
            RelicListUpdatePatch.relics2add.set(p, emptyListA);
            RelicListUpdatePatch.relics2remove.set(p, emptyListR);
        }
    }

    @SpirePatch(clz = AbstractRelic.class, method = "bossObtainLogic")
    public static class MyBossObtainLogicPatch {
        public static void Postfix(AbstractRelic __relic) {
            __relic.onEquip();
            AbstractPlayer p = AbstractDungeon.player;
            // Add relics
            ArrayList<AbstractRelic> relicListA = RelicListUpdatePatch.relics2add.get(p);
            for (AbstractRelic r : relicListA) {
                r.obtain();
            }
            // Remove relics
            ArrayList<AbstractRelic> relicListR = RelicListUpdatePatch.relics2remove.get(p);
            for (AbstractRelic r : relicListR) {
                r.onUnequip();
            }
            p.relics.removeAll(relicListR);
            // Update relic list
            p.reorganizeRelics();
            for (AbstractRelic r : relicListA) {
                r.onEquip();
                r.flash();
            }
            if (AbstractDungeon.topPanel != null) {
                AbstractDungeon.topPanel.adjustRelicHbs();
            }
            // Reset relics2add and relics2remove lists
            ArrayList<AbstractRelic> emptyListA = new ArrayList<AbstractRelic>();
            ArrayList<AbstractRelic> emptyListR = new ArrayList<AbstractRelic>();
            RelicListUpdatePatch.relics2add.set(p, emptyListA);
            RelicListUpdatePatch.relics2remove.set(p, emptyListR);
        }
    }


    @SpirePatch(clz = AbstractRoom.class, method = "spawnRelicAndObtain")
    public static class MySpawnRelicAndObtainPatch {
        public static void Postfix(AbstractRoom __room, float x, float y, AbstractRelic relic) {
            AbstractPlayer p = AbstractDungeon.player;
            // Add relics
            ArrayList<AbstractRelic> relicListA = RelicListUpdatePatch.relics2add.get(p);
            for (AbstractRelic r : relicListA) {
                r.obtain();
            }
            // Remove relics
            ArrayList<AbstractRelic> relicListR = RelicListUpdatePatch.relics2remove.get(p);
            for (AbstractRelic r : relicListR) {
                r.onUnequip();
            }
            p.relics.removeAll(relicListR);
            // Update relic list
            p.reorganizeRelics();
            for (AbstractRelic r : relicListA) {
                r.onEquip();
                r.flash();
            }
            if (AbstractDungeon.topPanel != null) {
                AbstractDungeon.topPanel.adjustRelicHbs();
            }
            // Reset relics2add and relics2remove lists
            ArrayList<AbstractRelic> emptyListA = new ArrayList<AbstractRelic>();
            ArrayList<AbstractRelic> emptyListR = new ArrayList<AbstractRelic>();
            RelicListUpdatePatch.relics2add.set(p, emptyListA);
            RelicListUpdatePatch.relics2remove.set(p, emptyListR);
        }
    }
}

