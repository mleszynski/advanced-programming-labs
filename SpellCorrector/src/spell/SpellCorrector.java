package spell;

import java.io.IOException;
import java.io.File;
import java.util.Scanner;
// IMPORTANT: use TreeSet to automatically alphabetize candidate sets for conflict resolution
import java.util.TreeSet;

public class SpellCorrector implements ISpellCorrector {
    private final Trie dictionary;
    private final TreeSet<String> allCandidates;
    private final TreeSet<String> goodCandidates;

    public SpellCorrector() {
        this.dictionary = new Trie();
        this.allCandidates = new TreeSet<>();
        this.goodCandidates = new TreeSet<>();
    }
    @Override
    public void useDictionary(String dictionaryFileName) throws IOException {
        File inFile = new File(dictionaryFileName);
        Scanner scanner = new Scanner(inFile);
        while(scanner.hasNext()) {
            String str = scanner.next();
            this.dictionary.add(str.toLowerCase());
        }
    }

    @Override
    public String suggestSimilarWord(String inputWord) {
        // IMPORTANT: handle empty word case
        if (inputWord.equals("")) return null;
        inputWord = inputWord.toLowerCase();
        // handle case when word is spelled correctly
        if (this.dictionary.find(inputWord) != null) return inputWord;

        // IMPORTANT: clear all candidate lists before making suggestion
        this.allCandidates.clear();
        this.goodCandidates.clear();
        getDeletions(inputWord);
        getInsertions(inputWord);
        getTranspositions(inputWord);
        getAlterations(inputWord);
        makeGoodCandidates();
        String suggestion = getSuggestion();

        // handle case for distance 2 words
        if (this.goodCandidates.isEmpty()) {
            // IMPORTANT: clone the TreeSet so that we aren't appending to and modifying the original at the same time
            TreeSet<String> tempCandidates = (TreeSet)this.allCandidates.clone();
            for (String str : tempCandidates) {
                getDeletions(str);
                getInsertions(str);
                getTranspositions(str);
                getAlterations(str);
            }
            makeGoodCandidates();
            suggestion = getSuggestion();
        }
        return suggestion;
    }

    private void getDeletions(String word) {
        // IMPORTANT: handle edge case where word is only 1 chars
        if (word.length() <= 1) return;
        StringBuilder tempWord = new StringBuilder();
        // check all words with one missing char
        for (int i = 0; i < word.length(); i++) {
            tempWord.append(word);
            tempWord.deleteCharAt(i);
            this.allCandidates.add(tempWord.toString());
            // IMPORTANT: clear the StringBuilder
            tempWord.setLength(0);
        }
    }

    private void getInsertions(String word) {
        StringBuilder tempWord = new StringBuilder();
        // iterate through the length of the word
        for (int i = 0; i < word.length() + 1; i++) {
            // iterate through the alphabet to add chars
            for (int j = 0; j < 26; j++) {
                char letter = (char)('a' + j);
                tempWord.append(word);
                tempWord.insert(i, letter);
                this.allCandidates.add(tempWord.toString());
                // clear the StringBuilder
                tempWord.setLength(0);
            }
        }
    }

    private void getTranspositions(String word) {
        // IMPORTANT: handle edge case where word is only 1 chars
        if (word.length() <= 1) return;
        StringBuilder tempWord = new StringBuilder();
        for (int i = 0; i < word.length() - 1; i++) {
            tempWord.append(word);
            char tempLetter = tempWord.charAt(i);
            tempWord.setCharAt(i, tempWord.charAt(i + 1));
            tempWord.setCharAt(i + 1, tempLetter);
            this.allCandidates.add(tempWord.toString());
            // clear the StringBuilder
            tempWord.setLength(0);
        }
    }

    private void getAlterations(String word) {
        StringBuilder tempWord = new StringBuilder();
        // iterate through the length of the word
        for (int i = 0; i < word.length(); i++) {
            // iterate through the alphabet to swap chars
            for (int j = 0; j < 26; j++) {
                char letter = (char)('a' + j);
                tempWord.append(word);
                tempWord.setCharAt(i, letter);
                this.allCandidates.add((tempWord.toString()));
                // clear the StringBuilder
                tempWord.setLength(0);
            }
        }
    }

    private void makeGoodCandidates() {
        Node tempNode;
        for (String str : this.allCandidates) {
            tempNode = this.dictionary.find(str);
            if (tempNode != null) this.goodCandidates.add(str);
        }
    }

    private String getSuggestion() {
        Node tempNode;
        int maxVal = 0;
        String suggestion = null;
        for (String str : this.goodCandidates) {
            tempNode = this.dictionary.find(str);
            if (tempNode.getValue() > maxVal) {
                maxVal = tempNode.getValue();
                suggestion = str;
            }
        }
        return suggestion;
    }
}
