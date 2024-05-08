package PaooGame;

import PaooGame.Item.Item_Chest;
import PaooGame.Tiles.Tile;
import PaooGame.entity.Entity;
import PaooGame.entity.NPC_FinalBoss;
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
                game.items.add(createAsset(3,45,38));
                break;
            case 2:
                game.items.add(createAsset(3,14,9));
                game.items.add(createAsset(3,55,37));
                game.items.add(createAsset(3,24,35));
                break;
            case 3:
                /*game.items[0] = new Item_Chest(game);
                game.items[0].worldX = 17 * Tile.TILE_SIZE;
                game.items[0].worldY = 25 * Tile.TILE_SIZE;

                game.items[1] = new Item_Chest(game);
                game.items[1].worldX = 45 * Tile.TILE_SIZE;
                game.items[1].worldY = 38 * Tile.TILE_SIZE;*/

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
                game.npc.add(createAsset(1,22,58));
            default: break;
        }
    }

    private void setMonster() {
            game.monster.clear();
            switch(game.currentLevel) {
                case 1:
                game.monster.add(createAsset(2,29,12));
                game.monster.add(createAsset(2,34,47));
                game.monster.add(createAsset(2,28,32));
                game.monster.add(createAsset(2,32,8));
                game.monster.add(createAsset(2,20,9));
                game.monster.add(createAsset(2,24,49));
                break;
            case 2:
                game.monster.add(createAsset(2,11,17));
                game.monster.add(createAsset(2,34,56));
                game.monster.add(createAsset(2,10,9));
                game.monster.add(createAsset(2,19,44));
                game.monster.add(createAsset(4,49,37));
                game.monster.add(createAsset(4,23,32));
                game.monster.add(createAsset(4,25,32));
                game.monster.add(createAsset(4,44,7));
                break;
            case 3:
                /*game.monster[0] = new Monster_Bloom(game);
                game.monster[0].worldX = 29 * Tile.TILE_SIZE;
                game.monster[0].worldY = 45 * Tile.TILE_SIZE;

                game.monster[1] = new Monster_Bloom(game);
                game.monster[1].worldX = 34 * Tile.TILE_SIZE;
                game.monster[1].worldY = 56 * Tile.TILE_SIZE;

                game.monster[2] = new Monster_Bloom(game);
                game.monster[2].worldX = 28 * Tile.TILE_SIZE;
                game.monster[2].worldY = 32 * Tile.TILE_SIZE;

                game.monster[3] = new Monster_Bloom(game);
                game.monster[3].worldX = 32 * Tile.TILE_SIZE;
                game.monster[3].worldY = 8 * Tile.TILE_SIZE;

                game.monster[4] = new Monster_Bloom(game);
                game.monster[4].worldX = 20 * Tile.TILE_SIZE;
                game.monster[4].worldY = 9 * Tile.TILE_SIZE;

                game.monster[5] = new Monster_Bloom(game);
                game.monster[5].worldX = 24 * Tile.TILE_SIZE;
                game.monster[5].worldY = 49 * Tile.TILE_SIZE;*/

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
            default -> new Monster_Crumble(game);
        };
        entity.worldX = x * Tile.TILE_SIZE;
        entity.worldY = y * Tile.TILE_SIZE;
        return entity;
    }
}
