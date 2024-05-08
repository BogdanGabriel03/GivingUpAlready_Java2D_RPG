package PaooGame;

import PaooGame.Tiles.Tile;
import PaooGame.entity.Entity;

import java.awt.*;
import java.util.ArrayList;

public class CollisionChecker {
    Game game;

    public CollisionChecker(Game game) {
        this.game = game;
    }

    public void checkTile(Entity entity) {
        int entityLeftWorldX = entity.worldX + entity.barrier.x;
        int entityRightWorldX = entity.worldX + entity.barrier.x+entity.barrier.width;
        int entityTopWorldY = entity.worldY + entity.barrier.y;
        int entityBottomWorldY = entity.worldY + entity.barrier.y + entity.barrier.height;

        int entityLeftCol = entityLeftWorldX/ Tile.TILE_SIZE;
        int entityRightCol = entityRightWorldX/Tile.TILE_SIZE;
        int entityTopRow = entityTopWorldY/Tile.TILE_SIZE;
        int entityBottomRow = entityBottomWorldY/Tile.TILE_SIZE;

        int tileNum1,tileNum2;

        switch(entity.getAction()) {
            case 1: // UP
                entityTopRow = (entityTopWorldY - entity.getSpeed())/Tile.TILE_SIZE;
                tileNum1 = game.tileMan.getTileNum(entityTopRow,entityLeftCol);
                tileNum2 = game.tileMan.getTileNum(entityTopRow,entityRightCol);
                break;
            case 2: // LEFT
                entityLeftCol = (entityLeftWorldX - entity.getSpeed())/Tile.TILE_SIZE;
                tileNum1 = game.tileMan.getTileNum(entityTopRow,entityLeftCol);
                tileNum2 = game.tileMan.getTileNum(entityBottomRow,entityLeftCol);
                break;
            case 3: // DOWN
                entityBottomRow = (entityBottomWorldY + entity.getSpeed())/Tile.TILE_SIZE;
                tileNum1 = game.tileMan.getTileNum(entityBottomRow,entityLeftCol);
                tileNum2 = game.tileMan.getTileNum(entityBottomRow,entityRightCol);
                break;
            case 4: // RIGHT
                entityRightCol = (entityRightWorldX + entity.getSpeed())/Tile.TILE_SIZE;
                tileNum1 = game.tileMan.getTileNum(entityTopRow,entityRightCol);
                tileNum2 = game.tileMan.getTileNum(entityBottomRow,entityRightCol);
                break;
            default:
                tileNum1=0;
                tileNum2=0;
        }
        if(game.tileMan.checkTile(tileNum1) || game.tileMan.checkTile(tileNum2)) {
            entity.collisionOn=true;
        }
    }

    public int checkItem(Entity entity, boolean player) {
        int index = 999;
        for(Entity item : game.items) {
            if(item !=null) {

                // Get entities solid area position
                entity.barrier.x = entity.worldX + entity.barrier.x;
                entity.barrier.y = entity.worldY + entity.barrier.y;

                // Get the object solid Area position
                item.barrier.x = item.worldX + item.barrier.x;
                item.barrier.y = item.worldY + item.barrier.y;

                switch(entity.getAction()) {
                    case 1: entity.barrier.y -= entity.getSpeed();break;            // UP
                    case 2: entity.barrier.x -= entity.getSpeed();break;            // LEFT
                    case 3: entity.barrier.y += entity.getSpeed();break;            // DOWN
                    case 4: entity.barrier.x += entity.getSpeed();break;            // RIGHT
                }
                if(entity.barrier.intersects(item.barrier)) {
                    if(item.collision) {
                        entity.collisionOn=true;
                    }
                    if(player) {
                        index=game.items.indexOf(item);
                    }
                }

                entity.barrier.x= entity.barrierInitialX;
                entity.barrier.y=entity.barrierInitialY;
                item.barrier.x=item.barrierInitialX;
                item.barrier.y=item.barrierInitialY;
            }
        }
        return index;
    }

    public int checkEntity(Entity entity, ArrayList<Entity> target) {
        int index = 999;
        for(Entity item : target) {
            if (item != null && !item.dying && item!=entity) {

                // Get entity's solid area position
                entity.barrier.x += entity.worldX;
                entity.barrier.y += entity.worldY;

                // Get the target solid Area position
                item.barrier.x += item.worldX;
                item.barrier.y += item.worldY;

                switch (entity.getAction()) {
                    case 1: entity.barrier.y -= entity.getSpeed();break;            // UP
                    case 2: entity.barrier.x -= entity.getSpeed();break;            // LEFT
                    case 3: entity.barrier.y += entity.getSpeed();break;            // DOWN
                    case 4: entity.barrier.x += entity.getSpeed();break;            // RIGHT
                }

                if (entity.barrier.intersects(item.barrier)) {
                    entity.collisionOn = true;
                    index = target.indexOf(item);
                }

                if(item.type==1 && entity.type==0)
                {
                    Rectangle npcInteractArea = new Rectangle(entity.barrier.x -30,entity.barrier.y - 30,110,110);
                    if (npcInteractArea.intersects(item.barrier)) {
                        index = target.indexOf(item);
                    }
                }

                entity.barrier.x = entity.barrierInitialX;
                entity.barrier.y = entity.barrierInitialY;
                item.barrier.x = item.barrierInitialX;
                item.barrier.y = item.barrierInitialY;
            }
        }
        return index;
    }

    public boolean checkPlayer(Entity entity) {

        boolean contactPlayer = false;

        // Get entity's solid area position
        entity.barrier.x = entity.worldX + entity.barrier.x;
        entity.barrier.y = entity.worldY + entity.barrier.y;

        // Get the player solid Area position
        Game.getPlayer().barrier.x = Game.getPlayer().worldX + Game.getPlayer().barrier.x;
        Game.getPlayer().barrier.y = Game.getPlayer().worldY + Game.getPlayer().barrier.y;

        switch(entity.getAction()) {
            case 1: entity.barrier.y -= entity.getSpeed();break;            // UP
            case 2: entity.barrier.x -= entity.getSpeed();break;            // LEFT
            case 3: entity.barrier.y += entity.getSpeed();break;            // DOWN
            case 4: entity.barrier.x += entity.getSpeed();break;            // RIGHT
        }

        if(entity.barrier.intersects(Game.getPlayer().barrier) && !entity.dying) {
            entity.collisionOn=true;
            contactPlayer = true;
        }

        entity.barrier.x= entity.barrierInitialX;
        entity.barrier.y=entity.barrierInitialY;
        Game.getPlayer().barrier.x=Game.getPlayer().barrierInitialX;
        Game.getPlayer().barrier.y=Game.getPlayer().barrierInitialY;

        return contactPlayer;
    }
}
