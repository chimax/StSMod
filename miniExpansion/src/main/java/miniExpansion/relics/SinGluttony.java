package miniExpansion.relics;

import basemod.abstracts.CustomRelic;
import com.badlogic.gdx.graphics.Texture;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.common.LoseHPAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.ui.campfire.AbstractCampfireOption;
import com.megacrit.cardcrawl.ui.campfire.RestOption;
import miniExpansion.MiniExpansion;
import miniExpansion.util.TextureLoader;

import static miniExpansion.MiniExpansion.makeRelicOutlinePath;
import static miniExpansion.MiniExpansion.makeRelicPath;

public class SinGluttony extends CustomRelic {
    /*
     * Upon pickup, raise your Max HP by 600.
     * Every time you play a card, you lose HP equals to its cost.
     * You can no longer Rest at Rest Sites.
     */

    // ID, images, text.
    public static final String ID = MiniExpansion.makeID("SinGluttony");
    private static final Texture IMG = TextureLoader.getTexture(makeRelicPath("SinGluttony.png"));
    private static final Texture OUTLINE = TextureLoader.getTexture(makeRelicOutlinePath("SinGluttony.png"));

    public SinGluttony() { super(ID, IMG, OUTLINE, AbstractRelic.RelicTier.SPECIAL, AbstractRelic.LandingSound.MAGICAL); }


    public String getUpdatedDescription() { return this.DESCRIPTIONS[0] + 600 + this.DESCRIPTIONS[1]; }

    @Override
    public void onEquip() {
        AbstractDungeon.player.increaseMaxHp(600, true);
    }

    @Override
    public void onPlayCard(AbstractCard c, AbstractMonster m) {
        AbstractPlayer p = AbstractDungeon.player;
        if (c.costForTurn > 0) {
            this.flash();
            this.addToTop(new LoseHPAction(p, p, c.costForTurn, AbstractGameAction.AttackEffect.FIRE));
        }
    }

    @Override
    public boolean canUseCampfireOption(AbstractCampfireOption option) {
        if (option instanceof RestOption && option.getClass().getName().equals(RestOption.class.getName())) {
            ((RestOption)option).updateUsability(false);
            return false;
        } else {
            return true;
        }
    }




}
