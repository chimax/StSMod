package miniExpansion.neow.intro;

import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.CharacterStrings;
import miniExpansion.MiniExpansion;
import miniExpansion.neow.AbstractNeowReward;

public class ScreenIntroRewardBlank extends AbstractNeowReward {
    public static final String ID = MiniExpansion.makeID(ScreenIntroRewardBlank.class.getSimpleName());
    private static final CharacterStrings characterStringsStrings = CardCrawlGame.languagePack.getCharacterString(ID);
    private static final String[] SPEECHES = characterStringsStrings.TEXT;
    private static final String[] TEXT = characterStringsStrings.UNIQUE_REWARDS;

    public ScreenIntroRewardBlank() {
        super(ID);
        this.optionLabel = TEXT[0];
    }


    @Override
    public void update() {
        if (this.activated) {
            this.activated = false;
        }
    }

    @Override
    public void activate() {
        this.activated = true;
    }

    @Override
    public String getSpeech() {
        return SPEECHES[0];
    }
}
