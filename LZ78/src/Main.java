import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

public class Main {
    static void compress(String s){
        String sub="";
        ArrayList<String> dic=new ArrayList<>() ;
        char nextChar=' ';
        int index=0;
        boolean found=false;
        try {
            FileWriter compress_write_file = new FileWriter("compressed.txt");
            FileWriter dictionary = new FileWriter("dictionary.txt");
            for(int i = 0 ; i < s.length() ; i++){
                for(int j = s.length() ; j > i;j--){
                    sub=s.substring(i,j);
                    if(dic.indexOf(sub)>-1 ){
                        if(i!=s.length()-1) {
                            //System.out.println(dic.indexOf(sub) + 1 + " " + nextChar);
                            compress_write_file.write(dic.indexOf(sub) + 1 + " " + nextChar+"\n");

                            dic.add(sub + nextChar);
                            dictionary.write(++index+" "+sub + nextChar+"\n");
                            found = true;
                            i += sub.length();
                        }
                        else{
                            //System.out.println(dic.indexOf(sub) + 1 + " " + "null");
                            compress_write_file.write(dic.indexOf(sub) + 1 + " " + "null\n");
                            found = true;
                            i += sub.length();
                        }
                        break;
                    }
                    nextChar=sub.charAt(sub.length()-1);
                }
                if(!found && i!= s.length()-1){
                   // System.out.println(0+" "+s.charAt(i));
                    compress_write_file.write(0+" "+s.charAt(i)+"\n");

                    dic.add(sub);
                    dictionary.write(++index+" "+sub+"\n");

                }
                found=false;
            }
            dictionary.close();
            compress_write_file.close();

        }catch (IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
    }

    static void decompress(StringBuilder s,ArrayList<String> symbol, ArrayList<Integer> index){
        try {
            FileWriter decompress_file=new FileWriter("decompressed.txt"),dictionary=new FileWriter("dictionary2.txt");
            ArrayList<String> dic=new ArrayList<>();
            int ind=0;
            for(int i = 0 ; i < symbol.size();i++){
                if(index.get(i)-1<0){
                    if(symbol.get(i)!="null"){
                    s.append(symbol.get(i));
                    dic.add(symbol.get(i));
                    }
                }else{
                    if(!symbol.get(i).equals("null")){
                    dic.add(dic.get(index.get(i)-1)+symbol.get(i));
                    s.append(dic.get(index.get(i)-1)+symbol.get(i));
                    }else{
                        s.append(dic.get(index.get(i)-1));
                    }
                }
            }
            for(int i = 0 ; i < dic.size();i++){
                dictionary.write(i+1 + " "+ dic.get(i)+"\n");
            }
            decompress_file.write(String.valueOf(s));
            decompress_file.close();
            dictionary.close();
        }catch (IOException e){
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
    }
    public static void main(String[] args) {
        try {
            String s="",tag="",temp="";
            ArrayList<Integer> index = new ArrayList<>();
            ArrayList<String> symbol = new ArrayList<>();
            boolean foundSpace=false;
            File myObj = new File("compress.txt");
            File compressed_file=new File("compressed.txt");
            Scanner myReader = new Scanner(myObj);
            while (myReader.hasNextLine()) {
                s = myReader.nextLine();
            }
            myReader = new Scanner(compressed_file);
            while (myReader.hasNextLine()) {
               temp="";
                tag = myReader.nextLine();
                for(int i = 0 ; i < tag.length() ; i++){
                    if(tag.charAt(i)!=' ' && !foundSpace){
                        temp+=tag.charAt(i);
                    }
                    else if(tag.charAt(i)==' '){
                        int p=Integer.parseInt(temp);
                        index.add(p);
                        foundSpace=true;
                        temp="";
                    }else{
                        temp+=tag.charAt(i);
                    }
                }
                symbol.add(temp);
                foundSpace=false;
            }
            compress(s);
            StringBuilder x=new StringBuilder();
            decompress(x,symbol,index);
            myReader.close();
        } catch (FileNotFoundException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
    }
}

