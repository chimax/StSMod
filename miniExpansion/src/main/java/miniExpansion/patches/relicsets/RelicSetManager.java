package miniExpansion.patches.relicsets;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.unlock.UnlockTracker;
import miniExpansion.relicsets.*;

import java.util.ArrayList;

public class RelicSetManager {
    public static ArrayList<AbstractRelicSet> relicSets;

    public static void initManager() {
        relicSets = new ArrayList<>();
        // Add relic sets
        relicSets.add(new ShipSet());
        relicSets.add(new NinjaSet());
        relicSets.add(new FishSet());
    }

    // Update relics to add and remove arraylists based on completeness
    public static boolean updateCollection() {
        boolean updated = false;
        AbstractPlayer p = AbstractDungeon.player;
        for (AbstractRelicSet s : relicSets) {
            if (s.setCollected() && !p.hasRelic(s.reward)) {
                ArrayList<AbstractRelic> relicListA = RelicListUpdatePatch.relics2add.get(p);
                relicListA.add(s.getReward());
                RelicListUpdatePatch.relics2add.set(p, relicListA);
                updated = true;
            }
            if (!s.setCollected() && p.hasRelic(s.reward)) {
                ArrayList<AbstractRelic> relicListR = RelicListUpdatePatch.relics2remove.get(p);
                for (AbstractRelic r : p.relics) {
                    if (r.relicId.equals(s.reward)) {
                        relicListR.add(r);
                        updated = true;
                    }
                }
                RelicListUpdatePatch.relics2remove.set(p, relicListR);
            }
        }
        return updated;
    }

    // Update player's relic list whenever relic is added or removed
    public static void updateRelicObtainLoss() {
        // Init
        AbstractPlayer p = AbstractDungeon.player;
        if (p == null) { return; }
        if (relicSets == null) { initManager(); }
        // Update collection
        if (!updateCollection()) { return; }
        // Add relics
        ArrayList<AbstractRelic> relicListA = RelicListUpdatePatch.relics2add.get(p);
        for (AbstractRelic r : relicListA) {
            relicSimpleObtain(r);
        }
        // Remove relics
        ArrayList<AbstractRelic> relicListR = RelicListUpdatePatch.relics2remove.get(p);
        for (AbstractRelic r : relicListR) {
            r.onUnequip();
        }
        p.relics.removeAll(relicListR);
        // Update relic list
        p.reorganizeRelics();
        if (AbstractDungeon.topPanel != null) {
            AbstractDungeon.topPanel.adjustRelicHbs();
        }
        // Call relic functions
        for (AbstractRelic r : relicListA) {
            r.onEquip();
            r.flash();
        }
        // Reset relics2add and relics2remove lists
        ArrayList<AbstractRelic> emptyListA = new ArrayList<>();
        ArrayList<AbstractRelic> emptyListR = new ArrayList<>();
        RelicListUpdatePatch.relics2add.set(p, emptyListA);
        RelicListUpdatePatch.relics2remove.set(p, emptyListR);
    }

    // Simple obtain logic that does not trigger relic list update
    public static void relicSimpleObtain(AbstractRelic relic) {
        if (relic.relicId.equals("Circlet") && AbstractDungeon.player.hasRelic("Circlet")) {
            AbstractRelic circlet = AbstractDungeon.player.getRelic("Circlet");
            ++circlet.counter;
            circlet.flash();
            relic.hb.hovered = false;
        } else {
            // Coordinates calculation
            float START_X = 64.0F * Settings.scale;
            float START_Y = Settings.isMobile ? (float)Settings.HEIGHT - 132.0F * Settings.scale : (float)Settings.HEIGHT - 102.0F * Settings.scale;
            float PAD_X = 72.0F * Settings.scale;
            // Display
            relic.hb.hovered = false;
            int row = AbstractDungeon.player.relics.size();
            relic.targetX = START_X + (float)row * PAD_X;
            relic.targetY = START_Y;
            // Add relic
            AbstractDungeon.player.relics.add(relic);
            relic.relicTip();
            UnlockTracker.markRelicAsSeen(relic.relicId);
        }
    }

    // Check if a relic is a set reward
    public static boolean isSetReward(String relicId) {
        if (relicSets == null) { initManager(); }
        for (AbstractRelicSet s : relicSets) {
            if (s.reward.equals(relicId)) {
                return true;
            }
        }
        return false;
    }

    // Check if a relic is a set reward
    public static boolean isSetReward(AbstractRelic relic) {
        if (relicSets == null) { initManager(); }
        for (AbstractRelicSet s : relicSets) {
            if (s.reward.equals(relic.relicId)) {
                return true;
            }
        }
        return false;
    }
}
