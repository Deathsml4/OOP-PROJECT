package src.DictionaryCommandLine;

import java.util.Objects;

public class Word {
    private String word_target;
    private String word_explain;

    /** Default constructor. */
    public Word() {
        this.word_target = "";
        this.word_explain = "";
    }

    /** Constructor with word and definition. */
    public Word(String word_target, String word_explain) {
        this.word_target = word_target;
        this.word_explain = word_explain;
    }

    /** word target getter. */
    public String getWord_target() {
        return word_target;
    }

    /** word target setter. */
    public void setWord_target(String word_target) {
        this.word_target = word_target;
    }

    /** word explain getter. */
    public String getWord_explain() {
        return word_explain;
    }

    /** word explain setter. */
    public void setWord_explain(String word_explain) {
        this.word_explain = word_explain;
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Word)) {
            return false;
        }

        Word other = (Word) o;
        return Objects.equals(word_target, word_explain);
    }

    @Override
    public int hashCode() {
        return Objects.hash(word_target, word_explain);
    }

    @Override
    public String toString() {
        return "Word(" + "word_target='" + word_target
                + "\',word_explain='" + word_explain + "\')";
    }
}
