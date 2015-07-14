package com.supanth.model;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import android.util.Log;

public class StoryDetailsParser {
    private InputStream urlStream;
    private XmlPullParserFactory factory;
    private XmlPullParser parser;

    private String blogText;

    private String urlString;
    private String tagName;


    public static final String ITEM = "item";
    public static final String CHANNEL = "channel";
    
    public static final String TITLE = "title";
    public static final String CREATOR = "dc:creator";
    public static final String LINK = "link";
    public static final String DESCRIPTION = "description";
    public static final String CATEGORY = "category";
    public static final String PUBLISHEDDATE = "pubDate";
    public static final String GUID = "guid";
    public static final String FEEDBURNERORIGLINK = "feedburner:origLink";

    
    public StoryDetailsParser(String urlString) {
        this.urlString = urlString;
    }

    public static InputStream downloadUrl(String urlString) throws IOException {
        URL url = new URL(urlString);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");
        conn.setDoInput(true);
        conn.connect();
        InputStream stream = conn.getInputStream();
        return stream;
    }
    
    private HashMap<String, String> getCurrentTagAttributes(XmlPullParser currentTagParser)
    {
    	HashMap<String, String> map = new HashMap<String, String>();
    	int attributeCount = currentTagParser.getAttributeCount();
    	for (int i = 0; i < attributeCount; i++)
    	{
    		Log.d("TAG",currentTagParser.getAttributeName(i) + " " + currentTagParser.getAttributeValue(i).toString());
    		map.put(currentTagParser.getAttributeName(i), currentTagParser.getAttributeValue(i).toString());
    	}
    	return map;
    }
    
    public String parse() {
        try {
            factory = XmlPullParserFactory.newInstance();
            parser = factory.newPullParser();
            urlStream = downloadUrl(urlString);
            parser.setInput(urlStream, null);
            int eventType = parser.getEventType();
            boolean done = false;
            blogText = "";
            boolean found = false;
            while (eventType != XmlPullParser.END_DOCUMENT && !done) {
                tagName = parser.getName();
                HashMap<String, String> attributesMap = getCurrentTagAttributes(parser);
                String className = attributesMap.get("class");
                if((className != null) && className.equals("post-content"))
                	found = true;
               switch (eventType) {
                    case XmlPullParser.START_DOCUMENT:
                        break;
                    case XmlPullParser.START_TAG:
                        if (tagName.equals("p")) {
                        	if(found)
                        	{
                        		blogText += parser.nextText().toString().trim();
                        		blogText += "\n\n";
                        	}
                        }
                        break;
                    case XmlPullParser.END_TAG:
                        if (tagName.equals("post-content")) {
                            done = true;
                        break;
                        }
                }
                eventType = parser.next();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        blogText = blogText.trim();
        return blogText;

    }
}
