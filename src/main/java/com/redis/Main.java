package com.redis;


import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Scanner;


import org.json.JSONObject;




public class Main {


    public static void main(String[] args)throws Exception {

        String jsonFile = "D:/Files/jsonFile.json";
        flatJson cf = new flatJson();

        Scanner sc = new Scanner(System.in);

        String content = new String(Files.readAllBytes(Paths.get(jsonFile)));

        JSONObject jsonObject = new JSONObject(content);
        System.out.println("This is json convert program");
        System.out.println("Enter the unique-Id filed:");
        String uniqueIdKey = sc.nextLine();
        String value = cf.findUniqueId(jsonObject,uniqueIdKey);

      cf.createFlatJsonIndividualFiles("",jsonObject,uniqueIdKey,value);

//        for (String key : jsonObject.keySet()) {
//
//            Object obj  = jsonObject.get(key);
//
//           JSONObject flatJson = cf.convertJsonToFlatJson(key,jsonObject.get(key));
//           if (flatJson.has(key+"_"+uniqueIdKey))
//               System.out.println(key);
//           else
//               flatJson.put(uniqueIdKey,value);
//
//
//            String flatJsonFile = "D:/Files/Work123/flatJson" + key + ".json";
//
//
//            try (FileWriter file = new FileWriter(flatJsonFile)) {
//                file.write(flatJson.toString());
//                System.out.println("Flat JSON data written to output.json");
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//
//
//        }
    }
}