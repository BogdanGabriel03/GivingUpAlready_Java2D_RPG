package PaooGame.Tiles;

import PaooGame.AssetSetter;

import java.awt.*;

public class Level_3 extends Level{
    public Level_3(TileManager _tileM, AssetSetter _assetS) {
        super(_tileM, _assetS);
        tileM.setUpMap(3);
        assetS.setLevel();
    }

    public void draw(Graphics2D g2) {
        tileM.draw(g2);
    }
}
