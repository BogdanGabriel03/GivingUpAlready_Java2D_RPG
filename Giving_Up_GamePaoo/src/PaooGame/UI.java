package PaooGame;

import PaooGame.Graphics.Assets;
import PaooGame.Tiles.Tile;

import java.awt.*;
import java.io.IOException;
import java.io.InputStream;
import java.text.DecimalFormat;

public class UI {
    Game game;
    Graphics2D g2;
    Font capTSF;
    public boolean messageOn = false;
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

    public void showMessage(String text) {
        message = text;
        messageOn=true;
    }
    public void draw(Graphics2D g2) {
        this.g2 = g2;
        g2.setFont(capTSF);
        //g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setColor(Color.white);

        if(Game.getGameState() == Game.GameState.PLAY_STATE) {
            g2.setFont(g2.getFont().deriveFont(40F));
            g2.drawString("Speed = " + Game.getPlayer().getSpeed(),25,40);
            g2.drawString("ATK = " + Game.getPlayer().getAttack(), 25,80);

            // Display play time
            playTime+=(double)1/30;
            g2.drawString("Time: " + decimalFormat.format(playTime),12*Tile.TILE_SIZE,40);

            if ( messageOn) {
                g2.setFont(g2.getFont().deriveFont(20F));
                g2.drawString(message,25, 3* Tile.TILE_SIZE);
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
        Color c = new Color(0,0,0);
        g2.setColor(c);
        g2.fillRoundRect(x,y,w,h,35,35);
        g2.setColor(Color.white);
        g2.setStroke(new BasicStroke(5));
        g2.drawRoundRect(x+5,y+5,w-10,h-10,25,25);
    }

    private void drawMainMenuScreen() {
        g2.setFont(g2.getFont().deriveFont(Font.BOLD,96F));
        String text = "Giving Up?";
        int x = getCenterXCoordforString(text);
        int y = Game.WND_HEIGHT/3;

        g2.setColor(new Color(80,80,80));
        g2.drawString(text,x+6,y+6);

        g2.setColor(Color.white);
        g2.drawString(text,x,y);

        //g2.drawImage(Assets.bossNpc,517,239,Tile.TILE_SIZE,Tile.TILE_SIZE,null);

        //MENU
        g2.setFont(g2.getFont().deriveFont(48F));
        text = "NEW GAME";
        x = getCenterXCoordforString(text);
        g2.drawString(text,x,y+150);
        if(command == 0) {
            g2.drawString(">",x-7*Tile.TILE_SIZE/10,y+150);
        }

        g2.setFont(g2.getFont().deriveFont(48F));
        text = "LOAD GAME";
        x = getCenterXCoordforString(text);
        g2.drawString(text,x,y+250);
        if(command == 1) {
            g2.drawString(">",x-7*Tile.TILE_SIZE/10,y+250);
        }

        g2.setFont(g2.getFont().deriveFont(48F));
        text = "QUIT";
        x = getCenterXCoordforString(text);
        g2.drawString(text,x,y+350);
        if(command == 2) {
            g2.drawString(">",x-7*Tile.TILE_SIZE/10,y+350);
        }
    }

    int getCenterXCoordforString(String str) {
        int length = (int)g2.getFontMetrics().getStringBounds(str,g2).getWidth();
        return (Game.WND_WIDTH-length)/2;
    }

    void drawEndLevelScreen() {
        // TO BE DONE; I AM EITHER MAKING THESE END_LEVEL_STATES TIME DEPENDENT, SO WHEN A TIMER ENDS THE PLAYER IS AUTOMATICALLY
        //      INTRODUCED IN THE NEXT LEVEL SCREEN OR MAKE A BUTTON INTERFACE
        //String text="";
        if (!Game.getPlayer().alive) {
            drawMessage(0,Game.WND_HEIGHT/2,"You lost!", 96F);
        }
        else if (Game.getPlayer().wonMessageOn) {
            drawMessage(0,Game.WND_HEIGHT/3,"You won\nthis challenge!", 96F);
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
