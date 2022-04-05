package miniExpansion.relics;

import basemod.abstracts.CustomRelic;
import basemod.abstracts.CustomSavable;
import com.badlogic.gdx.graphics.Texture;
import com.megacrit.cardcrawl.actions.common.RelicAboveCreatureAction;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.rewards.RewardItem;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import miniExpansion.MiniExpansion;
import miniExpansion.patches.rewards.CustomGold;
import miniExpansion.util.TextureLoader;

import static miniExpansion.MiniExpansion.*;

public class SetOfFish extends CustomRelic {
    /*
     * Chance to find a small amount of extra Gold after each combat.
     */

    // ID, images, text.
    public static final String ID = MiniExpansion.makeID("SetOfFish");
    private static final Texture IMG = TextureLoader.getTexture(makeRelicPath("Heart.png")); //TODO
    private static final Texture OUTLINE = TextureLoader.getTexture(makeRelicOutlinePath("Heart.png")); //TODO

    public static int GOLD_CHANCE = 50;
    private int rewardGold;

    public SetOfFish() { super(ID, IMG, OUTLINE, AbstractRelic.RelicTier.SPECIAL, LandingSound.FLAT); }


    @Override
    public String getUpdatedDescription() { return this.DESCRIPTIONS[0] + this.DESCRIPTIONS[1]; }

    @Override
    public void onVictory() {
        int roll = AbstractDungeon.treasureRng.random(0, 99);
        if (roll < SetOfFish.GOLD_CHANCE) {
            this.flash();
            this.rewardGold = AbstractDungeon.treasureRng.random(10, 20);
            this.addToBot(new RelicAboveCreatureAction(AbstractDungeon.player, this));
        } else {
            this.rewardGold = 0;
        }
        this.addRewardGold();
    }

    public void addRewardGold() {
        if (this.rewardGold > 0) {
            AbstractRoom r = AbstractDungeon.getCurrMapNode().getRoom();
            RewardItem ri = new RewardItem(this.rewardGold);
            ri.type = CustomGold.CUSTOM_GOLD;
            r.rewards.add(ri);
        }
    }

    //TODO
    //@Override
    //public String getUpdatedDescription()
}
