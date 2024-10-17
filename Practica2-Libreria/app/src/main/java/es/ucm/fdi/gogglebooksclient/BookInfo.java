package es.ucm.fdi.gogglebooksclient;

import java.net.URL;
import java.util.List;

public class BookInfo {
    private String title;
    private String authors;
    private URL infoLink;
    private String description;
    private int pages;

    public BookInfo(String title, String authors, URL infoLink, String description, int pages){
        this.title = title;
        this.authors = authors;
        this.infoLink = infoLink;
        this.description = description;
        this.pages = pages;
    }
    public BookInfo(){}

    public String getTitle(){
        return title;
    }

    public String getAuthors() {
        return authors;
    }

    public String getDescription() {
        return description;
    }

    public URL getInfoLink() {
        return infoLink;
    }

    public int getPages() {
        return pages;
    }
    public void setTitle(String title){
        this.title = title;
    }

    public void setAuthors(String authors) {
        this.authors = authors;
    }
    public void setDescription(String description) {
        this.description = description;
    }
    public void setInfoLink(URL infoLink) {
        this.infoLink = infoLink;
    }
    public void setPages(int pages) {
        this.pages = pages;
    }
    public void setURL(URL url) {
        this.infoLink = url;
    }
    static List<BookInfo> fromJsonResponse(String s){

    }
}
