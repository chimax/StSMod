package miniExpansion.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.utility.WaitAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.UIStrings;
import com.megacrit.cardcrawl.powers.DrawCardNextTurnPower;
import miniExpansion.MiniExpansion;

public class RunicDeltohedronAction extends AbstractGameAction {

    public static final String ID = MiniExpansion.makeID(RunicDeltohedronAction.class.getSimpleName());
    public static final UIStrings UITEXT = CardCrawlGame.languagePack.getUIString(ID);

    private boolean cardSelected;

    public RunicDeltohedronAction() {
        this.setValues(AbstractDungeon.player, AbstractDungeon.player, 20);
        this.cardSelected = false;
    }

    @Override
    public void update() {
        AbstractPlayer p = AbstractDungeon.player;
        if (this.duration == 0.5F) {
            AbstractDungeon.handCardSelectScreen.open(UITEXT.TEXT[0], this.amount, true, true, false, false, true);
            this.addToBot(new WaitAction(0.25F));
            this.tickDuration();
        } else {
            if (!AbstractDungeon.handCardSelectScreen.wereCardsRetrieved) {
                for (AbstractCard c : AbstractDungeon.handCardSelectScreen.selectedCards.group) {
                    this.cardSelected = true;
                    p.hand.moveToDeck(c, true);
                }
                AbstractDungeon.handCardSelectScreen.wereCardsRetrieved = true;
            }
            if (this.cardSelected) {
                this.addToBot(new ApplyPowerAction(p, p, new DrawCardNextTurnPower(p, 1), 1));
                this.cardSelected = false;
            }
            this.tickDuration();
        }
    }
}
