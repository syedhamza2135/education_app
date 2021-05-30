package english.grammar.app.Models;

public class ExampleModel {

    private String Example, urduMeaning;

    public ExampleModel() {
    }

    public ExampleModel(String example, String urduMeaning) {
        Example = example;
        this.urduMeaning = urduMeaning;
    }

    public String getExample() {
        return Example;
    }

    public void setExample(String example) {
        Example = example;
    }

    public String getUrduMeaning() {
        return urduMeaning;
    }

    public void setUrduMeaning(String urduMeaning) {
        this.urduMeaning = urduMeaning;
    }
}
