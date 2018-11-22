package com.keove.parserlibrary.xml;


import android.util.Log;

import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.io.StringWriter;
import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.util.ArrayList;

import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

public class ElementToObject
{
    public static String ListNameIndicator = "List";

    private String nodeToString(Node node) {
        try {
            StringWriter sw = new StringWriter();
              try {
                Transformer t = TransformerFactory.newInstance().newTransformer();
                t.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
                t.transform(new DOMSource(node), new StreamResult(sw));
              } catch (TransformerException te) {
                System.out.println("nodeToString Transformer Exception");
              }
              String preresult = sw.toString();
              String name = node.getNodeName();
              preresult = preresult.replace("<"+name+">", "");
              preresult = preresult.replace("</"+name+">", "");
              return preresult;
        } catch (OutOfMemoryError e) {

        }
          return "";
        }



    int level = 0;

    public String GetNodeString(Element element)
    {
        Boolean addtags = false;
        if(level>0) addtags = true;
        String opening = "<"+element.getNodeName()+">";
        String closing = "</"+element.getNodeName()+">";
        level++;
        String value = "";
        try {
            value += element.getFirstChild().getNodeValue();
        } catch (Exception e) {
            // TODO: handle exception
        }

        NodeList nodeList = element.getChildNodes();
        for(int i = 0; i<nodeList.getLength(); i++)
        {
            try
            {
                Element e = (Element)nodeList.item(i);
                value += GetNodeString(e);
            }
            catch (Exception e)
            {

            }
        }
        if(addtags)
        {
            return (opening+value+closing).replace("null", "");
        }
        else {
            return value.replace("null", "");
        }
    }

