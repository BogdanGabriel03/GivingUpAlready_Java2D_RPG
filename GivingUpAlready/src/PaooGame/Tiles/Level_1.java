package PaooGame.Tiles;

import PaooGame.AssetSetter;
import PaooGame.Game;
import PaooGame.ai.PathFinder;

import java.awt.*;

public class Level_1 extends Level{
    public Level_1(Game game, TileManager _tileM, AssetSetter _assetS) {
        super(game, _tileM, _assetS);
        tileM.setUpMap(1,game);
        assetS.setLevel();
    }

    public void draw(Graphics2D g2) {
        tileM.draw(g2);
    }
}
