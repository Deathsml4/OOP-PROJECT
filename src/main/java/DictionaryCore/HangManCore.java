package DictionaryCore;

import InsertApp.InsertApp;
import java.sql.*;
import java.util.HashMap;
import java.util.Map;
public class HangManCore {
    public static int MAX_LIFE = 8;
    private String word_target;
    private String hintWord;
    private int life = MAX_LIFE;
    private int suggestsRemain;
    private Map<Character, Character> current = new HashMap<>();

    private Connection connect() {
        String url = "jdbc:sqlite:D:/JavaProj/DictionaryOOP/dict_hh.db";
        Connection conn = null;
        try {
            conn = DriverManager.getConnection(url);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return conn;
    }

    public int getRandom() {
        double rand = Math.random();
        int random = (int) (rand * InsertApp.DATABASE_SIZE + 1);
        return random;
    }
    public boolean isAtoZ(char a) {
        if (a < 'A' || a > 'Z') {
            return false;
        }
        return true;
    }

    private void addWord(String word) {
        for (int i = 0; i < word.length(); i++) {
            if (!isAtoZ(word.charAt(i))) {
                current.put(word.charAt(i), word.charAt(i));
                continue;
            }
            current.put(word.charAt(i), '_');
        }
    }
    public void init() {
        current.clear();
        int random = getRandom();
        String sql = "SELECT word " +
                "FROM tbl_edict " +
                "WHERE id = ? ";
        try {
            Connection conn = this.connect();
            PreparedStatement pstmt  = conn.prepareStatement(sql);
            pstmt.setInt(1,random);
            ResultSet rs  = pstmt.executeQuery();
            while (rs.next()) {
                word_target = rs.getString("word").toUpperCase();
            }
            addWord(word_target);
            suggestsRemain = word_target.length()/3;
            life = MAX_LIFE;
            pstmt.close();
            conn.close();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public HangManCore() {
    }

    public String displayCurrentWord() {
        String ret="";
        for (int i = 0; i < word_target.length(); i++) {
            ret += current.get(word_target.charAt(i))+" ";
        }
        return ret.substring(0,ret.length()-1);
    }

    public boolean win() {
        for (char i : current.keySet()) {
            if (current.get(i) != i) {
                return false;
            }
        }
        return true;
    }

    public String guess(char a) {
        a = Character.toUpperCase(a);
        if (current.containsKey(a)) {
            current.replace(a, a);
            return displayCurrentWord();
        }
        --life;
        return "";
    }

    public String suggest() {
        --suggestsRemain;
        for (int i = 0; i < word_target.length(); i++) {
            char b = word_target.charAt(i);
            if (current.get(b) != b) {
                current.replace(b, b);
                hintWord = String.valueOf(b);
                break;
            }
        }
        return displayCurrentWord();
    }
    public String getAnswer() {
        String ret="";
        for (int i = 0; i < word_target.length(); i++) {
            ret += word_target.charAt(i)+" ";
        }
        return ret.substring(0,ret.length()-1);
    }
    public int getLife(){
        return life;
    }
    public int getSuggestsRemain(){
        return suggestsRemain;
    }
    public String getHintWord(){
        return hintWord;
    }
}
