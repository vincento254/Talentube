package com.example.talentube;

public class MediaObject {

    private String id;
    private String title;
    private String description;
    private String timeStamp;
    private String url;

    public MediaObject() {
    }

    public MediaObject(String id, String title, String description, String timeStamp, String url) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.timeStamp = timeStamp;
        this.url = url;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(String timeStamp) {
        this.timeStamp = timeStamp;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}