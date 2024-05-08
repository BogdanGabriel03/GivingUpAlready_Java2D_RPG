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
    public boolean won = false;
    public boolean wonMessageOn = false;
    public boolean enteredNewLvl = true;

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
        worldX = 31*Tile.TILE_SIZE;
        worldY = 46*Tile.TILE_SIZE;
        type=0;                                                 // a field that denotes the type of the entity created; PLAYER's type is 0;
    }

    public static Player Instance(Game game,KeyHandler keyH) {
        if(_instance == null) {
            _instance = new Player(game,keyH);
        }
        return _instance;
    }

    public void update() {
        if(enteredNewLvl) {
            setPosition();
            enteredNewLvl=false;
        }

        attacking = keyH.getAttackState();
        if ( attacking ) {
            playerAttack();
        }
        else {
            switch (keyH.getAction()) {
                case 1: action = 1;break;               // UP
                case 2: action = 2;break;               // LEFT
                case 3: action = 3;break;               // DOWN
                case 4: action = 4;break;               // RIGHT
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
                case 1: worldY -= speed;break;          // UP
                case 2: worldX -= speed;break;          // LEFT
                case 3: worldY += speed;break;          // DOWN
                case 4: worldX += speed;break;          // RIGHT
            }
        }

        if(!alive) {
            Game.setGameState(Game.GameState.END_LEVEL_STATE);
        }
    }

    public void draw(Graphics2D g) {
        switch(action) {
            case 1: // UP
                if(!attacking) {SetSprite(Assets.playerUp[counter/5]);}
                if(attacking) {SetSprite(Assets.attackLeft[attackCounter/5]);}              // need sprites for player attacking animation upward
                break;
            case 2: // LEFT
                if(!attacking) {SetSprite(Assets.playerLeft[counter/5]);}
                if(attacking) {SetSprite(Assets.attackLeft[attackCounter/5]);}
                break;
            case 3: // DOWN
                if(!attacking) {SetSprite(Assets.playerDown[counter/5]);}
                if(attacking) {SetSprite(Assets.attackRight[attackCounter/5]);}             // need sprites for player attacking animation downward
                break;
            case 4: // RIGHT
                if(!attacking) {SetSprite(Assets.playerRight[counter/5]);}
                if(attacking) {SetSprite(Assets.attackRight[attackCounter/5]);}
                break;
            default:
                break;
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

            switch(action) {
                case 1: worldY -= attackArea.height;break;              // UP
                case 2: worldX -= attackArea.width;break;               // LEFT
                case 3: worldY += attackArea.height;break;              // DOWN
                case 4: worldX += attackArea.width;break;               // RIGHT
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
            keyH.setAttackState(false);
        }
    }

    private void setPosition() {
        SetSprite(Assets.playerDown[0]);                                                        // Setting player start position when entering a new level and initial sprite
        switch(game.currentLevel) {
            case 1: worldX = 31*Tile.TILE_SIZE; worldY = 46*Tile.TILE_SIZE; break;              // LEVEL 1
            case 2: worldX = 33*Tile.TILE_SIZE; worldY = 57*Tile.TILE_SIZE; break;              // LEVEL 2
            case 3: worldX = 9*Tile.TILE_SIZE; worldY = 63*Tile.TILE_SIZE; break;               // LEVEL 3
            default: break;
        }
    }

    public void handleObject(int idx) {
        if(idx!=999) {
            String itemName = game.items.get(idx).name;
            switch(itemName) {
                case "chest":
                    game.playSE(1);
                    Item_Chest chest = (Item_Chest)game.items.get(idx);
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
                    game.items.remove(idx);
                    break;
                default:
                    break;
            }
        }
    }

    public void handleInterractNpc(int idx) {
        if(idx!=999) {

            if(keyH.getDialogue()) {
                Game.setGameState(Game.GameState.DIALOGUE_STATE);
                game.npc.get(idx).speak();
            }
        }
        keyH.setDialogue(false);
    }

    public void handleInterractMonster(int idx) {
        if ( idx != 999) {
            game.playSE(2);
            invincible = true;
            //HE DEAD
            alive = false;
        }
    }

    public void damageMonster(int idx) {
        if(idx!=999) {
            if(!game.monster.get(idx).invincible) {
                game.playSE(3);
                game.monster.get(idx).health -= 2;                          // REPLACE WITH PLAYER ATTACK EVENTUALLY
                game.monster.get(idx).invincible=true;
                game.monster.get(idx).damageReaction();
                if(game.monster.get(idx).health <= 0) {
                    game.monster.get(idx).dying = true;
                }
            }

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

    @Override
    public int getSpeed() {
        return speed;
    }
}
