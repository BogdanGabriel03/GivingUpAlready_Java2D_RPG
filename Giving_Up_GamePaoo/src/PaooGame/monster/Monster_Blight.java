package PaooGame.monster;

import PaooGame.Game;
import PaooGame.Graphics.Assets;
import PaooGame.entity.Entity;
import PaooGame.entity.Spell;
import PaooGame.spells.CrystalBeam;

import java.util.Random;

public class Monster_Blight extends Entity {
    private int actionLockCounter;
    private final Spell spell;
    public Monster_Blight(Game game) {
        super(game);
        type = 6;
        name = "Blight";             // Conglomerate of crystal eyes shooting shiny yet deadly beams of hardened light
        speed = 1;
        maxHealth= 15;
        health = maxHealth;
        actionLockCounter=0;        // Monster will walk a direction for 2 sec then choose other direction to go in
        action = 1;                 // 0 - UP , 1 - LEFT, 2 - DOWN , 3 - RIGHT

        barrier.x = 8;
        barrier.y = 8;
        barrier.width = 34;
        barrier.height = 42;

        barrierInitialX = barrier.x;
        barrierInitialY = barrier.y;

        img = Assets.monster_3[0];

        spell = new CrystalBeam(game);
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
            if(!Game.getPlayer().invincible) {
                game.playSE(2);
                Game.getPlayer().alive=false;
            }
        }

        switch (action) {
            case 1: SetSprite(Assets.monster_3[counter/7]);break;               // UP
            case 2: SetSprite(Assets.monster_3[counter/7]);break;               // LEFT
            case 3: SetSprite(Assets.monster_3[counter/7]);break;               // DOWN
            case 4: SetSprite(Assets.monster_3[counter/7]);break;               // RIGHT
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
        if(counter==28) { counter=0; }

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
        if(actionLockCounter >= 60 || collisionOn) {
            Random rand = new Random();
            switch(rand.nextInt(100)/25) {
                case 0: action = 1;break;
                case 1: action = 2;break;
                case 2: action = 3;break;
                case 3: action = 4;break;
            }
            actionLockCounter=0;
        }
        int i = new Random().nextInt(100)+1;
        if(i>50 && !spell.alive) {
            spell.set(worldX,worldY,action,true,this);
            game.spellList.add(spell);
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
