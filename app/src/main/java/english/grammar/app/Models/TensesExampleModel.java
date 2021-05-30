package english.grammar.app.Models;

public class TensesExampleModel {

    private String id, urduexample, englishexample;

    public TensesExampleModel() {
    }

    public TensesExampleModel(String id, String urduexample, String englishexample) {
        this.id = id;
        this.urduexample = urduexample;
        this.englishexample = englishexample;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUrduexample() {
        return urduexample;
    }

    public void setUrduexample(String urduexample) {
        this.urduexample = urduexample;
    }

    public String getEnglishexample() {
        return englishexample;
    }

    public void setEnglishexample(String englishexample) {
        this.englishexample = englishexample;
    }
}
