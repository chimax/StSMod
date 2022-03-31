package miniExpansion.patches.neow;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.evacipated.cardcrawl.modthespire.lib.*;
import com.google.gson.Gson;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.neow.NeowEvent;
import com.megacrit.cardcrawl.neow.NeowRoom;
import com.megacrit.cardcrawl.saveAndContinue.SaveAndContinue;
import com.megacrit.cardcrawl.saveAndContinue.SaveFile;
import com.megacrit.cardcrawl.saveAndContinue.SaveFileObfuscator;
import com.megacrit.cardcrawl.vfx.AbstractGameEffect;
import com.megacrit.cardcrawl.vfx.InfiniteSpeechBubble;
import javassist.CtBehavior;
import miniExpansion.neow.AbstractNeowReward;
import miniExpansion.neow.AbstractNeowScreen;
import miniExpansion.neow.NeowScreenManager;

import java.util.ArrayList;
import java.util.regex.Pattern;

@SpirePatch(clz = NeowEvent.class, method = SpirePatch.CLASS)
public class NeowEventPatch {
    /*
     *
     */
    public static SpireField<ArrayList<AbstractNeowReward>> neowRewards = new SpireField<>(() -> new ArrayList<>());
    public static SpireField<Integer> screenNumPublic = new SpireField<>(() -> 2);

    // When NeowEvent initialize, add all custom neow screens
    @SpirePatch(clz = NeowEvent.class, method = SpirePatch.CONSTRUCTOR, paramtypez = {boolean.class})
    public static class MyConstructorPatch {
        public static void Postfix(NeowEvent __instance, boolean isDone, int ___screenNum) {
            // Init screens
            if (NeowScreenManager.customScreens == null) { NeowScreenManager.initManager(); }
            // Copy screenNum for saving
            NeowEventPatch.screenNumPublic.set(__instance, ___screenNum);
        }
    }

    // We do two extra things in the function buttonEffect:
    // 1. After button pressed in screen 3, instead of ending Neow event, we go to the first custom screen.
    // 2. Whenever a button is pressed, we check the custom screen list, activate the corresponding custom reward,
    //    and go to next custom screen if exists.
    @SpirePatch(clz = NeowEvent.class, method = "buttonEffect")
    public static class MyButtonEffectPatch {
        private static final float DIALOG_X = 1100.0F * Settings.xScale;
        private static final float DIALOG_Y = AbstractDungeon.floorY + 60.0F * Settings.yScale;

        @SpireInsertPatch(locator = NeowEventPatch.NeowEventAfterClaimRewardLocator.class)
        public static SpireReturn<Void> MyButtonEffectInsertScreen3(NeowEvent __instance, int buttonPressed, @ByRef int[] ___screenNum) {
            // Set first custom screen
            if (NeowScreenManager.customScreens == null) { NeowScreenManager.initManager(); }
            AbstractNeowScreen activeScreen = NeowScreenManager.customScreens.get(0);
            if (activeScreen == null) { return SpireReturn.Continue(); }
            activeScreen.drawRewards();
            if (activeScreen.rewards.size() == 0) { return SpireReturn.Continue(); }
            // Dismiss bubble and previous options
            for (AbstractGameEffect e : AbstractDungeon.effectList) {
                if (e instanceof InfiniteSpeechBubble) {
                    ((InfiniteSpeechBubble) e).dismiss();
                }
            }
            __instance.roomEventText.clearRemainingOptions();
            // Set speech
            AbstractDungeon.effectList.add(new InfiniteSpeechBubble(DIALOG_X, DIALOG_Y, activeScreen.getSpeech()));
            // Set options
            ___screenNum[0] = activeScreen.currScreen;
            NeowEventPatch.neowRewards.set(__instance, activeScreen.rewards);
            __instance.roomEventText.updateDialogOption(0, activeScreen.rewards.get(0).optionLabel);
            for (int i = 1; i < activeScreen.rewards.size(); i++) {
                __instance.roomEventText.addDialogOption(activeScreen.rewards.get(i).optionLabel);
            }
            // Break the switch block
            NeowEvent.waitingToSave = true;
            // Copy screenNum for saving
            NeowEventPatch.screenNumPublic.set(__instance, ___screenNum[0]);
            return SpireReturn.Return();
        }

