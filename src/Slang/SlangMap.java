package Slang;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Random;

/**
 * Slang
 * Created by Hieu Tran Trung
 * Date 12/15/2021 - 6:14 PM
 * Description: ...
 */
public class SlangMap implements Serializable {
    private HashMap<String, Slang> slangMap = new HashMap<>();

    SlangMap() {

    }

    private void readFile() {
        String file_in = "/data/slang.txt";
        try {
            BufferedReader bw = new BufferedReader(new InputStreamReader
                    (new FileInputStream(file_in)));

            String line;
            while ((line = bw.readLine()) != null) {
                Slang slang = Slang.fromString(line);
                if (slang != null) {
                    slangMap.put(slang.getWord(), slang);
                }
            }

            bw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void writeFile() {
        String file_in = "/data/slang.txt";
        try {
            BufferedWriter bw = new BufferedWriter(
                    new OutputStreamWriter(
                    new FileOutputStream(file_in)));


            for (Slang value: slangMap.values()) {

            }

            bw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void loadDataStructure() throws IOException, ClassNotFoundException {
        String file_in = "/data/data.dat";
        ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file_in));
        slangMap = (HashMap<String, Slang>) ois.readObject();
        ois.close();
    }

    private void saveDataStructure() throws IOException {
        String file_out = "/data/data.dat";
        ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(file_out));
        oos.writeObject(slangMap);
        oos.close();
    }

    public Slang searchByKey(String word) {
        return slangMap.get(word);
    }

    public ArrayList<Slang> searchByDefinition(String keyword) {
        ArrayList<Slang> res = new ArrayList<>();


        return res;
    }

    public void add(Slang newSlang, int mode) {
        slangMap.put(newSlang.getWord(), newSlang);
    }

    public void delete(String word) {
        slangMap.remove(word);
    }

    public void edit(String word, Slang newSlang) {
        slangMap.replace(word, newSlang);
    }

    public void reset() {

    }

    public ArrayList<Slang> randomSlang(int number) {
        if (number <= 0) return null;

        ArrayList<Slang> res = new ArrayList<>();

        for (int i = 0; i < number; i++) {
            Object[] ketArray = slangMap.keySet().toArray();
            Object randomKey = ketArray[new Random().nextInt(ketArray.length)];

            res.add(slangMap.get(randomKey));
        }

        return res;
    }

}
