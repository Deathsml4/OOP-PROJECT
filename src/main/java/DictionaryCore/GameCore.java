package DictionaryCore;

import javax.lang.model.type.ArrayType;
import java.lang.reflect.Array;
import java.sql.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Vector;

public class GameCore {
    private static final Integer NUMBER_OF_QUESTIONS = 50;
    private String question;
    private String answer;
    private String option_a;
    private String option_b;
    private String option_c;
    private String option_d;
    private int score = 0;
    private int highScore;
    private int attemp = 3;
    private ArrayList<Boolean> used = new ArrayList<>();
    private Vector<String> arr = new Vector<>(7);

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
        int random = (int) (rand * NUMBER_OF_QUESTIONS + 1);
        if (used.get(random)) {
            return getRandom();
        }
        return random;
    }
    public GameCore() {
        used.add(true);
        for (int i = 1; i <= NUMBER_OF_QUESTIONS; i++) {
            used.add(false);
        }
    }
    public void fullCheck(){
        if(!used.contains(false)){
            for (int i = 1; i <= NUMBER_OF_QUESTIONS; i++) {
                used.set(i, false);
            }
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
                this.arr.clear();
                this.arr.add(rs.getString("option_a"));
                this.arr.add(rs.getString("option_b"));
                this.arr.add(rs.getString("option_c"));
                this.arr.add(rs.getString("option_d"));
                this.arr.add(arr.get(0));
                this.arr.add(arr.get(1));
                this.arr.add(arr.get(2));

                int rand_switch = rand % 4;
                this.option_a = arr.get(rand_switch++);
                this.option_b = arr.get(rand_switch++);
                this.option_c = arr.get(rand_switch++);
                this.option_d = arr.get(rand_switch);
            }

            conn.close();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }
    public void saveHighScore(int n){
        String sql = "UPDATE User SET word = ? WHERE id = 1";
        try {
            Connection conn = this.connect();
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, Integer.toString(n));
            pstmt.executeUpdate();
            pstmt.close();
            conn.close();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }
    public int getHigh(){
        String sql = "SELECT word FROM User WHERE id = 1 ";
        try {
            Connection conn = this.connect();
            PreparedStatement pstmt = conn.prepareStatement(sql);
            ResultSet rs = pstmt.executeQuery();
            int n = Integer.valueOf(rs.getString("word"));
            pstmt.close();
            conn.close();
            return n;
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return 0;
    }
    public String getQuestion(){
        return question;
    }
    public String getAnswer(){
        return answer;
    }
    public String getOption_a(){
        return option_a;
    }
    public String getOption_b(){
        return option_b;
    }
    public String getOption_c(){
        return option_c;
    }
    public String getOption_d(){
        return option_d;
    }
    public int getScore(){
        return score;
    }
    public void setScore(){
        this.score++;
    }
    public int getHighScore(){
        return highScore;
    }
    public void setHighScore(int n){
        this.highScore=n;
    }
    public int getAttemp(){
        return attemp;
    }
    public void setAttemp(){
        this.attemp--;
    }
    public boolean updateHighScore(){
        if(score>highScore){
            highScore = score;
            this.saveHighScore(highScore);
            return true;
        }
        return false;
    }
    public void resetStats(){
        this.score = 0;
        this.attemp = 3;
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
}

