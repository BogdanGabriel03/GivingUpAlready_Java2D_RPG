package PaooGame;

import PaooGame.Graphics.Assets;
import PaooGame.Tiles.Tile;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.text.DecimalFormat;

public class UI {
    Game game;
    Graphics2D g2;
    Font capTSF;
    private boolean messageOn = false;
    private int messageX;
    private int messageY;
    public String message = "";
    public int messageTime = 0;
    // index of command in the main menu screen
    public static int command = 0;
    // index of command in the end level screen
    public static int endLvlCommand =0;

    private double playTime=0;
    private DecimalFormat decimalFormat = new DecimalFormat("#0.00");

    public UI(Game game) {
        this.game = game;
        //arial_40 = new Font("Arial",Font.PLAIN,30);
        try {
            InputStream is = getClass().getResourceAsStream("/fonts/CaptainTallShipFont.ttf");
            capTSF = Font.createFont(Font.TRUETYPE_FONT,is);
        }catch(FontFormatException e) {
            e.printStackTrace();
        }catch(IOException e) {
            e.printStackTrace();
        }
    }

    public void showMessage(String text, int x, int y) {
        message = text;
        messageX = x;
        messageY = y;
        messageOn=true;
    }
    public void draw(Graphics2D g2) {
        this.g2 = g2;
        g2.setFont(capTSF);
        //g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setColor(Color.white);

        if(Game.getGameState() == Game.GameState.PLAY_STATE) {

            drawSubWindow(20,20,200,110);

            g2.setFont(g2.getFont().deriveFont(30F));
            g2.drawString("Speed = " + Game.getPlayer().getSpeed(),40,65);
            g2.drawString("ATK = " + Game.getPlayer().getAttack(), 40,105);

            // Display play time
            playTime+=(double)1/30;
            //g2.drawString("Time: " + decimalFormat.format(playTime),12*Tile.TILE_SIZE,40);
            g2.drawString("Monsters left: " + game.monsterCount, 11*Tile.TILE_SIZE, 100);

            for ( int i=0;i<3;++i) {
                if(Game.getPlayer().getSpell(i)) {
                    boolean r = Game.getPlayer().getSpellInstance(i).getElapsedTime() != 0;
                    drawSpellWindow(i,(11+2*i)*Tile.TILE_SIZE,Game.WND_HEIGHT - 90,r);
                }
            }

            if ( messageOn) {
                g2.setFont(g2.getFont().deriveFont(20F));
                g2.drawString(message,messageX, messageY);
                messageTime++;
                if(messageTime>60) {
                    messageTime=0;
                    messageOn = false;
                }
            }
        }
        else if(Game.getGameState() == Game.GameState.PAUSE_STATE) {
            drawPauseScreen();
        }
        else if(Game.getGameState() == Game.GameState.DIALOGUE_STATE) {
            drawDialogueScreen();
        }
        else if(Game.getGameState() == Game.GameState.MAIN_MENU_STATE) {
            drawMainMenuScreen();
        }
        else if(Game.getGameState() == Game.GameState.END_LEVEL_STATE) {
            messageOn=false;
            drawEndLevelScreen();
        }
    }

    private void drawPauseScreen() {
        g2.setFont(g2.getFont().deriveFont(80F));
        String text = "PAUSED";
        int length = (int)g2.getFontMetrics().getStringBounds(text,g2).getWidth();
        int x = (Game.WND_WIDTH-length)/2;
        int y = Game.WND_HEIGHT/2;

        g2.drawString(text,x,y);
    }

    private void drawDialogueScreen() {
        //Creating dialogue window
        int x = 2*Tile.TILE_SIZE;
        int y = Tile.TILE_SIZE/2;
        drawSubWindow(x,y, Game.WND_WIDTH - 4*Tile.TILE_SIZE,4*Tile.TILE_SIZE);
        g2.setFont(g2.getFont().deriveFont(26F));

        for(String line : message.split("\n")) {
            g2.drawString(line,x + Tile.TILE_SIZE,y+Tile.TILE_SIZE);
            y+=40;
        }
    }

    private void drawSubWindow(int x, int y, int w, int h) {
        Color c = new Color(0,0,0,125);
        g2.setColor(c);
        g2.fillRoundRect(x,y,w,h,35,35);
        g2.setColor(Color.white);
        g2.setStroke(new BasicStroke(5));
        g2.drawRoundRect(x+5,y+5,w-10,h-10,25,25);
    }

