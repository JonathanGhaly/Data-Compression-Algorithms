import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

public class Main {
    static void compress(String s) {
        ArrayList<String> dictionary = new ArrayList<>();
        String temp = "";
        for (int i = 0; i < 128; i++) {
            temp += (char) i;
            dictionary.add(temp);
            temp = "";
        }
        temp = "";
        String sub1 = "" + s.charAt(0);
        char nextChar = ' ';
        int index = 0;
        boolean found = false;
        try {
            FileWriter compress_write_file = new FileWriter("compressed.txt");
            StringBuilder q = new StringBuilder(sub1);
            for (int i = 0; i < s.length(); i++) {
                for (int j = s.length(); j > i; j--) {
                    sub1 = s.substring(i, j);
                    int ind = dictionary.lastIndexOf(sub1);
                    if (ind != - 1) {
                        compress_write_file.write(dictionary.lastIndexOf(sub1) + "\n");
                        dictionary.add(sub1 + nextChar);
                        i += sub1.length() - 1;
                        break;
                    }
                    nextChar = sub1.charAt(sub1.length() - 1);
                }
            }
            compress_write_file.close();

        } catch (
                IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {

        try {
            String s = "", tag = "",temp="";
            File myObj = new File("compress.txt");
            Scanner myReader = new Scanner(myObj);
            while (myReader.hasNextLine()) {
                s = myReader.nextLine();
            }
            compress(s);
            StringBuilder x = new StringBuilder();
            myReader.close();
        } catch (FileNotFoundException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
    }
}

