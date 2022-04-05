package miniExpansion.powers;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.utility.UseCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.helpers.FontHelper;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.SlowPower;
import miniExpansion.MiniExpansion;

public class NegativeSlowPower extends SlowPower {

    public static final String ID = MiniExpansion.makeID("NegativeSlowPower");
    private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(ID);
    public static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;

    public NegativeSlowPower(AbstractCreature owner, int amount) {
        super(owner, amount);
        this.canGoNegative = true;
    }

    @Override
    public void updateDescription() {
        this.description = DESCRIPTIONS[0];
        if (!(this.owner instanceof AbstractPlayer)) {
            this.description += FontHelper.colorString(this.owner.name, "y");
        }
        this.description += DESCRIPTIONS[1];
        if (this.amount < 0) {
            this.description += DESCRIPTIONS[2] + this.amount * -10 + DESCRIPTIONS[4];
            this.type = PowerType.BUFF;
        } else if (this.amount > 0) {
            this.description += DESCRIPTIONS[2] + this.amount * 10 + DESCRIPTIONS[3];
            this.type = PowerType.DEBUFF;
        } else {
            this.type = PowerType.BUFF;
        }
    }

    @Override
    public void onAfterUseCard(AbstractCard card, UseCardAction action) {
        this.addToBot(new ApplyPowerAction(this.owner, this.owner, new SlowPower(this.owner, -1), -1));
    }

    @Override
    public void stackPower(int stackAmount) {
        this.fontScale = 8.0F;
        if (this.amount > -5) {
            this.amount += stackAmount;
        }
    }
}
