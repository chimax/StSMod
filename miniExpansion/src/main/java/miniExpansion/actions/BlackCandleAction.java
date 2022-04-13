package miniExpansion.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.relics.AbstractRelic;

import java.util.ArrayList;

public class BlackCandleAction extends AbstractGameAction {

    public BlackCandleAction(AbstractCreature source) {
        this.setValues(AbstractDungeon.player, source, 0);
        this.actionType = ActionType.CARD_MANIPULATION;
        this.duration = Settings.ACTION_DUR_FAST;
    }

    @Override
    public void update() {
        AbstractRelic relic = AbstractDungeon.player.getRelic("miniExpansion:BlackCandle");
        if (this.duration == Settings.ACTION_DUR_FAST && relic != null && relic.counter != -1 && relic.counter < 3) {
            ArrayList<AbstractCard> cards = new ArrayList<>();
            for (AbstractCard c : AbstractDungeon.player.hand.group) {
                cards.add(c);
                relic.counter++;
                if (relic.counter >= 3) {
                    relic.grayscale = true;
                    break;
                }
            }
            for (AbstractCard c : cards) {
                AbstractDungeon.player.hand.moveToExhaustPile(c);
            }
        }
        this.tickDuration();
    }
}
