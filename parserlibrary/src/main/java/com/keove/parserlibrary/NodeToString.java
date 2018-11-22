package com.keove.parserlibrary;

import com.keove.parserlibrary.Util.XMLUtil;

import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

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
      if ((textContent = XMLUtil.getTextContent(n)) != null && !"".equals(textContent)) {
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

      if (!hasValidChildren && ((textContent = XMLUtil.getTextContent(n)) != null)) {
        sb.append(textContent);
      }

      sb.append("</").append(name).append('>');
    }

    return sb.toString();
  }
}