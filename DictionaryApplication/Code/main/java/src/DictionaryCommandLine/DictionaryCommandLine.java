package src.DictionaryCommandLine;

import java.util.Scanner;

public class DictionaryCommandLine {
    public static void showAllWords(src.DictionaryCommandLine.Dictionary dictionary) {
        int length = dictionary.size();
        for (int i = 1; i <= length; i++) {

        }
    }

    public void dictionaryBasic(int query, Dictionary dictionary) {
        switch (query) {
            case 0: DictionaryManagement.insertFromCommandline(dictionary); break;
            case 1: showAllWords(dictionary); break;
            default: break;
        }
    }
}
