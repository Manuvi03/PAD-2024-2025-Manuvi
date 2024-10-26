package es.ucm.fdi.gogglebooksclient;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.Loader;

import java.util.List;

public class BookLoaderCallbacks implements LoaderManager.LoaderCallbacks<List<BookInfo>>{

    private final Context context;
    public static final String EXTRA_QUERY = "queryString";
    public static final String EXTRA_PRINT_TYPE = "printType";

    public BookLoaderCallbacks(Context context)
    {
        this.context = context;
    }

    @NonNull
    @Override
    // crea el Loader para el ID dado
    public Loader<List<BookInfo>> onCreateLoader(int id, @Nullable Bundle args) {
        assert args != null;
        return new BookLoader(context,args.getString(EXTRA_QUERY), args.getString(EXTRA_PRINT_TYPE));
    }

    @Override
    // devuelve los datos cargados tras realizar la búsqueda
    public void onLoadFinished(@NonNull Loader<List<BookInfo>> loader, List<BookInfo> data) {
        // a través del Context, se envía la información a la main activity
        MainActivity ma = (MainActivity) context;
        ma.updateBooksResult(data);
    }

    @Override
    // vacío
    public void onLoaderReset(@NonNull Loader<List<BookInfo>> loader) {}
}