    private void drawSpellWindow(int idx, int x, int y, boolean r) {
        Color c = new Color(0,0,0);
        g2.setFont(g2.getFont().deriveFont(21F));
        if(r) {
            int timeI = Game.getPlayer().getSpellInstance(idx).getElapsedTime()/30;
            String time = "" + timeI;
            g2.setColor(c);
            g2.fillRoundRect(x+12,y-3*Tile.TILE_SIZE/4,26,26,15,15);
            g2.setColor(Color.white);
            g2.drawString(time,x+17+(timeI<10 ? 3 : 0),y-Tile.TILE_SIZE/4-3);
            g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.5f));
        }
        g2.drawImage(Assets.spellButton[idx],x,y,null);
        g2.setColor(c);
        g2.fillRoundRect(x+12,y+5*Tile.TILE_SIZE/4,26,26,15,15);
        g2.setColor(Color.white);
        String text="";
        if(idx==0) text="J";
        else if(idx==1) text="K";
        else if(idx==2) text="L";
        g2.drawString(text,x+19,y+7*Tile.TILE_SIZE/4-3);
        g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1f));
    }

    private void drawMainMenuScreen() {
        g2.setFont(g2.getFont().deriveFont(Font.BOLD,90F));
        String text = "Giving Up Already?";
        int x = getCenterXCoordforString(text);
        int y = Game.WND_HEIGHT/3;

        g2.setColor(new Color(141,85,36));
        g2.drawString(text,x+6,y+6);

        g2.setColor(new Color(224,172,105));
        g2.drawString(text,x,y);

        g2.drawImage(Assets.bossNpc,570,310,4*Tile.TILE_SIZE,4*Tile.TILE_SIZE,null);
        g2.drawImage(Assets.spellButton[0],58,83,null);
        g2.drawImage(Assets.spellButton[0],130,82,null);

        g2.setColor(new Color(255,224,189));
        //MENU
        g2.setFont(g2.getFont().deriveFont(48F));
        text = "NEW GAME";
        x = getCenterXCoordforString(text);
        g2.drawString(text,x,y+160);
        if(command == 0) {
            //g2.setColor(new Color(232,93,4));
            g2.drawString(">",x-7*Tile.TILE_SIZE/10,y+160);
        }

        g2.setFont(g2.getFont().deriveFont(48F));
        text = "LOAD GAME";
        x = getCenterXCoordforString(text);
        g2.drawString(text,x,y+235);
        if(command == 1) {
            //g2.setColor(new Color(232,93,4));
            g2.drawString(">",x-7*Tile.TILE_SIZE/10,y+235);
        }

        g2.setFont(g2.getFont().deriveFont(48F));
        text = "QUIT";
        x = getCenterXCoordforString(text);
        g2.drawString(text,x,y+310);
        if(command == 2) {
            //g2.setColor(new Color(232,93,4));
            g2.drawString(">",x-7*Tile.TILE_SIZE/10,y+310);
        }
    }

    int getCenterXCoordforString(String str) {
        int length = (int)g2.getFontMetrics().getStringBounds(str,g2).getWidth();
        return (Game.WND_WIDTH-length)/2;
    }

    void drawEndLevelScreen() {
        if (!Game.getPlayer().alive) {
            drawMessage(0,Game.WND_HEIGHT/2,"You lost!", 96F);
        }
        else if (Game.getPlayer().wonMessageOn) {
            drawMessage(0, Game.WND_HEIGHT / 3, "You won\nthis challenge!", 96F);
        }

        int x;
        x = drawMessage(0,Game.WND_HEIGHT/3 + 200, "Continue", 40F);

        if(endLvlCommand==0) {
            drawMessage(x - 7*Tile.TILE_SIZE/10,Game.WND_HEIGHT/3 + 200, ">", 40F);
        }
        x = drawMessage(0,Game.WND_HEIGHT/3 + 280, "Save & Exit", 40F);
        if(endLvlCommand==1) {
            drawMessage(x - 7*Tile.TILE_SIZE/10,Game.WND_HEIGHT/3 + 280, ">", 40F);
        }
    }

    int drawMessage(int x, int y, String text, float size) {
        g2.setFont(g2.getFont().deriveFont(Font.BOLD,size));
        int x2=0;
        for(String line : text.split("\n")) {
            x2 = getCenterXCoordforString(line);
            if ( x!=0) x2 = x;

            g2.setColor(new Color(80,80,80));
            g2.drawString(line,x2,y+6);

            g2.setColor(Color.white);
            g2.drawString(line,x2,y);

            y+=100;
        }
        return x2;
    }
}
