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
            }

            /*if(code == KeyEvent.VK_W) {
                    action=1;
                }
            }
            if(code == KeyEvent.VK_A) {
                action=2;
            }
            if(code == KeyEvent.VK_S) {
                action=3;
            }
            if(code == KeyEvent.VK_D) {
                action=4;
            }
            //Debug
            if(code == KeyEvent.VK_T) {
                checkDrawTime= !checkDrawTime;
            }
            // Game pause
            if(code == KeyEvent.VK_P) {
                checkDrawTime=false;
                Game.setGameState(Game.GameState.PAUSE_STATE);
            }
            if ( code == KeyEvent.VK_ENTER) {
                enterDialogue=true;
            }
            if ( code == KeyEvent.VK_SPACE) {
                attacking = true;
            }*/
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

    }

    @Override
    public void keyReleased(KeyEvent e) {
        int code = e.getKeyCode();
        if(code==KeyEvent.VK_W || code == KeyEvent.VK_A || code == KeyEvent.VK_S || code == KeyEvent.VK_D) {
            action=0;
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
}
