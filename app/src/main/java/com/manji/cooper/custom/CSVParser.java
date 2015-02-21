package com.manji.cooper.custom;

import com.manji.cooper.R;
import com.manji.cooper.utils.Utility;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

/**
 * Created by douglaspereira on 2015-02-07.
 */
public class CSVParser {

    public CSVParser(){

    }

    public CSVData parseCSV(String csv){
        String[] content = csv.split("\\n");
        HashMap<String, List<String>> csvObjectData = new HashMap();

        int index = 0, flag = 0;

        // Look for the beginning of the relevant csv data
        for (int i=0; i < content.length; i++){
            if (content[i].split(",").length > 0)
                flag += 1;

            if (flag == 2){
                index = i+1;
                break;
            }
        }

        List<String> attributesNames = Arrays.asList(content[index].split(","));
        List<String> attributesUnits = Arrays.asList(content[index + 1].split(","));

        String[] data = Arrays.copyOfRange(content, index + 2, content.length);

        for (String line : data){
            String key = parseKey(line);
            ArrayList<String> entries = parseEntries(line, key);

            if (key != null)
                csvObjectData.put(key.toLowerCase(), entries);
        }

        CSVData obj = new CSVData(Utility.activity, csvObjectData);

        obj.setAttributeNames(attributesNames);
        obj.setAttributeUnits(attributesUnits);

        csvObjectData.put(Utility.activity.getResources().getString(R.string.csv_attributes_names), attributesNames);
        csvObjectData.put(Utility.activity.getResources().getString(R.string.csv_attributes_units), attributesUnits);

        return obj;

    }

    private String parseKey(String entry){
        if (entry == null || entry.length() < 2) return null;

        String key = null;

        String firstChar = entry.charAt(0) + "";

        if (firstChar.equals("\"") && entry.length() > 1){
            key = entry.substring(1, entry.substring(1).indexOf(firstChar) + 1);
        }else{
            if (entry.split(",") != null && entry.split(",").length > 0)
                key = entry.split(",")[0];
        }

        return key;
    }

    private ArrayList<String> parseEntries(String entry, String key){
        if (key == null) return null;
        String attributes = entry.substring(key.length()+1);

        return new ArrayList<String>(Arrays.asList(attributes.split(",")));
    }
//
//    @Override
//    public void onResourceRetrieved(String content) {
//        Log.d("Success", content);
//
//        CSVData obj = parseCSVObject(content);
//        if (obj != null)
//            parsedListener.onResourceParsed(obj);
//        else
//            parsedListener.onError("Error parsing CSV file");
//    }
//
//    @Override
//    public void onError(String error) {
//        Log.e("Error", error);
//    }
}
