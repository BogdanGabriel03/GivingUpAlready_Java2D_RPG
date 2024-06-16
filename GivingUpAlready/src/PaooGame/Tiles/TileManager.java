package PaooGame.Tiles;

import PaooGame.Game;
import PaooGame.Graphics.Assets;
import PaooGame.ai.PathFinder;

import java.awt.*;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

public class TileManager {
    private final Tile[] tiles;
    private MapGenerator mapGen;
    private PathFinder pFinder;
    private int dim;
    private String map;
    boolean drawPath = false;

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
        if(drawPath) {
            g.setColor(new Color(255,0,0,70));
            for(int i=0;i<pFinder.path.size();++i) {
                int worldX = pFinder.path.get(i).col * Tile.TILE_SIZE;
                int worldY = pFinder.path.get(i).row * Tile.TILE_SIZE;

                int screenX = worldX - Game.getPlayer().getWorldX()+ Game.getPlayer().getScreenX();
                int screenY = worldY - Game.getPlayer().getWorldY() + Game.getPlayer().getScreenY();

                g.fillRect(screenX,screenY,Tile.TILE_SIZE,Tile.TILE_SIZE);
            }
        }
    }

    public int getTileNum(int x, int y) {
        return mapGen.getMap()[x][y];
    }

    public boolean checkTile(int idx) {
        return tiles[idx].getCollision();
    }

    public void setUpMap(int lvl, Game game) {
        switch(lvl) {
            case 1:
                mapGen.loadMap(56,"/maps/map1.txt");
                pFinder = new PathFinder(game, 56);
                break;
            case 2:
                mapGen.loadMap(64,"/maps/map2.txt");
                pFinder = new PathFinder(game, 64);
                break;
            case 3:
                mapGen.loadMap(72,"/maps/map3.txt");
                pFinder = new PathFinder(game, 72);
                break;
            default:
                break;
        }
    }

    public PathFinder getPathFinder() {
        return pFinder;
    }
}
