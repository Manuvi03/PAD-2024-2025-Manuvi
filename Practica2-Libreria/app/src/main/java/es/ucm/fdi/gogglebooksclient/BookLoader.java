package es.ucm.fdi.gogglebooksclient;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.loader.content.AsyncTaskLoader;

import java.util.Collections;
import java.util.List;

public class BookLoader extends AsyncTaskLoader<List<String>> {

    private String queryString;
    private String printType;


    public BookLoader(@NonNull Context context,String queryString, String printType) {
        super(context);
        this.queryString = queryString;
        this.printType = printType;
    }

    @Nullable
    @Override
    public List<String> loadInBackground() {

        return Collections.emptyList();
    }

    @Override
    public void onStartLoading(){
        forceLoad();
    }

    public List<String> getBookInfoJson(String queryString, String printType)
    {

    }







}
