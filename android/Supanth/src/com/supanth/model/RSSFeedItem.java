package com.supanth.model;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;


public class RSSFeedItem implements Serializable {
	private static final long serialVersionUID = 1L;
	private String title;
	private String author;
    private String link;
    private String description;
    private String category;
    private Date pubDate;
    private String guid;
    private String feedburnerOrigLink;

    public RSSFeedItem() {
    }

    public RSSFeedItem(String title, String author, String link, String description, String category, String pubDate,
            String guid, String feedburnerOrigLink) {
        this.title = title;
        this.author = author;
        this.link = link;
        this.description = description;
        this.category = category;
        this.guid = guid;
        this.feedburnerOrigLink = feedburnerOrigLink;
        
		try {
			this.pubDate = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss z", Locale.getDefault()).parse(pubDate);
		} catch (ParseException e) {
			e.printStackTrace();
			this.pubDate = new Date();
		}
    }

    public String getTitle() {
        return title;
    }

    public String getAuthor() {
        return author;
    }

    public String getLink() {
        return link;
    }

    public String getDescription() {
        return description;
    }

    public String getCategory() {
        return category;
    }

    public Date getPubDate() {
        return pubDate;
    }

    public String getGuid() {
        return guid;
    }

    public String getFeedburnerOrigLink() {
        return feedburnerOrigLink;
    }


    public void setTitle(String title) {
        this.title = title;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public void setPubDate(Date pubDate) {
        this.pubDate = pubDate;
    }

    public void setGuid(String guid) {
        this.guid = guid;
    }

    public void setFeedburnerOrigLink(String feedburnerOrigLink) {
        this.feedburnerOrigLink = feedburnerOrigLink;
    }

}
