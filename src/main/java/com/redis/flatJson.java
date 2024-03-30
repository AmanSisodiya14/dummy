package com.redis;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.FileWriter;
import java.io.IOException;

public class flatJson {

    public JSONObject convertJsonToFlatJson(String key, Object jsonObject) {
        JSONObject flatJson = new JSONObject();

        flatJsonHelper(key, jsonObject, flatJson);
        return flatJson;
    }

    private void flatJsonHelper(String prefix, Object data, JSONObject flatJson) {
        if (data instanceof JSONObject) {
            JSONObject nestedJson = (JSONObject) data;

            for (String key : nestedJson.keySet()) {
                flatJsonHelper(prefix + "_" + key, nestedJson.get(key), flatJson);
            }

        } else if (data instanceof JSONArray) {
            JSONArray nestedJsonArray = (JSONArray) data;

            for (int i = 0; i < nestedJsonArray.length(); i++) {

                flatJsonHelper(prefix + "_", nestedJsonArray.get(i), flatJson);
            }
        } else {
            flatJson.put(prefix, data);
        }
    }

    public String findUniqueId(Object data, String filed) {
        String value = "";
        if (data instanceof JSONObject) {
            JSONObject nestedJson = (JSONObject) data;
            if (nestedJson.has(filed)) {
                return nestedJson.getString(filed);
            } else {
                for (String key : nestedJson.keySet()) {
                    String result= findUniqueId(nestedJson.get(key), filed);
                    if (!result.isEmpty()) {
                        return result;
                    }
                }
            }
        } else if (data instanceof JSONArray) {
            JSONArray nestedJsonArray = (JSONArray) data;
            for (int i = 0; i < nestedJsonArray.length(); i++) {
               String result= findUniqueId(nestedJsonArray.get(i), filed);
                if (!result.isEmpty()) {
                    return result;
                }
            }
        }
        return value;
    }
    private final static String flatJsonFilePath ="D:/Exatip Assignment/FlatJsonFiles/";


    public void createFlatJsonIndividualFiles(String prefix, JSONObject jsonObject,String uniqueIdKey,String uniqueIdValue){

        JSONObject flatjson = new JSONObject();

        for (String key : jsonObject.keySet()){
            Object value = jsonObject.get(key);

            if(value instanceof JSONObject){

                createFlatJsonIndividualFiles(prefix+ (prefix.isEmpty()?"":"_") + key, (JSONObject) value,uniqueIdKey,uniqueIdValue);

            } else if(value instanceof  JSONArray){
                jsonArrayToFlatJson(prefix+(prefix.isEmpty()?"":"_")+key,value,uniqueIdKey,uniqueIdValue);

            }
            else {
                flatjson.put(prefix+(prefix.isEmpty()?"":"_")+key,value);
            }
        }


        if(!flatjson.isEmpty()){
            if (!flatjson.has(prefix+"_"+uniqueIdKey))
                flatjson.put(uniqueIdKey,uniqueIdValue);

            writeValueToFile(flatjson,prefix);
        }

    }
    private void jsonArrayToFlatJson(String prefix,Object data,String uniqueIdKey,String uniqueIdValue){

        JSONObject flatjson = new JSONObject();

        JSONArray nestedJsonArray = (JSONArray) data;

        JSONObject object =nestedJsonArray.getJSONObject(0);

        for (String key2:object.keySet()){

            if (object.get(key2) instanceof  JSONArray){
                System.out.println("innner");
                jsonArrayToFlatJson(prefix+(prefix.isEmpty()?"":"_")+key2,object.get(key2), uniqueIdKey,uniqueIdValue);
            } else if (object.get(key2) instanceof JSONObject) {
                JSONObject nestedJsonObject = (JSONObject) object.get(key2);
                createFlatJsonIndividualFiles(prefix+(prefix.isEmpty()?"":"_")+key2,nestedJsonObject,uniqueIdKey,uniqueIdValue);

            } else{
                System.out.println(key2+"-"+object.get(key2));
                flatjson.put(prefix+(prefix.isEmpty()?"":"_")+key2, object.get(key2));
            }
        }
        System.out.println(flatjson);

        if(!flatjson.isEmpty()) {
            if (!flatjson.has(prefix+"_"+uniqueIdKey))
                flatjson.put(uniqueIdKey,uniqueIdValue);

            writeValueToFile(flatjson, prefix);
        }
    }

    private static void writeValueToFile(JSONObject flatJson, String filename) {

            try (FileWriter file = new FileWriter(flatJsonFilePath+filename+".json")) {
                file.write(flatJson.toString());
                System.out.println("Flat JSON data written to "+filename);
            } catch (IOException e) {
                e.printStackTrace();
            }
    }

}
