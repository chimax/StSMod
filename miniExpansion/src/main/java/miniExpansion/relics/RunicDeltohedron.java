package miniExpansion.relics;

import basemod.abstracts.CustomRelic;
import com.badlogic.gdx.graphics.Texture;
import com.megacrit.cardcrawl.actions.common.RelicAboveCreatureAction;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import miniExpansion.MiniExpansion;
import miniExpansion.actions.RunicDeltohedronAction;
import miniExpansion.util.TextureLoader;

import static miniExpansion.MiniExpansion.makeRelicOutlinePath;
import static miniExpansion.MiniExpansion.makeRelicPath;

public class RunicDeltohedron extends CustomRelic {
    /*
     * At the end of your turn, you may shuffle any number of cards into your draw pile. If you do so, draw #b1 additional cards next turn.
     */

    // ID, images, text.
    public static final String ID = MiniExpansion.makeID("RunicDeltohedron");

    private static final Texture IMG = TextureLoader.getTexture(makeRelicPath("Deltohedron.png"));
    private static final Texture OUTLINE = TextureLoader.getTexture(makeRelicOutlinePath("Deltohedron.png"));

    public RunicDeltohedron() { super(ID, IMG, OUTLINE, AbstractRelic.RelicTier.BOSS, AbstractRelic.LandingSound.HEAVY); }

    @Override
    public void onPlayerEndTurn() {
        this.flash();
        this.addToBot(new RelicAboveCreatureAction(AbstractDungeon.player, this));
        this.addToBot(new RunicDeltohedronAction(AbstractDungeon.player));
    }

    // Description
    @Override
    public String getUpdatedDescription() {
        return DESCRIPTIONS[0];
    }

}
