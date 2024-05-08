package PaooGame.monster;

import PaooGame.Game;
import PaooGame.Graphics.Assets;
import PaooGame.entity.Entity;

import java.awt.*;
import java.util.Random;

public class Monster_Bloom extends Entity {
    private int actionLockCounter;
    public Monster_Bloom(Game game) {
        super(game);
        type = 2;
        name = "Bloom";             // Blue winged monsters, with the main body being an eye; everything they touch with it transforms into trees;
        speed = 1;
        maxHealth= 4;
        health = maxHealth;
        actionLockCounter=0;        // Monster will walk a direction for 2 sec then choose other direction to go in
        action = 1;                 // 0 - UP , 1 - LEFT, 2 - DOWN , 3 - RIGHT

        barrier.x = 12;
        barrier.y = 24;
        barrier.width = 26;
        barrier.height = 26;

        barrierInitialX = barrier.x;
        barrierInitialY = barrier.y;
        img = Assets.monster_1[0];

        //alive = true;
        //dying = false;
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
            case 1: SetSprite(Assets.monster_1[counter/5]);break;               // UP
            case 2: SetSprite(Assets.monster_1[counter/5]);break;               // LEFT
            case 3: SetSprite(Assets.monster_1[counter/5]);break;               // DOWN
            case 4: SetSprite(Assets.monster_1[counter/5]);break;               // RIGHT
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
        if(counter==20) {counter=0;}

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

    @Override
    public void setAction() {
        actionLockCounter++;
        if(actionLockCounter >= 60) {
            Random rand = new Random();
            switch(rand.nextInt(100)/25) {
                case 0: action = 1;break;               // UP
                case 1: action = 2;break;               // LEFT
                case 2: action = 3;break;               // DOWN
                case 3: action = 4;break;               // RIGHT
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
        if(action == 0) {
            actionLockCounter=59;
            setAction();
        }
    }
}
