package src.Game;

import src.InsertApp;

import java.sql.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class Hangman {
    private String word_target;
    private int life;
    private Map<Character, Character> current = new HashMap<>();

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

    public int getRandom() {
        double rand = Math.random();
        int random = (int) (rand * InsertApp.DATABASE_SIZE + 1);

        return random;
    }

    private void addWord(String word) {
        for (int i = 0; i < word.length(); i++) {
            if (word.charAt(i) == ' ' || word.charAt(i) == '-') {
                current.put(word.charAt(i), word.charAt(i));
                continue;
            }

            current.put(word.charAt(i), '_');
        }
    }

    public void init() {
        life = 5;
        current.clear();
        int random = getRandom();
        String sql = "SELECT word " +
                "FROM tbl_edict " +
                "WHERE id = ? ";
        try (Connection conn = this.connect();
             PreparedStatement pstmt  = conn.prepareStatement(sql)){

            // set the value
            pstmt.setInt(1,random);
            //
            ResultSet rs  = pstmt.executeQuery();

            // loop through the result set
            while (rs.next()) {
                word_target = rs.getString("word").toUpperCase();
            }

            addWord(word_target);

            pstmt.close();
            conn.close();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public Hangman() {
    }

    public void displayCurrentWord() {
        for (int i = 0; i < word_target.length(); i++) {
            System.out.print(current.get(word_target.charAt(i)) + " ");
        }

        System.out.println();
    }

    public boolean win() {
        for (char i : current.keySet()) {
            if (current.get(i) != i) {
                return false;
            }
        }

        return true;
    }

    public void guess(char a) {
        a = Character.toUpperCase(a);
        if (current.containsKey(a)) {
            current.replace(a, a);
            displayCurrentWord();
            if (win()) {
                System.out.println("You are the winner!");
            }

            return;
        }

        --life;
        if (life <= 0) {
            System.out.println("You are lost!");
            return;
        }
        displayCurrentWord();
    }

    public static void main(String[] args) {
        Hangman h = new Hangman();
        h.init();
        h.displayCurrentWord();
        Scanner sc = new Scanner(System.in);
        char a;
        while (sc.hasNext()) {
            a = sc.nextLine().charAt(0);
            h.guess(a);
        }
    }
}
