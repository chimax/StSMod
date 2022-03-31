package miniExpansion.neow;

import com.megacrit.cardcrawl.localization.CharacterStrings;

import java.util.ArrayList;

public abstract class AbstractNeowScreen {
    public String screenID;
    public String nextScreenID;
//    private String[] SPEECHES;
    public int currScreen;
    public int nextScreen;
    public boolean isLast;
    public boolean toSave;
    protected ArrayList<AbstractNeowReward> rewardsPool;
    public ArrayList<AbstractNeowReward> rewards;

    public AbstractNeowScreen(String screenID, String nextScreenID, boolean isLast, boolean toSave) {
        this.screenID = screenID;
        this.nextScreenID = nextScreenID;
        this.isLast = isLast;
        this.toSave = toSave;
        this.rewardsPool = new ArrayList<>();
        this.rewards = new ArrayList<>();
        this.currScreen = 99;
        this.nextScreen = 99;
    }

    public abstract void drawRewards();

    public abstract String getSpeech();

    public void updateNextScreenOnButtonPressed(int buttonPressed) {
        // Allow selecting certain options to go to other neow screens
    }
}
