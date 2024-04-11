package PaooGame.entity;

import PaooGame.Game;
import PaooGame.Graphics.Assets;
import PaooGame.Tiles.Tile;

import java.awt.*;
import java.awt.image.BufferedImage;

public abstract class Entity {
    public Game game;
    public int worldX;
    public int worldY;
    public int speed;
    public int health;
    public int maxHealth;
    public String name;
    public int attack;
    public BufferedImage img;
    public int type;
    public int action;
    public int counter;
    public String direction = "down";
    public boolean invincible = false;
    public boolean attacking = false;
    public int invincibleCounter = 0;
    public Rectangle barrier;
    public Rectangle attackArea;
    public int barrierInitialX,barrierInitialY;
    public boolean collision = false;
    public boolean collisionOn = false;
    public boolean dying = false;
    public boolean alive = true;
    public int dyingCounter =0;

    public Entity(Game game) {
        this.game = game;
        barrier = new Rectangle(0,0,Tile.TILE_SIZE, Tile.TILE_SIZE);
        attackArea = new Rectangle(0,0,0,0);
        worldX = 0;
        worldY = 0;
        speed = 0;
        health=0;
        maxHealth=0;
        attack=0;
        action=1;
        img=null;
        counter=0;
        barrierInitialX=0;
        barrierInitialY=0;
    }
    public void draw(Graphics2D g2) {
        int screenX = worldX - Game.getPlayer().getWorldX()+ Game.getPlayer().getScreenX();
        int screenY = worldY - Game.getPlayer().getWorldY() + Game.getPlayer().getScreenY();
        if(worldX > Game.getPlayer().getWorldX() - Game.getPlayer().getScreenX() - Tile.TILE_SIZE &&
                worldX < Game.getPlayer().getWorldX() + Game.getPlayer().getScreenX() + Tile.TILE_SIZE &&
                worldY > Game.getPlayer().getWorldY() - Game.getPlayer().getScreenY() -Tile.TILE_SIZE &&
                worldY < Game.getPlayer().getWorldY() + Game.getPlayer().getScreenY() + Tile.TILE_SIZE)
        {
            if(invincible) {
                g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.5f));
            }
            System.out.println(dying);
            if(dying == true) {
                System.out.println("He is dying");
                dyingAnimation(g2);
            }
            g2.drawImage(img, screenX, screenY, null);
            g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1f));
        }
    }
    public void dyingAnimation(Graphics2D g) {
        dyingCounter++;
        System.out.println(dyingCounter);
        if(dyingCounter/20%2 == 0) {
            g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0f));
            System.out.println("Invisible");
        }
        if(dyingCounter/20%2 == 1) {
            g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1f));
            System.out.println("Visible");
        }
        if(dyingCounter > 99) {
            dying=false;
            alive=false;
            //dyingCounter=0;
        }
    };
    public void speak() {};
    public void update() {};
    public void SetSprite(BufferedImage newImg) {this.img = newImg;}
    public void setAction() {};
    abstract public int getAction();
    abstract public int getSpeed();
}
