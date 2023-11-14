package src.Game;

import src.InsertApp;
import java.lang.Math;
import java.sql.*;

public class FindSuitableWord {
    private static final Integer NUMBER_OF_QUESTIONS = 50;
    private String question;
    private String answer;
    private String option_a;
    private String option_b;
    private String option_c;
    private String option_d;

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
        return random;
    }

    public FindSuitableWord() {
        int rand = getRandom();
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
                this.option_a = rs.getString("option_a");
                this.option_b = rs.getString("option_b");
                this.option_c = rs.getString("option_c");
                this.option_d = rs.getString("option_d");
                this.answer = rs.getString("answer");
            }

            conn.close();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
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
        FindSuitableWord f = new FindSuitableWord();
        f.print();
        System.out.println(f.choose('a'));
    }
}
