package miniExpansion.neow.intro;

import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.CharacterStrings;
import miniExpansion.MiniExpansion;
import miniExpansion.neow.AbstractNeowScreen;

public class ScreenIntro extends AbstractNeowScreen {
    public static final String ID = MiniExpansion.makeID(ScreenIntro.class.getSimpleName());
    private static final CharacterStrings characterStringsStrings = CardCrawlGame.languagePack.getCharacterString(ID);
    private static final String[] SPEECHES = characterStringsStrings.TEXT;


    public ScreenIntro(String nextScreenID, boolean isLast, boolean toSave) {
        super(ID, nextScreenID, isLast, toSave);
        this.rewardsPool.add(new ScreenIntroRewardBlank());
    }

    public ScreenIntro(){
        this(null, true, false);
    }

    @Override
    public void drawRewards() {
        this.rewards.addAll(this.rewardsPool);
    }

    @Override
    public String getSpeech() {
        return SPEECHES[0];
    }
}