        @SpireInsertPatch(locator = NeowEventPatch.NeowEventFinalOpenMapLocator.class)
        public static SpireReturn<Void> MyButtonEffectInsertCustomScreen(NeowEvent __instance, int buttonPressed, @ByRef int[] ___screenNum) {
            if (NeowScreenManager.customScreens == null) { NeowScreenManager.initManager(); }
            // Find corresponding custom screen
            if (___screenNum[0] == 99) {  return SpireReturn.Continue(); }
            AbstractNeowScreen activeScreen = NeowScreenManager.getScreen(___screenNum[0]);
            if (activeScreen == null) { return SpireReturn.Continue(); }
            // Dismiss bubble and previous options
            for (AbstractGameEffect e : AbstractDungeon.effectList) {
                if (e instanceof InfiniteSpeechBubble) {
                    ((InfiniteSpeechBubble) e).dismiss();
                }
            }
            __instance.roomEventText.clearRemainingOptions();
            // Apply rewards
            for (int i = 0; i < activeScreen.rewards.size(); i++) {
                if (buttonPressed == i) {
                    activeScreen.rewards.get(i).activate();
                    String speech = activeScreen.rewards.get(i).getSpeech();
                    if (speech != null) {
                        AbstractDungeon.effectList.add(new InfiniteSpeechBubble(DIALOG_X, DIALOG_Y, speech));
                    }
                }
            }
            // Save
            if (activeScreen.toSave) {
                NeowEvent.waitingToSave = true;
            }
            // Check next custom screen
            AbstractNeowScreen nextActiveScreen = NeowScreenManager.getScreen(activeScreen.nextScreen);
            // If no next custom screen or next screen no option, end
            if (nextActiveScreen != null) { nextActiveScreen.drawRewards(); }
            if (nextActiveScreen == null || nextActiveScreen.rewards.size() == 0) {
                ___screenNum[0] = 99;
                __instance.roomEventText.updateDialogOption(0, NeowEvent.OPTIONS[3]);
                __instance.roomEventText.clearRemainingOptions();
                // Copy screenNum for saving
                NeowEventPatch.screenNumPublic.set(__instance, ___screenNum[0]);
                return SpireReturn.Return();
            }
            // Dismiss bubble and previous options
            for (AbstractGameEffect e : AbstractDungeon.effectList) {
                if (e instanceof InfiniteSpeechBubble) {
                    ((InfiniteSpeechBubble) e).dismiss();
                }
            }
            __instance.roomEventText.clearRemainingOptions();
            // Set speech
            AbstractDungeon.effectList.add(new InfiniteSpeechBubble(DIALOG_X, DIALOG_Y, activeScreen.getSpeech()));
            // Set options
            ___screenNum[0] = nextActiveScreen.currScreen;
            NeowEventPatch.neowRewards.set(__instance, nextActiveScreen.rewards);
            __instance.roomEventText.updateDialogOption(0, nextActiveScreen.rewards.get(0).optionLabel);
            for (int i = 1; i < nextActiveScreen.rewards.size(); i++) {
                __instance.roomEventText.addDialogOption(nextActiveScreen.rewards.get(i).optionLabel);
            }
            // Copy screenNum for saving
            NeowEventPatch.screenNumPublic.set(__instance, ___screenNum[0]);
            return SpireReturn.Return();
        }

        public static void Postfix(NeowEvent __instance, int buttonPressed, int ___screenNum) {
            // Copy screenNum for saving
            NeowEventPatch.screenNumPublic.set(__instance, ___screenNum);
        }
    }

    // We need to update the custom rewards as well
    @SpirePatch(clz = NeowEvent.class, method = "update")
    public static class MyUpdatePatch {
        @SpireInsertPatch(locator = NeowEventPatch.NeowEventUpdateRewardLocator.class)
        public static void MyCustomNeowRewardUpdate(NeowEvent __instance) {
            for (AbstractNeowReward nr : NeowEventPatch.neowRewards.get(__instance)) {
                nr.update();
            }
        }
    }

    // Locate the last openMap function call in NeowEvent buttonEffect
    private static class NeowEventFinalOpenMapLocator extends SpireInsertLocator {
        @Override
        public int[] Locate(CtBehavior ctMethodToPatch) throws Exception {
            Matcher finalMatcher = new Matcher.MethodCallMatcher(NeowEvent.class, "openMap");
            return new int[]{LineFinder.findAllInOrder(ctMethodToPatch, finalMatcher)[1]};
        }
    }

    // Locate the second screenNum field access in NeowEvent buttonEffect,
    // which occurs when buttonPressed == 3
    private static class NeowEventAfterClaimRewardLocator extends SpireInsertLocator {
        @Override
        public int[] Locate(CtBehavior ctMethodToPatch) throws Exception {
            Matcher finalMatcher = new Matcher.FieldAccessMatcher(NeowEvent.class, "screenNum");
            return new int[]{LineFinder.findAllInOrder(ctMethodToPatch, finalMatcher)[1]};
        }
    }

    // Locate the rewards field access in NeowEvent update
    public static class NeowEventUpdateRewardLocator extends SpireInsertLocator {
        @Override
        public int[] Locate(CtBehavior ctMethodToPatch) throws Exception {
            Matcher finalMatcher = new Matcher.FieldAccessMatcher(NeowEvent.class, "rewards");
            return LineFinder.findInOrder(ctMethodToPatch, finalMatcher);
        }
    }

}
