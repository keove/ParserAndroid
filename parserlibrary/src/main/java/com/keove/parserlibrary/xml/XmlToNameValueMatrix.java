package com.keove.parserlibrary.xml;

import com.keove.parserlibrary.NameValue;
import com.keove.parserlibrary.NodeListToNameValueMatrix;

import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import java.io.StringReader;
import java.util.ArrayList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

public class XmlToNameValueMatrix
{

    public ArrayList<ArrayList<NameValue>> GetMatrix(String xmlstring, String elementname, ArrayList<String> childfields, ArrayList<String> attrfields)
    {
        try
        {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document document = builder.parse(new InputSource(new StringReader(xmlstring)));
            NodeList nodeList = document.getElementsByTagName(elementname);
            NodeListToNameValueMatrix parser = new NodeListToNameValueMatrix();
            return parser.GetMatrix(nodeList, elementname, childfields, attrfields);
        }
        catch (Exception e)
        {
            return null;
        }


    }


}
