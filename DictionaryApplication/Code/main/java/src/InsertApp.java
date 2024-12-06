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
        String url = "jdbc:sqlite:C:/Code part 2/Java Code/DictionaryApplication/new_dict_hh.db";
        Connection conn = null;
        try {
            conn = DriverManager.getConnection(url);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return conn;
    }

    public void deleteHistory() {
        String sql = "DELETE FROM search_history"
                + " WHERE id = (SELECT MIN(id) FROM search_history)";
        try {
            Connection conn = this.connect();
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.executeUpdate();
            pstmt.close();
            conn.close();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public void deleteDuplicateHistory(String wt) {
        String sql = "DELETE FROM search_history "
                + "WHERE word = ?";
        try {
            Connection conn = this.connect();
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, wt);
            pstmt.executeUpdate();
            pstmt.close();
            conn.close();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public boolean checkDuplicateHistory(String wt) {
        boolean ret = false;
        String sql = "SELECT COUNT(id) AS total FROM search_history "
                + "WHERE word = ?";
        try {
            Connection conn = this.connect();
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, wt);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                ret = !(rs.getInt("total") == 0);
            }
            pstmt.close();
            conn.close();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        return ret; // true if duplicate, else false if not or error
    }

    public boolean checkHistory() {
        String sql = "SELECT COUNT(word) AS total FROM search_history";
        try {
            Connection conn = this.connect();
            PreparedStatement pstmt  = conn.prepareStatement(sql);
            ResultSet rs  = pstmt.executeQuery();
            if (rs.next()) {
                int ret = rs.getInt("total");
                boolean res = !(ret > 10);
                pstmt.close();
                conn.close();
                return res;
            } else {
                pstmt.close();
                conn.close();
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return true;
    }
    public void insertToHistory(String word_target, String word_explain) {
        String sql = "INSERT INTO search_history (word, description) VALUES(?,?)";
        if (checkDuplicateHistory(word_target)) {
            deleteDuplicateHistory(word_target);
        }
        try {
            Connection conn = this.connect();
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, word_target);
            pstmt.setString(2, word_explain);
            pstmt.executeUpdate();

            pstmt.close();
            conn.close();

            while (!checkHistory()) {
                deleteHistory();
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    /**
     * Insert a new row into the warehouses table
     *
     * @param word_target từ vựng
     * @param word_explain giải thích
     */
    public void insert(String word_target, String word_explain) {
        String sql = "INSERT INTO User (word, description) VALUES(?,?)";

        try (Connection conn = this.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, word_target);
            pstmt.setString(2, word_explain);
            pstmt.executeUpdate();
            pstmt.close();
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
                + "FROM tbl_edict WHERE word = ? " /*+
                "LIMIT 10"*/;

        try (Connection conn = this.connect();
             PreparedStatement pstmt  = conn.prepareStatement(sql)){

            // set the value
            pstmt.setString(1,wt);
            //
            ResultSet rs  = pstmt.executeQuery();

            String word_target = "";
            String word_explain = "";
            // loop through the result set
            while (rs.next()) {
                word_target = rs.getString("word");
                word_explain = rs.getString("description");
                System.out.println(word_target +  "\t" +
                        word_explain); //+ "\t" +
                        //rs.getDouble("capacity"));
                //insertToHistory(word_target, word_explain);
            }

            pstmt.close();
            conn.close();
            insertToHistory(word_target, word_explain);
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

            pstmt.close();
            conn.close();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public void insertToTrie(Trie trie){
        String sql = "SELECT word "
                + "FROM tbl_edict" /*+
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

            pstmt.close();
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
        app.hasTheKeyword("border");
        //app.hasTheExplain("ánh");
    }

}