package miniExpansion;

import basemod.AutoAdd;
import basemod.BaseMod;
import basemod.ModLabeledToggleButton;
import basemod.ModPanel;
import basemod.abstracts.CustomRelic;
import basemod.helpers.RelicType;
import basemod.interfaces.*;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Texture;
import com.evacipated.cardcrawl.mod.stslib.Keyword;
import com.evacipated.cardcrawl.modthespire.lib.SpireConfig;
import com.evacipated.cardcrawl.modthespire.lib.SpireInitializer;
import com.google.gson.Gson;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.helpers.FontHelper;
import com.megacrit.cardcrawl.localization.*;
import miniExpansion.actions.RunicDeltohedronAction;
import miniExpansion.relics.*;
import miniExpansion.util.IDCheckDontTouchPls;
import miniExpansion.util.TextureLoader;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Properties;



@SpireInitializer
public class MiniExpansion implements
        EditCardsSubscriber,
        EditRelicsSubscriber,
        EditStringsSubscriber,
        EditKeywordsSubscriber,
        EditCharactersSubscriber,
        PostInitializeSubscriber {
    // Make sure to implement the subscribers *you* are using (read basemod wiki). Editing cards? EditCardsSubscriber.
    // Making relics? EditRelicsSubscriber. etc., etc., for a full list and how to make your own, visit the basemod wiki.
    public static final Logger logger = LogManager.getLogger(MiniExpansion.class.getName());
    private static String modID;

    // Mod-settings settings. This is if you want an on/off savable button
    public static Properties miniExpansionDefaultSettings = new Properties();
    public static final String ENABLE_PLACEHOLDER_SETTINGS = "enablePlaceholder";
    public static boolean enablePlaceholder = true; // The boolean we'll be setting on/off (true/false)

    //This is for the in-game mod settings panel.
    private static final String MODNAME = "Mini Expansion";
    private static final String AUTHOR = "MCrescent"; // And pretty soon - You!
    private static final String DESCRIPTION = "A mini expansion to Slay the Spire.";
    
    // =============== INPUT TEXTURE LOCATION =================

    //Mod Badge - A small icon that appears in the mod settings menu next to your mod.
    public static final String BADGE_IMAGE = "Resources/images/Badge.png";
    
    // =============== MAKE IMAGE PATHS =================
    
    public static String makeCardPath(String resourcePath) {
        return "Resources/images/cards/" + resourcePath;
    }
    
    public static String makeRelicPath(String resourcePath) {
        return "Resources/images/relics/" + resourcePath;
    }
    
    public static String makeRelicOutlinePath(String resourcePath) { return "Resources/images/relics/outline/" + resourcePath; }
    
    public static String makeOrbPath(String resourcePath) { return "Resources/images/orbs/" + resourcePath; }
    
    public static String makePowerPath(String resourcePath) {
        return "Resources/images/powers/" + resourcePath;
    }
    
    public static String makeEventPath(String resourcePath) {
        return "Resources/images/events/" + resourcePath;
    }



    
    
    // =============== SUBSCRIBE, CREATE THE COLOR_GRAY, INITIALIZE =================
    
    public MiniExpansion() {
        logger.info("Subscribe to BaseMod hooks");
        
        BaseMod.subscribe(this);

        setModID("miniExpansion");
        
        logger.info("Done subscribing");
        
//        logger.info("Creating the color " + TheDefault.Enums.COLOR_GRAY.toString());
//        BaseMod.addColor(TheDefault.Enums.COLOR_GRAY, DEFAULT_GRAY, DEFAULT_GRAY, DEFAULT_GRAY,
//                DEFAULT_GRAY, DEFAULT_GRAY, DEFAULT_GRAY, DEFAULT_GRAY,
//                ATTACK_DEFAULT_GRAY, SKILL_DEFAULT_GRAY, POWER_DEFAULT_GRAY, ENERGY_ORB_DEFAULT_GRAY,
//                ATTACK_DEFAULT_GRAY_PORTRAIT, SKILL_DEFAULT_GRAY_PORTRAIT, POWER_DEFAULT_GRAY_PORTRAIT,
//                ENERGY_ORB_DEFAULT_GRAY_PORTRAIT, CARD_ENERGY_ORB);
//        logger.info("Done creating the color");
        
        
        logger.info("Adding mod settings");
        // This loads the mod settings.
        // The actual mod Button is added below in receivePostInitialize()
        miniExpansionDefaultSettings.setProperty(ENABLE_PLACEHOLDER_SETTINGS, "FALSE"); // This is the default setting. It's actually set...
        try {
            SpireConfig config = new SpireConfig("defaultMod", "theDefaultConfig", miniExpansionDefaultSettings); // ...right here
            // the "fileName" parameter is the name of the file MTS will create where it will save our setting.
            config.load(); // Load the setting and set the boolean to equal it
            enablePlaceholder = config.getBool(ENABLE_PLACEHOLDER_SETTINGS);
        } catch (Exception e) {
            e.printStackTrace();
        }
        logger.info("Done adding mod settings");
        
    }
    
    // ====== NO EDIT AREA ======
    // DON'T TOUCH THIS STUFF. IT IS HERE FOR STANDARDIZATION BETWEEN MODS AND TO ENSURE GOOD CODE PRACTICES.
    public static void setModID(String ID) {
        Gson coolG = new Gson();
        InputStream in = MiniExpansion.class.getResourceAsStream("/IDCheckStringsDONT-EDIT-AT-ALL.json");
        assert in != null;
        IDCheckDontTouchPls EXCEPTION_STRINGS = coolG.fromJson(new InputStreamReader(in, StandardCharsets.UTF_8), IDCheckDontTouchPls.class);
        logger.info("You are attempting to set your mod ID as: " + ID);
        if (ID.equals(EXCEPTION_STRINGS.DEFAULTID)) {
            throw new RuntimeException(EXCEPTION_STRINGS.EXCEPTION);
        } else if (ID.equals(EXCEPTION_STRINGS.DEVID)) {
            modID = EXCEPTION_STRINGS.DEFAULTID;
        } else {
            modID = ID;
        }
        logger.info("Success! ID is " + modID);
    }
    
    public static String getModID() { return modID; }
    
    private static void pathCheck() {
        Gson coolG = new Gson();
        InputStream in = MiniExpansion.class.getResourceAsStream("/IDCheckStringsDONT-EDIT-AT-ALL.json");
        assert in != null;
        IDCheckDontTouchPls EXCEPTION_STRINGS = coolG.fromJson(new InputStreamReader(in, StandardCharsets.UTF_8), IDCheckDontTouchPls.class);
        String packageName = MiniExpansion.class.getPackage().getName();
        FileHandle resourcePathExists = Gdx.files.internal("Resources");
        if (!modID.equals(EXCEPTION_STRINGS.DEVID)) {
            if (!packageName.equals(getModID())) {
                throw new RuntimeException(EXCEPTION_STRINGS.PACKAGE_EXCEPTION + getModID());
            }
            if (!resourcePathExists.exists()) {
                throw new RuntimeException(EXCEPTION_STRINGS.RESOURCE_FOLDER_EXCEPTION + "Resources");
            }
        }
    }
    // ====== YOU CAN EDIT AGAIN ======
    
    
    public static void initialize() {
        logger.info("========================= Initializing Mod. =========================");
        MiniExpansion miniexpansion = new MiniExpansion();
        logger.info("========================= Mod Initialized. =========================");
    }
    



    
    // =============== LOAD THE CHARACTER =================
    
    @Override
    public void receiveEditCharacters() {
        logger.info("No edit to characters is made.");
//        logger.info("Beginning to edit characters. " + "Add " + TheDefault.Enums.THE_DEFAULT.toString());
//
//        BaseMod.addCharacter(new TheDefault("the Default", TheDefault.Enums.THE_DEFAULT),
//                THE_DEFAULT_BUTTON, THE_DEFAULT_PORTRAIT, TheDefault.Enums.THE_DEFAULT);
//
//        receiveEditPotions();
//        logger.info("Added " + TheDefault.Enums.THE_DEFAULT.toString());
    }

    
    
    // =============== POST-INITIALIZE =================
    
    @Override
    public void receivePostInitialize() {
        logger.info("Loading badge image and mod options");
        
        // Load the Mod Badge
        Texture badgeTexture = TextureLoader.getTexture(BADGE_IMAGE);
        
        // Create the Mod Menu
        ModPanel settingsPanel = new ModPanel();
        
        // Create the on/off button:
        ModLabeledToggleButton enableNormalsButton = new ModLabeledToggleButton("This is the text which goes next to the checkbox.",
                350.0f, 700.0f, Settings.CREAM_COLOR, FontHelper.charDescFont, // Position (trial and error it), color, font
                enablePlaceholder, // Boolean it uses
                settingsPanel, // The mod panel in which this button will be in
                (label) -> {}, // thing??????? idk
                (button) -> { // The actual button:
            
            enablePlaceholder = button.enabled; // The boolean true/false will be whether the button is enabled or not
            try {
                // And based on that boolean, set the settings and save them
                SpireConfig config = new SpireConfig("defaultMod", "theDefaultConfig", miniExpansionDefaultSettings);
                config.setBool(ENABLE_PLACEHOLDER_SETTINGS, enablePlaceholder);
                config.save();
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        
        settingsPanel.addUIElement(enableNormalsButton); // Add the button to the settings panel. Button is a go.
        
        BaseMod.registerModBadge(badgeTexture, MODNAME, AUTHOR, DESCRIPTION, settingsPanel);

        
        // =============== EVENTS =================
        // https://github.com/daviscook477/BaseMod/wiki/Custom-Events

        // You can add the event like so:
        // BaseMod.addEvent(IdentityCrisisEvent.ID, IdentityCrisisEvent.class, TheCity.ID);
        // Then, this event will be exclusive to the City (act 2), and will show up for all characters.
        // If you want an event that's present at any part of the game, simply don't include the dungeon ID

        // If you want to have more specific event spawning (e.g. character-specific or so)
        // deffo take a look at that basemod wiki link as well, as it explains things very in-depth
        // btw if you don't provide event type, normal is assumed by default

        // Create a new event builder
        // Since this is a builder these method calls (outside of create()) can be skipped/added as necessary
//        AddEventParams eventParams = new AddEventParams.Builder(IdentityCrisisEvent.ID, IdentityCrisisEvent.class) // for this specific event
//            .dungeonID(TheCity.ID) // The dungeon (act) this event will appear in
//            .playerClass(TheDefault.Enums.THE_DEFAULT) // Character specific event
//            .create();

        // Add the event
//        BaseMod.addEvent(eventParams);

        logger.info("Done loading badge Image and mod options");
    }
    



    
    // ================ ADD POTIONS ===================
    
    public void receiveEditPotions() {
        logger.info("No edit to potions is made.");
        
        // Class Specific Potion. If you want your potion to not be class-specific,
        // just remove the player class at the end (in this case the "TheDefaultEnum.THE_DEFAULT".
        // Remember, you can press ctrl+P inside parentheses like addPotions)
//        BaseMod.addPotion(PlaceholderPotion.class, PLACEHOLDER_POTION_LIQUID, PLACEHOLDER_POTION_HYBRID, PLACEHOLDER_POTION_SPOTS, PlaceholderPotion.POTION_ID, TheDefault.Enums.THE_DEFAULT);
//
//        logger.info("Done editing potions");
    }
    



    
    // ================ ADD RELICS ===================
    
    @Override
    public void receiveEditRelics() {
        logger.info("Adding relics");
        logger.info("AutoAdd sucks, adding manually");

        // This adds a character specific relic. Only when you play with the mentioned color, will you get this relic.
//        BaseMod.addRelicToCustomPool(new PlaceholderRelic(), TheDefault.Enums.COLOR_GRAY);
//        BaseMod.addRelicToCustomPool(new BottledPlaceholderRelic(), TheDefault.Enums.COLOR_GRAY);
//        BaseMod.addRelicToCustomPool(new DefaultClickableRelic(), TheDefault.Enums.COLOR_GRAY);
        
        // This adds a relic to the Shared pool. Every character can find this relic.
        BaseMod.addRelic(new RunicDeltohedron(), RelicType.SHARED);
        BaseMod.addRelic(new AnglerfishLantern(), RelicType.SHARED);
        BaseMod.addRelic(new RedCandle(), RelicType.SHARED);

        BaseMod.addRelic(new SinPride(), RelicType.SHARED);
        BaseMod.addRelic(new SinGreed(), RelicType.SHARED);
        BaseMod.addRelic(new SinLust(), RelicType.SHARED);
        BaseMod.addRelic(new SinWrath(), RelicType.SHARED);
        BaseMod.addRelic(new SinEnvy(), RelicType.SHARED);
        BaseMod.addRelic(new SinGluttony(), RelicType.SHARED);
        BaseMod.addRelic(new SinSloth(), RelicType.SHARED);

        BaseMod.addRelic(new SetOfShip(), RelicType.SHARED);
        BaseMod.addRelic(new SetOfNinja(), RelicType.SHARED);
        BaseMod.addRelic(new SetOfFish(), RelicType.SHARED);
        
        // Mark relics as seen - makes it visible in the compendium immediately
        // If you don't have this it won't be visible in the compendium until you see them in game
        // (the others are all starters so they're marked as seen in the character file)
//        UnlockTracker.markRelicAsSeen(BottledPlaceholderRelic.ID);


        logger.info("Done adding relics!");
    }
    


    
    
    // ================ ADD CARDS ===================
    
    @Override
    public void receiveEditCards() {
        logger.info("No edit to cards is made.");
        //Ignore this
//        pathCheck();
        // Add the Custom Dynamic Variables
//        logger.info("Add variables");
        // Add the Custom Dynamic variables
//        BaseMod.addDynamicVariable(new DefaultCustomVariable());
//        BaseMod.addDynamicVariable(new DefaultSecondMagicNumber());
        
//        logger.info("Adding cards");
        // Add the cards
        // Don't delete these default cards yet. You need 1 of each type and rarity (technically) for your game not to crash
        // when generating card rewards/shop screen items.

        // This method automatically adds any cards so you don't have to manually load them 1 by 1
        // For more specific info, including how to exclude cards from being added:
        // https://github.com/daviscook477/BaseMod/wiki/AutoAdd

        // The ID for this function isn't actually your modid as used for prefixes/by the getModID() method.
        // It's the mod id you give MTS in ModTheSpire.json - by default your artifact ID in your pom.xml

//        new AutoAdd("MiniExpansion") // ${project.artifactId}
//            .packageFilter(AbstractDefaultCard.class) // filters to any class in the same package as AbstractDefaultCard, nested packages included
//            .setDefaultSeen(true)
//            .cards();

        // .setDefaultSeen(true) unlocks the cards
        // This is so that they are all "seen" in the library,
        // for people who like to look at the card list before playing your mod

//        logger.info("Done adding cards!");
    }
    


    
    
    // ================ LOAD THE TEXT ===================
    
    @Override
    public void receiveEditStrings() {
        logger.info("You seeing this?");
        logger.info("Beginning to edit strings for mod with ID: " + getModID());
        // CardStrings
        BaseMod.loadCustomStringsFile(CardStrings.class,
                "Resources/localization/eng/MiniExpansion-Card-Strings.json");
        // PowerStrings
        BaseMod.loadCustomStringsFile(PowerStrings.class,
                "Resources/localization/eng/MiniExpansion-Power-Strings.json");
        // RelicStrings
        BaseMod.loadCustomStringsFile(RelicStrings.class,
                "Resources/localization/eng/MiniExpansion-Relic-Strings.json");
        // Event Strings
        BaseMod.loadCustomStringsFile(EventStrings.class,
                "Resources/localization/eng/MiniExpansion-Event-Strings.json");
        // PotionStrings
        BaseMod.loadCustomStringsFile(PotionStrings.class,
                "Resources/localization/eng/MiniExpansion-Potion-Strings.json");
        // CharacterStrings
        BaseMod.loadCustomStringsFile(CharacterStrings.class,
                "Resources/localization/eng/MiniExpansion-Character-Strings.json");
        // OrbStrings
        BaseMod.loadCustomStringsFile(OrbStrings.class,
                "Resources/localization/eng/MiniExpansion-Orb-Strings.json");
        // UI
        BaseMod.loadCustomStringsFile(UIStrings.class,
                "Resources/localization/eng/MiniExpansion-UI-Strings.json");
        logger.info("Done editing strings");
    }
    



    
    // ================ LOAD THE KEYWORDS ===================

    @Override
    public void receiveEditKeywords() {
        // Keywords on cards are supposed to be Capitalized, while in Keyword-String.json they're lowercase
        //
        // Multiword keywords on cards are done With_Underscores
        //
        // If you're using multiword keywords, the first element in your NAMES array in your keywords-strings.json has to be the same as the PROPER_NAME.
        // That is, in Card-Strings.json you would have #yA_Long_Keyword (#y highlights the keyword in yellow).
        // In Keyword-Strings.json you would have PROPER_NAME as A Long Keyword and the first element in NAMES be a long keyword, and the second element be a_long_keyword
        
        Gson gson = new Gson();
        String json = Gdx.files.internal("Resources/localization/eng/MiniExpansion-Keyword-Strings.json").readString(String.valueOf(StandardCharsets.UTF_8));
        com.evacipated.cardcrawl.mod.stslib.Keyword[] keywords = gson.fromJson(json, com.evacipated.cardcrawl.mod.stslib.Keyword[].class);
        
        if (keywords != null) {
            for (Keyword keyword : keywords) {
                BaseMod.addKeyword(getModID().toLowerCase(), keyword.PROPER_NAME, keyword.NAMES, keyword.DESCRIPTION);
                //  getModID().toLowerCase() makes your keyword mod specific (it won't show up in other cards that use that word)
            }
        }
    }
    


    
    // this adds "ModName:" before the ID of any card/relic/power etc.
    // in order to avoid conflicts if any other mod uses the same ID.
    public static String makeID(String idText) {
        return getModID() + ":" + idText;
    }
}
