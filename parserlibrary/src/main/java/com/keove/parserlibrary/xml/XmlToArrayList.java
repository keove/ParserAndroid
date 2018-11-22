package com.keove.parserlibrary.xml;


import android.app.Activity;
import android.util.Log;
import com.keove.parserlibrary.NodeListToArrayList;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import java.io.StringReader;
import java.util.ArrayList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import cz.msebera.android.httpclient.NameValuePair;

//import org.apache.http.NameValuePair;

public class XmlToArrayList
	{


		
		ArrayList<OnParseCompleteListener> listeners = new ArrayList<OnParseCompleteListener>();
		
		public XmlToArrayList(Activity activity)
		{
			act = activity;
		}
		
		public XmlToArrayList()
		{
			
		}
		
		
		public static ArrayList<NameValuePair> replacelist = null;
		
		private Activity act = null;
		
		public interface OnParseCompleteListener
		{
			public abstract void OnParseComplete(ArrayList<Object> list);
		}
		
		public void AddListener(OnParseCompleteListener listener,Boolean cleanothers)
		{
			if(cleanothers) listeners = new ArrayList<OnParseCompleteListener>();
			listeners.add(listener);
		}
		
		
		private void InvokeListeners(ArrayList<Object> list)
		{
			for (OnParseCompleteListener listener : listeners) {
				listener.OnParseComplete(list);
			}
		}
		
		
		
		
		
		public void GetObjectsAsync(String xmlString, String elementname, Class elementclass)
		{
			xmlStringasync = xmlString;
			elementnameasync = elementname;
			elementclassasync = elementclass;
			
			GetObjectsThread thread = new GetObjectsThread();
			//thread.setPriority(Thread.MIN_PRIORITY);
			thread.start();
		}
		
		String xmlStringasync = "";
		String elementnameasync = "";
		@SuppressWarnings("rawtypes")
        Class elementclassasync;
		
		class GetObjectsThread extends Thread
		{
			@Override
            public void run()
			{
				try
	            {
					if(replacelist != null)
					{
						if(replacelist.size() > 0)
						{
							for (NameValuePair data : replacelist) {
								xmlStringasync = xmlStringasync.replace(data.getName(), data.getValue());
							}
						}
					}
					Runtime.getRuntime().gc();
		            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		            DocumentBuilder builder = factory.newDocumentBuilder();
		            
		            
		            Document document = builder.parse(new InputSource(new StringReader(xmlStringasync)));
		            NodeList nodeList = document.getElementsByTagName(elementnameasync);
		            NodeListToArrayList parser = new NodeListToArrayList();
		            Runtime.getRuntime().gc();
		            final ArrayList<Object> list = parser.GetObjects(nodeList, elementnameasync, elementclassasync);
		            if(act!=null)
		            {
		            	act.runOnUiThread(new Runnable()
		            	{	
							public void run() 
							{	
								InvokeListeners(list);
							}
						});
		            }
		            else
		            {
		            	InvokeListeners(list);
					}
	            }
	            catch (Exception e)
	            {
	            	Log.e("Xml To ArrayList","line 31 : "+e.toString());
	            	InvokeListeners(null);
	            }
			}
		}
		
		
		
		
		public ArrayList<Object> GetObjects(String xmlString, String elementname, Class elementclass, String undertag)
		{
			
			//xmlString = xmlString.replaceAll( "&([^;]+(?!(?:\\w|;)))", "&amp;$1" );
			
			try
            {
				if(replacelist != null)
				{
					if(replacelist.size() > 0)
					{
						for (NameValuePair data : replacelist) {
							xmlString = xmlString.replace(data.getName(), data.getValue());
						}
					}
				}
				Runtime.getRuntime().gc();
	            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
	            DocumentBuilder builder = factory.newDocumentBuilder();
	            
	            
	            Document document = builder.parse(new InputSource(new StringReader(xmlString)));
	            NodeList prefrootlist = document.getElementsByTagName(undertag);
	            //NodeList nodeList = document.getElementsByTagName(elementname);
	            Element prefelem = (Element)prefrootlist.item(0);
	            NodeList filtered = prefelem.getElementsByTagName(elementname);
	            NodeListToArrayList parser = new NodeListToArrayList();
	            Runtime.getRuntime().gc();
	            return parser.GetObjects(filtered, elementname, elementclass);
            }
            catch (Exception e)
            {
            	Log.e("Xml To ArrayList","line 31 : "+e.toString());
            	return null;
            }
			
		}
		
		
		
		public ArrayList<Object> GetObjects(String xmlString, String elementname, Class elementclass)
		{
			
			//xmlString = xmlString.replaceAll( "&([^;]+(?!(?:\\w|;)))", "&amp;$1" );
			
			try
            {
				if(replacelist != null)
				{
					if(replacelist.size() > 0)
					{
						for (NameValuePair data : replacelist) {
							xmlString = xmlString.replace(data.getName(), data.getValue());
						}
					}
				}
				Runtime.getRuntime().gc();
	            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
	            DocumentBuilder builder = factory.newDocumentBuilder();
	            
	            
	            Document document = builder.parse(new InputSource(new StringReader(xmlString)));
	            NodeList nodeList = document.getElementsByTagName(elementname);
	            NodeListToArrayList parser = new NodeListToArrayList();
	            Runtime.getRuntime().gc();
	            return parser.GetObjects(nodeList, elementname, elementclass);
            }
            catch (Exception e)
            {
            	Log.e("Xml To ArrayList","line 31 : "+e.toString());
            	return null;
            }
			
		}
	
	}
