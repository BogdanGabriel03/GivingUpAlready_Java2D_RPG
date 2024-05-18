package PaooGame;

import PaooGame.GameWindow.GameWindow;

//import java.sql.Connection;
//import java.sql.DriverManager;
import java.sql.*;

public class Main
{
    public static void main(String[] args)
    {
        Connection c = null;
        Statement stmnt = null;
        try {
            Class.forName("org.sqlite.JDBC");
            c = DriverManager.getConnection("jdbc:sqlite:Save.db");
            c.setAutoCommit(false);

            stmnt = c.createStatement();

            //String sql0 = "DROP TABLE IF EXISTS SAVE_DATA";
            //stmnt.executeUpdate(sql0);

            String sql = "CREATE TABLE IF NOT EXISTS SAVE_DATA " +
                    "(SAVE_ID INT PRIMARY KEY NOT NULL," +
                    " LEVEL INT NOT NULL, " +
                    " ATTACK INT NOT NULL, " +
                    " SPEED INT NOT NULL, " +
                    " SPELL_FIREBALL INT NOT NULL, " +
                    " SPELL_STONE_SHIELD INT NOT NULL)";
            stmnt.execute(sql);
        } catch (SQLException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
        Game paooGame = new Game("Giving Up Already ?", c);
        paooGame.StartGame();
    }
}
