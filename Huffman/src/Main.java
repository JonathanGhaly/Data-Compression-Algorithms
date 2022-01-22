import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.*;


//class Huffman{
//    /**
//     * @param root The node to print its right and left nodes
//     * @param s
//     */
//    public static void print(Node root,String s){
//        //if you need to compress characters Only add this:  Character.isLetter(root.character)
//        if(root.r == null && root.l==null){
//            System.out.println(root.character + " | " + s);
//            return;
//        }
//        print(root.l, s + "0");
//        print(root.r,s+"1");
//    }
//}

public class Main {
    /**
     * Print Huffman tree
     * @param root The node to print its right and left nodes
     * @param s
     */
    public static void print(Node root, String s) {
        //if you need to compress characters Only add this:  Character.isLetter(root.character)
        if (root.r == null && root.l == null) {
            System.out.println(root.character + " | " + s);
            return;
        }
        print(root.l, s + "0");
        print(root.r, s + "1");
    }

    /**
     * assign Dictionary of the Huffman tree
     * @param dict
     * @param root
     * @param s
     */
    public static void assign(HashMap<Character, String> dict, Node root, String s) {
        //if you need to compress characters Only add this:  Character.isLetter(root.character)
        if (root.r == null && root.l == null) {
            //System.out.println(root.character + " | " + s);
            dict.put(root.character, s);
            return;
        }
        assign(dict, root.l, s + "0");
        assign(dict, root.r, s + "1");
    }

    public static void main(String[] args) {
        int choice = 1;
        Scanner in = new Scanner(System.in);

        while (true) {
            System.out.println("Jonathan Huffman compression and decompression ");
            System.out.println("------------------------------------------------");
            System.out.println("Please choose \n1- Compress\n2- Decompress\n3- Exit");

            choice = Integer.parseInt(in.nextLine());
            //Compression
            if (choice == 1) {
                String s = "";
                try {
                    File file = new File("input_compress.txt");
                    Scanner myReader = new Scanner(file);
                    String data = "";
                    while (myReader.hasNextLine()) {
                        data = myReader.nextLine();
                        System.out.println(data);
                    }
                    s = data;
                    myReader.close();
                } catch (FileNotFoundException e) {
                    System.out.println("An error occurred.");
                    e.printStackTrace();
                }

                int size;
                //s = in.nextLine();
                HashMap<Character, Integer> uniqueChar = new HashMap<Character, Integer>();
                HashMap<Character, String> dict = new HashMap<Character, String>();
                //convert string to unique map with frequencies
                for (int i = 0; i < s.length(); i++) {
                    if (uniqueChar.containsKey(s.charAt(i))) {
                        uniqueChar.replace(s.charAt(i), uniqueChar.get(s.charAt(i)) + 1);
                    } else {
                        uniqueChar.put(s.charAt(i), 1);
                    }
                }
                size = uniqueChar.size();
                PriorityQueue<Node> qNode = new PriorityQueue<Node>(size, new ImpComparator());
                for (Map.Entry m : uniqueChar.entrySet()) {
                    Node huffNode = new Node();
                    huffNode.character = (char) m.getKey();
                    huffNode.elem = (int) m.getValue();
                    huffNode.r = null;
                    huffNode.l = null;
                    qNode.add(huffNode);
                }
                Node root = null;

                while (qNode.size() > 1) {
                    Node n1 = qNode.peek();
                    qNode.poll();
                    Node n2 = qNode.peek();
                    qNode.poll();
                    Node t = new Node();
                    t.elem = n1.elem + n2.elem;
                    t.character = '-';
                    t.l = n1;
                    t.r = n2;
                    root = t;
                    qNode.add(t);
                }
                System.out.println(" Char | Huffman code ");
                System.out.println("--------------------");
                print(root, "");
                assign(dict, root, "");
                String compressed = "";
                for (int i = 0; i < s.length(); i++) {
                    compressed += dict.get(s.charAt(i));
                }
                System.out.println(compressed);
                try {
                    FileWriter myWriter = new FileWriter("datacompressed.txt");
                    myWriter.write(compressed);
                    myWriter.close();
                } catch (IOException e) {
                    System.out.println("An error occurred.");
                    e.printStackTrace();
                }
                try {
                    FileWriter myWriter = new FileWriter("dictionary.txt");
                    for (Map.Entry m : dict.entrySet()) {
                        myWriter.write(m.getKey() + " " + m.getValue() + ", ");
                    }
                    myWriter.close();
                } catch (IOException e) {
                    System.out.println("An error occurred.");
                    e.printStackTrace();
                }
                double compressedRatio = (double) (s.length()*8)/compressed.length();
                System.out.println("\ncompression ratio: "+ String.format("%.2f", compressedRatio)+"\n" );
                System.out.println("_______________________________________");
            }
            //Decompression
            else if (choice == 2) {
                char c;
                String s = "", temp = "", data = "";
                int biggestLen = 0;
                try {
                    File file = new File("dictionary.txt");
                    Scanner myReader = new Scanner(file);
                    while (myReader.hasNextLine()) {
                        data = myReader.nextLine();
                        System.out.println(data);
                    }
                    s = data;
                    myReader.close();
                } catch (FileNotFoundException e) {
                    System.out.println("An error occurred.");
                    e.printStackTrace();
                }
                HashMap<String, Character> dict = new HashMap<String, Character>();
                for (int i = 0; i < s.length(); i++) {
                    c = s.charAt(i);
                    int j = i + 2;
                    while (s.charAt(j) < '2' && s.charAt(j) >= '0') {
                        temp += s.charAt(j);
                        if (j == s.length() - 1) break;
                        if (j + 1 != s.length())
                            j++;
                    }
                    dict.put(temp, c);
                    if (temp.length() > biggestLen) {
                        biggestLen = temp.length();
                    }
                    //System.out.println((Character) c+" "+temp);
                    i = j + 1;
                    temp = "";
                }
                try {
                    File file = new File("datacompressed.txt");
                    Scanner myReader = new Scanner(file);
                    while (myReader.hasNextLine()) {
                        data = myReader.nextLine();
                        System.out.println(data);
                    }
                    s = data;
                    myReader.close();
                } catch (FileNotFoundException e) {
                    System.out.println("An error occurred.");
                    e.printStackTrace();
                }
                data = "";
                for (int i = 0; i < s.length(); i++) {
                    for (int j = Math.min(i + biggestLen, s.length()); j > i; j--) {
                        temp = s.substring(i, j);
                        if (dict.containsKey(temp)) {
                            data += dict.get(temp);
                            break;
                        }
                    }
                    i += temp.length() - 1;
                }
                System.out.println("\nText: " + data);
                try {
                    FileWriter myWriter = new FileWriter("output_decompression.txt");
                    myWriter.write(data);
                    myWriter.close();
                } catch (IOException e) {
                    System.out.println("An error occurred.");
                    e.printStackTrace();
                }
                System.out.println("_______________________________________");
            } else if (choice == 3) {
                return;
            } else {
                System.out.println("Wrong command");
            }
        }
    }
}
