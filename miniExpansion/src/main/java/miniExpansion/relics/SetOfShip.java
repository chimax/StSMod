package miniExpansion.relics;

import basemod.abstracts.CustomRelic;
import com.badlogic.gdx.graphics.Texture;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.RelicAboveCreatureAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.powers.MetallicizePower;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import miniExpansion.MiniExpansion;
import miniExpansion.util.TextureLoader;

import static miniExpansion.MiniExpansion.makeRelicOutlinePath;
import static miniExpansion.MiniExpansion.makeRelicPath;

public class SetOfShip extends CustomRelic {
    /*
     *
     */

    // ID, images, text.
    public static final String ID = MiniExpansion.makeID("SetOfShip");

    private static final Texture IMG = TextureLoader.getTexture(makeRelicPath("Ship.png"));
    private static final Texture OUTLINE = TextureLoader.getTexture(makeRelicOutlinePath("Ship.png"));

    public SetOfShip() {
        super(ID, IMG, OUTLINE, AbstractRelic.RelicTier.SPECIAL, AbstractRelic.LandingSound.HEAVY);
    }

    @Override
    public void atBattleStart() {
        flash();
        AbstractPlayer p = AbstractDungeon.player;
        this.addToTop(new ApplyPowerAction(p, p, new MetallicizePower(p, 3), 3));
        this.addToTop(new RelicAboveCreatureAction(p, this));
    }

    // Description
    @Override
    public String getUpdatedDescription() { return DESCRIPTIONS[0] + DESCRIPTIONS[1] + 3 + DESCRIPTIONS[2]; }
}
