package PaooGame.Tiles;

import PaooGame.AssetSetter;
import PaooGame.Game;
import PaooGame.ai.PathFinder;

import java.awt.*;

public abstract class Level {
    private int dim;
    protected Game game;
    private int timer;
    protected TileManager tileM;
    protected AssetSetter assetS;

    public Level(Game game, TileManager _tileM, AssetSetter _assetS) {
        this.game = game;
        tileM = _tileM;
        assetS = _assetS;
        dim=0;
        timer=0;
    }

    public void draw(Graphics2D g2) {}
    public PathFinder getPathFinder() {
        return tileM.getPathFinder();
    }
}
