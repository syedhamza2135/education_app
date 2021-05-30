package english.grammar.app.Models;

public class ChatModel {

    private String id, dialog, person_name;

    public ChatModel() {
    }

    public ChatModel(String id, String dialog, String person_name) {
        this.id = id;
        this.dialog = dialog;
        this.person_name = person_name;
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
}
