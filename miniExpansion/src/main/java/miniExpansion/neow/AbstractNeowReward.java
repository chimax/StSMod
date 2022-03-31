package miniExpansion.neow;

import com.megacrit.cardcrawl.localization.CharacterStrings;

public abstract class AbstractNeowReward {
    public String rewardID;
//    private static String[] TEXT;
//    private static String[] SPEECHES;
    public String optionLabel;
    public boolean activated;

    public AbstractNeowReward(String rewardID) {
        this.rewardID = rewardID;
        this.optionLabel = "Unknown";
        this.activated = false;
    }

    public abstract void update();

    public abstract void activate();

    public String getSpeech() {
        // Reward of last screen can have custom speech
        return null;
    }
}
