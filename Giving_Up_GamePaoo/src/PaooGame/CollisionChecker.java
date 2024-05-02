package PaooGame;

import PaooGame.Tiles.Tile;
import PaooGame.entity.Entity;

import java.awt.*;

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
            case 1: //up
                entityTopRow = (entityTopWorldY - entity.getSpeed())/Tile.TILE_SIZE;
                tileNum1 = game.tileMan.getTileNum(entityTopRow,entityLeftCol);
                tileNum2 = game.tileMan.getTileNum(entityTopRow,entityRightCol);
                //System.out.println(entityTopWorldY + " " + entityLeftCol + " " + entityRightCol + " " + tileNum1 + " " + tileNum2);
                break;
            case 2: //left
                entityLeftCol = (entityLeftWorldX - entity.getSpeed())/Tile.TILE_SIZE;
                tileNum1 = game.tileMan.getTileNum(entityTopRow,entityLeftCol);
                tileNum2 = game.tileMan.getTileNum(entityBottomRow,entityLeftCol);
                break;
            case 3: //down
                entityBottomRow = (entityBottomWorldY + entity.getSpeed())/Tile.TILE_SIZE;
                tileNum1 = game.tileMan.getTileNum(entityBottomRow,entityLeftCol);
                tileNum2 = game.tileMan.getTileNum(entityBottomRow,entityRightCol);
                break;
            case 4: //right
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
        for(int i=0;i<game.items.length;++i) {
            if(game.items[i]!=null) {

                // Get entities solid area position
                entity.barrier.x = entity.worldX + entity.barrier.x;
                entity.barrier.y = entity.worldY + entity.barrier.y;
                // Get the object solid Area position
                game.items[i].barrier.x = game.items[i].worldX + game.items[i].barrier.x;
                game.items[i].barrier.y = game.items[i].worldY + game.items[i].barrier.y;

                switch(entity.getAction()) {
                    case 1: //up
                        entity.barrier.y -= entity.getSpeed();
                        if(entity.barrier.intersects(game.items[i].barrier)) {
                            if(game.items[i].collision) {
                                entity.collisionOn=true;
                            }
                            if(player) {
                                index=i;
                            }
                        }
                        break;
                    case 2: //left
                        entity.barrier.x -= entity.getSpeed();
                        if(entity.barrier.intersects(game.items[i].barrier)) {
                            if(game.items[i].collision) {
                                entity.collisionOn=true;
                            }
                            if(player) {
                                index=i;
                            }
                        }
                        break;
                    case 3: //down
                        entity.barrier.y += entity.getSpeed();
                        if(entity.barrier.intersects(game.items[i].barrier)) {
                            if(game.items[i].collision) {
                                entity.collisionOn=true;
                            }
                            if(player) {
                                index=i;
                            }
                        }
                        break;
                    case 4: //right
                        entity.barrier.x += entity.getSpeed();
                        if(entity.barrier.intersects(game.items[i].barrier)) {
                            if(game.items[i].collision) {
                                entity.collisionOn=true;
                            }
                            if(player) {
                                index=i;
                            }
                        }
                        break;
                }
                entity.barrier.x= entity.barrierInitialX;
                entity.barrier.y=entity.barrierInitialY;
                game.items[i].barrier.x=game.items[i].barrierInitialX;
                game.items[i].barrier.y=game.items[i].barrierInitialY;
            }
        }
        return index;
    }

    public int checkEntity(Entity entity, Entity[] target) {
        int index = 999;
        for(int i=0;i<target.length;++i) {
            if (target[i] != null && !target[i].dying) {


                    // Get entity's solid area position
                    entity.barrier.x += entity.worldX;
                    entity.barrier.y += entity.worldY;
                    // Get the target solid Area position
                    target[i].barrier.x += target[i].worldX;
                    target[i].barrier.y += target[i].worldY;

                    switch (entity.getAction()) {
                        case 1: //up
                            entity.barrier.y -= entity.getSpeed();
                            if (entity.barrier.intersects(target[i].barrier)) {
                                entity.collisionOn = true;
                                index = i;
                            }
                            break;
                        case 2: //left
                            entity.barrier.x -= entity.getSpeed();
                            if (entity.barrier.intersects(target[i].barrier)) {
                                entity.collisionOn = true;
                                index = i;
                            }
                            break;
                        case 3: //down
                            entity.barrier.y += entity.getSpeed();
                            if (entity.barrier.intersects(target[i].barrier)) {
                                entity.collisionOn = true;
                                index = i;
                            }
                            break;
                        case 4: //right
                            entity.barrier.x += entity.getSpeed();
                            if (entity.barrier.intersects(target[i].barrier)) {
                                entity.collisionOn = true;
                                index = i;
                            }
                            break;
                    }
                if(target[i].type==1 && entity.type==0)
                {
                    Rectangle npcInteractArea = new Rectangle(entity.barrier.x -30,entity.barrier.y - 30,110,110);
                    if (npcInteractArea.intersects(target[i].barrier)) {
                        index = i;
                    }
                }
                entity.barrier.x = entity.barrierInitialX;
                entity.barrier.y = entity.barrierInitialY;
                target[i].barrier.x = target[i].barrierInitialX;
                target[i].barrier.y = target[i].barrierInitialY;
            }
        }
        return index;
    }

    public boolean checkPlayer(Entity entity) {

        boolean contactPlayer = false;

        // Get entity's solid area position
        entity.barrier.x = entity.worldX + entity.barrier.x;
        entity.barrier.y = entity.worldY + entity.barrier.y;
        // Get the target solid Area position
        Game.getPlayer().barrier.x = Game.getPlayer().worldX + Game.getPlayer().barrier.x;
        Game.getPlayer().barrier.y = Game.getPlayer().worldY + Game.getPlayer().barrier.y;

        switch(entity.getAction()) {
            case 1: //up
                entity.barrier.y -= entity.getSpeed();
                break;
            case 2: //left
                entity.barrier.x -= entity.getSpeed();
                break;
            case 3: //down
                entity.barrier.y += entity.getSpeed();
                break;
            case 4: //right
                entity.barrier.x += entity.getSpeed();
                break;
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
