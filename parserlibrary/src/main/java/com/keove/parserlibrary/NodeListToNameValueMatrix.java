package com.keove.parserlibrary;

import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import java.util.ArrayList;

public class NodeListToNameValueMatrix
{

    public ArrayList<ArrayList<NameValue>> GetMatrix(NodeList nodes, String elementname, ArrayList<String> childfields, ArrayList<String> attrfields)
    {
        try
        {
            ArrayList<ArrayList<NameValue>> matrix = new ArrayList<ArrayList<NameValue>>();
            for(int i = 0; i<nodes.getLength(); i++)
            {
                Element element = (Element)nodes.item(i);
                if(element.getNodeName().contentEquals(elementname))
                {
                    ElementToNameValueArray parser = new ElementToNameValueArray();
                    ArrayList<NameValue> pairarray = parser.GetArrayList(element,attrfields,childfields);
                    if(pairarray!=null)
                    {
                        if(pairarray.size()>0)
                        {
                            matrix.add(pairarray);
                        }
                    }
                }
            }
            return matrix;
        }
        catch (Exception e)
        {
            return null;
        }

    }


}
