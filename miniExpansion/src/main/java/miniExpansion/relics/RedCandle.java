package miniExpansion.relics;

import basemod.abstracts.CustomRelic;
import com.badlogic.gdx.graphics.Texture;
import com.megacrit.cardcrawl.actions.common.RelicAboveCreatureAction;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import miniExpansion.MiniExpansion;
import miniExpansion.actions.RedCandleAction;
import miniExpansion.util.TextureLoader;

import static miniExpansion.MiniExpansion.makeRelicOutlinePath;
import static miniExpansion.MiniExpansion.makeRelicPath;

public class RedCandle extends CustomRelic {
    /*
     * At the end of your turn, you may shuffle any number of cards into your draw pile. If you do so, draw #b1 additional cards next turn.
     */

    // ID, images, text.
    public static final String ID = MiniExpansion.makeID("RedCandle");

    private static final Texture IMG = TextureLoader.getTexture(makeRelicPath("Heart.png")); // TODO
    private static final Texture OUTLINE = TextureLoader.getTexture(makeRelicOutlinePath("Heart.png")); // TODO

    public RedCandle() { super(ID, IMG, OUTLINE, RelicTier.UNCOMMON, AbstractRelic.LandingSound.MAGICAL); }

    // TODO
    @Override
    public String getUpdatedDescription() {
        return DESCRIPTIONS[0];
    }

    @Override
    public void atTurnStartPostDraw() {
        this.flash();
        this.addToBot(new RelicAboveCreatureAction(AbstractDungeon.player, this));
        this.addToBot(new RedCandleAction(AbstractDungeon.player, 1));
    }

}
