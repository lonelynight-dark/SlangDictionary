package main;

import main.Slang.Slang;
import main.Slang.SlangMap;

import java.io.IOException;

/**
 * com.GUI
 * Created by Hieu Tran Trung
 * Date 12/15/2021 - 6:06 PM
 * Description: ...
 */
public class Main {
    public static void main(String[] args) {
        System.out.println("Hello world");

        SlangMap slangMap = new SlangMap();

        Slang slang = slangMap.searchByKey("ABCXYZ");

        if (slang == null)
            System.out.println("not found");
        else
            System.out.println(slang.getDefinitionList());

        /*
        System.out.println(slangMap.searchByDefinition("ool"));
        slangMap.reset();
        */

        System.out.println(slangMap.randomSlang(4));

/*
        slangMap.add(new com.GUI.Slang("ABCXYZ", "No meaning"), true);
        slangMap.add(new com.GUI.Slang("ABCXYZ", "No meaning 2"), false);
        slangMap.add(new com.GUI.Slang("ABCXYZ", "No meaning 3"), false);
*/

        try {
            slangMap.saveDataStructure();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
