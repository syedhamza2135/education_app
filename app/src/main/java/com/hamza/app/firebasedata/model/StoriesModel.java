package com.hamza.app.firebasedata.model;

public class StoriesModel {

    String title,story;

    public StoriesModel(String title, String story) {
        this.title = title;
        this.story = story;
    }

    public StoriesModel() {
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getStory() {
        return story;
    }

    public void setStory(String story) {
        this.story = story;
    }
}
