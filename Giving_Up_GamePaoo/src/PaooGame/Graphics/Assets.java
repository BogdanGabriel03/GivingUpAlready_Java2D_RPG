package PaooGame.Graphics;

import PaooGame.Tiles.Tile;
import PaooGame.UtilityTool;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;

/*! \class public class Assets
    \brief Clasa incarca fiecare element grafic necesar jocului.

    Game assets include tot ce este folosit intr-un joc: imagini, sunete, harti etc.
 */
public class Assets
{
        /// Referinte catre elementele grafice (dale) utilizate in joc.
    public static BufferedImage[] playerLeft = new BufferedImage[4];;
    public static BufferedImage[] playerRight = new BufferedImage[4];;
    public static BufferedImage[] playerUp = new BufferedImage[4];;
    public static BufferedImage[] playerDown = new BufferedImage[4];;
    public static BufferedImage[] attackRight = new BufferedImage[4];
    public static BufferedImage[] attackLeft = new BufferedImage[4];
    public static BufferedImage[] tiles = new BufferedImage[7];
    public static BufferedImage[] monster_1 = new BufferedImage[4];
    public static BufferedImage chest;
    public static BufferedImage bossNpc;


    /*! \fn public static void Init()
        \brief Functia initializaza referintele catre elementele grafice utilizate.

        Aceasta functie poate fi rescrisa astfel incat elementele grafice incarcate/utilizate
        sa fie parametrizate. Din acest motiv referintele nu sunt finale.
     */
    public static void Init()
    {
            /// Se creaza temporar un obiect SpriteSheet initializat prin intermediul clasei ImageLoader
        SpriteSheet sheet = new SpriteSheet(ImageLoader.LoadImage("/textures/ResourcesSheet.png"));

            /// Se obtin subimaginile corespunzatoare elementelor necesare.

        playerRight[0] = setup(sheet,0,0);
        playerRight[1] = setup(sheet,1, 0);
        playerRight[2] = setup(sheet,2, 0);
        playerRight[3] = setup(sheet,3, 0);

        playerLeft[0] = setup(sheet,4, 0);
        playerLeft[1] = setup(sheet,5, 0);
        playerLeft[2] = setup(sheet,6, 0);
        playerLeft[3] = setup(sheet,7, 0);

        playerDown[0] = setup(sheet,6,1);
        playerDown[1] = setup(sheet,7,1);
        playerDown[2] = setup(sheet,8,1);
        playerDown[3] = setup(sheet,9,1);

        attackRight[0] = setup(sheet,8, 0);
        attackRight[1] = setup(sheet,9, 0);
        attackRight[2] = setup(sheet,0, 1);
        attackRight[3] = setup(sheet,1, 1);

        attackLeft[0] = setup(sheet,2, 1);
        attackLeft[1] = setup(sheet,3, 1);
        attackLeft[2] = setup(sheet,4,1);
        attackLeft[3] = setup(sheet,5,1);

        playerUp[0] = setup(sheet,0,2);
        playerUp[1] = setup(sheet,1,2);
        playerUp[2] = setup(sheet,2,2);
        playerUp[3] = setup(sheet,3,2);
        tiles[0] = setup(sheet,4,2);     // grass
        tiles[1] = setup(sheet,5,2);     // grass with accents
        tiles[2] = setup(sheet,6,2);     // tree
        tiles[3] = setup(sheet,7,2);     // sand
        tiles[4] = setup(sheet,8,2);     // sand with accents
        tiles[5] = setup(sheet,9,2);     // water
        tiles[6] = setup(sheet,0,3);     // water with accents
        chest = setup(sheet,1,3);
        bossNpc = sheet.crop(2,3);
        monster_1[0] = setup(sheet,3,3);
        monster_1[1] = setup(sheet,4,3);
        monster_1[2] = setup(sheet,5,3);
        monster_1[3] = monster_1[1];
    }

    private static BufferedImage setup(SpriteSheet sheet, int x, int y) {
        UtilityTool uTool = new UtilityTool();
        BufferedImage img = sheet.crop(x,y);
        img = uTool.scaleImage(img,Tile.TILE_SIZE,Tile.TILE_SIZE);
        return img;
    }
}
