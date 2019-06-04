package ec.app;

//import com.sun.java.util.jar.pack.Instruction;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.Buffer;
import java.util.ArrayList;

public class result_analysis {
    private static FileReader fl_reader;
    private static String path;
    private static BufferedReader bf_reader;
    private static String TS = "TS";
    private static String V = "V";
    private static String UC = "UC";
    private static String ET = "ET";
    private static String WT = "WT";
    private static String RFT = "RFT";



    public result_analysis(){//class constructor
    }
    public static void main(String[] args){
        path = "C:\\Users\\vince\\Desktop\\123\\bestTrees_for_further_analysis";
        reading_analysis();

    }
    public static void reading_analysis(){
          int TS_Counter = 0;
          int V_Counter = 0;
          int UC_Counter = 0;
          int ET_Counter = 0;
          int WT_Counter= 0;
          int RFT_Counter = 0;
        try{
            fl_reader = new FileReader(path);
            bf_reader = new BufferedReader(fl_reader);
            ArrayList<String> current_lines = new ArrayList<>();
            String currentLine = "";
            String oneLine = "";
            while((currentLine = bf_reader.readLine())!=null){
                while(!currentLine.contains("#")){
                    System.out.println("1");
                    oneLine = oneLine + currentLine;
                    currentLine = bf_reader.readLine();
                    System.out.println(oneLine);
                }
            if(currentLine.contains("#")) oneLine = oneLine + currentLine;
            current_lines.add(oneLine);
            oneLine ="";//reset
            }
//            ArrayList<String> counter = new ArrayList<>();
            for(int i = 0; i < current_lines.size(); i++){
                String s = current_lines.get(i);
                String[] splited = s.split("\\s+");
                for(String c : splited){
                    System.out.println(c);
                    if(c.equalsIgnoreCase("TS")) TS_Counter++;
                    if (c.equalsIgnoreCase("V")) V_Counter++;
                    if (c.equalsIgnoreCase("UC")) UC_Counter++;
                    if(c.equalsIgnoreCase("ET")) ET_Counter++;
                    if(c.equalsIgnoreCase("WT"))WT_Counter++;
                    if(c.equalsIgnoreCase("RFT"))RFT_Counter++;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("TS freq: " + TS_Counter);
        System.out.println("V freq: " + V_Counter);
        System.out.println("UC freq: " + UC_Counter);
        System.out.println("ET freq: " + ET_Counter);
        System.out.println("WT freq: " + WT_Counter);
        System.out.println("RFT freq: " + RFT_Counter);
    }


}
