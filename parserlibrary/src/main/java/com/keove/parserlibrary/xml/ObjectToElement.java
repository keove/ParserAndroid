package com.keove.parserlibrary.xml;


import com.keove.parserlibrary.JPM.JPM;
import com.keove.parserlibrary.ParseUtils;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;


public class ObjectToElement {

    public String GetElement(Object object) {
        try {

            List<Field> props = ParseUtils.getFieldsUpTo(object.getClass(),Object.class);
                    //object.getClass().getDeclaredFields();
            String name = object.getClass().getName().substring(object.getClass().getName().lastIndexOf(".") + 1);
            String opening = "<" + name + " ";
            String closing = " >";
            String closing2 = "</" + name + ">";
            String attrs = "";

            ArrayList<Field> objects = new ArrayList<>();
            ArrayList<Field> lists = new ArrayList<>();

            for (Field field : props) {
                if (field.isAnnotationPresent(JPM.class)) {

                    JPM map = (JPM) field.getAnnotation(JPM.class);

                    if (map.Type().contentEquals(JPM.Xarr)) {
                        lists.add(field);
                    }

                    else if (map.Type().contentEquals(JPM.Xobj)) {
                        objects.add(field);
                    }
                    else {
                        String value = field.get(object).toString();
                        String attrname = field.getName();

                        attrs += (" " + attrname + "=\"");
                        attrs += (value + "\"");
                    }

                }
            }

            String currentXml = opening + attrs + closing;

            if (objects.size() > 0) {
                for (int i = 0; i < objects.size(); i++) {
                    try {
                        currentXml += GetElement(objects.get(i).get(object));
                    } catch (Exception ex) {

                    }

                }
            }

            if (lists.size() > 0) {
                for (int i = 0; i < lists.size(); i++) {
                    try {
                        Field listField = lists.get(i);
                        String listName = listField.getName();
                        String listXMLString = "<" + listName + ">";
                        ArrayList<?> list = (ArrayList<?>) listField.get(object);
                        if (list != null && list.size() > 0) {
                            for (int j = 0; j < list.size(); j++) {
                                Object listMember = list.get(j);
                                listXMLString += GetElement(listMember);
                            }
                        }
                        listXMLString += "</" + listName + ">";
                        currentXml += listXMLString;
                    } catch (Exception ex) {

                    }

                }
            }

            return currentXml + closing2;

            //return opening+attrs+closing;

        } catch (Exception e) {
            return "<error exception=\"" + e.toString() + "\"/>";
        }
    }





}
