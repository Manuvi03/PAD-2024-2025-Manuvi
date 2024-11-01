package es.ucm.fdi.googlebooksclient;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.List;

public class BooksResultListAdapter extends RecyclerView.Adapter<BooksResultListAdapter.ViewHolder> {

    private List<BookInfo> mBooksData;
    private final LayoutInflater mInflater;


    public BooksResultListAdapter(List<BookInfo> bookInfoArrayList, Context context) {
        this.mInflater = LayoutInflater.from(context);
        this.mBooksData = bookInfoArrayList;
    }

    public void setBooksData(List<BookInfo> books) {
        this.mBooksData = books;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Inflar el layout para cada elemento de la lista
        View view = mInflater.inflate(R.layout.book_list_element, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        // Configuración de la UI para cada elemento
        BookInfo bookInfo = mBooksData.get(position);
        holder.title.setText(bookInfo.getTitle());

        // Configuración de los autores
        List<String> authors = bookInfo.getAuthors();
        holder.author.setText(authors != null ? String.join(", ", authors) : "Autor desconocido");

        // Configuración de las páginas
        holder.pages.setText(String.valueOf(bookInfo.getPages()));

        // Configuración de la imagen usando Picasso
        // utiliza un placeholder para cuando no hay imagen o hay un error
        String s = bookInfo.getThumbnail();
        Picasso.get().load(s)
                .placeholder(R.drawable.placeholder_book)
                .error(R.drawable.menu_book)
                        .into(holder.bookImg, new Callback() {
                            @Override
                            public void onSuccess() {
                                Log.d("Picasso", "Imagen cargada con exito");
                            }

                            @Override
                            public void onError(Exception e) {
                                Log.e("Picasso", "Fallo al cargar la imagen: " + e.getMessage());
                            }
                        });

        // Listener para abrir el URL del libro en el navegador
        holder.itemView.setOnClickListener(view -> {
            Log.i("BookLink",String.valueOf(bookInfo.getInfoLink()));
            Uri bookUri = Uri.parse(String.valueOf(bookInfo.getInfoLink()));
            Intent intent = new Intent(Intent.ACTION_VIEW, bookUri);
            view.getContext().startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        // Devuelve el tamaño de la lista de libros
        return mBooksData != null ? mBooksData.size() : 0;
    }

    // Clase interna ViewHolder para el RecyclerView
    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView bookImg;
        TextView title, author, pages;

        public ViewHolder(View view) {
            super(view);
            bookImg = view.findViewById(R.id.bookImageView);
            title = view.findViewById(R.id.tituloTextView);
            author = view.findViewById(R.id.autoresTextView);
            pages = view.findViewById(R.id.paginasTextView);
        }
    }
}
