package PaooGame.Tiles;

import PaooGame.Game;

import java.awt.*;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

public class MapGenerator {
    private int[][] mapTile = null;
    private int dim;

    public MapGenerator() {}

    public void loadMap(int dim, String mapPath) {
        this.dim = dim;
        this.mapTile = new int[dim][dim];
        try {
            InputStream is = getClass().getResourceAsStream(mapPath);
            BufferedReader br = new BufferedReader(new InputStreamReader(is));

            for ( int i=0;i<dim;++i) {
                String[] numbers = br.readLine().split(" ");

                for ( int j=0;j<dim;++j) {
                    mapTile[i][j]=Integer.parseInt(numbers[j]);
                }
            }
        }catch(Exception e) {
            e.printStackTrace();
        }
    }
    void draw(Graphics2D g, Tile[] tiles) {
        int worldRow=0,worldCol=0;
        for ( int i=0;i<dim*dim;++i) {
            int worldX = worldCol * Tile.TILE_SIZE;
            int worldY = worldRow * Tile.TILE_SIZE;

            int screenX = worldX - Game.getPlayer().getWorldX()+ Game.getPlayer().getScreenX();
            int screenY = worldY - Game.getPlayer().getWorldY() + Game.getPlayer().getScreenY();
            if(worldX > Game.getPlayer().getWorldX() - Game.getPlayer().getScreenX() - Tile.TILE_SIZE &&
                    worldX < Game.getPlayer().getWorldX() + Game.getPlayer().getScreenX() + Tile.TILE_SIZE &&
                    worldY > Game.getPlayer().getWorldY() - Game.getPlayer().getScreenY() -Tile.TILE_SIZE &&
                    worldY < Game.getPlayer().getWorldY() + Game.getPlayer().getScreenY() + Tile.TILE_SIZE)
            {g.drawImage(tiles[mapTile[i/dim][i%dim]].img, screenX, screenY, null);}
            worldCol++;
            if(worldCol==dim) {worldCol=0;worldRow++;}
        }
    }
    public int[][] getMap() {
        return mapTile;
    }
}
