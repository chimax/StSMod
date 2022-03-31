package miniExpansion.neow;

import miniExpansion.neow.intro.ScreenIntro;

import java.util.ArrayList;

public class NeowScreenManager {
    public static ArrayList<AbstractNeowScreen> customScreens;
    public static ArrayList<Integer> customScreenIndices;
    private static int nextIndex;

    public static void initManager() {
        customScreens = new ArrayList<>();
        customScreenIndices = new ArrayList<>();
        // Init next available ID. Let us start from 20
        nextIndex = 20;
        // Add screens
        addScreen(new ScreenIntro());
        // Link screens
        linkScreenIndices();
    }

    // Find next unused screen index
    public static int assignIndex() {
        while (customScreenIndices.contains(nextIndex) || nextIndex <= 3 ||
                nextIndex == 10 || nextIndex == 99 || nextIndex == 999) { nextIndex++; }
        int thisIndex = nextIndex;
        nextIndex++;
        return thisIndex;
    }

    // Add a custom screen to manager
    public static void addScreen(AbstractNeowScreen s) {
        int thisIndex = assignIndex();
        s.currScreen = thisIndex;
        customScreens.add(s);
        customScreenIndices.add(thisIndex);
    }

    // Get index of a custom screen in manager given its string ID
    public static int getScreenIndex(String screenID) {
        for (int i = 0; i < customScreens.size(); i++) {
            if (customScreens.get(i).screenID.equals(screenID)) {
                return i;
            }
        }
        return 99;
    }

    // Set up the nextScreen for each custom screen in manager
    public static void linkScreenIndices() {
        for (AbstractNeowScreen s : customScreens) {
            s.nextScreen = getScreenIndex(s.nextScreenID);
        }
    }
}
