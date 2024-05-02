package PaooGame.Tiles;

import PaooGame.Game;
import PaooGame.Graphics.Assets;

import java.awt.*;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

public class TileManager {
    private final Tile[] tiles;
    private MapGenerator mapGen;
    private int dim;
    private String map;

    public TileManager() {
        mapGen = new MapGenerator();

        tiles = new Tile[7];
        tiles[0] = new Tile(Assets.tiles[0]);       // grass tile

        tiles[1] = new Tile(Assets.tiles[1]);       // grass with accents tile

        tiles[2] = new Tile(Assets.tiles[2]);       // tree tile
        tiles[2].SetSolid();

        tiles[3] = new Tile(Assets.tiles[3]);       // sand tile

        tiles[4] = new Tile(Assets.tiles[4]);       // sand with accents tile

        tiles[5] = new Tile(Assets.tiles[5]);       // water tile
        tiles[5].SetSolid();

        tiles[6] = new Tile(Assets.tiles[6]);       // water with accents tile
        tiles[6].SetSolid();
    }

    public void draw(Graphics2D g) {
        mapGen.draw(g,tiles);
    }

    public int getTileNum(int x, int y) {
        return mapGen.getMap()[x][y];
    }

    public boolean checkTile(int idx) {
        return tiles[idx].getCollision();
    }

    public void setUpMap(int lvl) {
        switch(lvl) {
            case 1:
                mapGen.loadMap(56,"/maps/map1.txt");
                break;
            case 2:
                mapGen.loadMap(64,"/maps/map2.txt");
                break;
            case 3:
                break;
            default:
                break;
        }
    }
}
