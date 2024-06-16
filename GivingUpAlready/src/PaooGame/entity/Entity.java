package PaooGame.entity;

import PaooGame.Game;
import PaooGame.Graphics.Assets;
import PaooGame.Tiles.Tile;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.Objects;

public abstract class Entity {
    protected Game game;
    public int worldX;
    public int worldY;
    public int speed;
    public int health;
    public int maxHealth;
    public String name;
    public int attack;
    public BufferedImage img;

    // indicates the type of entity; [0 - PLAYER], [1 - NPC], [2 - MONSTER_BLOOM], [3 - ITEM_CHEST], [4 - MONSTER_CRUMBLE], [5 - SPELL_CHEST], [6 - MONSTER_BLIGHT]
    public int type;

    public int action;
    public int counter;
    //public String direction = "down";
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
    public boolean hpBarOn = false;
    public int hpBarCounter =0;
    public boolean onPath = false;

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
            // MONSTER HP BAR
            if((type==2 || type == 4 || type == 6) && hpBarOn) {

                double oneScale = (double)Tile.TILE_SIZE/maxHealth;
                double hpBarValue = oneScale * health;

                g2.setColor(new Color(35,35,35));
                g2.fillRect(screenX -2,screenY-12,Tile.TILE_SIZE +4 ,11);
                if(!dying) {
                    g2.setColor(new Color(255,0,30));
                    g2.fillRect(screenX,screenY-10,(int)hpBarValue,7);
                }

                hpBarCounter++;
                if(hpBarCounter > 150) {
                    hpBarCounter=0;
                    hpBarOn=false;
                }

            }
            if(invincible || Objects.equals(name, "StoneShield")) {
                g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.4f));
            }
            if(dying) {
                dyingAnimation(g2);
            }
            g2.drawImage(img, screenX, screenY, null);
            g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1f));
        }
    }
    public void dyingAnimation(Graphics2D g) {
        dyingCounter++;
        if(dyingCounter/5%2==0) {
            g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0f));
        }
        else if(dyingCounter/5%2==1) {
            g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1f));
        }
        if(dyingCounter > 30) {
            alive=false;
            dyingCounter=0;
        }
    };

    public void searchPath(int goalCol, int goalRow) {
        int startCol = (worldX + barrier.x)/Tile.TILE_SIZE;
        int startRow = (worldY + barrier.y)/Tile.TILE_SIZE;

        game.lvlInstance.getPathFinder().setNodes(startCol,startRow,goalCol,goalRow,this);

        if(game.lvlInstance.getPathFinder().search()) {
            int nextX = game.lvlInstance.getPathFinder().path.get(0).col * Tile.TILE_SIZE;
            int nextY = game.lvlInstance.getPathFinder().path.get(0).row * Tile.TILE_SIZE;

            int barrierLeftX = worldX + barrier.x;
            int barrierRightX = worldX + barrier.x + barrier.width;
            int barrierTopY = worldY + barrier.y;
            int barrierBottomY = worldY + barrier.y + barrier.height;

            if(barrierTopY > nextY && barrierLeftX >= nextX && barrierRightX < nextX + Tile.TILE_SIZE) {
                action = 1;
            }
            else if(barrierTopY < nextY && barrierLeftX >= nextX && barrierRightX < nextX + Tile.TILE_SIZE) {
                action = 3;
            }
            else if(barrierTopY >= nextY && barrierBottomY < nextY +Tile.TILE_SIZE) {
               if(barrierLeftX > nextX) {action=2;}         // left
               if(barrierLeftX < nextX) {action=4;}         // right
            }
            else if(barrierTopY > nextY && barrierLeftX > nextX) {
                action = 1;                         // up
                checkCollision();
                if(collisionOn) {action = 2;}       //left
            }
            else if(barrierTopY > nextY && barrierLeftX < nextX) {
                action = 1;                         // up
                checkCollision();
                if(collisionOn) {action = 4;}       //right
            }
            else if (barrierTopY < nextY && barrierLeftX > nextX) {
                action = 3;                         //down
                checkCollision();
                if(collisionOn) {action = 2;}       //left
            }
            else if(barrierTopY < nextY && barrierLeftX < nextX) {
                action = 3;                         //down
                checkCollision();
                if(collisionOn) {action = 4;}       //right
            }

            /*int nextCol = game.lvlInstance.getPathFinder().path.get(0).col;
            int nextRow = game.lvlInstance.getPathFinder().path.get(0).row;

            if(nextCol == goalCol && nextRow == goalRow) {
                onPath = false;
            }*/

        }
    }

    public void speak() {};
    public void damageReaction() {};
    public void update() {};
    public void SetSprite(BufferedImage newImg) {this.img = newImg;}
    public void setAction() {};
    private void checkCollision() {
        collisionOn=false;
        game.collissionChecker.checkTile(this);
        game.collissionChecker.checkItem(this,false);
        game.collissionChecker.checkEntity(this,game.npc);
        game.collissionChecker.checkEntity(this,game.monster);
        boolean contactPlayer = game.collissionChecker.checkPlayer(this);
    }
    abstract public int getAction();
    abstract public int getSpeed();
}
