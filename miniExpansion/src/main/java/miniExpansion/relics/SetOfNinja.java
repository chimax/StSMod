package miniExpansion.relics;

import basemod.abstracts.CustomRelic;
import com.badlogic.gdx.graphics.Texture;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.RelicAboveCreatureAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.powers.DexterityPower;
import com.megacrit.cardcrawl.powers.StrengthPower;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import miniExpansion.MiniExpansion;
import miniExpansion.util.TextureLoader;

import static miniExpansion.MiniExpansion.makeRelicOutlinePath;
import static miniExpansion.MiniExpansion.makeRelicPath;

public class SetOfNinja extends CustomRelic {
    /*
     * At the start of your turn, set the counter of Kunai and Shuriken to 1.
     */

    // ID, images, text.
    public static final String ID = MiniExpansion.makeID("SetOfNinja");
    private static final Texture IMG = TextureLoader.getTexture(makeRelicPath("Heart.png")); //TODO
    private static final Texture OUTLINE = TextureLoader.getTexture(makeRelicOutlinePath("Heart.png")); //TODO

    public SetOfNinja() {
        super(ID, IMG, OUTLINE, AbstractRelic.RelicTier.SPECIAL, LandingSound.FLAT);
    }


    @Override
    public String getUpdatedDescription() { return this.DESCRIPTIONS[0] + this.DESCRIPTIONS[1]; }

    @Override
    public void atTurnStartPostDraw() {
        AbstractPlayer p = AbstractDungeon.player;
        this.flash();
        this.addToBot(new RelicAboveCreatureAction(p, this));
        if (p.hasRelic("Kunai")) {
            AbstractRelic r = p.getRelic("Kunai");
            r.counter++;
            if (r.counter % 3 == 0) {
                r.counter = 0;
                r.flash();
                this.addToBot(new RelicAboveCreatureAction(p, r));
                this.addToBot(new ApplyPowerAction(p, p, new DexterityPower(p, 1), 1));
            }
        }
        if (p.hasRelic("Shuriken")) {
            AbstractRelic r = p.getRelic("Shuriken");
            r.counter++;
            if (r.counter % 3 == 0) {
                r.counter = 0;
                r.flash();
                this.addToBot(new RelicAboveCreatureAction(p, r));
                this.addToBot(new ApplyPowerAction(p, p, new StrengthPower(p, 1), 1));
            }
        }
    }

}
