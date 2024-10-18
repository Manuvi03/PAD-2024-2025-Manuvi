package es.ucm.fdi.gogglebooksclient;

import android.content.Context;
import android.net.Uri;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.loader.content.AsyncTaskLoader;

import java.util.Collections;
import java.util.List;

public class BookLoader extends AsyncTaskLoader<List<String>> {

    private String queryString;
    private String printType;
    private String URL_BASE = "https://www.googleapis.com/books/v1/volumes?";


    public BookLoader(@NonNull Context context,String queryString, String printType) {
        super(context);
        this.queryString = queryString;
        this.printType = printType;
    }

    @Nullable
    @Override
    public List<String> loadInBackground() {
        return getBookInfoJson(queryString, printType);
    }

    @Override
    public void onStartLoading(){
        forceLoad();
    }

    public List<String> getBookInfoJson(String queryString, String printType)
    {
        Uri builtUri = Uri.parse(URL_BASE).buildUpon().appendQueryParameter("inAuthors", queryString).
                appendQueryParameter("printType", printType).build();
        return Collections.emptyList();

    }







}
