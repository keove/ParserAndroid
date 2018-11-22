package com.keove.parserlibrary.json;

import android.util.Log;

import org.json.JSONArray;

import java.util.ArrayList;

public class JsonArrayToArrayList
{
    public ArrayList<Object> GetObjects(JSONArray jarray, Class objclass)
    {
        try {
            ArrayList<Object> objects = new ArrayList<Object>();
            for(int i = 0; i<jarray.length(); i++)
            {
                JsonToObject parser = new JsonToObject();
                Object object = parser.GetObject(jarray.getJSONObject(i), objclass);
                if(object != null)
                {
                    objects.add(object);
                }
            }
            return objects;
        } catch (Exception e) {
            Log.e("JsonArrayToArrayList","Line 26 e:"+e.toString());
            return null;
        }
    }
}
