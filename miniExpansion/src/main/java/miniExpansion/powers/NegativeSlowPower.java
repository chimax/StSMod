package miniExpansion.powers;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.utility.UseCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.helpers.FontHelper;
import com.megacrit.cardcrawl.powers.SlowPower;

public class NegativeSlowPower extends SlowPower {

    // TODO: Define when this is buff and when is debuff
    public NegativeSlowPower(AbstractCreature owner, int amount) {
        super(owner, amount);
        this.canGoNegative = true;
    }

    // TODO
    @Override
    public void updateDescription() {
        this.description = DESCRIPTIONS[2] + this.amount * 10 + DESCRIPTIONS[3];
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
