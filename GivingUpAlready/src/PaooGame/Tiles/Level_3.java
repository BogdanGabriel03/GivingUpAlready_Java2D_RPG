package PaooGame.Tiles;

import PaooGame.AssetSetter;
import PaooGame.Game;

import java.awt.*;

public class Level_3 extends Level{
    public Level_3(Game game, TileManager _tileM, AssetSetter _assetS) {
        super(game, _tileM, _assetS);
        tileM.setUpMap(3, game);
        assetS.setLevel();
    }

    public void draw(Graphics2D g2) {
        tileM.draw(g2);
    }
}
