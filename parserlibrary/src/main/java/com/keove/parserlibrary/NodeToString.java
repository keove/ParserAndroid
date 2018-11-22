package com.keove.parserlibrary;

import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.lang.reflect.Method;

/**
 * @author <a href="mailto:ovidiu@feodorov.com">Ovidiu Feodorov</a>
 * @version <tt>$Revision: 3282 $</tt> $Id: XMLUtil.java 3282 2007-11-01
 *          15:32:29Z timfox $
 */
public class NodeToString {

  public static String elementToString(Node n) {

    String name = n.getNodeName();

    short type = n.getNodeType();

    if (Node.CDATA_SECTION_NODE == type) {
      return "<![CDATA[" + n.getNodeValue() + "]]&gt;";
    }

    if (name.startsWith("#")) {
      return "";
    }

    StringBuffer sb = new StringBuffer();
    sb.append('<').append(name);

    NamedNodeMap attrs = n.getAttributes();
    if (attrs != null) {
      for (int i = 0; i < attrs.getLength(); i++) {
        Node attr = attrs.item(i);
        sb.append(' ').append(attr.getNodeName()).append("=\"").append(attr.getNodeValue()).append(
            "\"");
      }
    }

    String textContent = null;
    NodeList children = n.getChildNodes();

    if (children.getLength() == 0) {
      if ((textContent = getTextContent(n)) != null && !"".equals(textContent)) {
        sb.append(textContent).append("</").append(name).append('>');
        ;
      } else {
        sb.append("/>").append('\n');
      }
    } else {
      sb.append('>').append('\n');
      boolean hasValidChildren = false;
      for (int i = 0; i < children.getLength(); i++) {
        String childToString = elementToString(children.item(i));
        if (!"".equals(childToString)) {
          sb.append(childToString);
          hasValidChildren = true;
        }
      }

      if (!hasValidChildren && ((textContent = getTextContent(n)) != null)) {
        sb.append(textContent);
      }

      sb.append("</").append(name).append('>');
    }

    return sb.toString();
  }

  private static final Object[] EMPTY_ARRAY = new Object[0];

  public static String getTextContent(final Node n)
  {
    if (n.hasChildNodes())
    {
      StringBuffer sb = new StringBuffer();
      NodeList nl = n.getChildNodes();
      for (int i = 0; i < nl.getLength(); i++)
      {
        sb.append(elementToString(nl.item(i)));
        if (i < nl.getLength() - 1)
        {
          sb.append('\n');
        }
      }

      String s = sb.toString();
      if (s.length() != 0)
      {
        return s;
      }
    }

    Method[] methods = Node.class.getMethods();

    for (Method getTextContext : methods)
    {
      if ("getTextContent".equals(getTextContext.getName()))
      {
        try
        {
          return (String)getTextContext.invoke(n, EMPTY_ARRAY);
        }
        catch (Exception e)
        {
          //XMLUtil.log.error("Failed to invoke getTextContent() on node " + n, e);
          return null;
        }
      }
    }

    String textContent = null;

    if (n.hasChildNodes())
    {
      NodeList nl = n.getChildNodes();
      for (int i = 0; i < nl.getLength(); i++)
      {
        Node c = nl.item(i);
        if (c.getNodeType() == Node.TEXT_NODE)
        {
          textContent = n.getNodeValue();
          if (textContent == null)
          {
            // TODO This is a hack. Get rid of it and implement this properly
            String s = c.toString();
            int idx = s.indexOf("#text:");
            if (idx != -1)
            {
              textContent = s.substring(idx + 6).trim();
              if (textContent.endsWith("]"))
              {
                textContent = textContent.substring(0, textContent.length() - 1);
              }
            }
          }
          if (textContent == null)
          {
            break;
          }
        }
      }

      // TODO This is a hack. Get rid of it and implement this properly
      String s = n.toString();
      int i = s.indexOf('>');
      int i2 = s.indexOf("</");
      if (i != -1 && i2 != -1)
      {
        textContent = s.substring(i + 1, i2);
      }
    }

    return textContent;
  }


}