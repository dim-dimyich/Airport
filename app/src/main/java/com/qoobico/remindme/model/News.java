package com.qoobico.remindme.model;

import java.io.Serializable;

/**
 * Created by Winner on 28.03.2016.
 */
public class News implements Serializable {
    String id, title, description, news_image, created_at, comment;

    public News(){

    }
    public News(String id, String title, String description, String news_image, String created_at, String comment){
        this.id = id;
        this.title = title;
        this.description = description;
        this.news_image = news_image;
        this.created_at = created_at;
        this.comment = comment;


    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNewsTitle() {
        return title;
    }

    public void setNewsTitle(String title) {
        this.title = title;
    }

    public String getNewsDescription() {
        return description;
    }

    public void setNewsDescription(String description) {
        this.description = description;
    }

    public String getNews_image() {
        return news_image;
    }

    public void setNews_image(String news_image) {
        this.news_image = news_image;
    }

    public String getCreateNews() {
        return created_at;
    }

    public void setCreateNews(String created_at) {
        this.created_at = created_at;
    }


    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }


}
