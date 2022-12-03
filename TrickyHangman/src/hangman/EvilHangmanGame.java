package hangman;

import java.io.File;
import java.io.IOException;
import java.util.*;


public class EvilHangmanGame implements IEvilHangmanGame{
    private TreeSet<String> validWords;
    private SortedSet<Character> guessedLetters;
    private String curGuessKey;
    private int numGuesses;

    public EvilHangmanGame() {
        this.validWords = new TreeSet<>();
        this.guessedLetters = new TreeSet<Character>();
    }

    @Override
    public void startGame(File dictionary, int wordLength) throws IOException, EmptyDictionaryException {
        if (wordLength < 2) throw new EmptyDictionaryException();
        // IMPORTANT: clear the set every new game
        this.validWords.clear();
        this.guessedLetters.clear();
        setNumGuesses(0);

        // read through input data
        Scanner scanner = new Scanner(dictionary);
        while (scanner.hasNext()) {
            String word = scanner.next();
            if (word.length() == wordLength) {
                this.validWords.add(word.toLowerCase());
            }
        }
        scanner.close();
        if (this.validWords.isEmpty()) throw new EmptyDictionaryException();

        char[] chars = new char[wordLength];
        Arrays.fill(chars, '-');
        setCurGuessKey(new String(chars));
    }

    @Override
    public Set<String> makeGuess(char guess) throws GuessAlreadyMadeException {
        // IMPORTANT: wrapper class methods are nice
        guess = Character.toLowerCase(guess);
        if(this.guessedLetters.contains(guess)) throw new GuessAlreadyMadeException();
        else {
            Map<String, TreeSet<String>> wordMap = new HashMap<>();
            TreeSet<String> potentialKeys = new TreeSet<>();

            // group all words according to key values
            for(String curWord : this.validWords) {
                String curKey = makeKey(guess, curWord);
                // check to see if we need to add the key
                if (!wordMap.containsKey(curKey)) {
                    wordMap.put(curKey, new TreeSet<String>());
                }
                wordMap.get(curKey).add(curWord);
            }

            // check which key values are viable (biggest)
            int maxSize = 0;
            for (String key : wordMap.keySet()) {
                int tempSize = wordMap.get(key).size();
                if (tempSize > maxSize) maxSize = tempSize;
            }
            for (String key : wordMap.keySet()) {
                if (wordMap.get(key).size() == maxSize) potentialKeys.add(key);
            }

            // check which key has the letter appearing the least amount of times
            if (potentialKeys.size() > 1) {
                int lowestFreqCount = getCurGuessKey().length();
                for (String key : potentialKeys) {
                    if (lowestFreqCount > calcFreqCount(guess, key)) {
                        lowestFreqCount = calcFreqCount(guess, key);
                    }
                }
                TreeSet<String> delSet = new TreeSet<>();
                for (String key : potentialKeys) {
                    if (calcFreqCount(guess, key) != lowestFreqCount) {
                        delSet.add(key);
                    }
                }
                potentialKeys.removeAll(delSet);
            }

            // IMPORTANT: fancy iteration to break ties
            if (potentialKeys.size() > 1) {
                for (int i = getCurGuessKey().length() - 1; i >= 0; i--) {
                    boolean isFound = false;
                    for (String key : potentialKeys) {
                        if (key.charAt(i) == guess) isFound = true;
                    }
                    TreeSet<String> delSet = new TreeSet<>();
                    if (isFound) {
                        for (String key : potentialKeys) {
                            if (key.charAt(i) != guess) delSet.add(key);
                        }
                    }
                    potentialKeys.removeAll(delSet);
                    if (potentialKeys.size() == 1) break;
                }
            }

            String newKey = potentialKeys.first();
            int freqCount = calcFreqCount(guess, newKey);
            setValidWords(wordMap.get(newKey));
            setCurGuessKey(newKey);

            if (freqCount != 0) {
                if (freqCount == 1) {
                    System.out.println("Yes, there is 1 " + guess + "\n");
                } else {
                    System.out.println("Yes, there are " + freqCount + " " + guess + "\'s\n");
                }
            } else {
                System.out.println("Sorry, there are no " + guess + "\'s\n");
                this.numGuesses--;
            }
            this.guessedLetters.add(guess);
        }
        return this.validWords;
    }

    @Override
    public SortedSet<Character> getGuessedLetters() {
        return this.guessedLetters;
    }

    // count the number of times a letter appears in a key
    public int calcFreqCount(char guess, String key) {
        int freqCount = 0;
        for (int i = 0; i < key.length(); i++) {
            if (key.charAt(i) == guess) freqCount++;
        }
        return freqCount;
    }

    public String makeKey(char guess, String curWord) {
        StringBuilder pattern = new StringBuilder();
        for (int i = 0; i < curWord.length(); i++) {
            if (this.curGuessKey.charAt(i) == '-') {
                if (curWord.charAt(i) == guess) {
                    pattern.append(guess);
                } else {
                    pattern.append('-');
                }
            } else {
                pattern.append(this.curGuessKey.charAt(i));
            }
        }
        return pattern.toString();
    }

    public String getCurGuessKey() { return this.curGuessKey; }

    public void setCurGuessKey(String newGuess) { this.curGuessKey = newGuess; }

    public TreeSet<String> getValidWords() { return this.validWords; }

    public void setValidWords(TreeSet<String> newWords) {
//        this.validWords.clear();
//        this.validWords.addAll(newWords);
        this.validWords = newWords;
    }

    public int getNumGuesses() { return this.numGuesses; }

    public void setNumGuesses(int numGuesses) { this.numGuesses = numGuesses; }

    public void printLetters() {
        for (char letter : this.guessedLetters) {
            // print each individual letter
            System.out.print(" " + letter);
        }
        System.out.println();  // print out newline
    }
}
