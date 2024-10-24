package es.ucm.fdi.gogglebooksclient;

import android.util.Log;

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

    static List<BookInfo> fromJsonResponse(String s){
        List<BookInfo> bookList = new ArrayList<>();
        try {
            JSONObject json = new JSONObject(s);
            JSONArray jsonBooks = json.getJSONArray("items");
            Log.i("JSON", jsonBooks.toString());

            for(int i = 0; i < jsonBooks.length(); i++){
                try {
                    JSONObject book = jsonBooks.getJSONObject(i);
                    JSONObject info = book.getJSONObject("volumeInfo");

                    // Obtener el título (esto es obligatorio, así que no necesita try-catch)
                    String title = info.getString("title");

                    // Intentar obtener la descripción
                    String description = "";
                    try {
                        if (!info.getString("description").isEmpty()) {
                            description = info.getString("description");
                        }
                    } catch (Exception e) {
                        System.out.println("Descripción no disponible: " + e.getMessage());
                    }

                    // Intentar obtener el infoLink
                    String sLink = "";
                    try {
                        if (!info.getString("infoLink").isEmpty()) {
                            sLink = info.getString("infoLink");
                        }
                    } catch (Exception e) {
                        System.out.println("InfoLink no disponible: " + e.getMessage());
                    }

                    // Convertir sLink a URL
                    URL infoLink = null;
                    try {
                        infoLink = new URL(sLink);
                    } catch (Exception e) {
                        System.out.println("URL no válida: " + e.getMessage());
                    }

                    // Intentar obtener el número de páginas
                    int pages = 0;
                    try {
                        pages = info.getInt("pageCount");
                    } catch (Exception e) {
                        System.out.println("Número de páginas no disponible: " + e.getMessage());
                    }

                    // Intentar obtener los autores
                    StringBuilder authors = new StringBuilder();
                    try {
                        JSONArray jsonAuthors = info.optJSONArray("authors");
                        if (jsonAuthors != null) {
                            for (int j = 0; j < jsonAuthors.length(); j++) {
                                authors.append(jsonAuthors.getString(j));
                                if (j < jsonAuthors.length() - 1)
                                    authors.append(", ");
                            }
                        }
                    } catch (Exception e) {
                        System.out.println("Autores no disponibles: " + e.getMessage());
                    }

                    // Crear y agregar el objeto BookInfo
                    BookInfo bookInfo = new BookInfo(title, authors.toString(), infoLink, description, pages);
                    bookList.add(bookInfo);

                } catch (Exception e) {
                    System.out.println("Error procesando el libro: " + e.getMessage());
                }
            }


        } catch (Exception ignored){}

        return bookList;
    }
}
