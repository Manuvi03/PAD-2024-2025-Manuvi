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
    private int pages;
    private String thumbnail;

    public BookInfo(String title, List<String> authors, URL infoLink, int pages, String thumbnail) {
        this.title = title;
        this.authors = authors;
        this.infoLink = infoLink;
        this.pages = pages;
        this.thumbnail = thumbnail;
    }

    public String getTitle() {
        return title;
    } //"volumeInfo"->"title"

    public List<String> getAuthors() {
        return authors;
    } //"volumeInfo"->"authors[]"

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

    public void setInfoLink(URL infoLink) {
        this.infoLink = infoLink;
    }

    public void setPages(int pages) {
        this.pages = pages;
    }

    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }

    static List<BookInfo> fromJsonResponse(String s) {
        List<BookInfo> bookList = new ArrayList<>();
        List<String> authorsArrayList = new ArrayList<>();

        try {
            JSONObject json = new JSONObject(s);
            JSONArray jsonBooks = json.getJSONArray("items");Log.i("JSON", jsonBooks.toString());

            for (int i = 0; i < jsonBooks.length(); i++) {
                JSONObject book = jsonBooks.getJSONObject(i);
                JSONObject info = book.getJSONObject("volumeInfo");

                String title = info.optString("title");

                String thumbnail = null;
                JSONObject imageLinks = info.optJSONObject("imageLinks");
                if (imageLinks != null) {
                    thumbnail = imageLinks.optString("thumbnail");
                }

                int pages = info.optInt("pageCount");

                URL infoLink = null;
                String sLink = info.optString("previewLink");
                if (!sLink.isEmpty()) {
                    try {
                        infoLink = new URL(sLink);
                    } catch (MalformedURLException e) {
                        Log.e("BookInfo", "Invalid URL: " + e.getMessage());
                    }
                }

                JSONArray authorsArray = info.optJSONArray("authors");
                authorsArrayList.clear(); // Elimino el contenido para volver a usar el array
                if (authorsArray != null) {
                    for (int j = 0; j < authorsArray.length(); j++) {
                        authorsArrayList.add(authorsArray.optString(j));
                    }
                }

                BookInfo bookInfo = new BookInfo(title, new ArrayList<>(authorsArrayList), infoLink, pages, thumbnail);
                bookList.add(bookInfo);
            }
            Log.i("JSON", bookList.toString());
            return bookList;

        } catch (JSONException e) {
            Log.e("BookInfo", "Error parsing JSON: " + e.getMessage());
            return null;
        }
    }
}
