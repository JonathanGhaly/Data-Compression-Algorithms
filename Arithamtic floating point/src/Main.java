import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class Main {
    static void compress() {
        String data = "";
        try {
            File myObj = new File("input.txt");
            Scanner myReader = new Scanner(myObj);
            while (myReader.hasNextLine()) {
                data = myReader.nextLine();
                System.out.println(data);
            }
            myReader.close();
        } catch (FileNotFoundException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
        HashMap<Character, Integer> map = new HashMap<>(data.length());
        for (int i = 0; i < data.length(); i++) {
            Integer occ = map.putIfAbsent(data.charAt(i), 0);
            occ = occ == null ? 0 : occ;
            map.put(data.charAt(i), occ + 1);
        }
        double low = 0;
        double high = 1;
        double range = 1;
        for (int i = 0; i < data.length(); i++) {
            high = low + range * highRange(data.charAt(i), map, data.length());
            low = low + range * lowRange(data.charAt(i), map, data.length());
            range = high - low;
        }
        System.out.println("Value = " + ((high + low) / 2));
        System.out.println("High = " + high);
        System.out.println("Low = " + low);
        String finalData = data;
        map.forEach(((character, integer) -> {
            System.out.println("\"" + character + "\" from " + lowRange(character, map, finalData.length()) + " to " +
                    highRange(character, map, finalData.length()));
        }));
    }

    public static void decompress() {
        String data = "";
        double val = 0.0;
        int length = 0;
        ArrayList<Character> characters = new ArrayList<>();
        ArrayList<Double> from = new ArrayList<>();
        ArrayList<Double> to = new ArrayList<>();

        try {
            File myObj = new File("input2.txt");
            Scanner myReader = new Scanner(myObj);
            val = myReader.nextDouble();
            length = myReader.nextInt();
            String temp = myReader.next();
            for (int i = 0; i < temp.length(); i++) {
                characters.add(temp.charAt(i));
            }
            temp = myReader.nextLine();
            temp = myReader.nextLine();
            String t = "";
            for (int i = 0; i < temp.length(); i++) {
                if (temp.charAt(i) == ' ') {
                    double s = Double.parseDouble(t);
                    from.add(s);
                    t = "";
                } else {
                    t += temp.charAt(i);
                }
            }
            temp = myReader.nextLine();
            double q = Double.parseDouble(t);
            from.add(q);
            t = "";
            for (int i = 0; i < temp.length(); i++) {
                if (temp.charAt(i) == ' ') {
                    double s = Double.parseDouble(t);
                    to.add(s);
                    t = "";
                } else {
                    t += temp.charAt(i);
                }
            }
            q = Double.parseDouble(t);
            to.add(q);
            myReader.close();
        } catch (FileNotFoundException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
        StringBuilder s = new StringBuilder();
        double lower, upper, range;
        for (int i = 0; i < length; i++) {
            int idx = - 1;
            for (int j = 0; j < characters.size(); j++) {
                if (val < to.get(j) && val > from.get(j)) {
                    idx = j;
                    break;
                }
            }
            s.append(characters.get(idx));
            lower = from.get(idx);
            upper = to.get(idx);
            range = upper - lower;
            val = (val - lower) / range;
        }
        System.out.println(s);
    }

    static double lowRange(char c, HashMap<Character, Integer> map, int length) {
        int sum = 0;
        for (Map.Entry<Character, Integer> s :
                map.entrySet()) {
            if (s.getKey() != c) {
                sum += s.getValue();
            } else
                break;
        }
        return (double) sum / length;
    }

    static double highRange(char c, HashMap<Character, Integer> map, int length) {
        return lowRange(c, map, length) + ((double) map.get(c) / length);
    }

    public static void main(String[] args) {
        compress();
        decompress();
    }
}
