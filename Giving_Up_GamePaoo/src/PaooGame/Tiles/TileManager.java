package PaooGame.Tiles;

import PaooGame.Game;
import PaooGame.Graphics.Assets;

import java.awt.*;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

public class TileManager {
    private final Tile[] tiles;
    private final int[][] mapTileNum;

    public TileManager() {
        mapTileNum = new int[56][56];
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
        loadMap("/maps/map1.txt");
    }

    public void loadMap(String map) {
        try {
            InputStream is = getClass().getResourceAsStream(map);
            BufferedReader br = new BufferedReader(new InputStreamReader(is));

            for ( int i=0;i<56;++i) {
                String[] numbers = br.readLine().split(" ");

                for ( int j=0;j<56;++j) {
                    mapTileNum[i][j]=Integer.parseInt(numbers[j]);
                }
            }
        }catch(Exception e) {
            e.printStackTrace();
        }
    }

    public void draw(Graphics2D g) {
        int worldRow=0,worldCol=0;
        for ( int i=0;i<56*56;++i) {
            int worldX = worldCol * Tile.TILE_SIZE;
            int worldY = worldRow * Tile.TILE_SIZE;

            int screenX = worldX - Game.getPlayer().getWorldX()+ Game.getPlayer().getScreenX();
            int screenY = worldY - Game.getPlayer().getWorldY() + Game.getPlayer().getScreenY();
            if(worldX > Game.getPlayer().getWorldX() - Game.getPlayer().getScreenX() - Tile.TILE_SIZE &&
                worldX < Game.getPlayer().getWorldX() + Game.getPlayer().getScreenX() + Tile.TILE_SIZE &&
                worldY > Game.getPlayer().getWorldY() - Game.getPlayer().getScreenY() -Tile.TILE_SIZE &&
                worldY < Game.getPlayer().getWorldY() + Game.getPlayer().getScreenY() + Tile.TILE_SIZE)
            {g.drawImage(tiles[mapTileNum[i/56][i%56]].img, screenX, screenY, null);}
            worldCol++;
            if(worldCol==56) {worldCol=0;worldRow++;}
        }
    }

    public int getTileNum(int x, int y) {
        return mapTileNum[x][y];
    }

    public boolean checkTile(int idx) {
        return tiles[idx].getCollision();
    }
}
