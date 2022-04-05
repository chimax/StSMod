package miniExpansion.relics;

import basemod.abstracts.CustomRelic;
import com.badlogic.gdx.graphics.Texture;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.powers.NoDrawPower;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import miniExpansion.MiniExpansion;
import miniExpansion.powers.NegativeSlowPower;
import miniExpansion.util.TextureLoader;

import static miniExpansion.MiniExpansion.makeRelicOutlinePath;
import static miniExpansion.MiniExpansion.makeRelicPath;

public class SinPride extends CustomRelic {

    /*
     * At the start of your turn, gain 7 Slow. You lose 1 Slow whenever you play a card (minimum at -5 Slow).
     */

    // ID, images, text.
    public static final String ID = MiniExpansion.makeID("SinPride");
    private static final Texture IMG = TextureLoader.getTexture(makeRelicPath("SinPride.png"));
    private static final Texture OUTLINE = TextureLoader.getTexture(makeRelicOutlinePath("SinPride.png"));

    public SinPride() { super(ID, IMG, OUTLINE, AbstractRelic.RelicTier.SPECIAL, AbstractRelic.LandingSound.MAGICAL); }


    public String getUpdatedDescription() { return this.DESCRIPTIONS[0] + 7 + this.DESCRIPTIONS[1]; }

    @Override
    public void atTurnStart() {
        flash();
        AbstractPlayer p = AbstractDungeon.player;
        this.addToBot(new ApplyPowerAction(p, p, new NegativeSlowPower(p, 7), 7));
    }
}
