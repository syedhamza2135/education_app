package english.grammar.app.Models;

public class PartOfSpeechModel {

    private String id, name_eng, name_urdu, def_eng, def_urdu, eg_eng;

    public PartOfSpeechModel() {
    }

    public PartOfSpeechModel(String id, String name_eng, String name_urdu, String def_eng, String def_urdu, String eg_eng) {
        this.id = id;
        this.name_eng = name_eng;
        this.name_urdu = name_urdu;
        this.def_eng = def_eng;
        this.def_urdu = def_urdu;
        this.eg_eng = eg_eng;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName_eng() {
        return name_eng;
    }

    public void setName_eng(String name_eng) {
        this.name_eng = name_eng;
    }

    public String getName_urdu() {
        return name_urdu;
    }

    public void setName_urdu(String name_urdu) {
        this.name_urdu = name_urdu;
    }

    public String getDef_eng() {
        return def_eng;
    }

    public void setDef_eng(String def_eng) {
        this.def_eng = def_eng;
    }

    public String getDef_urdu() {
        return def_urdu;
    }

    public void setDef_urdu(String def_urdu) {
        this.def_urdu = def_urdu;
    }

    public String getEg_eng() {
        return eg_eng;
    }

    public void setEg_eng(String eg_eng) {
        this.eg_eng = eg_eng;
    }
}
