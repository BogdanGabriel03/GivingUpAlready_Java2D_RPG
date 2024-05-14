package PaooGame;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class KeyHandler implements KeyListener {
    // 0 - idle
    // 1 - up
    // 2 - left
    // 3 - down
    // 4 - right
    private int action;
    private Game game;
    private boolean checkDrawTime=false;
    private boolean enterDialogue = false;
    private boolean attacking = false;
    private int castedSpell = -1;

    KeyHandler(Game game) {
        this.game = game;
    }
    @Override
    public void keyTyped(KeyEvent e) {
    }

    @Override
    public void keyPressed(KeyEvent e) {
        int code=e.getKeyCode();
        if(Game.getGameState() == Game.GameState.MAIN_MENU_STATE) {
            if(code == KeyEvent.VK_W) {
                UI.command = (UI.command+2)%3;
            }
            if(code == KeyEvent.VK_S) {
                UI.command = (UI.command+1)%3;
            }
            if(code == KeyEvent.VK_ENTER) {
                switch(UI.command) {
                    case 0:
                        Game.setGameState(Game.GameState.PLAY_STATE);
                        //play music sss
                        break;
                    case 1:
                        break;
                    case 2:
                        System.exit(0);
                        break;
                }
            }
        }
        else if(Game.getGameState() == Game.GameState.PLAY_STATE) {
            switch(code) {
                case KeyEvent.VK_W:
                    action=1;
                    break;
                case KeyEvent.VK_A:
                    action=2;
                    break;
                case KeyEvent.VK_S:
                    action=3;
                    break;
                case KeyEvent.VK_D:
                    action=4;
                    break;
                case KeyEvent.VK_T:
                    checkDrawTime= !checkDrawTime;
                    break;
                case KeyEvent.VK_P:
                    checkDrawTime=false;
                    Game.setGameState(Game.GameState.PAUSE_STATE);
                    break;
                case KeyEvent.VK_ENTER:
                    enterDialogue=true;
                    break;
                case KeyEvent.VK_SPACE:
                    game.playSE(4);
                    attacking = true;
                    break;
                case KeyEvent.VK_J:
                    castedSpell=0;
                    break;
                case KeyEvent.VK_K:
                    castedSpell=1;
                    break;
            }
        }
        else if ( Game.getGameState() == Game.GameState.PAUSE_STATE) {
            if(code == KeyEvent.VK_P) {
                Game.setGameState(Game.GameState.PLAY_STATE);
            }
        }
        else if( Game.getGameState() == Game.GameState.DIALOGUE_STATE) {
            if ( code == KeyEvent.VK_ENTER) {
                Game.setGameState(Game.GameState.PLAY_STATE);
            }
        }
        else if ( Game.getGameState() == Game.GameState.END_LEVEL_STATE) {
            if(code == KeyEvent.VK_W || code == KeyEvent.VK_S) {
                UI.endLvlCommand = (UI.endLvlCommand+1)%2;
            }
            if(code == KeyEvent.VK_ENTER) {
                switch(UI.endLvlCommand) {
                    case 0:     // continue game ( if you lost it will restart you from level 1 )
                        Game.getPlayer().enteredNewLvl = true;
                        Game.setGameState(Game.GameState.PLAY_STATE);
                        Game.getPlayer().alive=true;
                        //play music sss
                        break;
                    case 1:     // save and exit
                        break;
                }
            }
        }

    }

    @Override
    public void keyReleased(KeyEvent e) {
        int code = e.getKeyCode();
        if(code==KeyEvent.VK_W || code == KeyEvent.VK_A || code == KeyEvent.VK_S || code == KeyEvent.VK_D) {
            action=0;
        }
        if(code==KeyEvent.VK_J || code==KeyEvent.VK_K) {
            castedSpell=-1;
        }
    }

    public int getAction() {
        return action;
    }

    public boolean getChecker() {
        return checkDrawTime;
    }

    public boolean getDialogue() {
        return enterDialogue;
    }
    public void setDialogue(boolean b) {
        enterDialogue = b;
    }
    public boolean getAttackState() {
        return attacking;
    }
    public void setAttackState(boolean state) {
        attacking = state;
    }
    public void setCastedSpell(int idx) {
        castedSpell = idx;
    }
    public int getCastedSpell() {
        return castedSpell;
    }
}
