package PaooGame.Tiles;

import PaooGame.AssetSetter;

import java.awt.*;

public class Level {
    private int dim;
    //private String map_path=null;
    private int timer;
    protected TileManager tileM;
    protected AssetSetter assetS;

    public Level(TileManager _tileM, AssetSetter _assetS) {
        tileM = _tileM;
        assetS = _assetS;
        dim=0;
        timer=0;
    }

    public void draw(Graphics2D g2) {}
}
