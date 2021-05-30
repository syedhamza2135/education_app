package english.grammar.app.Models;

public class DialoguesModel {

    private String id, dialog, person_name, conversationType_Id;

    public DialoguesModel() {
    }

    public DialoguesModel(String id, String dialog, String person_name, String conversationType_Id) {
        this.id = id;
        this.dialog = dialog;
        this.person_name = person_name;
        this.conversationType_Id = conversationType_Id;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDialog() {
        return dialog;
    }

    public void setDialog(String dialog) {
        this.dialog = dialog;
    }

    public String getPerson_name() {
        return person_name;
    }

    public void setPerson_name(String person_name) {
        this.person_name = person_name;
    }

    public String getConversationType_Id() {
        return conversationType_Id;
    }

    public void setConversationType_Id(String conversationType_Id) {
        this.conversationType_Id = conversationType_Id;
    }
}
