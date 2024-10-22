package es.ucm.fdi.gogglebooksclient;

import org.json.JSONArray;
import org.json.JSONObject;

import java.net.URL;
import java.util.ArrayList;
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
    } //"volumeInfo"->"title"

    public String getAuthors() {
        return authors;
    } //"volumeInfo"->"authors[]"

    public String getDescription() {
        return description;
    } //"volumeInfo"->"description"

    public URL getInfoLink() {
        return infoLink;
    } //"volumeInfo"->"infoLink"

    public int getPages() {
        return pages;
    } //"volumeInfo"->"pageCount"

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
        List<BookInfo> bookList = new ArrayList<>();
        try {
            JSONObject json = new JSONObject(s);
            JSONArray jsonBooks = new JSONArray(json.getJSONArray("items"));

            for(int i = 0; i < jsonBooks.length(); i++){
                JSONObject book = jsonBooks.getJSONObject(i);
                JSONObject info = book.getJSONObject("volumeInfo");

                String title = info.getString("title");
                String description = info.getString("description");
                String sLink = info.getString("infoLink");
                URL infoLink = new URL(sLink);
                int pages = info.getInt("pageCount");

                String authors = "";
                JSONArray jsonAuthors = info.optJSONArray("authors");
                if(jsonAuthors != null){
                    for (int j = 0; j < jsonAuthors.length(); j++){
                        authors += jsonAuthors.getString(j);
                        if(j < jsonAuthors.length() - 1)
                            authors += ", ";
                    }
                }
                BookInfo bookInfo = new BookInfo(title, authors, infoLink, description, pages);
                bookList.add(bookInfo);

            }

        } catch (Exception e){}

        return bookList;
    }
}
