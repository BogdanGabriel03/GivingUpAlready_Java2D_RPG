package PaooGame.Tiles;

import PaooGame.AssetSetter;

import java.awt.*;

public class Level_1 extends Level{
    public Level_1(TileManager _tileM, AssetSetter _assetS) {
        super(_tileM, _assetS);
        tileM.setUpMap(1);
        assetS.setLevel();
    }

    public void draw(Graphics2D g2) {
        tileM.draw(g2);
    }
}
