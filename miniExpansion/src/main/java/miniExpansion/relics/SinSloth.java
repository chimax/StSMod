package miniExpansion.relics;

import basemod.abstracts.CustomRelic;
import com.badlogic.gdx.graphics.Texture;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.actions.common.RelicAboveCreatureAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import miniExpansion.MiniExpansion;
import miniExpansion.util.TextureLoader;

import static miniExpansion.MiniExpansion.makeRelicOutlinePath;
import static miniExpansion.MiniExpansion.makeRelicPath;

public class SinSloth extends CustomRelic {

    /*
     *
     */

    // ID, images, text.
    public static final String ID = MiniExpansion.makeID("SinSloth");
    private static final Texture IMG = TextureLoader.getTexture(makeRelicPath("SinSloth.png"));
    private static final Texture OUTLINE = TextureLoader.getTexture(makeRelicOutlinePath("SinSloth.png"));

    private boolean overplayed = false;

    public SinSloth() { super(ID, IMG, OUTLINE, AbstractRelic.RelicTier.SPECIAL, AbstractRelic.LandingSound.MAGICAL); }

    // TODO
    // public String getUpdatedDescription() { return this.DESCRIPTIONS[0]; }

    @Override
    public void atBattleStart() {
        this.counter = 0;
        this.overplayed = false;
    }

    @Override
    public void atTurnStartPostDraw() {
        this.counter = 0;
        if (this.overplayed) {
            this.flash();
        } else {
            this.beginLongPulse();
        }
    }

    @Override
    public void onPlayCard(AbstractCard card, AbstractMonster m) {
        ++this.counter;
        if (this.counter > 6) {
            this.stopPulse();
        }
    }

    @Override
    public boolean canPlay(AbstractCard card) {
        if (this.overplayed) {
            card.cantUseMessage = ""; //TODO
            return false;
        } else {
            return true;
        }
    }

    @Override
    public void onPlayerEndTurn() {
        AbstractPlayer p = AbstractDungeon.player;
        if (this.overplayed) {
            this.overplayed = false;
        }
        if (this.counter > 6) {
            this.overplayed = true;
        }
        if (this.counter == 0) {
            this.flash();
            this.addToTop(new GainBlockAction(p, p, 30));
            this.addToTop(new RelicAboveCreatureAction(p, this));
        }
    }

    @Override
    public void onVictory() {
        this.counter = -1;
        this.stopPulse();
    }
}
