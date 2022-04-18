package miniExpansion.patches.saveAndContinue;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.evacipated.cardcrawl.modthespire.lib.*;
import com.megacrit.cardcrawl.saveAndContinue.SaveAndContinue;
import com.megacrit.cardcrawl.saveAndContinue.SaveFile;
import com.megacrit.cardcrawl.saveAndContinue.SaveFileObfuscator;
import javassist.CtBehavior;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.function.Function;
import java.util.regex.Pattern;

public class SaveLoadManager {
    private static HashMap<String, Object> fields;
    private static ArrayList<Function<Void, Void>> fieldsSavingLogic;

    // ================ FOR PUBLIC USE ================

    // Init manager and keep track of all custom fields to save
    public static void initManager() {
        // Init
        fields = new HashMap<>();
        fieldsSavingLogic = new ArrayList<>();
        // Default field values
        fields.put("neow_screen_num", 99);
        // ADD HERE
        // Store the functions that determine field values
        fieldsSavingLogic.add(NeowEventSaveLoad::save);
        // ADD HERE
    }

    public static void saveField(String key, Object value) {
        fields.put(key, value);
    }

    public static Object loadField(String key) {
        return fields.get(key);
    }


    // ================ FOR PRIVATE USE, SAVING ================

    // SpirePatch: Write the custom fields to json
    @SpirePatch(clz = SaveAndContinue.class, method = "save")
    public static class MySavePatch {
        @SpireInsertPatch(locator = SaveLoadManager.GameSaverLocator.class, localvars = {"params"})
        public static void MySaveParamsPutNeowScreenNum(SaveFile save, @ByRef HashMap<Object, Object>[] params) {
            if (fields == null) { initManager(); }
            // Update field values
            for (Function<Void, Void> fn : fieldsSavingLogic) {
                fn.apply(null);
            }
            // Write to save file
            for (String key: fields.keySet()) {
                params[0].put(key, fields.get(key));
            }
        }
    }

    private static class GameSaverLocator extends SpireInsertLocator {
        @Override
        public int[] Locate(CtBehavior ctMethodToPatch) throws Exception {
            Matcher finalMatcher = new Matcher.FieldAccessMatcher(SaveFile.class, "metric_campfire_rested");
            return LineFinder.findInOrder(ctMethodToPatch, finalMatcher);
        }
    }



    // ================ FOR PRIVATE USE, LOADING ================

    // SpirePatch: Load the custom fields from json
    @SpirePatch(clz = SaveAndContinue.class, method = "loadSaveFile", paramtypez = {String.class})
    public static class MyLoadSaveFilePatch {
        public static void Prefix(String filePath) {
            if (fields == null) { initManager(); }
            String saveStr;
            try {
                FileHandle file = Gdx.files.local(filePath);
                String data = file.readString();
                saveStr = SaveFileObfuscator.isObfuscated(data) ? SaveFileObfuscator.decode(data, "key") : data;
                loadCustomFieldsFromJson(saveStr);
            } catch (Exception ignored) { }
        }
    }

    // Load custom fields from json
    private static void loadCustomFieldsFromJson(String saveStr) {
        fields.put("neow_screen_num", loadInt(saveStr, "neow_screen_num"));
        // ADD HERE
    }

    private static String loadStr(String str, String field) {
        Pattern p = Pattern.compile("\"" + field + "\": [a-zA-Z0-9]*,");
        java.util.regex.Matcher m = p.matcher(str);
        String regexResult;
        if (m.find()) {
            regexResult = m.group();
        } else {
            return null;
        }
        return regexResult.substring(regexResult.indexOf(" ")+1, regexResult.length()-1);
    }

    private static int loadInt(String str, String field) {
        String regexResult = loadStr(str, field);
        if (regexResult == null) { return -1; }
        return Integer.parseInt(regexResult);
    }
}
