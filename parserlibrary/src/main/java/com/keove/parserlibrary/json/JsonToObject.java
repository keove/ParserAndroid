package com.keove.parserlibrary.json;

import android.graphics.Typeface;
import android.util.Log;

import com.keove.parserlibrary.JPM.JPM;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;

public class JsonToObject {

    //public static String ListNameIndicator = "List";

    public Object GetObject(JSONObject jobject, Class objclass) {
        Object o = null;
        try {
            o = objclass.newInstance();
            Iterator<String> iter = jobject.keys();
            while (iter.hasNext()) {
                String key = iter.next();
                try {
                    Field field = o.getClass().getField(key);
                    if (field.isAnnotationPresent(JPM.class)) {
                        handleFieldAnnotated(o, field, key, jobject);
                    }
                    else {
                        handleField(o, key, jobject);
                    }
                } catch (Exception ex) {

                }


            }
        } catch (Exception e) {
            Log.i("JsonToObject", "line 33 e:" + e.toString());
        }
        return o;
    }


    private void handleField(Object o, String key, JSONObject jobject) {
        try {
            Field field = o.getClass().getField(key);

            /*if(field.isAnnotationPresent(JParseMap.class))
            {
                JParseMap map = (JParseMap)field.getAnnotation(JParseMap.class);
                if(map.FieldType().contentEquals(JParseMap.JSON_OBJECT))
                {
                    JSONObject fieldJsonObject = jobject.getJSONObject(key);
                    field.set(o,fieldJsonObject);
                }
            }

            else
            {*/
            if (field.getType().getName().contains("ArrayList")) {
                handleArraylist(jobject, key, field, o);
            }
            else {
                if (field.getType().equals(String.class)) // field is a basic String
                {
                    field.set(o, String.valueOf(jobject.get(key)));
                    field.set(o, jobject.getString(key));
                }
                else // field is a complex object
                {
                    Object object = new JsonToObject().GetObject(jobject.getJSONObject(key),
                            field.getType());
                    field.set(o, object);
                }
            }
            // }


        }
        catch (Exception e) {
            Log.i("JsonToObject", e.toString());
        }
    }

    private void handleFieldAnnotated(Object o, Field field, String key, JSONObject jobject) throws JSONException, IllegalAccessException {

        try {
            JPM map = (JPM) field.getAnnotation(JPM.class);

            if (map.Type().contentEquals(JPM.B)) {
                try {
                    field.setBoolean(o, jobject.getBoolean(key));
                }
                catch (Exception ex) {
                    String sVal = jobject.getString(key);
                    if (sVal.contentEquals("1") || sVal.contentEquals("T") || sVal.contentEquals(
                            "true")) {
                        field.setBoolean(o, true);
                    }
                    else field.setBoolean(o, false);
                }
            }
            else if (map.Type().contentEquals(JPM.S)) {
                field.set(o, jobject.getString(key));
            }
            else if (map.Type().contentEquals(JPM.D)) {
                field.set(o, jobject.getDouble(key));
            }
            else if (map.Type().contentEquals(JPM.F)) {
                Float f = Float.valueOf(jobject.getString(key));
                field.set(o, f);
            }
            else if (map.Type().contentEquals(JPM.I)) {
                field.set(o, jobject.getInt(key));
            }
            else if (map.Type().contentEquals(JPM.Date)) {
                try {
                    SimpleDateFormat format = new SimpleDateFormat(map.Format());
                    Date date = format.parse(jobject.getString(key));
                    field.set(o, date);
                }
                catch (Exception ex) {
                    field.set(o, null);
                }
            }

            else if (map.Type().contentEquals(JPM.Xobj)) {
                JSONObject jo = jobject.getJSONObject(key);
                Object ob = new JsonToObject().GetObject(jo, map.Klass());
                field.set(o, ob);
            }
            else if (map.Type().contentEquals(JPM.Xarr)) {
                JSONArray jarray = jobject.getJSONArray(key);
                if (jarray != null && jarray.length() > 0) {
                    for (int i = 0; i < jarray.length(); i++) {
                        Object ao = new JsonToObject().GetObject(jarray.getJSONObject(i),
                                map.Klass());
                        ((ArrayList<Object>) field.get(o)).add(ao);
                    }
                }

            }
            else if (map.Type().contentEquals(JPM.Sar)) {

                JSONArray jarray = jobject.getJSONArray(key);
                ArrayList<String> vals = new ArrayList<>();
                if (jarray != null && jarray.length() > 0) {
                    for (int i = 0; i < jarray.length(); i++) {
                        String s = jarray.getString(i);
                        vals.add(s);
                    }

                    field.set(o, vals);
                }
            }

            else if (map.Type().contentEquals(JPM.Jar)) {

            }
            else if (map.Type().contentEquals(JPM.Jobj)) {
                JSONObject jo = jobject.getJSONObject(key);
                field.set(o, jo);
            }
            else if (map.Type().contentEquals(JPM.Typeface)) {
                try {
                    String fontpath = jobject.getString(key);
                    Typeface face = Typeface.createFromAsset(JParse.context.getAssets(),
                            "fonts/" + fontpath);
                    field.set(o, face);
                }
                catch (Exception ex) {
                    ex.printStackTrace();
                }

            }

            else if (map.Type().contentEquals(JPM.Enum)) {

                try {
                    String ename = jobject.getString(key);
                    field.set(o, Enum.valueOf((Class<Enum>) field.getType(), ename));

                }
                catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        }
        catch (Exception ex) {
            ex.printStackTrace();
        }

    }


    private void handleArraylist(JSONObject jobject, String key, Field field, Object o) throws JSONException {

        try {
            ParameterizedType ftype = (ParameterizedType) field.getGenericType();
            Class<?> fclass = (Class<?>) ftype.getActualTypeArguments()[0];

            if (fclass.getName().contains("String")) {
                //jobject.getJSONArray(key);
            }
            else {
                JSONArray jarray = null;
                try {
                    jarray = jobject.getJSONArray(key);
                    jarray.length();
                }
                catch (Exception e) {
                    if (jarray == null || jarray.length() < 1) {
                        String jastring = jobject.getString(key);
                        jastring = jastring.replace("\"[", "[").replace("]\"", "]");
                        jarray = new JSONArray(jastring);
                    }
                }

                if (jarray != null && jarray.length() > 0) {
                    for (int i = 0; i < jarray.length(); i++) {
                        try {
                            JsonToObject jto = new JsonToObject();
                            Object object = jto.GetObject(jarray.getJSONObject(i), fclass);
                            ((ArrayList<Object>) field.get(o)).add(object);
                        }
                        catch (Exception e) {

                        }

                    }
                }

            }
        }
        catch (Exception ex) {

        }


    }


}
