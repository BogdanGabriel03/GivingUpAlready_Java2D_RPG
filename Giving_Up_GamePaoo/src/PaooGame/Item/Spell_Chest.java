package PaooGame.Item;

import PaooGame.Game;
import PaooGame.Graphics.Assets;
import PaooGame.entity.Entity;

import java.util.Random;

public class Spell_Chest extends Entity {

    // CHEST CONTENT    =>      [0 - FIRE_SPELL]    [1 - STONE_SHIELD]      [2 - WATER_WALKING]
    public Spell_Chest(Game game) {
        super(game);
        name = "spellChest";
        img = Assets.chest;
        collision = true;
        type = 5;                    // a field that denotes the type of the entity created; SPELL_CHEST's type is 5;
    }

    @Override
    public int getAction() {
        return 0;
    }

    @Override
    public int getSpeed() {
        return 0;
    }

    public int getChestContent() {
        if(game.currentLevel==1) return 0;
        else if(game.currentLevel==2) {
            //Random rand = new Random();                               // Needs to be implemented
            //return rand.nextInt(1,3);
            return 1;
        }
        else return 3;
    }
}
