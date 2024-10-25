package es.ucm.fdi.gogglebooksclient;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONException;


import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;


public class BookInfo {
    private String title;
    private List<String> authors;
    private URL infoLink;
    private String description;
    private int pages;
    private String thumbnail;

    public BookInfo(String title, List<String> authors, URL infoLink, String description, int pages) {
        this.title = title;
        this.authors = authors;
        this.infoLink = infoLink;
        this.description = description;
        this.pages = pages;
    }

    public BookInfo() {
    }

    public String getTitle() {
        return title;
    } //"volumeInfo"->"title"

    public List<String> getAuthors() {
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

    public String getThumbnail() {
        return thumbnail;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setAuthors(List<String> authors) {
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

    public void setThumbnail(String title) {
        this.thumbnail = thumbnail;
    }

    static List<BookInfo> fromJsonResponse(String s) {
        List<BookInfo> bookList = new ArrayList<>();
        try {
            JSONObject json = new JSONObject(s);
            JSONArray jsonBooks = json.getJSONArray("items");
            Log.i("JSON", jsonBooks.toString());

            for (int i = 0; i < jsonBooks.length(); i++) {
                JSONObject book = jsonBooks.getJSONObject(i);
                JSONObject info = book.getJSONObject("volumeInfo");

                // Obtener el título (esto es obligatorio
                String title = info.optString("title");
                String description = info.optString("description");
                int pages = info.optInt("pageCount");
                String sLink = info.optString("infoLink");
                URL infoLink;
                if (!sLink.isEmpty()) {
                    infoLink = new URL(sLink);
                } else {
                    infoLink = null;
                }

                JSONArray authorsArray = info.getJSONArray("authors");
                List<String> authorsArrayList = new ArrayList<>();

                for (int j = 0; j < authorsArray.length(); j++) {
                    authorsArrayList.add(authorsArray.optString(j));
                }

                // Crear y agregar el objeto BookInfo
                BookInfo bookInfo = new BookInfo(title, authorsArrayList, infoLink, description, pages);
                bookList.add(bookInfo);

            }
            return bookList;
        } catch (MalformedURLException | JSONException e) {
            Log.i("JSON", "Error al parsear el JSON");
            return null;
        }
    }
}
