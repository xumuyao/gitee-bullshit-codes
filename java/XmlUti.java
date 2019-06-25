public class XmlUtil{
	public static String getByTag(String xml, String tag) {
	    if (xml == null || xml.equals("") || tag == null || tag.equals(""))
	      return ""; 
	    int beg = xml.indexOf("<" + tag + ">");
	    if (beg < 0) return ""; 
	    int end = xml.indexOf("</" + tag + ">");
	    if (end < 0) return ""; 
    return xml.substring(beg + 2 + tag.length(), end);
  }
}