package src;

import src.Trie.Trie;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.ResultSet;


/**
 *
 * @author sqlitetutorial.net
 */
public class InsertApp {
    public static final int DATABASE_SIZE = 108854;

    /**
     * Connect to the test.db database
     *
     * @return the Connection object
     */
    private Connection connect() {
        // SQLite connection string
        String url = "jdbc:sqlite:C:/Code part 2/Java Code/DictionaryApplication/dict_hh.db";
        Connection conn = null;
        try {
            conn = DriverManager.getConnection(url);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return conn;
    }

    /**
     * Insert a new row into the warehouses table
     *
     * @param word_target từ vựng
     * @param word_explain giải thích
     */
    public void insert(String word_target, String word_explain) {
        String sql = "INSERT INTO av (word, description) VALUES(?,?)";

        try (Connection conn = this.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, word_target);
            pstmt.setString(2, word_explain);
            pstmt.executeUpdate();
            conn.close();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    /**
     * Get the result for a keyword.
     * @param wt word_target
     */
    public void hasTheKeyword(String wt){
        String sql = "SELECT word, description "
                + "FROM av WHERE word LIKE ? " /*+
                "LIMIT 10"*/;

        try (Connection conn = this.connect();
             PreparedStatement pstmt  = conn.prepareStatement(sql)){

            // set the value
            pstmt.setString(1,wt + '%');
            //
            ResultSet rs  = pstmt.executeQuery();

            // loop through the result set
            while (rs.next()) {
                System.out.println(rs.getString("word") +  "\t" +
                        rs.getString("description")); //+ "\t" +
                        //rs.getDouble("capacity"));
            }

            conn.close();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public void hasTheExplain(String wt){
        String sql = "SELECT word, description "
                + "FROM va WHERE word LIKE ? " /*+
                "LIMIT 10"*/;

        try (Connection conn = this.connect();
             PreparedStatement pstmt  = conn.prepareStatement(sql)){

            // set the value
            pstmt.setString(1, '%' + wt + '%');
            //
            ResultSet rs  = pstmt.executeQuery();

            // loop through the result set
            while (rs.next()) {
                System.out.println(rs.getString("word") +  "\t" +
                        rs.getString("description")); //+ "\t" +
                //rs.getDouble("capacity"));
            }

            conn.close();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public void insertToTrie(Trie trie){
        String sql = "SELECT word "
                + "FROM av" /*+
                "LIMIT 10"*/;

        try (Connection conn = this.connect();
             PreparedStatement pstmt  = conn.prepareStatement(sql)){

            // set the value
            //pstmt.setInt(1, id);
            //
            ResultSet rs  = pstmt.executeQuery();

            // loop through the result set
            while (rs.next()) {
                String str = rs.getString("word");
                trie.insert(str);
                //System.out.println(str);
            }

            conn.close();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    /**
     * @param args the command line argument
     */
    public static void main(String[] args) {

        InsertApp app = new InsertApp();
        //app.connect();
        // insert three new rows
        /*app.insert("rose", "hoa hồng");
        app.insert("strawberry", "dâu tây");
        app.insert("raspberry", "quả mâm xôi");*/
        //app.hasTheKeyword("sex");
        app.hasTheExplain("ánh");
    }

}