package com.main;

import Slang.Slang;
import Slang.SlangMap;

import java.io.IOException;

/**
 * com.main
 * Created by Hieu Tran Trung
 * Date 12/15/2021 - 6:06 PM
 * Description: ...
 */
public class Main {
    public static void main(String[] args) {
        System.out.println("Hello world");

        SlangMap slangMap = new SlangMap();

        Slang slang = slangMap.searchByKey("5EVER");

        System.out.println(slang.getDefinitionList());

        System.out.println(slangMap.searchByDefinition("ool"));

        try {
            slangMap.saveDataStructure();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
