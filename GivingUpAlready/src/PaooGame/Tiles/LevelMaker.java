package PaooGame.Tiles;

import PaooGame.Game;

public class LevelMaker {

    private Game game;
    private TileManager tileM;

    public LevelMaker(Game _game, TileManager _tileM) {
        this.game = _game;
        this.tileM = _tileM;
        tileM.setUpMap(game.currentLevel,game);
    }

    public Level update() {
        if(!Game.getPlayer().alive) {
            game.currentLevel=1;
        }
        else if ( Game.getPlayer().wonMessageOn) {
            game.currentLevel++;
            //if(game.currentLevel>3) {
            //   game.currentLevel=1;
            //}
        }

        if(game.currentLevel == 1 ) { return new Level_1(game, game.tileMan, game.assetSetter); }
        else if ( game.currentLevel == 2) { return new Level_2(game, game.tileMan, game.assetSetter); }
        else return new Level_3(game, game.tileMan, game.assetSetter);                                        // Of course need to implement end game after 3rd level is completed
    }
}
