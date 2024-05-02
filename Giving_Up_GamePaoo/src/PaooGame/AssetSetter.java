package PaooGame;

import PaooGame.Item.Item_Chest;
import PaooGame.Tiles.Tile;
import PaooGame.entity.NPC_FinalBoss;
import PaooGame.monster.Monster_Bloom;

public class AssetSetter {
    Game game;
    public AssetSetter(Game game) {
        this.game = game;
    }

    public void setObject() {
        switch(game.currentLevel) {
            case 1:
                game.items[0] = new Item_Chest(game);
                game.items[0].worldX = 17 * Tile.TILE_SIZE;
                game.items[0].worldY = 25 * Tile.TILE_SIZE;

                game.items[1] = new Item_Chest(game);
                game.items[1].worldX = 45 * Tile.TILE_SIZE;
                game.items[1].worldY = 38 * Tile.TILE_SIZE;

                break;
            case 2:
                game.items[0] = new Item_Chest(game);
                game.items[0].worldX = 17 * Tile.TILE_SIZE;
                game.items[0].worldY = 25 * Tile.TILE_SIZE;

                game.items[1] = new Item_Chest(game);
                game.items[1].worldX = 45 * Tile.TILE_SIZE;
                game.items[1].worldY = 38 * Tile.TILE_SIZE;

                break;
            case 3:
                game.items[0] = new Item_Chest(game);
                game.items[0].worldX = 17 * Tile.TILE_SIZE;
                game.items[0].worldY = 25 * Tile.TILE_SIZE;

                game.items[1] = new Item_Chest(game);
                game.items[1].worldX = 45 * Tile.TILE_SIZE;
                game.items[1].worldY = 38 * Tile.TILE_SIZE;

                break;
            default:
                break;
        }
    }

    public void setNPC() {
        switch(game.currentLevel) {
            case 1:
                game.npc[0] = new NPC_FinalBoss(game);
                game.npc[0].worldX = 26 *Tile.TILE_SIZE;
                game.npc[0].worldY = 9 * Tile.TILE_SIZE;
                break;
            case 2:
                game.npc[0] = new NPC_FinalBoss(game);
                game.npc[0].worldX = 23 *Tile.TILE_SIZE;
                game.npc[0].worldY = 44 * Tile.TILE_SIZE;
            case 3:
                game.npc[0] = new NPC_FinalBoss(game);
                game.npc[0].worldX = 23 *Tile.TILE_SIZE;
                game.npc[0].worldY = 44 * Tile.TILE_SIZE;
            default:
                break;
        }
    }

    public void setMonster() {

        switch(game.currentLevel) {
            case 1:
                game.monster[0] = new Monster_Bloom(game);
                game.monster[0].worldX = 29 * Tile.TILE_SIZE;
                game.monster[0].worldY = 45 * Tile.TILE_SIZE;

                game.monster[1] = new Monster_Bloom(game);
                game.monster[1].worldX = 34 * Tile.TILE_SIZE;
                game.monster[1].worldY = 47 * Tile.TILE_SIZE;

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
                game.monster[5].worldY = 49 * Tile.TILE_SIZE;

                break;
            case 2:
                game.monster[0] = new Monster_Bloom(game);
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
                game.monster[5].worldY = 49 * Tile.TILE_SIZE;

                break;
            case 3:
                game.monster[0] = new Monster_Bloom(game);
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
                game.monster[5].worldY = 49 * Tile.TILE_SIZE;

                break;
            default:
                break;
        }
    }

    public void setLevel() {
        setObject();
        setNPC();
        setMonster();
    }
}
