package PaooGame;

import javax.swing.plaf.nimbus.State;
import java.sql.*;
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
    private boolean loaded=false;

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
                        game.currentLevel=1;
                        game.lvlInstance = game.lvlMaker.update();
                        Game.getPlayer().enteredNewLvl=true;
                        Game.getPlayer().alive=true;
                        Game.getPlayer().resetPlayer();
                        Game.setGameState(Game.GameState.PLAY_STATE);
                        //play music sss
                        break;
                    case 1:
                        // LOADING SAVED STATS FROM DB
                        Connection c1 = game.getDBConn();
                        int var = checkIfEmpty(c1);
                        if(var==0) {
                            game.ui.drawMessage(0,0,"No previously loaded game!", 50F);
                        }
                        else {
                            loadGame(c1);
                            game.lvlInstance = game.lvlMaker.update();
                            Game.getPlayer().enteredNewLvl=true;
                            Game.getPlayer().alive=true;
                            Game.setGameState(Game.GameState.PLAY_STATE);
                        }
                        break;
                    case 2:
                        Connection c2 = game.getDBConn();

                        try {
                            c2.close();
                        } catch (SQLException e3) {
                            e3.printStackTrace();
                        }
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
                if(game.currentLevel<4) {
                    switch(UI.endLvlCommand) {
                        case 0:     // continue game ( if you lost it will restart you from level 1 )
                            Game.getPlayer().enteredNewLvl = true;
                            Game.setGameState(Game.GameState.PLAY_STATE);
                            Game.getPlayer().alive=true;
                            //play music sssw
                            break;
                        case 1:     // save and exit
                            PreparedStatement pstmnt = null;
                            Connection c = game.getDBConn();
                            try {
                                String sql = "INSERT INTO SAVE_DATA (SAVE_ID,LEVEL,ATTACK,SPEED,SPELL_FIREBALL,SPELL_STONE_SHIELD) " +
                                        "VALUES (?, ?, ?, ?, ?, ?);";
                                pstmnt = c.prepareStatement(sql);

                                if(checkIfEmpty(c)==0) {
                                    pstmnt.setInt(1,1);
                                }
                                else {
                                    int idx = getIdx(c);
                                    pstmnt.setInt(1, idx+1);
                                }
                                pstmnt.setInt(2,game.currentLevel);
                                pstmnt.setInt(3,Game.getPlayer().getAttack());
                                pstmnt.setInt(4,Game.getPlayer().getSpeed());
                                pstmnt.setInt(5,Game.getPlayer().getSpell(0) ? 1 : 0);
                                pstmnt.setInt(6,Game.getPlayer().getSpell(1) ? 1 : 0);
                                pstmnt.executeUpdate();

                                c.commit();
                                //c.close();
                            } catch ( Exception E ) {
                                System.err.println( E.getClass().getName() + ": " + E.getMessage() );
                                System.exit(0);
                            }
                            finally {
                                if (pstmnt != null) {
                                    try { pstmnt.close(); } catch (SQLException e2) { e2.printStackTrace(); }
                                }
                            }
                            game.updated=false;
                            Game.getPlayer().wonMessageOn=false;
                            Game.setGameState(Game.GameState.MAIN_MENU_STATE);
                            break;
                    }
                }
                else {
                    Connection c2 = game.getDBConn();

                    try {
                        c2.close();
                    } catch (SQLException e3) {
                        e3.printStackTrace();
                    }
                    System.exit(0);
                }
            }
        }
        else if (Game.getGameState() == Game.GameState.WARNING_STATE) {
            if(code == KeyEvent.VK_W || code == KeyEvent.VK_S) {
                UI.warningCommand = (UI.warningCommand+1)%2;
            }
            if(code == KeyEvent.VK_ENTER) {
                switch(UI.warningCommand) {
                    case 0:                     // lost progress ( yes )
                        Game.setGameState(Game.GameState.PLAY_STATE);
                        break;
                    case 1:                     // cancelled loading new game ( no )
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

    private int checkIfEmpty(Connection c) {
        int rowCount=0;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            String sql = "SELECT COUNT(*) FROM SAVE_DATA";
            pstmt = c.prepareStatement(sql);
            rs = pstmt.executeQuery();

            if (rs.next()) {
                rowCount = rs.getInt(1);
            }
        } catch(Exception except) {
            except.printStackTrace();
        }
        return rowCount;
    }

    private int getIdx(Connection c) {
        int idx=0;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            String sql = "SELECT SAVE_ID FROM SAVE_DATA";
            pstmt = c.prepareStatement(sql);
            rs = pstmt.executeQuery();

            while(rs.next()) {
                idx = rs.getInt(1);
            }
        } catch ( Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (rs != null) rs.close();
                if (pstmt != null) pstmt.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return idx;
    }

    private void loadGame(Connection c) {
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            String sql = "SELECT * FROM SAVE_DATA ORDER BY SAVE_ID DESC LIMIT 1;";
            pstmt = c.prepareStatement(sql);
            rs = pstmt.executeQuery();

            if(rs.next()) {
                game.currentLevel=rs.getInt("LEVEL");
                Game.getPlayer().attack = rs.getInt("ATTACK");
                Game.getPlayer().speed = rs.getInt("SPEED");
                Game.getPlayer().setSpell(0,rs.getInt("SPELL_FIREBALL"));
                Game.getPlayer().setSpell(1,rs.getInt("SPELL_STONE_SHIELD"));

                for ( int i=0;i<3;++i) {
                    if(Game.getPlayer().getSpell(i)) {
                        Game.getPlayer().setSpellInstance(i);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (rs != null) rs.close();
                if (pstmt != null) pstmt.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
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
    public boolean getLoaded() {
        return loaded;
    }
}
