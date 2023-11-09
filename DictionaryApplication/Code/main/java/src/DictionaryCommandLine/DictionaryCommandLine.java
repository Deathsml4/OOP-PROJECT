package src.DictionaryCommandLine;
import src.InsertApp;
import src.Trie.Trie;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class DictionaryCommandLine {
    public static void showAllWords(Trie trie) {
        ArrayList<String> list = trie.showAllWord();
        System.out.println(list.size());
        for (int i =0; i < list.size(); i++) {
            System.out.println(list.get(i));
        }

        /*int length = dictionary.size();
        for (int i = 1; i <= length; i++) {
            System.out.print(i + ". ");
            System.out.println(dictionary.get(i-1));
        }*/
    }

    public static void dictionaryBasic(int query, Trie trie/*, Dictionary dictionary*/) {
        switch (query) {
            case 0: DictionaryManagement.insertFromCommandline(); break;
            case 1: showAllWords(trie); break;
            default: break;
        }
    }

    public static void main(String[] args) {
        Trie trie = new Trie();
        InsertApp app = new InsertApp();

        app.insertToTrie(trie);

        /*ArrayList<String> l = trie.search("a");
        for (String s : l) {
            System.out.println(s);
        }*/
        //DictionaryCommandLine dc = new DictionaryCommandLine();
        String word_target = "aby";
        ArrayList<String> result = trie.search(word_target);
        for (String s : result) {
            System.out.println(s);
        }
        //dc.dictionaryBasic(0, d);
        //dc.dictionaryBasic(1, trie);
        //dc.dictionaryBasic(1,trie);
    }
}
