package com.keove.parserlibrary;

import android.util.Log;

import com.keove.parserlibrary.xml.ElementToObject;

import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import java.util.ArrayList;

public class NodeListToArrayList
	{
		public ArrayList<Object> GetObjects(NodeList nodes, String elementname, Class objclass)
		{
			try
            {
				ArrayList<Object> objects = new ArrayList<Object>();
				for(int i = 0; i<nodes.getLength(); i++)
				{
					Element element = (Element)nodes.item(i);
					if(element.getNodeName().contentEquals(elementname))
					{

						ElementToObject parser = new ElementToObject();
						Object object = parser.GetObject(element, objclass);
						if(object != null)
						{
							objects.add(object);
						}
					}
				}
				return objects;
            }
            catch (Exception e)
            {
            	Log.e("NodeList To Array","line 34 : "+e.toString());
            	return null;
            }
		}
	}
