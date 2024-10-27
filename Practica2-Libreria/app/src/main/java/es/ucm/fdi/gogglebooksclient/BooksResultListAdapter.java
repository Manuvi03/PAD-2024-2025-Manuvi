package es.ucm.fdi.gogglebooksclient;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import es.ucm.fdi.gogglebooksclient.BookInfo;
import es.ucm.fdi.gogglebooksclient.databinding.ActivityMainBinding;
import es.ucm.fdi.gogglebooksclient.databinding.BookListElementBinding;

public class BooksResultListAdapter extends RecyclerView.Adapter<BooksResultListAdapter.ViewHolder> {

    // creating variables for arraylist and context.
    private LinkedList<BookInfo> mBooksData;

    // creating constructor for array list and context.
    public BooksResultListAdapter(LinkedList<BookInfo> bookInfoArrayList) {
        this.mBooksData = bookInfoArrayList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // inflating our layout for item of recycler view item.
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.book_list_element
                ,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        // En nuestra bindViewHolder estamos
        // configurando la información en nuestera UI.
        BookInfo bookInfo = mBooksData.get(position);
        holder.setData(bookInfo);

    }

    @Override
    public int getItemCount() {
        // inside get item count method we
        // are returning the size of our array list.
        return mBooksData.size();
    }

    public void setItems(LinkedList<BookInfo> items){mBooksData = items;}

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView bookImg;
        TextView title, author, url;

        //SE LLAMA ASI PORQUE EL BINDING SE GENERA A PARTIR DEL NOMBRE DEL LAYOUT EN ESTE CASO ACTIVITYMAIN
        public ViewHolder(View view) {
            super(view);
            bookImg = view.findViewById(R.id.bookImageView);
            title = view.findViewById(R.id.tituloTextView);
            author = view.findViewById(R.id.autoresTextView);
            url = view.findViewById(R.id.paginasTextView);


        }

        public void setData(final BookInfo book)
        {
            title.setText(book.getTitle());
            author.setText("");
            List<String> aut = book.getAuthors();
            for(String autor: aut)
            {
                author.append(autor + " ");
            }
            url.setText(book.getInfoLink().toString()); // estoy hay que cambiarlo


        }

    }




}
