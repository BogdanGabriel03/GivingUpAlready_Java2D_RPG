package PaooGame.entity;

import PaooGame.Game;
import PaooGame.Graphics.Assets;
import PaooGame.Item.Item_Chest;
import PaooGame.Item.Spell_Chest;
import PaooGame.KeyHandler;
import PaooGame.Tiles.Tile;
import PaooGame.spells.FireSpell;
import PaooGame.spells.StoneShield;

import java.awt.*;
import java.awt.image.BufferedImage;

public class Player extends Entity{
    private int attackCounter =0;
    public KeyHandler keyH;
    private final int screenX;
    private final int screenY;
    static Player _instance = null;
    public boolean won = false;
    public boolean wonMessageOn = false;
    public boolean enteredNewLvl = true;
    private final boolean[] spells = new boolean[3];
    private final Spell[] spellInstances = new Spell[3];
    private final int INITIAL_SPEED=8;
    private final int INITIAL_ATTACK=3;

    protected Player(Game game,KeyHandler keyH) {
        super(game);
        speed=INITIAL_SPEED;
        attack=INITIAL_ATTACK;
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
            if ( keyH.getAction()!=0) action = keyH.getAction();
            if(keyH.getAction() != 0 ) {
                counter++;
                if(counter==20) {
                    counter=0;
                }
            }
        }

        int spellIdx = keyH.getCastedSpell();
        if(spellIdx!=-1 && spells[spellIdx] && spellInstances[spellIdx].elapsedTime == 0) {
            spellInstances[spellIdx].set(worldX,worldY,action,true,this);
            spellInstances[spellIdx].elapsedTime=spellInstances[spellIdx].rechargeTime;
            game.spellList.add(spellInstances[spellIdx]);
        }
        else if(spellIdx!=-1 && !spells[spellIdx]){
            game.ui.showMessage("Spell not discovered yet!", 25, 6*Tile.TILE_SIZE);
        }
        else if(spellIdx!=-1 && spells[spellIdx] && spellInstances[spellIdx].elapsedTime !=0) {
            game.ui.showMessage("Spell recharging!", 25, 6*Tile.TILE_SIZE);
        }
        else if(spellIdx!=-1){
            game.ui.showMessage("Unknown problem!", 25, 6*Tile.TILE_SIZE);
        }

        for ( Spell s : spellInstances) {
            if(s!=null && s.elapsedTime!=0) {
                s.elapsedTime--;
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

        if (invincible) {
            invincibleCounter++;
            if ( invincibleCounter > 60) {
                invincible=false;
                invincibleCounter=0;
            }
        }

        if(!alive) {
            resetPlayer();
            Game.setGameState(Game.GameState.END_LEVEL_STATE);
        }
    }

    public void draw(Graphics2D g) {
        switch(action) {
            case 1: // UP
                if(!attacking) {SetSprite(Assets.playerUp[counter/5]);}
                if(attacking) {SetSprite(Assets.attackUp[attackCounter/5]);}
                break;
            case 2: // LEFT
                if(!attacking) {SetSprite(Assets.playerLeft[counter/5]);}
                if(attacking) {SetSprite(Assets.attackLeft[attackCounter/5]);}
                break;
            case 3: // DOWN
                if(!attacking) {SetSprite(Assets.playerDown[counter/5]);}
                if(attacking) {SetSprite(Assets.attackDown[attackCounter/5]);}
                break;
            case 4: // RIGHT
                if(!attacking) {SetSprite(Assets.playerRight[counter/5]);}
                if(attacking) {SetSprite(Assets.attackRight[attackCounter/5]);}
                break;
            default:
                break;
        }
        if(invincible && invincibleCounter/15%2==0) {
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
            damageMonster(monsterIdx,this.attack);

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
            case 1: worldX = 27*Tile.TILE_SIZE; worldY = 49*Tile.TILE_SIZE; break;              // LEVEL 1
            case 2: worldX = 16*Tile.TILE_SIZE; worldY = 56*Tile.TILE_SIZE; break;              // LEVEL 2
            case 3: worldX = 9*Tile.TILE_SIZE; worldY = 64*Tile.TILE_SIZE; break;               // LEVEL 3
            default: break;
        }
    }

    public void resetPlayer() {
        // RESETTING PLAYER
        for ( int i=0;i<3;++i) {
            spells[i]=false;
            spellInstances[i]=null;
        }
        invincible=false;
        invincibleCounter=0;
        speed = INITIAL_SPEED;
        attack = INITIAL_ATTACK;
    }

    public void handleObject(int idx) {
        if(idx!=999) {
            String itemName = game.items.get(idx).name;
            switch(itemName) {
                case "itemChest":
                    game.playSE(1);
                    Item_Chest iChest = (Item_Chest)game.items.get(idx);
                    switch(iChest.getChestContent()) {
                        case 0: game.ui.showMessage("ATTACK UP!",25, 4*Tile.TILE_SIZE);  setAttack(1);   break;
                        case 1: game.ui.showMessage("SPEED UP!", 25, 4*Tile.TILE_SIZE);   setSpeed(1);    break;
                        case 2: break;
                        case 3: break;
                    }
                    game.items.remove(idx);
                    break;
                case "spellChest":
                    Spell_Chest sChest = (Spell_Chest)game.items.get(idx);
                    switch(sChest.getChestContent()) {
                        case 0: game.ui.showMessage("NEW SPELL UNLOCKED! <FIREBALL>", 25, 4* Tile.TILE_SIZE);           spells[0]=true;spellInstances[0] = new FireSpell(game);         break;
                        case 1: game.ui.showMessage("NEW SPELL UNLOCKED! <STONE SHIELD>", 25, 4* Tile.TILE_SIZE);       spells[1] = true;spellInstances[1] = new StoneShield(game);       break;          // to be modified
                        case 2: game.ui.showMessage("NEW SPELL UNLOCKED! <WATER WALKING>", 25, 4* Tile.TILE_SIZE);     spells[2] = true;spellInstances[2] = new FireSpell(game);       break;          // to be modified
                        default: break;
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
            //HE DEAD
            if(!invincible) {
                alive = false;
                game.playSE(2);
            }
        }
    }

    public void damageMonster(int idx, int attack) {
        if(idx!=999) {
            if(!game.monster.get(idx).invincible) {
                game.playSE(3);
                game.monster.get(idx).health -= attack;                          // REPLACE WITH PLAYER ATTACK EVENTUALLY (DONE)
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
    public int getAction() {return action;}
    public int getAttack() {return attack;}
    public void setAttack(int change) {attack+=change; }
    public void setSpeed(int change) {speed+=change; }
    public boolean getSpell(int idx) {return spells[idx];}
    public void setSpell(int idx,int val) {spells[idx] = (val == 1);}
    public Spell getSpellInstance(int idx) {return spellInstances[idx];}
    public void setSpellInstance(int idx) {
        if(idx==0) spellInstances[idx] = new FireSpell(game);
        else if(idx==1) spellInstances[idx] = new StoneShield(game);
        else if(idx==2) spellInstances[idx] = new FireSpell(game);
    }

    @Override
    public int getSpeed() {
        return speed;
    }
}
