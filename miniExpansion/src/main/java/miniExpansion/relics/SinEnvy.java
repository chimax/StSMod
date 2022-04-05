package miniExpansion.relics;

import basemod.abstracts.CustomRelic;
import com.badlogic.gdx.graphics.Texture;
import com.megacrit.cardcrawl.actions.common.GainEnergyAction;
import com.megacrit.cardcrawl.actions.common.RelicAboveCreatureAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import miniExpansion.MiniExpansion;
import miniExpansion.util.TextureLoader;

import static miniExpansion.MiniExpansion.makeRelicOutlinePath;
import static miniExpansion.MiniExpansion.makeRelicPath;

public class SinEnvy extends CustomRelic {

    /*
     * At the start of your turn, gain 99 [E]. You cannot play cards with even number costs.
     */

    // ID, images, text.
    public static final String ID = MiniExpansion.makeID("SinEnvy");
    private static final Texture IMG = TextureLoader.getTexture(makeRelicPath("SinEnvy.png"));
    private static final Texture OUTLINE = TextureLoader.getTexture(makeRelicOutlinePath("SinEnvy.png"));

    public SinEnvy() { super(ID, IMG, OUTLINE, AbstractRelic.RelicTier.SPECIAL, AbstractRelic.LandingSound.MAGICAL); }


    public String getUpdatedDescription() { return this.DESCRIPTIONS[0] + 99 + this.DESCRIPTIONS[1]; }

    @Override
    public void atTurnStart() {
        this.flash();
        this.addToBot(new RelicAboveCreatureAction(AbstractDungeon.player, this));
        this.addToBot(new GainEnergyAction(99));
    }

    @Override
    public boolean canPlay(AbstractCard card) {
        if (card.costForTurn >= 0 && card.costForTurn % 2 == 0) {
            card.cantUseMessage = this.DESCRIPTIONS[2];
            return false;
        } else {
            return true;
        }
    }

}
