package PaooGame.entity;

import PaooGame.Game;
import PaooGame.Graphics.Assets;

public class Spell extends Entity{

    protected int rechargeTime;
    protected int elapsedTime;
    protected Entity user;

    public Spell(Game game) {
        super(game);
        alive=false;
    }

    public void set(int worldX, int worldY, int action, boolean alive, Entity user) {
        this.worldX = worldX;
        this.worldY = worldY;
        this.action = action;
        this.alive = alive;
        this.health = this.maxHealth;
        this.user = user;
    }
    public void update() {

        health--;
        if(health <= 0) {
            alive = false;
        }

        counter++;
        if(counter==25) counter=0;

    }
    @Override
    public int getAction() {
        return action;
    }

    @Override
    public int getSpeed() {
        return speed;
    }

    public int getElapsedTime() { return elapsedTime;}
    public void setElapsedTime(int val) {elapsedTime=val;}
    public int getRechargeTime() {return rechargeTime;}
}
