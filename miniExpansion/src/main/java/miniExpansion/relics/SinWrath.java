package miniExpansion.relics;

import basemod.abstracts.CustomRelic;
import com.badlogic.gdx.graphics.Texture;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.LoseHPAction;
import com.megacrit.cardcrawl.actions.common.RelicAboveCreatureAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.characters.Defect;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.powers.DexterityPower;
import com.megacrit.cardcrawl.powers.FocusPower;
import com.megacrit.cardcrawl.powers.StrengthPower;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import miniExpansion.MiniExpansion;
import miniExpansion.util.TextureLoader;

import static miniExpansion.MiniExpansion.makeRelicOutlinePath;
import static miniExpansion.MiniExpansion.makeRelicPath;

public class SinWrath extends CustomRelic {

    /*
     * While your HP is at or below 10, you have 10 additional Strength and Dexterity,
     * but you lose 1 HP at the end of your turn.
     */

    // ID, images, text.
    public static final String ID = MiniExpansion.makeID("SinWrath");
    private static final Texture IMG = TextureLoader.getTexture(makeRelicPath("SinWrath.png"));
    private static final Texture OUTLINE = TextureLoader.getTexture(makeRelicOutlinePath("SinWrath.png"));

    private boolean isActive = false;

    public SinWrath() { super(ID, IMG, OUTLINE, AbstractRelic.RelicTier.SPECIAL, AbstractRelic.LandingSound.MAGICAL); }


    public String getUpdatedDescription() { return this.DESCRIPTIONS[0] + 10 + this.DESCRIPTIONS[1] + 10 + this.DESCRIPTIONS[2]; }

    @Override
    public void atBattleStart() {
        AbstractPlayer p = AbstractDungeon.player;
        if (p.currentHealth <= 10) {
            this.flash();
            this.pulse = true;
            if (p instanceof Defect) {
                this.addToTop(new ApplyPowerAction(p, p, new FocusPower(p, 3), 3));
            } else {
                this.addToTop(new ApplyPowerAction(p, p, new StrengthPower(p, 10), 10));
                this.addToTop(new ApplyPowerAction(p, p, new DexterityPower(p, 10), 10));
            }
            this.addToTop(new RelicAboveCreatureAction(AbstractDungeon.player, this));
            this.isActive = true;
            p.hand.applyPowers();
        } else {
            this.isActive = false;
        }
    }

    @Override
    public void onLoseHp(int damageAmount) {
        AbstractPlayer p = AbstractDungeon.player;
        if (p.currentHealth - damageAmount <= 10 && !this.isActive  && AbstractDungeon.getCurrRoom().phase == AbstractRoom.RoomPhase.COMBAT) {
            this.flash();
            this.pulse = true;
            if (p instanceof Defect) {
                this.addToTop(new ApplyPowerAction(p, p, new FocusPower(p, 3), 3));
            } else {
                this.addToTop(new ApplyPowerAction(p, p, new StrengthPower(p, 10), 10));
                this.addToTop(new ApplyPowerAction(p, p, new DexterityPower(p, 10), 10));
            }
            this.addToTop(new RelicAboveCreatureAction(AbstractDungeon.player, this));
            this.isActive = true;
            p.hand.applyPowers();
        }
    }

    @Override
    public int onPlayerHeal(int healAmount) {
        AbstractPlayer p = AbstractDungeon.player;
        if (p.currentHealth + healAmount > 10 && this.isActive && AbstractDungeon.getCurrRoom().phase == AbstractRoom.RoomPhase.COMBAT) {
            this.pulse = false;
            if (p instanceof Defect) {
                this.addToTop(new ApplyPowerAction(p, p, new FocusPower(p, -3), -3));
            } else {
                this.addToTop(new ApplyPowerAction(p, p, new StrengthPower(p, -10), -10));
                this.addToTop(new ApplyPowerAction(p, p, new DexterityPower(p, -10), -10));
            }
            this.stopPulse();
            this.isActive = false;
            p.hand.applyPowers();
        }
        return healAmount;
    }

    @Override
    public void onPlayerEndTurn() {
        AbstractPlayer p = AbstractDungeon.player;
        if (this.isActive) {
            this.flash();
            this.addToTop(new LoseHPAction(p, p, 1, AbstractGameAction.AttackEffect.FIRE));
        }
    }

    @Override
    public void onVictory() {
        this.pulse = false;
        this.isActive = false;
    }

}
