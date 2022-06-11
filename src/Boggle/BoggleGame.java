package Boggle;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

public class BoggleGame {

    public static ArrayList<String> dictionary = new ArrayList<>();

    public static void main(String[] args) throws IOException {

        /* Store dictionary file in ArrayList */
        File dict = new File("dictionary.txt");
        Scanner dictReader = new Scanner(dict);
        while (dictReader.hasNextLine()) {
            dictionary.add(dictReader.nextLine());
        }


    }

    // todo: test this method
    public boolean verifyWord_Dict(String target, int lower, int upper) {
        if (lower <= upper) { // if there are more elements to check, that are still in range
            int mid = (upper - lower) / 2; // update middle index
            boolean found = false;
            int currChar = 0; // update character index

            // check if current element = target
            if (dictionary.get(mid+lower).equals(target)) {
                return true;
            }

            // check if character index needs to be updated
            while (dictionary.get(mid).charAt(currChar) == target.charAt(currChar)) {
                if (currChar == dictionary.size() - 1 || currChar == target.length() - 1) { // if out of range
                    if (dictionary.get(mid).length() > target.length()) { // if word contains target
                        found = true;
                        break;
                    }
                }
                currChar += 1;
            }

            if (dictionary.get(mid).charAt(currChar) > target.charAt(currChar) || found) { // if word's character's ascii value is greater than the characters, or if word contains the target
                return verifyWord_Dict(target, lower, mid - 1); // recursive call w/ updated upper bound
            }
            return verifyWord_Dict(target, mid + 1, upper); // recursive call w/ updated lower bound
        }
        return false;
    }

    // todo:
    public boolean verifyWord_Board(char[] dictionary, String target) {

    }
}
