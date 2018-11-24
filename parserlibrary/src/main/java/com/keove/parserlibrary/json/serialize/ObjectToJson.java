package com.keove.parserlibrary.json.serialize;

import com.keove.videoapp.annotations.JPM;
import com.keove.videoapp.data.parsers.ParseUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Field;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by cagriozdes on 08/03/16.
 */


public class ObjectToJson {

    public JSONObject toJsonObject(Object object) {
        try {
            JSONObject j = new JSONObject();
            List<Field> fields = ParseUtils.getFieldsUpTo(object.getClass(), Object.class);

            for (Field field : fields) {
                handleField(j, field, object);
            }

            return j;
        } catch (Exception ex) {
            return null;
        }
    }

    public String toJsonString(Object object) {
        JSONObject j = toJsonObject(object);
        if (j != null) return j.toString();
        else return "";
    }

    private void handleField(JSONObject j, Field field, Object object) {
        try {
            if (field.isAnnotationPresent(JPM.class)) handleAnnotated(j, field, object);
            else handleFieldReflected(j, field, object);
        } catch (Exception ex) {}
    }

    private void handleAnnotated(JSONObject j, Field field, Object object) throws IllegalAccessException, JSONException {
        JPM map = (JPM) field.getAnnotation(JPM.class);
        String name = field.getName();

        if (map.Type().contentEquals(JPM.B)) {
            j.put(name, field.getBoolean(object));
        }
        else if (map.Type().contentEquals(JPM.S)) {
            String value = field.get(object).toString();
            j.put(name, value);
        }
        else if (map.Type().contentEquals(JPM.D)) {
            j.put(name, field.getDouble(object));
        }
        else if (map.Type().contentEquals(JPM.F)) {
            j.put(name, field.getFloat(object));
        }
        else if (map.Type().contentEquals(JPM.I)) {
            j.put(name, field.getInt(object));
        }
        else if (map.Type().contentEquals(JPM.Date)) {
            try {
                Date date = (Date) field.get(object);
                SimpleDateFormat format = new SimpleDateFormat(map.Format());
                String dateString = format.format(date);
                j.put(name, dateString);
            } catch (Exception ex) {
                j.put(name, field.get(object).toString());
            }
        }

        else if (map.Type().contentEquals(JPM.Xobj)) {
            JSONObject jobject = new ObjectToJson().toJsonObject(field.get(object));
            j.put(name, jobject);
        }
        else if (map.Type().contentEquals(JPM.Xarr)) {
            //Class k = map.Klass();
            ArrayList<?> list = (ArrayList<?>) field.get(object);
            if (list != null && list.size() > 0) {
                JSONArray jarray = new JSONArray();
                for (int i = 0; i < list.size(); i++) {
                    Object lo = list.get(i);
                    JSONObject li = new ObjectToJson().toJsonObject(lo);
                    //jarray.put(toJsonObject(list.get(i)));
                    jarray.put(li);
                }

                j.put(name, (Object) jarray);
            }
        }

        else if (map.Type().contentEquals(JPM.Jar)) {
            j.put(name, field.get(object));
        }
        else if (map.Type().contentEquals(JPM.Jobj)) {
            j.put(name, field.get(object));
        }

        else if(map.Type().contentEquals(JPM.Enum)) {
            Enum enu = (Enum) field.get(object);
            String ename = enu.name();
            j.put(name,ename);
        }
    }

    private void handleFieldReflected(JSONObject j, Field f, Object object) {

    }

}
