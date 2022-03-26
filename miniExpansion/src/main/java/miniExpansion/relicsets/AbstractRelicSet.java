package miniExpansion.relicsets;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.RelicLibrary;
import com.megacrit.cardcrawl.relics.AbstractRelic;

import java.util.ArrayList;

public abstract class AbstractRelicSet {
    public ArrayList<String> relics;
    public String reward;
    public boolean allRequired;
    public int numRequired;

    public AbstractRelicSet() {
        this.relics = new ArrayList<>();
        this.reward = "";
        this.allRequired = true;
        this.numRequired = -1;
    }

    public AbstractRelicSet(String reward, boolean allRequired, int numRequired) {
        this.relics = new ArrayList<>();
        this.reward = reward;
        this.allRequired = allRequired;
        this.numRequired = numRequired;
    }

    public boolean setCollected(){
        AbstractPlayer p = AbstractDungeon.player;
        //if (p == null) { return false; }
        int numCollected = 0;
        for (String r : this.relics) {
            if (!p.hasRelic(r) && this.allRequired) {
                return false;
            } else {
                numCollected++;
            }
        }
        return (numCollected >= this.numRequired);
    }

    public AbstractRelic getReward(){
        return RelicLibrary.getRelic(this.reward);
    }
}
