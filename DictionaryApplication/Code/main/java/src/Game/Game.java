package src.Game;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public abstract class Game {
    protected int life;

    /** Connect to db. */
    protected Connection connect() {
        // SQLite connection string
        String url = "jdbc:sqlite:C:/Code part 2/Java Code/DictionaryApplication/new_dict_hh.db";
        Connection conn = null;
        try {
            conn = DriverManager.getConnection(url);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return conn;
    }

    /** cmt.*/
    public abstract void init();
}
