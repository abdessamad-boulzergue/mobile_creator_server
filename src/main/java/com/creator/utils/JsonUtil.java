package com.creator.utils;

import org.json.JSONObject;

import java.util.Date;

public class JsonUtil {
  
  public static String getFieldValue(JSONObject userJSONData, String fieldName) {
    String fielValue = "";
   if (userJSONData!=null && fieldName!=null && userJSONData.has("REPONSE")){
     JSONObject jsonObj = userJSONData.getJSONObject("REPONSE");
     if (jsonObj!=null && jsonObj.has("LIGNE")){
       Object obj = jsonObj.get("LIGNE");
       if (obj instanceof JSONObject) {
         jsonObj = jsonObj.getJSONObject("LIGNE");
       }
       if (jsonObj!=null && jsonObj.has("UTIL")){
         obj = jsonObj.get("UTIL");
         if (obj instanceof JSONObject) {
           jsonObj = jsonObj.getJSONObject("UTIL");
         }
         if (jsonObj!=null && jsonObj.has(fieldName)){
           fielValue  = jsonObj.get(fieldName).toString();
         }
       }
     }
   }
   return fielValue;
 }

  
  
  public static Date getDateFromString(String stringDate) {
    Date created = null;
    if(stringDate!=null && !stringDate.isEmpty()){
      try {
        created = new Date(Long.valueOf(stringDate));
      } catch (Exception e) {
        created = null;
      }
    }
    return created;
  }



}
