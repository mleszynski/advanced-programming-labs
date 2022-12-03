package hangman;

import java.io.File;
import java.util.Scanner;
import java.io.IOException;

public class EvilHangman {
    public static void main(String[] args) {
        EvilHangmanGame game = new EvilHangmanGame();
        File dictionary = new File(args[0]);
        int wordLength = Integer.parseInt(args[1]);
        boolean dictBroken = false;
        Scanner userInput = new Scanner(System.in);

        try {
            game.startGame(dictionary, wordLength);
            game.setNumGuesses(Integer.parseInt(args[2]));
        } catch (IOException | EmptyDictionaryException ex) {
            ex.printStackTrace();
            dictBroken = true;
        }

        if (!dictBroken) {
            while (game.getNumGuesses() > 0) {
                System.out.println("You have " + game.getNumGuesses() + " guesses left.");
                System.out.print("Used letters:");
                game.printLetters();
                System.out.println("Word: " + game.getCurGuessKey());
                System.out.print("Enter Guess: ");

                char newGuess = userInput.next().charAt(0);

                if (!Character.isLetter(newGuess)) {
                    System.out.print("Invalid input!\n");
                    continue;
                }

                try {
                    game.makeGuess(newGuess);
                } catch (GuessAlreadyMadeException e) {
                    System.out.print("Guess already made!\n");
                    continue;
                }

                // make win condition
                if (!game.getCurGuessKey().contains("-")) break;
            }
            userInput.close();
            // check final condition
            if(game.getCurGuessKey().contains("-")) {
                System.out.println("You lose!");
            } else {
                System.out.println("You win!");
            }
            System.out.println("The word was " + game.getValidWords().first());
        }
    }
}
