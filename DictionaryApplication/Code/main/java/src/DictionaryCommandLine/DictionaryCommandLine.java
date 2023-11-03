package src.DictionaryCommandLine;
import src.InsertApp;

public class DictionaryCommandLine {
    public static void showAllWords(src.DictionaryCommandLine.Dictionary dictionary) {
        InsertApp app = new InsertApp();

        /*int length = dictionary.size();
        for (int i = 1; i <= length; i++) {
            System.out.print(i + ". ");
            System.out.println(dictionary.get(i-1));
        }*/
    }

    public void dictionaryBasic(int query, Dictionary dictionary) {
        switch (query) {
            case 0: DictionaryManagement.insertFromCommandline(dictionary); break;
            case 1: showAllWords(dictionary); break;
            default: break;
        }
    }

    public static void main(String[] args) {
        Dictionary d = new Dictionary();
        DictionaryCommandLine dc = new DictionaryCommandLine();
        //dc.dictionaryBasic(0, d);
        dc.dictionaryBasic(1, d);
    }
}
