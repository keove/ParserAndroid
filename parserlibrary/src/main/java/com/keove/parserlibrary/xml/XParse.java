package com.keove.parserlibrary.xml;

import java.util.ArrayList;

public class XParse
{
	public static ArrayList<Object> GO(String xml, String elementName, Class klass)
	{
		XmlToArrayList parser = new XmlToArrayList();
		return parser.GetObjects(xml, elementName, klass);
	}

	public static Object GO(String xml, String elementName, Class klass, boolean single)
	{
		XmlToArrayList parser = new XmlToArrayList();
		ArrayList<Object> list = parser.GetObjects(xml, elementName, klass);
		if(list!=null && list.size() > 0) return list.get(0); else return null;
	}
	
	
}
