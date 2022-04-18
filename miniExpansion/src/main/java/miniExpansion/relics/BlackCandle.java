package miniExpansion.relics;

import basemod.abstracts.CustomRelic;
import com.badlogic.gdx.graphics.Texture;
import com.megacrit.cardcrawl.actions.common.RelicAboveCreatureAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import miniExpansion.MiniExpansion;
import miniExpansion.actions.BlackCandleAction;
import miniExpansion.actions.RunicDeltohedronAction;
import miniExpansion.util.TextureLoader;

import static miniExpansion.MiniExpansion.makeRelicOutlinePath;
import static miniExpansion.MiniExpansion.makeRelicPath;

public class BlackCandle extends CustomRelic {
    /*
     * At the end of your turn, you may shuffle any number of cards into your draw pile. If you do so, draw #b1 additional cards next turn.
     */

    // ID, images, text.
    public static final String ID = MiniExpansion.makeID("BlackCandle");

    private static final Texture IMG = TextureLoader.getTexture(makeRelicPath("Heart.png")); // TODO
    private static final Texture OUTLINE = TextureLoader.getTexture(makeRelicOutlinePath("Heart.png")); // TODO

    public BlackCandle() { super(ID, IMG, OUTLINE, RelicTier.UNCOMMON, AbstractRelic.LandingSound.MAGICAL); }

    @Override
    public String getUpdatedDescription() {
        return DESCRIPTIONS[0] + 3 + DESCRIPTIONS[1];
    }

    @Override
    public void atBattleStart() {
        this.grayscale = false;
        this.counter = 0;
    }

    @Override
    public void onPlayerEndTurn() {
        if (this.counter < 3 && !AbstractDungeon.player.hand.isEmpty()) {
            this.flash();
            this.addToBot(new RelicAboveCreatureAction(AbstractDungeon.player, this));
            this.addToBot(new BlackCandleAction(AbstractDungeon.player));
        }
    }

    @Override
    public void onVictory() {
        this.grayscale = false;
        this.counter = -1;
    }
}
