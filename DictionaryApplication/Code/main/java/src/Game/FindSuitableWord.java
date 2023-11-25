package src.Game;

import javax.swing.text.Style;
import java.util.Scanner;
import java.lang.Math;
import java.sql.*;
import java.util.ArrayList;

public class FindSuitableWord {
    private static final Integer NUMBER_OF_QUESTIONS = 50;
    private String question;
    private String answer;
    private String option_a;
    private String option_b;
    private String option_c;
    private String option_d;
    private ArrayList<Boolean> used = new ArrayList<>();
    private String[] arr = new String[4];

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
        int random = (int) (rand * NUMBER_OF_QUESTIONS + 1);
        if (used.get(random)) {
            return getRandom();
        }

        return random;
    }

    public FindSuitableWord() {
        used.add(true);
        for (int i = 1; i <= NUMBER_OF_QUESTIONS; i++) {
            used.add(false);
        }
    }

    public void initQuestion() {
        int rand = getRandom();
        used.set(rand, true);
        String sql = "SELECT question, option_a, option_b, option_c, option_d, answer " +
                "FROM DictGameDtb " +
                "WHERE id = ? ";
        try (Connection conn = this.connect();
             PreparedStatement pstmt  = conn.prepareStatement(sql)){

            // set the value
            pstmt.setInt(1,rand);
            //
            ResultSet rs  = pstmt.executeQuery();

            // loop through the result set
            while (rs.next()) {
                this.question = rs.getString("question");
                /*this.option_a = rs.getString("option_a");
                this.option_b = rs.getString("option_b");
                this.option_c = rs.getString("option_c");
                this.option_d = rs.getString("option_d");*/
                this.answer = rs.getString("answer");

                this.arr[0] = rs.getString("option_a");
                this.arr[1] = rs.getString("option_b");
                this.arr[2] = rs.getString("option_c");
                this.arr[3] = rs.getString("option_d");

                int rand_switch = getRandom() % 4;
                this.option_a = arr[rand_switch++];
                if (rand_switch >= 4 ) rand_switch -= 4;
                this.option_b = arr[rand_switch++];
                if (rand_switch >= 4 ) rand_switch -= 4;
                this.option_c = arr[rand_switch++];
                if (rand_switch >= 4 ) rand_switch -= 4;
                this.option_d = arr[rand_switch++];
            }

            pstmt.close();
            conn.close();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public String getAnswer() {
        return answer;
    }

    public void print() {
        System.out.println(question);
        System.out.println("A." + option_a);
        System.out.println("B." + option_b);
        System.out.println("C." + option_c);
        System.out.println("D." + option_d);
    }

    public boolean compare(String cp) {
        return cp.equals(answer);
    }

    public boolean choose(char a) {
        a = Character.toUpperCase(a);
        boolean res = false;
        int flag = 0;
        switch (a) {
            case 'A': res = compare(option_a); break;
            case 'B': res = compare(option_b); break;
            case 'C': res = compare(option_c); break;
            case 'D': res = compare(option_d); break;
            default: break;
        }

        return res;
    }

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        FindSuitableWord f = new FindSuitableWord();
        int health = 1;
        int point = 0;
        while (health > 0 && point < 10) {
            f.initQuestion();
            f.print();
            char your_answer = sc.next().charAt(0);
            if (f.choose(your_answer)) {
                ++point;
                System.out.println("Correct Answer!");
                continue;
            }

            System.out.println("Wrong Answer!");
            System.out.println("The result is :" + f.getAnswer());
            --health;
        }

        System.out.println("Game over");
        System.out.println("Score: " + point);
    }
}
