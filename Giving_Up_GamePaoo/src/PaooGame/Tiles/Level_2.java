package PaooGame.Tiles;

import PaooGame.AssetSetter;

import java.awt.*;

public class Level_2 extends Level{
    public Level_2(TileManager _tileM, AssetSetter _assetS) {
        super(_tileM, _assetS);
        tileM.setUpMap(2);
        assetS.setLevel();
    }

    public void draw(Graphics2D g2) {
        tileM.draw(g2);
    }
}
