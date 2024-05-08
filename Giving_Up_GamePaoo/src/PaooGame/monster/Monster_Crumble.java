package PaooGame.monster;

import PaooGame.Game;
import PaooGame.Graphics.Assets;
import PaooGame.entity.Entity;

import java.util.Random;

public class Monster_Crumble extends Entity {
    private int actionLockCounter;
    public Monster_Crumble(Game game) {
        super(game);
        type=4;                     // a field that denotes the type of the entity created; MONSTER_CRUMBLE's type is 4;
        name = "Crumble";           // Red monster, resembling a rock with an eye; it is made out of magma so don't touch it; it rolls on the ground;
        speed = 2;
        maxHealth = 10;
        health = maxHealth;
        actionLockCounter=0;
        action = 0;                 // 0 - UP , 1 - LEFT, 2 - DOWN , 3 - RIGHT

        barrier.x = 5;
        barrier.y = 5;
        barrier.width = 40;
        barrier.height = 45;

        barrierInitialX = barrier.x;
        barrierInitialY = barrier.y;
        img = Assets.monster_2[0];

    }

    public void update() {
        setAction();

        collisionOn = false;
        game.collissionChecker.checkTile(this);
        game.collissionChecker.checkItem(this,false);
        game.collissionChecker.checkEntity(this,game.npc);
        game.collissionChecker.checkEntity(this,game.monster);
        boolean attackPlayer = game.collissionChecker.checkPlayer(this);

        if (attackPlayer) {
            game.playSE(2);
            Game.getPlayer().alive=false;
        }

        switch (action) {
            case 1: SetSprite(Assets.monster_2[counter/5]);break;               // UP
            case 2: SetSprite(Assets.monster_2[counter/5]);break;               // LEFT
            case 3: SetSprite(Assets.monster_2[counter/5]);break;               // DOWN
            case 4: SetSprite(Assets.monster_2[counter/5]);break;               // RIGHT
        }

        if(!collisionOn) {
            switch (action) {
                case 1: worldY -= speed;break;              // UP
                case 2: worldX -= speed;break;              // LEFT
                case 3: worldY += speed;break;              // DOWN
                case 4: worldX += speed;break;              // RIGHT
            }
        }

        counter++;
        if(counter==30) {
            counter=0;
        }

        if (invincible) {
            hpBarOn = true;
            hpBarCounter=0;
            invincibleCounter++;
            if ( invincibleCounter > 30) {
                invincible=false;
                invincibleCounter=0;
            }
        }
    }

    public void setAction() {
        actionLockCounter++;
        if(actionLockCounter >= 75 || collisionOn) {
            Random rand = new Random();
            switch(rand.nextInt(100)/25) {
                case 0:
                    action = 1;     //up
                    break;
                case 1:
                    action = 2;     //left
                    break;
                case 2:
                    action = 3;     //down
                    break;
                case 3:
                    action = 4;     //right
                    break;
            }
            actionLockCounter=0;
        }
    }

    @Override
    public int getAction() {
        return action;
    }

    @Override
    public int getSpeed() {
        return speed;
    }

    public void damageReaction() {
        actionLockCounter=0;
        action = Game.getPlayer().getAction();
        if(action==0) {
            actionLockCounter=74;
            setAction();
        }
    }
}
