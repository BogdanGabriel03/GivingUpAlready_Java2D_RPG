package PaooGame.Tiles;

public class Level {
    private int dim=0;
    private int type=1;
    private String map_path=null;
    private int timer=0;
    private TileManager tileM;

    public Level(int _dim, int _type, String _map_path,int _timer) {
        dim = _dim;
        type = _type;
        map_path = _map_path;
        timer = _timer;
        tileM = new TileManager();
    }
}
