package es.ucm.fdi.gogglebooksclient;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.Loader;

import java.util.List;

public class BookLoaderCallbacks implements LoaderManager.LoaderCallbacks<List<String>>{

    private final Context context;
    public static final String EXTRA_QUERY = "queryString";
    public static final String EXTRA_PRINT_TYPE = "printType";

    public BookLoaderCallbacks(Context context)
    {
        this.context = context;
    }

    @NonNull
    @Override
    public Loader<List<String>> onCreateLoader(int id, @Nullable Bundle args) {
        assert args != null;
        return new BookLoader(context,args.getString(EXTRA_QUERY), args.getString(EXTRA_PRINT_TYPE));
    }

    @Override
    public void onLoadFinished(@NonNull Loader<List<String>> loader, List<String> data) {

    }

    @Override
    public void onLoaderReset(@NonNull Loader<List<String>> loader) {

    }
}