    @SuppressWarnings({ "rawtypes", "unchecked" })
    public Object GetObject(Element element, Class objclass)
    {
        try
{
            Object o = objclass.newInstance();
            NodeList nodes = element.getChildNodes();

            for(int i = 0; i<nodes.getLength(); i++)
            {
                try
{
                    Element e = (Element)nodes.item(i);
                    String name = e.getNodeName();
                    Field field = o.getClass().getField(name);








                    // if field is of an arraylist type
                    if(field.getType().getName().contains("ArrayList"))
                    {
                        // checking if string arraylist or complext object arraylist
                        ParameterizedType ftype  = (ParameterizedType)field.getGenericType();
                        Class<?> fclass = (Class<?>)ftype.getActualTypeArguments()[0];

                        // its an String arraylist
                        if(fclass.getName().contains("String"))
                        {



                            NodeList nl = e.getChildNodes();
                            if(nl!=null)
                            {
                                if(nl.getLength() < 2)
                                {
                                    String value = e.getFirstChild().getNodeValue(); //field.set(o, value);
                                    if(value!=null) { ((ArrayList<String>)field.get(o)).add(value); } // set value
                                    else// stuff for cdata
                                    {
                                        String value2 = nodeToString((Node)e);
                                        //field.set(o, value2);
                                        ((ArrayList<String>)field.get(o)).add(value2);
                                    }
                                }
                                else
                                {
                                    try
                                    {
                                        String listname = "";
                                        String clsname = "";
                                        if(fclass.getName().contains("."))
                                        {
                                            clsname = fclass.getName().substring(fclass.getName().lastIndexOf(".")+1);
                                        }
                                        else
                                        {
                                            clsname = fclass.getName();
                                        }
                                        listname = clsname+ListNameIndicator;
                                        // this means the list is under a single parent dedicated to only this type of item, ex : <ItemList><Item /><Item/></ItemList>
                                        if(listname.contentEquals(e.getNodeName()))
                                        {
                                            NodeList stringnl = e.getElementsByTagName(clsname);

                                            for(int k = 0; k<stringnl.getLength(); k++)
                                            {
                                                try
                                                {
                                                    String value3 = nodeToString(stringnl.item(k));
                                                    ((ArrayList<String>)field.get(o)).add(value3);
                                                }
                                                catch (Exception e2)
                                                {

                                                }
                                            }

                                        }
                                        // this means the items are not hetorogenly contained under a parent, its parent also can contain other types
                                        else
                                        {
                                            String value3 = nodeToString((Node)e);
                                            ((ArrayList<String>)field.get(o)).add(value3);
                                        }
                                    }
                                    catch(Exception ex)
                                    {
                                        String value3 = nodeToString((Node)e);
                                        ((ArrayList<String>)field.get(o)).add(value3);
                                    }

                                }
                            }
                            else
                            { }//PropertyUtils.setProperty(o, name, e.getNodeValue());
                        }
                        // its an arraylist of complex object type
                        else
                        {
                            try
                            {
                                String listname = "";
                                String clsname = "";
                                if(fclass.getName().contains("."))
                                {
                                    clsname = fclass.getName().substring(fclass.getName().lastIndexOf(".")+1);
                                }
                                else
                                {
                                    clsname = fclass.getName();
                                }
                                listname = clsname+ListNameIndicator;

                                // this means the list is under a single parent dedicated to only this type of item, ex : <ItemList><Item /><Item/></ItemList>
                                if(listname.contentEquals(e.getNodeName()))
                                {
                                    NodeList nl = e.getElementsByTagName(clsname);
                                    ElementToObject eto2;
                                    for(int k = 0; k<nl.getLength(); k++)
                                    {
                                        try
                                        {
                                            Element innere = (Element)nl.item(k);
                                            eto2 = new ElementToObject();
                                            Object listmember = eto2.GetObject(innere, fclass);
                                            ((ArrayList<Object>)field.get(o)).add(listmember);
                                        }
                                        catch (Exception e2)
                                        {
                                            Log.e("ElementToObject", e2.toString());
                                        }
                                    }
                                }
                                // this means the items are not hetorogenly contained under a parent, its parent also can contain other types
                                else
                                {
                                    ElementToObject eto2 = new ElementToObject();
                                    Object object = eto2.GetObject(e, fclass);
                                    ((ArrayList<Object>)field.get(o)).add(object);
                                }
                            }
                            catch (Exception e2)
                            {
                                ElementToObject eto2 = new ElementToObject();
                                Object object = eto2.GetObject(e, fclass);
                                ((ArrayList<Object>)field.get(o)).add(object);
                            }
                            //field.set(o, object);
                        }
                    }



                    // if field is single object
                    else
                    {
                        // if field is a basic String class
                        if(field.getType().equals(String.class))
                        {
                            NodeList nl = e.getChildNodes();
                            if(nl!=null)
                            {
                                if(nl.getLength() < 2)
                                {
                                    String value = e.getFirstChild().getNodeValue(); //field.set(o, value);
                                    if(value!=null) { field.set(o, value); } // set value
                                    else// stuff for cdata
                                    {
                                        String value2 = nodeToString((Node)e);
                                        field.set(o, value2);
                                    }
                                }
                                else
                                {
                                    String value3 = nodeToString((Node)e);
                                    field.set(o, value3);
                                }
                            }
                            else
                            { }//PropertyUtils.setProperty(o, name, e.getNodeValue());
                        }
                        else if(field.getType().equals(double.class) || field.getType().equals(Double.class))
                        {
                            NodeList nl = e.getChildNodes();
                            if(nl!=null)
                            {
                                if(nl.getLength() < 2)
                                {
                                    String value = e.getFirstChild().getNodeValue(); //field.set(o, value);
                                    if(value!=null) { field.set(o, Double.valueOf(value)); } // set value
                                    else// stuff for cdata
                                    {
                                        String value2 = nodeToString((Node)e);
                                        field.set(o, Double.valueOf(value2));
                                    }
                                }
                                else
                                {
                                    String value3 = nodeToString((Node)e);
                                    field.set(o, Double.valueOf(value3));
                                }
                            }
                            else
                            { }
                        }
                        else if(field.getType().equals(int.class) || field.getType().equals(Integer.class))
                        {
                            NodeList nl = e.getChildNodes();
                            if(nl!=null)
                            {
                                if(nl.getLength() < 2)
                                {
                                    String value = e.getFirstChild().getNodeValue(); //field.set(o, value);
                                    if(value!=null) { field.set(o, Integer.valueOf(value)); } // set value
                                    else// stuff for cdata
                                    {
                                        String value2 = nodeToString((Node)e);
                                        field.set(o, Integer.valueOf(value2));
                                    }
                                }
                                else
                                {
                                    String value3 = nodeToString((Node)e);
                                    field.set(o, Integer.valueOf(value3));
                                }
                            }
                            else
                            { }
                        }
                        // if field is a complex object
                        else
                        {
                            ElementToObject eto2 = new ElementToObject();
                            Object object = eto2.GetObject(e, field.getType());
                            field.set(o, object);
                        }
                    }




}
catch (Exception e)
{
                    Log.w("ElementToObject", "line 190 : "+e.toString());
}
                catch (OutOfMemoryError e) { Log.w("ElementToObject","line 194"+e.toString());}
            }


            NamedNodeMap nnm = element.getAttributes();
            for(int j = 0; j<nnm.getLength(); j++)
            {
                try
                {
                    Node n = nnm.item(j);
                    String name = n.getNodeName();
                    Field field = o.getClass().getField(name);
                    if(field.getType().equals(String.class))
                    {
                        String value = n.getNodeValue();
                        field.set(o, value);
                    }
                    else if(field.getType().equals(double.class) || field.getType().equals(Double.class))
                    {
                        String value = n.getNodeValue();
                        field.set(o, Double.valueOf(value));
                    }
                    else
                    {
                        ElementToObject eto2 = new ElementToObject();
                        Object object = eto2.GetObject((Element)n, field.getType());
                        field.set(o, object);
                    }
                }
                catch (Exception e) {}
            }


            return o;
}
catch (Exception e)
{
            Log.e("Element To Object","line 38 : "+e.toString());
            return null;
}
    }
}
