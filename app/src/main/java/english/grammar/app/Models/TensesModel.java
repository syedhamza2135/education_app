package english.grammar.app.Models;

public class TensesModel {

    private String id, name, def_urdu, method_urdu, def_eng, method_eng, main_tense;

    public TensesModel() {
    }

    public TensesModel(String id, String name, String def_urdu, String method_urdu, String def_eng, String method_eng, String main_tense) {
        this.id = id;
        this.name = name;
        this.def_urdu = def_urdu;
        this.method_urdu = method_urdu;
        this.def_eng = def_eng;
        this.method_eng = method_eng;
        this.main_tense = main_tense;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDef_urdu() {
        return def_urdu;
    }

    public void setDef_urdu(String def_urdu) {
        this.def_urdu = def_urdu;
    }

    public String getMethod_urdu() {
        return method_urdu;
    }

    public void setMethod_urdu(String method_urdu) {
        this.method_urdu = method_urdu;
    }

    public String getDef_eng() {
        return def_eng;
    }

    public void setDef_eng(String def_eng) {
        this.def_eng = def_eng;
    }

    public String getMethod_eng() {
        return method_eng;
    }

    public void setMethod_eng(String method_eng) {
        this.method_eng = method_eng;
    }

    public String getMain_tense() {
        return main_tense;
    }

    public void setMain_tense(String main_tense) {
        this.main_tense = main_tense;
    }
}
