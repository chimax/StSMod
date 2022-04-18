package miniExpansion.neow;

import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import miniExpansion.neow.intro.ScreenIntro;

import java.util.ArrayList;

public class NeowScreenManager {
    public static ArrayList<AbstractNeowScreen> customScreens;
    public static ArrayList<Integer> customScreenIndices;
    private static int nextNumber;

    public static void initManager() {
        customScreens = new ArrayList<>();
        customScreenIndices = new ArrayList<>();
        // Init next available ID. Let us start from 20
        nextNumber = 20;
        // Add screens
        addScreen(new ScreenIntro());
        // Link screens
        linkScreenIndices();
    }

    // Find next unused screen index
    public static int assignIndex() {
        while (customScreenIndices.contains(nextNumber) || nextNumber <= 3 ||
                nextNumber == 10 || nextNumber == 99 || nextNumber == 999) { nextNumber++; }
        int thisIndex = nextNumber;
        nextNumber++;
        return thisIndex;
    }

    // Add a custom screen to manager
    public static void addScreen(AbstractNeowScreen s) {
        int thisIndex = assignIndex();
        s.currScreen = thisIndex;
        customScreens.add(s);
        customScreenIndices.add(thisIndex);
    }

    // Get a custom screen given its int currScreen
    public static AbstractNeowScreen getScreen(int currScreen) {
        for (AbstractNeowScreen s : customScreens) {
            if (s.currScreen == currScreen) {
                return s;
            }
        }
        return null;
    }

    //  Get a custom screen given its string ID
    public static AbstractNeowScreen getScreen(String screenID) {
        for (AbstractNeowScreen s : customScreens) {
            if (s.screenID.equals(screenID)) {
                return s;
            }
        }
        return null;
    }

    // Get int currScreen of a custom screen given its string ID
    public static int getScreenNumber(String screenID) {
        for (AbstractNeowScreen s : customScreens) {
            if (s.screenID.equals(screenID)) {
                return s.currScreen;
            }
        }
        return 99;
    }


    // Set up the nextScreen for each custom screen in manager
    public static void linkScreenIndices() {
        for (AbstractNeowScreen s : customScreens) {
            s.nextScreen = getScreenNumber(s.nextScreenID);
        }
    }
}
