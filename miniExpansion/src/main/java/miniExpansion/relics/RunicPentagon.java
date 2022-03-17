package miniExpansion.relics;

import basemod.abstracts.CustomRelic;
import com.badlogic.gdx.graphics.Texture;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import miniExpansion.MiniExpansion;
import miniExpansion.actions.RunicPentagonAction;
import miniExpansion.util.TextureLoader;

import static miniExpansion.MiniExpansion.makeRelicOutlinePath;
import static miniExpansion.MiniExpansion.makeRelicPath;

public class RunicPentagon extends CustomRelic {
    /*
     * At the start of your turn, draw #b1 additional cards. At the end of your turn, you may shuffle any number of cards into your draw pile.
     */

    // ID, images, text.
    public static final String ID = MiniExpansion.makeID("RunicPentagon");

    private static final Texture IMG = TextureLoader.getTexture(makeRelicPath("Heart.png"));
    private static final Texture OUTLINE = TextureLoader.getTexture(makeRelicOutlinePath("Heart.png"));

    public RunicPentagon() {
        super(ID, IMG, OUTLINE, AbstractRelic.RelicTier.BOSS, AbstractRelic.LandingSound.HEAVY);
    }

//    // Gain 1 energy on equip.
//    @Override
//    public void onEquip() {
//        ++AbstractDungeon.player.masterHandSize;
//    }
//
//    // Lose 1 energy on unequip.
//    @Override
//    public void onUnequip() {
//        --AbstractDungeon.player.masterHandSize;
//    }

    @Override
    public void onPlayerEndTurn() {
        flash();
        AbstractDungeon.actionManager.addToBottom(new RunicPentagonAction());
    }

    // Description
    @Override
    public String getUpdatedDescription() {
        return DESCRIPTIONS[0];
    }

}
