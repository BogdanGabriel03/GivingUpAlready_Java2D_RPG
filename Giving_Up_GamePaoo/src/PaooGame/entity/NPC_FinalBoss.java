package PaooGame.entity;

import PaooGame.Game;
import PaooGame.Graphics.Assets;

public class NPC_FinalBoss extends Entity{
    private int action;
    private String[] dialogues;
    private int dialogueIdx=0;

    public NPC_FinalBoss(Game game) {
        super(game);
        dialogues = new String[3];
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
    }

    private void setDialogue() {
        dialogues[0] = "You passed the first challenge! However\nyou have still two more to come so be\nprepared human!";
        dialogues[1] = "Advance thorw the portal to\nthe next probe!";
        dialogues[2] = "Time is ticking! Hurry up!";
    }
    public void speak() {
        game.ui.message = dialogues[dialogueIdx];
        dialogueIdx = (dialogueIdx+1)%3;
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
