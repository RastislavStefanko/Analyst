/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.fidentis.featurepoints;

import java.io.BufferedReader;
import static java.io.File.separatorChar;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Map;
import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;

/**
 *
 * @author Galvanizze
 */
public class FpTexter {

    private static String SEP = ";";
    private static String textName = "fp_text.csv";
    private static FpTexter instance  = new FpTexter();
    private static Map<FacialPointType, List<String>> fpTexts = new HashMap<>();

    private FpTexter() {}

    public static FpTexter getInstance() {
        if (fpTexts.isEmpty()) {
            instance.loadTexts();
        }
        return instance;
    }

    private void loadTexts() {

        BufferedReader br = null;
        String line = "";
        String filePath = "";

        try {
            filePath = new java.io.File(".").getCanonicalPath() + separatorChar + "models" + separatorChar + "resources" + separatorChar + textName;
            
            int counter = 0;
            
            br = new BufferedReader(new FileReader(filePath));
            while ((line = br.readLine()) != null) {

                counter++;

                // Preskocit hlavicku
                if (counter == 1) {
                    continue;
                }

                String[] lineParts = line.split(SEP);
                if (lineParts.length != 3 ) {
                    continue;
                }

                List<String> values = new ArrayList<>();
                values.add(lineParts[1]);
                values.add(lineParts[2]);
                
                fpTexts.put(FacialPointType.valueOf(lineParts[0]), values);
                
            }

        } catch (FileNotFoundException e) {
        } catch (IOException e) {
        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (IOException e) {
                }
            }
        }
    }
    
    public String getFPname(FacialPointType type) {
        return getFPvalue(type, 0);
    }
    
    public String getFPinfo(FacialPointType type) {
        return getFPvalue(type, 1);
    }
    
    private String getFPvalue(FacialPointType type, int index){
        if (fpTexts.isEmpty()) {
            return "";
        }
        
        List<String> values = fpTexts.get(type);
        if (values != null && !values.isEmpty()) {
            return values.get(index);
        } else {
            return "";
        }       
    }

}
