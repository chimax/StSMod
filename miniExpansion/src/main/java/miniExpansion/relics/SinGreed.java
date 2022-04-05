package miniExpansion.relics;

import basemod.abstracts.CustomRelic;
import com.badlogic.gdx.graphics.Texture;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.common.RelicAboveCreatureAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.rewards.RewardItem;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import miniExpansion.MiniExpansion;
import miniExpansion.patches.rewards.CustomGold;
import miniExpansion.util.TextureLoader;

import static miniExpansion.MiniExpansion.makeRelicOutlinePath;
import static miniExpansion.MiniExpansion.makeRelicPath;

public class SinGreed extends CustomRelic {
    /*
     * Add extra gold to each combat reward. At the end of your turn, take damage equals to the sum of your gold number digits.
     */

    // ID, images, text.
    public static final String ID = MiniExpansion.makeID("SinGreed");
    private static final Texture IMG = TextureLoader.getTexture(makeRelicPath("SinGreed.png"));
    private static final Texture OUTLINE = TextureLoader.getTexture(makeRelicOutlinePath("SinGreed.png"));

    private int rewardGold;

    public SinGreed() { super(ID, IMG, OUTLINE, AbstractRelic.RelicTier.SPECIAL, AbstractRelic.LandingSound.MAGICAL); }


    public String getUpdatedDescription() { return this.DESCRIPTIONS[0]; }

    @Override
    public void onPlayerEndTurn() {
        AbstractPlayer p = AbstractDungeon.player;
        int gold = p.gold;
        for (; gold > 0; gold = gold / 10) {
            if (gold % 10 != 0) {
                this.flash();
                this.addToBot(new DamageAction(p, new DamageInfo(p, gold%10, DamageInfo.DamageType.THORNS), AbstractGameAction.AttackEffect.FIRE));
            }
        }
    }

    @Override
    public void onVictory() {
        this.flash();
        this.rewardGold = AbstractDungeon.treasureRng.random(1, 39);
        this.addToBot(new RelicAboveCreatureAction(AbstractDungeon.player, this));
        this.addRewardGold();
    }

    public void addRewardGold() {
        if (this.rewardGold > 0) {
            AbstractRoom r = AbstractDungeon.getCurrMapNode().getRoom();
            RewardItem ri = new RewardItem(this.rewardGold);
            ri.type = CustomGold.CUSTOM_GOLD;
            r.rewards.add(ri);
            this.rewardGold = 0;
        }
    }
}
