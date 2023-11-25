package src.Game;

import src.InsertApp;

import java.sql.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class Hangman {
    public static int MAX_LIFE = 5;
    private String word_target;
    private int life = MAX_LIFE;
    private int suggestsRemain = 7;
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

    public void suggest() {
        if (suggestsRemain == 0) {
            System.out.println("Sorry, out of suggestions!");
            return;
        }

        if (win()) {
            System.out.println("You have won");
            return;
        }

        --suggestsRemain;
        for (int i = 0; i < word_target.length(); i++) {
            char b = word_target.charAt(i);
            if (current.get(b) != b) {
                current.replace(b, b);
                displayCurrentWord();
                break;
            }
        }
    }

    public String getResult() {
        return word_target;
    }

    public static void main(String[] args) {
        Hangman h = new Hangman();
        Scanner sc = new Scanner(System.in);
        char a;
        String temp;
        boolean gameEnded = false;
        while (!gameEnded) {
            h.init();
            h.displayCurrentWord();
            while (h.life > 0 && !h.win()) {
                System.out.println("[guess/suggest]"); // Chọn suggest hoặc kí tự mà bạn đoán
                temp = sc.nextLine();
                if (temp.equals("suggest")) {
                    h.suggest();
                    continue;
                }

                a = temp.charAt(0);
                h.guess(a);
            }

            if (h.life > 0) {
                continue;
            }

            if (h.life == 0) {
                System.out.println("Result is : " + h.getResult());
            }

            System.out.println("Continue ? [true/false]"); // Chọn true để tiếp, false để thôi
            gameEnded = !(sc.nextBoolean());
            if (gameEnded) {
                break;
            }

            h.life = MAX_LIFE;
            h.suggestsRemain = 7;
            if (sc.hasNextLine()) {
                temp = sc.nextLine();
            }
        }
    }
}
