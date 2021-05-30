package english.grammar.app.Models;

public class StoriesModel {

    private String id, title, story;

    public StoriesModel() {
    }

    public StoriesModel(String id, String title, String story) {
        this.id = id;
        this.title = title;
        this.story = story;
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

    public String getStory() {
        return story;
    }

    public void setStory(String story) {
        this.story = story;
    }
}
