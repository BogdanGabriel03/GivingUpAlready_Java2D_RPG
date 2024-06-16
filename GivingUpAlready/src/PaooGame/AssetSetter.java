package PaooGame;

import PaooGame.Item.Item_Chest;
import PaooGame.Item.Spell_Chest;
import PaooGame.Tiles.Tile;
import PaooGame.entity.Entity;
import PaooGame.entity.NPC_FinalBoss;
import PaooGame.monster.Monster_Blight;
import PaooGame.monster.Monster_Bloom;
import PaooGame.monster.Monster_Crumble;

public class AssetSetter {
    Game game;
    public AssetSetter(Game game) {
        this.game = game;
    }

    private void setObject() {
        game.items.clear();
        switch(game.currentLevel) {
            case 1:
                game.items.add(createAsset(3,17,25));
                game.items.add(createAsset(5,45,38));
                break;
            case 2:
                game.items.add(createAsset(3,14,9));
                game.items.add(createAsset(3,55,37));
                game.items.add(createAsset(5,24,35));
                break;
            case 3:
                game.items.add(createAsset(3,31,18));
                game.items.add(createAsset(3,48,48));       //5
                break;
            default: break;
        }
    }

    private void setNPC() {
        game.npc.clear();
        switch(game.currentLevel) {
            case 1:
                game.npc.add(createAsset(1,26,9));
                break;
            case 2:
                game.npc.add(createAsset(1,54,7));
                break;
            case 3:
                game.npc.add(createAsset(1,15,28));
            default: break;
        }
    }

    private void setMonster() {
            game.monster.clear();
            switch(game.currentLevel) {
                case 1:
                //game.monster.add(createAsset(2,29,12));
                //game.monster.add(createAsset(2,34,47));
                //game.monster.add(createAsset(2,28,32));
                //game.monster.add(createAsset(2,32,8));
                //game.monster.add(createAsset(2,20,9));
                game.monster.add(createAsset(2,24,49));
                break;
            case 2:
                //game.monster.add(createAsset(2,11,17));
                //game.monster.add(createAsset(2,34,56));
                game.monster.add(createAsset(2,10,9));
                //game.monster.add(createAsset(2,19,44));
                //game.monster.add(createAsset(4,49,37));
                //game.monster.add(createAsset(4,23,32));
                game.monster.add(createAsset(4,25,32));
                //game.monster.add(createAsset(4,44,7));
                break;
            case 3:
                game.monster.add(createAsset(2,22,58));
                game.monster.add(createAsset(2,18,63));
                game.monster.add(createAsset(4,60,57));
                game.monster.add(createAsset(4,60,47));
                game.monster.add(createAsset(4,30,18));
                game.monster.add(createAsset(4,47,33));
                game.monster.add(createAsset(4,17,24));
                game.monster.add(createAsset(6,42,8));          //level3 type monster
                game.monster.add(createAsset(6,20,8));          //level3 type monster
                game.monster.add(createAsset(6,20,10));         //level3 type monster


                break;
            default: break;
        }
    }

    public void setLevel() {
        setObject();
        setNPC();
        setMonster();
    }

    private Entity createAsset(int type, int x, int y) {
        Entity entity = switch (type) {
            case 1 -> new NPC_FinalBoss(game);
            case 2 -> new Monster_Bloom(game);
            case 3 -> new Item_Chest(game);
            case 4 -> new Monster_Crumble(game);
            case 5 -> new Spell_Chest(game);
            default -> new Monster_Blight(game);
        };
        entity.worldX = x * Tile.TILE_SIZE;
        entity.worldY = y * Tile.TILE_SIZE;
        return entity;
    }
}
