import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

public class main {
    static void compression(String s, int searchBufferSize){
        int currentPosition=0,index,position,length;
        char nextChar = ' ';
        try {
            FileWriter compress_write_file = new FileWriter("compressed.txt");
        while(true){
            String sub=s.substring(Math.max(0,currentPosition-searchBufferSize),currentPosition);
            for(int j=searchBufferSize ; j>=0 ;--j){
                String sub2=s.substring(currentPosition,Math.min(currentPosition+j, s.length()));
                index=sub.lastIndexOf(sub2);
                if(index!=-1){
                    position=sub.length() - index;
                    length=sub2.length();
                    compress_write_file.write("<"+position+","+length+","+ nextChar+">"+"\n");
                    //System.out.println(nextChar+" "+ position+" "+length);
                    currentPosition+= j;

                    break;
                }
                nextChar=sub2.charAt(sub2.length()-1);
            }

            currentPosition++;
            if (currentPosition >= s.length()) break;
        }
            compress_write_file.close();

        } catch (IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
    }
    public static void main(String[] args) {
        try {
            String s="";
            File myObj = new File("compress.txt");
            Scanner myReader = new Scanner(myObj);
            while (myReader.hasNextLine()) {
                s = myReader.nextLine();
            }
            compression(s,5);
            myReader.close();
        } catch (FileNotFoundException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
    }
}