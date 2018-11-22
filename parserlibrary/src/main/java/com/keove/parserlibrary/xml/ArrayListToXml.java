package com.keove.parserlibrary.xml;

import java.util.ArrayList;


public class ArrayListToXml
{
    public String Deserialize(ArrayList<Object> list, String roottag)
    {
        ObjectToElement deserializer = new ObjectToElement();
        try
        {
            String opening = "<"+roottag+">";
            for (Object object : list) {
                opening+= deserializer.GetElement(object);
            }
            return opening+"</"+roottag+">";
        }
        catch (Exception e)
        {
            return null;
        }
    }
}
