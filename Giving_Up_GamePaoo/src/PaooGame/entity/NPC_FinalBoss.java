package PaooGame.entity;

import PaooGame.Game;
import PaooGame.Graphics.Assets;

public class NPC_FinalBoss extends Entity{
    private int action;
    private String[] dialogues;
    private int dialogueIdx=0;

    public NPC_FinalBoss(Game game) {
        super(game);
        dialogues = new String[4];
        barrier.x = 15;
        barrier.y = 30;
        barrier.width=70;
        barrier.height=70;
        setDialogue();
        action=0;
        speed=0;
        barrierInitialX=barrier.x;
        barrierInitialY=barrier.y;
        img = Assets.bossNpc;
        type=1;
    }

    private void setDialogue() {
        dialogues[0] = "You need to defeat all enemies to pass\nthis challenge! Look carefully and be\nprepared human!";
        dialogues[1] = "Shall you have finished, come find me\n and I will teleport you to the next\n challenge!";
        dialogues[2] = "Time is ticking! Hurry up!";
        //dialogues[3] = "You must defeat all enemies to pass!";
    }
    public void speak() {
        if (Game.getPlayer().won) {
            //game.ui.message = dialogues[3];
            Game.getPlayer().wonMessageOn = true;
            Game.setGameState(Game.GameState.END_LEVEL_STATE);
        }
        else {
            game.ui.message = dialogues[dialogueIdx];
            dialogueIdx = (dialogueIdx+1)%3;
            //if(dialogueIdx==0) Game.setGameState(Game.GameState.END_LEVEL_STATE);
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
}
