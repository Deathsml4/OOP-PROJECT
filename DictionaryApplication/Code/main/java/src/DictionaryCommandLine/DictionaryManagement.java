package src.DictionaryCommandLine;

import src.InsertApp;
import java.util.Scanner;

public class DictionaryManagement {
    public static void insertFromCommandline(/*Dictionary dictionary*/) {
        InsertApp app = new InsertApp();
        Scanner sc = new Scanner(System.in);
        int n = sc.nextInt();
        sc.nextLine();
        for (int i = 0; i < n; i++) {
            String word_target = sc.nextLine();
            String word_explain = sc.nextLine();
            Word word = new Word(word_target, word_explain);
            //dictionary.add(word);
            app.insert(word_target, word_explain);
         }
    }



    public static void main(String[] args) {
        DictionaryManagement dm = new DictionaryManagement();
        dm.insertFromCommandline();
    }
}
