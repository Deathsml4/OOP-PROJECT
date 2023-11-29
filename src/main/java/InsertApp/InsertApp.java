package InsertApp;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.ResultSet;
import Trie.Trie;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class InsertApp {
  /**
   * Connect to the test.db database
   *
   * @return the Connection object
   */
  public static final int DATABASE_SIZE = 139240;
  private Connection connect() throws SQLException {
        String url = "jdbc:sqlite:D:/JavaProj/DictionaryOOP/dict_hh.db";
        Connection conn = DriverManager.getConnection(url);
        return conn;
  }



    /**
     * Insert a new row into the warehouses table
     *
     * @param word_target từ vựng
     * @param word_explain giải thích
     */
    public void insert(String word_target, String word_explain) {
        String sql = "INSERT INTO tbl_edict (word, description) VALUES(?,?)";
        try {
            Connection conn = this.connect();
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, word_target);
            pstmt.setString(2, word_explain);
            pstmt.executeUpdate();
            pstmt.close();
            conn.close();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }
    public void delete(String word_target) {
        String sql = "DELETE FROM tbl_edict WHERE word = ?";
        try {
            Connection conn = this.connect();
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, word_target);
            pstmt.executeUpdate();
            pstmt.close();
            conn.close();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }
    public void replace(String word_target, String word_explain) {
        String sql = "UPDATE tbl_edict "
                + "SET description = ?" +
                "WHERE word = ?";

        try {
            Connection conn = this.connect();
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, word_explain);
            pstmt.setString(2, word_target);
            pstmt.executeUpdate();
            pstmt.close();
            conn.close();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }
    public void edit(String word_target, String word_explain){
        String sql = "UPDATE tbl_edict "
                + "SET description = ?" +
                "WHERE word = ?";
        String s = wordCheck(word_target);
        String word_edited;
        if(s.indexOf("</Q></N>")!=-1){
            word_edited = wordCheck(word_target).substring(0, s.length()-20)+"<br />"+word_explain+"</Q></N></I></F></C>";
        }else{
            word_edited=wordCheck(word_target)+"\n"+word_explain;
        }
        try {
            Connection conn = this.connect();
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, word_edited);
            pstmt.setString(2, word_target);
            pstmt.executeUpdate();
            pstmt.close();
            conn.close();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public void deleteHistory() {
        String sql = "DELETE FROM search_history "
                + "WHERE id = (SELECT MIN(id) FROM search_history)";
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
                boolean res = !(ret > 11);
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
    public void insertToHistory(String word_target) {
        String sql = "INSERT INTO search_history (word) VALUES (?)";
        if (checkDuplicateHistory(word_target)) {
            deleteDuplicateHistory(word_target);
        }

        try {
            Connection conn = this.connect();
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, word_target);
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
    public ObservableList<String> getHistory(){
        String sql = "SELECT word FROM search_history";
        try {
            Connection conn = this.connect();
            PreparedStatement pstmt  = conn.prepareStatement(sql);
            ResultSet rs  = pstmt.executeQuery();
            ObservableList<String> ret = FXCollections.observableArrayList();
            while(rs.next()) {
                ret.add(rs.getString("word"));
            }
            pstmt.close();
            conn.close();
            return ret;
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return null;
    }
    public void deleteAllHis(){
        String sql = "DELETE FROM search_history";
        try {
            Connection conn = this.connect();
            PreparedStatement pstmt  = conn.prepareStatement(sql);
            pstmt.executeUpdate();
            pstmt.close();
            conn.close();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    /**
     * Get the warehouse whose capacity greater than a specified capacity
     * @param wt word_target
     */

    public String wordCheck(String wt){
        String sql = "SELECT word, description "
                + "FROM tbl_edict WHERE word LIKE ? " +
                "LIMIT 1";

        try {
            Connection conn = this.connect();
            PreparedStatement pstmt  = conn.prepareStatement(sql);
            pstmt.setString(1, wt + "%");
            ResultSet rs  = pstmt.executeQuery();
            if (rs.next()) {
                String ret = rs.getString("description");
                pstmt.close();
                conn.close();
                return ret;
            } else {
                pstmt.close();
                conn.close();
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return null;
    }
    public void hasTheExplain(String wt){
        String sql = "SELECT word, description "
                + "FROM words WHERE description LIKE ? " +
                "LIMIT 1";
        try {
            Connection conn = this.connect();
            PreparedStatement pstmt  = conn.prepareStatement(sql);
            pstmt.setString(1, '%' + wt + '%');
            ResultSet rs  = pstmt.executeQuery();
            // loop through the result set
            if (rs.next()) {
                System.out.println(rs.getString("word") +  "\t" +
                        rs.getString("description"));
            }
            pstmt.close();
            conn.close();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }
    public void insertToTrie(Trie trie){
        String sql = "SELECT word "
                + "FROM tbl_edict";

        try (Connection conn = this.connect();
             PreparedStatement pstmt  = conn.prepareStatement(sql)){
            ResultSet rs  = pstmt.executeQuery();
            while (rs.next()) {
                String str = rs.getString("word");
                trie.insert(str);
            }
            pstmt.close();
            conn.close();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }
}
