package PaooGame.spells;

import PaooGame.Game;
import PaooGame.Graphics.Assets;
import PaooGame.entity.Spell;

public class StoneShield extends Spell {
    public StoneShield(Game game) {
        super(game);
        name = "StoneShield";
        maxHealth = 120;
        health = maxHealth;
        rechargeTime = 600;
        elapsedTime=0;
        img = Assets.stoneShield[0];
    }

    public void update() {
        super.update();
        action = Game.getPlayer().keyH.getAction();
        if(Game.getPlayer().collisionOn) action=0;
        speed = Game.getPlayer().getSpeed();

        switch(action) {
            case 1: worldY-=speed; break;           // UP
            case 2: worldX-=speed; break;           // LEFT
            case 3: worldY+=speed; break;           // DOWN
            case 4: worldX+=speed; break;           // RIGHT
        }

        switch(action) {                                // I have to make the sprites for that
            case 1: SetSprite(Assets.stoneShield[counter/9]); break;           // UP
            case 2: SetSprite(Assets.stoneShield[counter/9]); break;           // LEFT
            case 3: SetSprite(Assets.stoneShield[counter/9]); break;           // DOWN
            case 4: SetSprite(Assets.stoneShield[counter/9]); break;           // RIGHT
        }

        if(user == Game.getPlayer()) {
            int monsterIdx = game.collissionChecker.checkEntity(user,game.monster);
            if(monsterIdx!=999) {
                Game.getPlayer().invincible = true;
                alive=false;
            }
        }
        else {
            // what monster can become impervious to dmg
        }
    }
}
