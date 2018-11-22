package com.keove.parserlibrary;

import org.w3c.dom.Attr;
import org.w3c.dom.NodeList;

import java.util.ArrayList;

public class ElementToNameValueArray
{


    public ArrayList<NameValue> GetArrayList(org.w3c.dom.Element element, ArrayList<String> attrfields, ArrayList<String> childfields)
    {

        ArrayList<NameValue> array = new ArrayList<NameValue>();
        NodeList nodes = element.getChildNodes();
        for(int i = 0; i<nodes.getLength(); i++)
        {
            try
            {
                org.w3c.dom.Element e = (org.w3c.dom.Element)nodes.item(i);
                String name = e.getNodeName();
                if(Included(childfields, name))
                {
                    NameValue pair = new NameValue();
                    pair.name = name;
                    pair.value = e.getFirstChild().getNodeValue();
                    array.add(pair);
                }
            }
            catch (Exception e)
            {

            }
        }


        for (String string : attrfields)
        {
            try
            {
                Attr attr = element.getAttributeNode(string);
                NameValue pair = new NameValue();
                pair.name = string;
                pair.value = attr.getValue();
                array.add(pair);
            }
            catch (Exception e)
            {

            }
        }

        return array;

    }



    public Boolean Included(ArrayList<String> list, String key)
    {
        for (String string : list) {
            if(string.contentEquals(key)) return true;
        }
        return false;
    }




}
