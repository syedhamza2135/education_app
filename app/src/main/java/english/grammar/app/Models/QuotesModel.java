package english.grammar.app.Models;

public class QuotesModel {

    private String id, eng_quote, author, fav;

    public QuotesModel() {
    }

    public QuotesModel(String id, String eng_quote, String author, String fav) {
        this.id = id;
        this.eng_quote = eng_quote;
        this.author = author;
        this.fav = fav;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getEng_quote() {
        return eng_quote;
    }

    public void setEng_quote(String eng_quote) {
        this.eng_quote = eng_quote;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getFav() {
        return fav;
    }

    public void setFav(String fav) {
        this.fav = fav;
    }
}
