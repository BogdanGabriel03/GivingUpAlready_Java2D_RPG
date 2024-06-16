package PaooGame.spells;

import PaooGame.Game;
import PaooGame.Graphics.Assets;
import PaooGame.entity.Spell;

public class CrystalBeam extends Spell {
    public CrystalBeam(Game game) {
        super(game);
        name = "LightBeam";
        speed = 5;
        maxHealth = 150;
        health = maxHealth;
        //attack = 3;
        rechargeTime = 90;
        elapsedTime=0;
        action=1;           // UP
        img = Assets.crystalBeamUD[0];
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
            case 1: SetSprite(Assets.crystalBeamUD[counter/13]); break;           // UP
            case 2: SetSprite(Assets.crystalBeamLR[counter/13]); break;           // LEFT
            case 3: SetSprite(Assets.crystalBeamUD[counter/13]); break;           // DOWN
            case 4: SetSprite(Assets.crystalBeamLR[counter/13]); break;           // RIGHT
        }

        boolean contactPlayer = game.collissionChecker.checkPlayer(this);
        if(contactPlayer) {
            if(!Game.getPlayer().invincible) {
                Game.getPlayer().alive=false;
            }
            alive=false;
        }
    }
}
