package miniExpansion.patches.relics;

import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import miniExpansion.relics.SetOfShip;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;

public class SetOfShipPatch {
    private static final Logger logger = LogManager.getLogger(SetOfShipPatch.class.getName());

    @SpirePatch(clz = AbstractRelic.class, method = "onEquip")
    public static class EquipPatch {
        public static void Postfix(AbstractRelic __relic){
            AbstractPlayer p = AbstractDungeon.player;
            boolean toAdd = (!p.hasRelic("miniExpansion:SetOfShip")) && (
                    (__relic.relicId.equals("Anchor") && p.hasRelic("HornCleat") && p.hasRelic("CaptainsWheel")) ||
                    (__relic.relicId.equals("HornCleat") && p.hasRelic("Anchor") && p.hasRelic("CaptainsWheel")) ||
                    (__relic.relicId.equals("CaptainsWheel") && p.hasRelic("HornCleat") && p.hasRelic("Anchor")));
            if (toAdd) {
                logger.info("Adding set of ship");
                SetOfShip ship = new SetOfShip();
                ArrayList<AbstractRelic> relicListA = RelicListUpdatePatch.relics2add.get(p);
                relicListA.add(ship);
                RelicListUpdatePatch.relics2add.set(p, relicListA);
            }

        }
    }

    @SpirePatch(clz = AbstractRelic.class, method = "onUnequip")
    public static class UnequipPatch {
        public static void Postfix(AbstractRelic __relic) {
            AbstractPlayer p = AbstractDungeon.player;
            boolean toRemove = !__relic.relicId.equals("miniExpansion:SetOfShip") && p.hasRelic("miniExpansion:SetOfShip") &&
                    (__relic.relicId.equals("Anchor") || __relic.relicId.equals("HornCleat") ||  __relic.relicId.equals("CaptainsWheel"));

            if (toRemove) {
                logger.info("Removing set of ship");
                //p.loseRelic("miniExpansion:SetOfShip");
                ArrayList<AbstractRelic> relicListR = RelicListUpdatePatch.relics2remove.get(p);
                for (AbstractRelic r : p.relics) {
                    if (r.relicId.equals("miniExpansion:SetOfShip")) {
                        relicListR.add(r);
                    }
                }
                RelicListUpdatePatch.relics2remove.set(p, relicListR);
            }
        }
    }
}
