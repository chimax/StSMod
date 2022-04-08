package miniExpansion.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.utility.WaitAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.UIStrings;
import miniExpansion.MiniExpansion;

import java.util.Iterator;

public class RedCandleAction extends AbstractGameAction {
    public static final String ID = MiniExpansion.makeID(RedCandleAction.class.getSimpleName());
    public static final UIStrings UITEXT = CardCrawlGame.languagePack.getUIString(ID);

    public RedCandleAction(AbstractCreature source, int amount) {
        this.setValues(AbstractDungeon.player, source, amount);
        this.actionType = ActionType.CARD_MANIPULATION;
    }

    @Override
    public void update() {
        if (this.duration == 0.5F) {
            AbstractDungeon.handCardSelectScreen.open(UITEXT.TEXT[0], this.amount, false, true, false, false, true);
            this.addToBot(new WaitAction(0.25F));
            this.tickDuration();
        } else {
            if (!AbstractDungeon.handCardSelectScreen.wereCardsRetrieved) {
                if (!AbstractDungeon.handCardSelectScreen.selectedCards.group.isEmpty()) {
                    for (AbstractCard c : AbstractDungeon.handCardSelectScreen.selectedCards.group) {
                        AbstractDungeon.player.hand.moveToExhaustPile(c);
                    }
                }

                AbstractDungeon.handCardSelectScreen.wereCardsRetrieved = true;
            }

            this.tickDuration();
        }
    }
}
