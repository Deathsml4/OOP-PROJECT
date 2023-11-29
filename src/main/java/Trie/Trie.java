package Trie;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.ArrayList;
import java.util.Map;
import java.util.TreeMap;

public class Trie {
    protected static final ObservableList<String> searchWords = FXCollections.observableArrayList();
    protected static final TrieNode root = new TrieNode();

    public static ObservableList<String> getSearchWords() {
        return searchWords;
    }

    public static void insert(String word_target) {
        int length = word_target.length();
        TrieNode p = root;
        for (int level = 0; level < length; level++) {
            char index = word_target.charAt(level);
            if (p.child.get(index) == null) {
                p.child.put(index, new TrieNode());
            }

            p = p.child.get(index);
        }

        p.isEndOfWord = true;
    }

    /**
     * Get all words ended in the subtree of node p.
     * @param p the current node
     * @param word_target the word that p represents
     */
    private static void dfsGetWordsSubtree(TrieNode p, String word_target) {
        try{
            if (p.isEndOfWord) {
                searchWords.add(word_target);
            }

        for (char index : p.child.keySet()) {
            if (p.child.get(index) != null) {
                dfsGetWordsSubtree(p.child.get(index), word_target + index);
            }
        }
        } catch (NullPointerException e){
            System.out.println("Từ không tồn tại!");
        }
    }

    public static ObservableList<String> search(String prefix) {
        ObservableList<String> s = FXCollections.observableArrayList();
        try{
        if (prefix.isEmpty()) {
            return FXCollections.observableArrayList();
        }

        searchWords.clear();
        int length = prefix.length();
        TrieNode p = root;
        for (int level = 0; level < length; level++) {
            char index = prefix.charAt(level);
            if (p.child.get(index) == null) {
                getSearchWords();
            }
            p = p.child.get(index);
        }
        dfsGetWordsSubtree(p, prefix);
        return getSearchWords();

        } catch(NullPointerException e){
            System.out.println("Từ không tồn tại!");
        }
        return s;
    }
    public static ObservableList<String> showAllWord() {
        searchWords.clear();
        //int length = prefix.length();
        TrieNode p = root;

        dfsGetWordsSubtree(p, "");
        return getSearchWords();
    }

    public static void delete(String target) {
        int length = target.length();

        TrieNode p = root;

        for (int level = 0; level < length; level++) {
            char index = target.charAt(level);
            if (p.child.get(index) == null) {
                System.out.println("This word has not been inserted");
                return;
            }
            p = p.child.get(index);
        }

        if (!p.isEndOfWord) {
            System.out.println("This word has not been inserted");
            return;
        }
        p.isEndOfWord = false;
    }

    /** Node in Trie Data Structure.*/
    public static class TrieNode {
        Map<Character, TrieNode> child = new TreeMap<>();
        boolean isEndOfWord = false;

        TrieNode() {
        }
    }
}