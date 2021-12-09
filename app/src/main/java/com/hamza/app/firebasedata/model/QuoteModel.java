package com.hamza.app.firebasedata.model;

public class QuoteModel {
    String author,eng_quote;

    public QuoteModel(String author, String eng_quote) {
        this.author = author;
        this.eng_quote = eng_quote;

    }

    public QuoteModel() {
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getEng_quote() {
        return eng_quote;
    }

    public void setEng_quote(String eng_quote) {
        this.eng_quote = eng_quote;
    }

}
