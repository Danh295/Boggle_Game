package Boggle;

import java.io.File;
import java.io.IOException;
import java.util.Scanner;

import static Boggle.BoggleGame.dictionary;

public class Main {

    public static void main(String[] args) throws IOException {
        File dict = new File("dictionary.txt");
        Scanner dictReader = new Scanner(dict);
        while (dictReader.hasNext()) {
            dictionary.add(dictReader.next());

        }

        BoggleGUI myFrame = new BoggleGUI();
    }
}
