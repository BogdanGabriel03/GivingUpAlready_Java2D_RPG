package PaooGame.Item;

import PaooGame.Game;
import PaooGame.Graphics.Assets;
import PaooGame.entity.Entity;

import java.util.Random;

public class Item_Chest extends Entity {

    // Chest content =>  { (0 - ATK Up) , (1 - Speed Up) , (2 - Range Up) , (3 - Time Back) }

    public Item_Chest(Game game) {
        super(game);
        name = "chest";
        img = Assets.chest;
        collision = true;
        type = 3;
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
        Random rand = new Random();
        return rand.nextInt(2);
    }
}
