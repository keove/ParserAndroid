package com.keove.parserlibrary;

import android.util.Log;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.xml.sax.InputSource;

import java.io.StringReader;
import java.io.StringWriter;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

public class XmlContentRetriever
{

    public static String GetStringByTag(String tag, String source)
    {
        try
        {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document document = builder.parse(new InputSource(new StringReader(source)));
            Node node = document.getElementsByTagName(tag).item(0);
            return nodeToString(node);

        } catch (Exception e) {
            Log.e("xml content retriever : ",e.toString());
            return "";
        }




    }


    private static String nodeToString(Node node) {
          StringWriter sw = new StringWriter();
          try {
            Transformer t = TransformerFactory.newInstance().newTransformer();
            t.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
            t.transform(new DOMSource(node), new StreamResult(sw));
          } catch (TransformerException te) {
            System.out.println("nodeToString Transformer Exception");
          }

          return sw.toString();
        }


}
