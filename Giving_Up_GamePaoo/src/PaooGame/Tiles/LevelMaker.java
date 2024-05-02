package PaooGame.Tiles;

import PaooGame.Game;

import java.awt.*;

public class LevelMaker {

    private Game game;
    private TileManager tileM;

    public LevelMaker(Game _game, TileManager _tileM) {
        this.game = _game;
        this.tileM = _tileM;
        tileM.setUpMap(game.currentLevel);
    }

    public void update() {
        if(!Game.getPlayer().alive) {
            game.currentLevel=1;
            tileM.setUpMap(game.currentLevel);
        }
        else if ( Game.getPlayer().wonMessageOn) {
            game.currentLevel++;
            tileM.setUpMap(game.currentLevel);
        }
        //tileM.setUpMap(game.currentLevel);
    }

    public void draw(Graphics2D g2) {
        tileM.draw(g2);
    }
}
