package miniExpansion.relics;

import basemod.abstracts.CustomRelic;
import com.badlogic.gdx.graphics.Texture;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import miniExpansion.MiniExpansion;
import miniExpansion.util.TextureLoader;

import static miniExpansion.MiniExpansion.makeRelicOutlinePath;
import static miniExpansion.MiniExpansion.makeRelicPath;

public class AnglerfishFlashlight extends CustomRelic {

    /*
     * Whenever you use a potion, gain 7 gold
     */

    // ID, images, text.
    public static final String ID = MiniExpansion.makeID("AnglerfishFlashlight");
    private static final Texture IMG = TextureLoader.getTexture(makeRelicPath("Anglerfish.png"));
    private static final Texture OUTLINE = TextureLoader.getTexture(makeRelicOutlinePath("Anglerfish.png"));

    public AnglerfishFlashlight() { super(ID, IMG, OUTLINE, AbstractRelic.RelicTier.COMMON, AbstractRelic.LandingSound.FLAT); }

    public String getUpdatedDescription() {
        return this.DESCRIPTIONS[0] + 7 + this.DESCRIPTIONS[1];
    }

    @Override
    public void onUsePotion() {
        flash();
        AbstractDungeon.player.gainGold(7);
        CardCrawlGame.sound.play("GOLD_GAIN_5", 0.1F);
    }

    @Override
    public boolean canSpawn() {
        return Settings.isEndless || AbstractDungeon.floorNum <= 48;
    }


}
