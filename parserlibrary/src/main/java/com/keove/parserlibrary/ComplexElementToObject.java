package com.keove.parserlibrary;

import android.util.Log;

import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import java.io.StringReader;
import java.lang.reflect.Field;
import java.util.ArrayList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import cz.msebera.android.httpclient.NameValuePair;
import cz.msebera.android.httpclient.message.BasicNameValuePair;

//import org.apache.http.NameValuePair;
//import org.apache.http.message.BasicNameValuePair;


public class ComplexElementToObject
{


	
	private ArrayList<ComplexElementProperty> complexlist;
	private ArrayList<AttributeProperty> attrlist;
	private ArrayList<TagProperty> taglist;
	private String TagName;
	private Class c;
	private Boolean isArrayOffC;
	
	public ComplexElementToObject me;
	
	
	public ComplexElementToObject(ComplexElementProperty cep)
	{
		this.complexlist = cep.complexlist;
		this.attrlist = cep.attrlist;
		this.taglist = cep.taglist;
		this.TagName = cep.TagName;
		this.c = cep.c;
		
		me = this;
	}
	
	

	public static ArrayList<BasicNameValuePair> replacelist = null;
	
	

	
	public Object Parse(String source)
	{
		try 
		{
			if(replacelist != null)
			{
				if(replacelist.size() > 0)
				{
					for (NameValuePair data : replacelist) {
						source = source.replace(data.getName(), data.getValue());
					}
				}
			}
			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			DocumentBuilder builder = factory.newDocumentBuilder();
			Document document = builder.parse(new InputSource(new StringReader(source)));
			NodeList nodeList = document.getElementsByTagName(TagName);
			ArrayList<Object> mainlist = new ArrayList<Object>();
			


			Object complex = this.c.newInstance();
			Element complexelement = (Element)nodeList.item(0);
				
			if(attrlist != null)
			{
				for (AttributeProperty ap : attrlist)
				{
					try 
					{
						Field field = complex.getClass().getField(ap.AttrName);
						Attr attr = complexelement.getAttributeNode(ap.AttrName);
						field.set(complex, attr.getValue());	
					} catch (Exception e)
					{
						Log.e("Complex Element To Object", "Line 72"+e.toString());
					}
				}
			}
				
			if(taglist != null)
			{
				for (TagProperty tp : taglist)
				{
					try 
					{
						Field field = complex.getClass().getField(tp.TagName);
						NodeList nl = complexelement.getElementsByTagName(tp.TagName);
						Element tageleElement = (Element)nl.item(0);
						field.set(complex, tageleElement.getFirstChild().getNodeValue());
					} catch (Exception e)
					{
						Log.e("Complex Element To Object", "Line 89"+e.toString());
					}
				}	
			}
				
			if(complexlist != null)
			{
				for (ComplexElementProperty cep : complexlist)
				{
					try 
					{
						Field field = complex.getClass().getField(cep.TagName);
						NodeList nl = complexelement.getElementsByTagName(cep.TagName);
						if(cep.isArrayOffC)
						{
							ArrayList<Object> complexprops = new ArrayList<Object>();
							for(int j = 0; j<nl.getLength(); j++)
							{
								try 
								{
									Node complexnode = nl.item(j);
									String complexString = NodeToString.elementToString(complexnode);
									ComplexElementToObject ceo = new ComplexElementToObject(cep);
									Object o = ceo.Parse(complexString);
									complexprops.add(o);
								} catch (Exception e)
								{
									Log.e("Complex Element To Object", "Line 198"+e.toString());
								}
											
							}
							field.set(complex, complexprops);
						}
						else
						{
							Node complexnode = nl.item(0);
							String complexString = NodeToString.elementToString(complexnode);
							ComplexElementToObject ceo = new ComplexElementToObject(cep);
							Object o = ceo.Parse(complexString);
							field.set(complex, o);
						}
					} 
					catch (Exception e)
					{
						Log.e("Complex Element To Object", "Line 126"+e.toString());
					}
				}
	
			}
				
			return complex;
		
		}catch(Exception ex)
		{
			Log.e("Complex Element To Object", "Line 152"+ex.toString());
			return null;
		}
		
	}
	
	
	
	
	
	
	
}
