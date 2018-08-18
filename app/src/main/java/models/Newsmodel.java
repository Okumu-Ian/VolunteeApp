package models;

import java.io.Serializable;

public class Newsmodel implements Serializable {

    private StringBuilder newsText;
    private String main_news;
    private String holderId;
    private String holderName;
    private String timestamp;
    private String imageUrl;

    public Newsmodel() {
    }

    public StringBuilder getNewsText() {
        return newsText;
    }

    public void setNewsText(StringBuilder newsText) {
        this.newsText = newsText;
    }

    public String getMain_news() {
        return main_news;
    }

    public void setMain_news(String main_news) {
        this.main_news = main_news;
    }

    public String getHolderId() {
        return holderId;
    }

    public void setHolderId(String holderId) {
        this.holderId = holderId;
    }

    public String getHolderName() {
        return holderName;
    }

    public void setHolderName(String holderName) {
        this.holderName = holderName;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
}
