package com.keove.parserlibrary.json;


import android.content.Context;
import com.keove.parserlibrary.json.serialize.ObjectToJson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;


public class JParse {

    public static Context context;

    public static ArrayList<Object> GO(String raw, Class objclass) {
        try {
            JSONArray jarray = new JSONArray(raw);
            JsonArrayToArrayList parser = new JsonArrayToArrayList();
            return parser.GetObjects(jarray, objclass);
        } catch (Exception e) {
            return null;
        }
    }

    public static ArrayList<Object> GO(JSONArray jarray, Class objclass) {
        JsonArrayToArrayList parser = new JsonArrayToArrayList();
        return parser.GetObjects(jarray, objclass);
    }

    public static Object GO(JSONObject jobject, Class objclass) {
        JsonToObject jto = new JsonToObject();
        return jto.GetObject(jobject, objclass);
    }

    public static Object GO(String raw, Class objclass, boolean single) {
        try {
            JSONObject jobject = new JSONObject(raw);
            return GO(jobject, objclass);
        } catch (Exception e) {
            return null;
        }
    }

    public static JSONObject SERIALIZE_JSON(Object object) {
        ObjectToJson otj = new ObjectToJson();
        return otj.toJsonObject(object);
    }

    public static JSONArray jsonArray(ArrayList<?> array) {

        JSONArray jarray = new JSONArray();
        try {
            for (int i = 0; i < array.size(); i++) {
                try {
                    jarray.put(new ObjectToJson().toJsonObject(array.get(i)));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return jarray;
    }

    public static String jsonArrayString(ArrayList<?> array) {
        JSONArray jarray = jsonArray(array);
        return jarray.toString();
    }

    public static String SERIALIZE(Object object) {

        ObjectToJson otj = new ObjectToJson();
        return otj.toJsonString(object);
    }


    public static Map<String, Object> jsonToMap(JSONObject json) throws JSONException {
        Map<String, Object> retMap = new HashMap<String, Object>();

        if (json != JSONObject.NULL) {
            retMap = toMap(json);
        }
        return retMap;
    }

    public static Map<String, Object> toMap(JSONObject object) throws JSONException {
        Map<String, Object> map = new HashMap<String, Object>();

        Iterator<String> keysItr = object.keys();
        while (keysItr.hasNext()) {
            String key = keysItr.next();
            Object value = object.get(key);

            if (value instanceof JSONArray) {
                value = toList((JSONArray) value);
            }

            else if (value instanceof JSONObject) {
                value = toMap((JSONObject) value);
            }
            map.put(key, value);
        }
        return map;
    }

    public static List<Object> toList(JSONArray array) throws JSONException {
        List<Object> list = new ArrayList<Object>();
        for (int i = 0; i < array.length(); i++) {
            Object value = array.get(i);
            if (value instanceof JSONArray) {
                value = toList((JSONArray) value);
            }

            else if (value instanceof JSONObject) {
                value = toMap((JSONObject) value);
            }
            list.add(value);
        }
        return list;
    }


}
