import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;

public class AdaptaiveHuffman {
    public void compress(){
        FileInputStream fIn = null;
        FileWriter fWr = null;
        File file = new File("input.txt");
        byte[] str =  new byte[(int)file.length()];
        try {
            fIn = new FileInputStream(file);
            fIn.read(str);
            fIn.close();
        }catch (IOException e){
            e.printStackTrace();
        }
        Tree root = new Tree(null,0,null);
        String bits = "",temp;
        for(byte data : str){
            Character c = (char) data;
            Tree node = root.isContain(c);
            if(node == null){
                temp="";
                Tree nyt = root.isContain(null);
                bits+=root.getCode(nyt,"");
                for(int i = 7 ; i >=0 ; i--){
                    if(((data >> i)&1)==1)
                        temp+='1';
                    else
                        temp +='0';
                }
                bits+=temp;
                nyt.createNode(c);
            }else{
                bits+=root.getCode(node,"");
                node.reOrder();
            }
        }
        try {
            fWr = new FileWriter("compressedbits.txt");
            fWr.write(bits);
            fWr.close();
        }catch (IOException e){
            e.printStackTrace();
        }
    }
}
