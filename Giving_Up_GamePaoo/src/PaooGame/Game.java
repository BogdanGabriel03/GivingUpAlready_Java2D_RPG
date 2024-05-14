package PaooGame;

import PaooGame.GameWindow.GameWindow;
import PaooGame.Graphics.Assets;
import PaooGame.Tiles.Level;
import PaooGame.Tiles.LevelMaker;
import PaooGame.Tiles.Tile;
import PaooGame.Tiles.TileManager;
import PaooGame.entity.Entity;
import PaooGame.entity.Player;

import java.awt.*;
import java.awt.image.BufferStrategy;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class Game implements Runnable
{
    private GameWindow      wnd;        /*!< Fereastra in care se va desena tabla jocului*/
    public boolean updated;
    private boolean         runState;   /*!< Flag ce starea firului de executie.*/
    private Thread          gameThread; /*!< Referinta catre thread-ul de update si draw al ferestrei*/
    private BufferStrategy  bs;         /*!< Referinta catre un mecanism cu care se organizeaza memoria complexa pentru un canvas.*/
    public final static int WND_WIDTH = Tile.TILE_SIZE * 16;
    public final static int WND_HEIGHT = Tile.TILE_SIZE * 12;
    public TileManager tileMan;
    private KeyHandler keyH;
    public CollisionChecker collissionChecker;
    public int l=0;

    // ENTITIES
    private static Player p;
    public ArrayList<Entity> items = new ArrayList<>();
    public ArrayList<Entity> npc = new ArrayList<>();
    public ArrayList<Entity> monster = new ArrayList<>();
    public int monsterCount;
    public ArrayList<Entity> entityList = new ArrayList<>();
    public ArrayList<Entity> spellList = new ArrayList<>();
    public AssetSetter assetSetter = new AssetSetter(this);

    // LEVEL LOGIC
    private LevelMaker lvlMaker;
    public int currentLevel=1;
    private Level lvlInstance;

    // MUSIC, SOUND EFFECTS AND USER INTERFACE
    Sound music = new Sound();
    Sound se = new Sound();
    public UI ui = new UI(this);

    // GAME STATE
    public enum GameState {
        PLAY_STATE,
        PAUSE_STATE,
        DIALOGUE_STATE,
        MAIN_MENU_STATE,
        END_LEVEL_STATE;
    }
    private static GameState gState;

    /// Sunt cateva tipuri de "complex buffer strategies", scopul fiind acela de a elimina fenomenul de
    /// flickering (palpaire) a ferestrei.
    /// Modul in care va fi implementata aceasta strategie in cadrul proiectului curent va fi triplu buffer-at

    ///                         |------------------------------------------------>|
    ///                         |                                                 |
    ///                 ****************          *****************        ***************
    ///                 *              *   Show   *               *        *             *
    /// [ Ecran ] <---- * Front Buffer *  <------ * Middle Buffer * <----- * Back Buffer * <---- Draw()
    ///                 *              *          *               *        *             *
    ///                 ****************          *****************        ***************

    public Graphics g;

    // CONSTRUCTOR
    public Game(String title)
    {
        wnd = new GameWindow(title, WND_WIDTH, WND_HEIGHT);
        keyH = new KeyHandler(this);
        collissionChecker  = new CollisionChecker(this);
        p = Player.Instance(this,keyH);
        runState = false;
    }

    /*! \fn private void init()
        \brief  Metoda construieste fereastra jocului, initializeaza aseturile, listenerul de tastatura etc.
        Fereastra jocului va fi construita prin apelul functiei BuildGameWindow();
        Sunt construite elementele grafice (assets): dale, player, elemente active si pasive.

     */
    private void InitGame()
    {
        // BUILDING GAME WINDOW
        wnd.BuildGameWindow(keyH);

        // LOADING GRAPHIC ELEMENTS
        Assets.Init();
        tileMan = new TileManager();
        lvlMaker = new LevelMaker(this, tileMan);
        lvlInstance = lvlMaker.update();
        gState = GameState.MAIN_MENU_STATE;
    }

    public void run()
    {
        // INIT GAME OBJECT
        InitGame();
        long oldTime = System.nanoTime();
        long curentTime;

        final int framesPerSecond   = 30;
        final double timeFrame      = 1000000000.0 / framesPerSecond;

        while (runState)
        {
            curentTime = System.nanoTime();
            if((curentTime - oldTime) > timeFrame)
            {
                // UPDATE ELEMENTS ON SCREEN
                Update();
                // DRAW ELEMENTS TO GRAPHIC WINDOW
                Draw();
                oldTime = curentTime;
            }
        }

    }

    /*! \fn public synchronized void start()
        \brief Creaza si starteaza firul separat de executie (thread).

        Metoda trebuie sa fie declarata synchronized pentru ca apelul acesteia sa fie semaforizat.
     */
    public synchronized void StartGame()
    {
        if(!runState)
        {
            runState = true;
                /// Se construieste threadul avand ca parametru obiectul Game. De retinut faptul ca Game class
                /// implementeaza interfata Runnable. Threadul creat va executa functia run() suprascrisa in clasa Game.
            gameThread = new Thread(this);
            // THREAD EXECUTES run()
            gameThread.start();
        }
        else
        {
            // THREAD ALREADY ON
            return;
        }
    }

    /*! \fn public synchronized void stop()
        \brief Opreste executie thread-ului.

        Metoda trebuie sa fie declarata synchronized pentru ca apelul acesteia sa fie semaforizat.
     */
    public synchronized void StopGame()
    {
        if(runState)
        {
                /// Actualizare stare thread
            runState = false;
                /// Metoda join() arunca exceptii motiv pentru care trebuie incadrata intr-un block try - catch.
            try
            {
                    /// Metoda join() pune un thread in asteptare panca cand un altul isi termina executie.
                    /// Totusi, in situatia de fata efectul apelului este de oprire a threadului.
                gameThread.join();
            }
            catch(InterruptedException ex)
            {
                    /// In situatia in care apare o exceptie pe ecran vor fi afisate informatii utile pentru depanare.
                ex.printStackTrace();
            }
        }
        else
        {
                /// Thread-ul este oprit deja.
            return;
        }
    }

    private void Update()
    {
        if(gState == GameState.PLAY_STATE) {
            updated = false;
            p.update();
            monsterCount=0;
            for ( int i=0; i<monster.size(); ++i) {
                if(monster.get(i) !=null) {
                    if(monster.get(i).alive && !monster.get(i).dying) {
                        monster.get(i).update();
                        monsterCount++;
                    }
                    else if(!monster.get(i).alive){
                        monster.set(i, null);
                    }
                }
            }

            for ( int i=0; i<spellList.size(); ++i) {
                if(spellList.get(i) !=null) {
                    if(spellList.get(i).alive ) {
                        spellList.get(i).update();
                    }
                    else {
                        spellList.remove(i);
                    }
                }
            }

            if(monsterCount==0) p.won=true;
        }
        if ( gState == GameState.END_LEVEL_STATE) {
            if(!updated) {lvlInstance = lvlMaker.update();updated = true;p.won=false;}
        }
    }


    private void Draw()
    {
        // Returnez bufferStrategy pentru canvasul existent
        bs = wnd.GetCanvas().getBufferStrategy();
        if(bs == null)
        {
            // Se executa doar la primul apel al metodei Draw()
            try
            {
                    /// Se construieste tripul buffer
                wnd.GetCanvas().createBufferStrategy(3);
                return;
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
        }
        g = bs.getDrawGraphics();
        Graphics2D g2 = (Graphics2D) g;

        /// Se sterge ce era
        g.clearRect(0, 0, wnd.GetWndWidth(), wnd.GetWndHeight());

        if(gState== GameState.MAIN_MENU_STATE  || gState == GameState.END_LEVEL_STATE) {
            ui.draw(g2);
        }
        else {
            //DEBUG STUFF ( CALCULATE TIME NEEDED TO RENDER )
            long drawStart=0;
            boolean ok = keyH.getChecker();
            if(ok) { drawStart = System.nanoTime();}

            // DRAWING TILES

            lvlInstance.draw(g2);
            //lvlMaker.draw(g2);

            // ADDING ENTITIES TO THE LIST ( PLAYER, ITEMS, NPC-S, MONSTERS )
            entityList.add(p);
            for (Entity value : npc) {
                if (value != null) entityList.add(value);
            }
            for (Entity item : items) {
                if (item != null) entityList.add(item);
            }
            for (Entity value : monster) {
                if (value != null) entityList.add(value);
            }
            for (Entity value : spellList) {
                if (value != null) entityList.add(value);
            }

            // SORTING ENTITIES
            Collections.sort(entityList, new Comparator<Entity>() {
                public int compare(Entity e1, Entity e2) {
                    return e1.worldY - e2.worldY;
                }
            });

            // DRAWING ENTITIES AND EMPTYING LIST
            for (Entity entity : entityList) {
                entity.draw(g2);
            }
            entityList.clear();
            // DRAWING USER INTERFACE
            ui.draw(g2);

            // DEBUG STUFF ( SHOW TIME NEEDED TO RENDER )
            if(ok)
            {
                long drawEnd = System.nanoTime();long passed = drawEnd-drawStart;
                System.out.println("Draw Time: " + passed);
            }
        }

        // EFFECTIVELY DRAWING AND CLEAN UP
        bs.show();
        g.dispose();
    }

    public static Player getPlayer() {
        return p;
    }

    public static GameState getGameState() {
        return gState;
    }

    public static void setGameState(GameState gs) {
        gState = gs;
    }

    public void playMusic(int i) {
        music.setFile(i);
        music.play();
        music.loop();
    }

    public void stopMusic() {
        music.stop();
    }

    public void playSE(int i) {
        se.setFile(i);
        se.play();
    }
}

