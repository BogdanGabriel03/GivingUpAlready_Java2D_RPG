package PaooGame.spells;

import PaooGame.Game;
import PaooGame.Graphics.Assets;
import PaooGame.entity.Spell;

public class FireSpell extends Spell {

    public FireSpell(Game game) {
        super(game);
        name = "FireSpell";
        speed = 7;
        maxHealth = 60;
        health = maxHealth;
        attack = 3;
        rechargeTime = 210;
        elapsedTime=0;
        img = Assets.fireSpell[0];
    }

    public void update() {
        super.update();

        switch(action) {
            case 1: worldY-=speed; break;           // UP
            case 2: worldX-=speed; break;           // LEFT
            case 3: worldY+=speed; break;           // DOWN
            case 4: worldX+=speed; break;           // RIGHT
        }

        switch(action) {
            case 1: SetSprite(Assets.fireSpell[counter/5]); break;           // UP
            case 2: SetSprite(Assets.fireSpell[counter/5]); break;           // LEFT
            case 3: SetSprite(Assets.fireSpell[counter/5]); break;           // DOWN
            case 4: SetSprite(Assets.fireSpell[counter/5]); break;           // RIGHT
        }

        if(user == Game.getPlayer()) {
            int monsterIdx = game.collissionChecker.checkEntity(this,game.monster);
            if(monsterIdx!=999) {
                Game.getPlayer().damageMonster(monsterIdx,this.attack);
                alive=false;
            }
        }
    }
}
