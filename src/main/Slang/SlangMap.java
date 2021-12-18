package main.Slang;

import java.io.*;
import java.util.*;
import java.util.regex.Pattern;

/**
 * com.GUI.Slang
 * Created by Hieu Tran Trung
 * Date 12/15/2021 - 6:14 PM
 * Description: ...
 */
public class SlangMap implements Serializable {
    private HashMap<String, Slang> slangMap = new HashMap<>();

    public SlangMap() {
        try {
            loadDataStructure(); // load data structure
        } catch (IOException e) {
            String file_in = "src/resources/data/slang.txt";
            readFile(file_in); // first run need to load from file
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    private void readFile(String file_in) {
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
        writeFile(false, null);
    }

    private void writeFile(boolean isAppend, Slang newSlang) {
        String file_out = "src/resources/data/slang.txt";
        try {
            BufferedWriter bw = new BufferedWriter(
                    new OutputStreamWriter(
                    new FileOutputStream(file_out, isAppend)));

            if (isAppend) {
                bw.write(newSlang.toString());
            }
            else {
                for (Slang value: slangMap.values()) {
                    bw.write(value.toString());
                }
            }

            bw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void loadDataStructure() throws IOException, ClassNotFoundException {
        String file_in = "src/resources/data/data.dat";
        ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file_in));
        slangMap = (HashMap<String, Slang>) ois.readObject();
        ois.close();
    }

    public void saveDataStructure() throws IOException {
        String file_out = "src/resources/data/data.dat";
        ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(file_out));
        oos.writeObject(slangMap);
        oos.close();
    }

    public Slang searchByKey(String word) {
        return slangMap.get(word.toUpperCase());
    }

    public ArrayList<Slang> searchByDefinition(String keyword) {
        ArrayList<Slang> res = new ArrayList<>();

        for (Slang slang : slangMap.values()) {
            ArrayList<String>  value = slang.getDefinitionList();
            for (String str: value)
                // https://stackoverflow.com/questions/86780/how-to-check-if-a-string-contains-another-string-in-a-case-insensitive-manner-in
                if (Pattern.compile(Pattern.quote(keyword), Pattern.CASE_INSENSITIVE).matcher(str).find()) {
                    res.add(slang);
                    break;
                }
        }

        return res;
    }

    public void add(Slang newSlang, boolean isOverride) {
        if (slangMap.get(newSlang.getWord()) == null) {
            slangMap.put(newSlang.getWord(), newSlang);
            writeFile(true, newSlang);
            return;
        }
        if (isOverride) {
            slangMap.replace(newSlang.getWord(), newSlang);
        }
        else {
            slangMap.get(newSlang.getWord()).addDefinition(newSlang.getDefinitionList());
        }
        writeFile();
    }

    public void delete(String word) {
        slangMap.remove(word);
        writeFile();
    }

    public void edit(String word, Slang newSlang) {
        if (word.equalsIgnoreCase(newSlang.getWord()))
            slangMap.replace(word, newSlang);
        else {
            slangMap.remove(word);
            slangMap.put(newSlang.getWord(), newSlang);
        }
        writeFile();
    }

    public void reset() {
        slangMap.clear();
        readFile("src/resources/data/slang_copy.txt");
        writeFile();
    }

    public ArrayList<Slang> randomSlang(int number) {
        if (number <= 0) return null;

        ArrayList<Slang> res = new ArrayList<>();

        for (int i = 0; i < number; i++) {
            Object[] keyArray = slangMap.keySet().toArray();
            Object randomKey = keyArray[new Random().nextInt(keyArray.length)];

            res.add(slangMap.get((String)randomKey));
        }

        return res;
    }

}
