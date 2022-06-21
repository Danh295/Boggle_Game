package Boggle;

import java.io.File;
import java.io.IOException;
import java.util.Scanner;

import static Boggle.BoggleGame.dictionary;

public class Main {

    public static void main(String[] args) throws IOException {
        File dict = new File("dictionary.txt");
        Scanner dictReader = new Scanner(dict);
        String currWord;
        while (dictReader.hasNext()) {
            currWord = dictReader.next();
            if (currWord.length() > 2) dictionary.add(currWord);

        }

        BoggleGUI myFrame = new BoggleGUI();
    }
}
