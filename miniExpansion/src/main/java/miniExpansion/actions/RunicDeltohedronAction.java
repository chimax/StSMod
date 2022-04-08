package miniExpansion.actions;

import basemod.BaseMod;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.GameActionManager;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.utility.WaitAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardQueueItem;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.UIStrings;
import com.megacrit.cardcrawl.powers.DrawCardNextTurnPower;
import miniExpansion.MiniExpansion;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;

public class RunicDeltohedronAction extends AbstractGameAction {
    private static final Logger logger = LogManager.getLogger(RunicDeltohedronAction.class.getName());

    public static final String ID = MiniExpansion.makeID(RunicDeltohedronAction.class.getSimpleName());
    public static final UIStrings UITEXT = CardCrawlGame.languagePack.getUIString(ID);

    private final ArrayList<AbstractCard> savedCardQueue;
    private boolean cardSelected;

    public RunicDeltohedronAction(AbstractCreature source) {
        this.setValues(AbstractDungeon.player, source, -1);
        this.actionType = ActionType.CARD_MANIPULATION;
        this.cardSelected = false;
        this.savedCardQueue = new ArrayList<>();
    }

    @Override
    public void update() {
        AbstractPlayer p = AbstractDungeon.player;
        // First time update
        if (this.duration == 0.5F) {
            // Save current card queue items
            for (CardQueueItem c : AbstractDungeon.actionManager.cardQueue) {
                this.savedCardQueue.add(c.card);
            }
            // Open hand selection screen - this will clear the card queue! (why?)
            AbstractDungeon.handCardSelectScreen.open(UITEXT.TEXT[0], 99, true, true);
            this.addToBot(new WaitAction(0.25F));
            this.tickDuration();
        // All the other times
        } else {
            // Shuffle selected cards to deck
            if (!AbstractDungeon.handCardSelectScreen.wereCardsRetrieved) {
                if (!AbstractDungeon.handCardSelectScreen.selectedCards.group.isEmpty()) {
                    for (AbstractCard c : AbstractDungeon.handCardSelectScreen.selectedCards.group) {
                        this.cardSelected = true;
                        p.hand.moveToDeck(c, true);
                    }
                }
                AbstractDungeon.handCardSelectScreen.wereCardsRetrieved = true;
            }
            // If card selected, draw 1 next turn
            if (this.cardSelected) {
                this.addToBot(new ApplyPowerAction(p, p, new DrawCardNextTurnPower(p, 1), 1));
                this.cardSelected = false;
            }
            // Recover the card queue, if the cards are still in hand
            for (AbstractCard c : this.savedCardQueue) {
                if (p.hand.group.contains(c)) {
                    AbstractDungeon.actionManager.cardQueue.add(new CardQueueItem(c, true));
                }
            }
            // Do this only once
            this.savedCardQueue.clear();

            this.tickDuration();
        }
    }


}
