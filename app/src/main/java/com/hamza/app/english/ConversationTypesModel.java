package com.hamza.app.english;

public class ConversationTypesModel {

    private String id, type, conversation_id;

    public ConversationTypesModel() {
    }

    public ConversationTypesModel(String id, String type, String conversation_id) {
        this.id = id;
        this.type = type;
        this.conversation_id = conversation_id;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getConversation_id() {
        return conversation_id;
    }

    public void setConversation_id(String conversation_id) {
        this.conversation_id = conversation_id;
    }
}
