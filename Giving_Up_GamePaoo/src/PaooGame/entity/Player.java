package PaooGame.entity;

import PaooGame.Game;
import PaooGame.Graphics.Assets;
import PaooGame.Item.Item_Chest;
import PaooGame.KeyHandler;
import PaooGame.Tiles.Tile;

import java.awt.*;
import java.awt.image.BufferedImage;

public class Player extends Entity{
    private int attackCounter =0;
    private KeyHandler keyH;
    private final int screenX;
    private final int screenY;
    static Player _instance = null;
    protected Player(Game game,KeyHandler keyH) {
        super(game);
        speed=4;
        attack=3;
        this.keyH = keyH;
        attackArea.width = 32;
        attackArea.height = 32;
        barrier = new Rectangle(12,24,26,26);
        barrierInitialX = 12;
        barrierInitialY=24;
        img = Assets.playerDown[0];
        screenX = (Game.WND_WIDTH- Tile.TILE_SIZE)/2;
        screenY = (Game.WND_HEIGHT- Tile.TILE_SIZE)/2;
        worldX = 28*Tile.TILE_SIZE;
        worldY = 49*Tile.TILE_SIZE;
    }
    public static Player Instance(Game game,KeyHandler keyH) {
        if(_instance == null) {
            _instance = new Player(game,keyH);
        }
        return _instance;
    }

    public void update() {
        attacking = keyH.getAttackState();
        if ( attacking ) {
            playerAttack();
        }
        else {
            switch (keyH.getAction()) {
                /*case 0:
                    action = 0;
                    break;*/
                case 1: //up
                    action = 1;
                    break;
                case 2: //left
                    action = 2;
                    break;
                case 3: //down
                    action = 3;
                    break;
                case 4: //right
                    action = 4;
                    break;
            }
            if(keyH.getAction() != 0) {
                counter++;
                if(counter==20) {
                    counter=0;
                }
            }
        }

        collisionOn = false;
        game.collissionChecker.checkTile(this);

        // Check collision with objects
        int itemIdx = game.collissionChecker.checkItem(this,true);
        handleObject(itemIdx);
        // Check NPC collision
        int npcIdx = game.collissionChecker.checkEntity(this,game.npc);
        handleInterractNpc(npcIdx);
        // Check Monster collision
        int monsterIdx = game.collissionChecker.checkEntity(this,game.monster);
        handleInterractMonster(monsterIdx);

        if(!collisionOn) {
            switch (keyH.getAction()) {
                case 1: //up
                    worldY -= speed;
                    break;
                case 2: //left
                    worldX -= speed;
                    break;
                case 3: //down
                    worldY += speed;
                    break;
                case 4: //right
                    worldX += speed;
                    break;
            }
        }

        if (invincible) {
            invincibleCounter++;
            if ( invincibleCounter > 45) {
                invincible=false;
                invincibleCounter=0;
            }
        }
    }

    public void draw(Graphics2D g) {
        switch(action) {
            case 1: // up
                if(!attacking) {SetSprite(Assets.playerUp[counter/5]);}
                if(attacking) {SetSprite(Assets.attackLeft[attackCounter/5]);}
                break;
            case 2: // left
                if(!attacking) {SetSprite(Assets.playerLeft[counter/5]);}
                if(attacking) {SetSprite(Assets.attackLeft[attackCounter/5]);}
                break;
            case 3: // down
                if(!attacking) {SetSprite(Assets.playerDown[counter/5]);}
                if(attacking) {SetSprite(Assets.attackRight[attackCounter/5]);}
                break;
            case 4: // right
                if(!attacking) {SetSprite(Assets.playerRight[counter/5]);}
                if(attacking) {SetSprite(Assets.attackRight[attackCounter/5]);}
                break;
            default:
                break;
        }
        if(invincible) {
            g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.5f));
        }
        g.drawImage(this.img, screenX, screenY,null);
        g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1f));
    }

    public void playerAttack() {
        attackCounter++;
        if(attackCounter/5>=1) {

            // SAVE THE CURRENT DATA
            int currentWorldX = worldX;
            int currentWorldY = worldY;
            int solidAreaW = barrier.width;
            int solidAreaH = barrier.height;

            //
            switch(action) {
                case 1: // UP
                    worldY -= attackArea.height;
                    break;
                case 2: // LEFT
                    worldX -= attackArea.width;
                    break;
                case 3: // DOWN
                    worldY += attackArea.height;
                    break;
                case 4: // RIGHT
                    worldX += attackArea.width;
                    break;
            }

            barrier.width = attackArea.width;
            barrier.height = attackArea.height;
            // CHECK MONSTER COLLISION
            int monsterIdx = game.collissionChecker.checkEntity(this,game.monster);
            damageMonster(monsterIdx);
            // RESTORE DATA
            worldX = currentWorldX;
            worldY = currentWorldY;
            barrier.width = solidAreaW;
            barrier.height = solidAreaH;
        }
        if(attackCounter >= 20) {
            attackCounter = 0;
            attacking = false;
            keyH.setAttackState(attacking);
        }
    }

    public int getScreenX() {return screenX;}
    public int getScreenY() {return screenY;}
    public int getWorldX() {return worldX;}
    public int getWorldY() {return worldY;}
    public int getAction() {return keyH.getAction();}
    public int getAttack() {return attack;}
    public void setAttack(int change) {attack+=change; }
    public void setSpeed(int change) {speed+=change; }

    public void handleObject(int idx) {
        if(idx!=999) {
            String itemName = game.items[idx].name;
            switch(itemName) {
                case "chest":
                    game.playSE(1);
                    Item_Chest chest = (Item_Chest)game.items[idx];
                    switch(chest.getChestContent()) {
                        case 0:     // ATK UP
                            game.ui.showMessage("ATTACK UP!");
                            setAttack(1);
                            break;
                        case 1:     // Speed UP
                            game.ui.showMessage("SPEED UP!");
                            setSpeed(1);
                            break;
                        case 2:
                            break;
                        case 3:
                            break;
                    }
                    game.items[idx]=null;
                    break;
                default:
            }
        }
    }

    public void handleInterractNpc(int idx) {
        if(idx!=999) {

            if(keyH.getDialogue()) {
                Game.setGameState(Game.GameState.DIALOGUE_STATE);
                game.npc[idx].speak();
            }
        }
        keyH.setDialogue(false);
    }

    public void handleInterractMonster(int idx) {
        if ( idx != 999) {
            if(!invincible) {
                game.playSE(2);
                invincible = true;
            }
        }
    }

    public void damageMonster(int idx) {
        if(idx!=999) {
            if(!game.monster[idx].invincible) {
                game.playSE(3);
                game.monster[idx].health -= 2;
                game.monster[idx].invincible=true;
                game.monster[idx].damageReaction();
                if(game.monster[idx].health <= 0) {
                    game.monster[idx].dying = true;
                }
            }

        }
    }
    @Override
    public int getSpeed() {
        return speed;
    }
}
