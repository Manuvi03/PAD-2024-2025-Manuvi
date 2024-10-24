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

import es.ucm.fdi.gogglebooksclient.BookInfo;
import es.ucm.fdi.gogglebooksclient.databinding.ActivityMainBinding;
import es.ucm.fdi.gogglebooksclient.databinding.BookListElementBinding;

public class BooksResultListAdapter extends RecyclerView.Adapter<BooksResultListAdapter.ViewHolder> {

    // creating variables for arraylist and context.
    private LinkedList<BookInfo> mBooksData;
    private Context mcontext;

    // creating constructor for array list and context.
    public BooksResultListAdapter(LinkedList<BookInfo> bookInfoArrayList, Context mcontext) {
        this.mBooksData = bookInfoArrayList;
        this.mcontext = mcontext;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // inflating our layout for item of recycler view item.
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.book_rv_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        // inside on bind view holder method we are
        // setting our data to each UI component.
        BookInfo bookInfo = mBooksData.get(position);
        holder.nameTV.setText(bookInfo.getTitle());
        holder.publisherTV.setText(bookInfo.getPublisher());
        holder.pageCountTV.setText("No of Pages : " + bookInfo.getPageCount());
        holder.dateTV.setText(bookInfo.getPublishedDate());

        // below line is use to set image from URL in our image view.
        Picasso.get().load(bookInfo.getThumbnail()).into(holder.bookIV);

        // below line is use to add on click listener for our item of recycler view.
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // inside on click listener method we are calling a new activity
                // and passing all the data of that item in next intent.
                Intent i = new Intent(mcontext, BookDetails.class);
                i.putExtra("title", bookInfo.getTitle());
                i.putExtra("subtitle", bookInfo.getSubtitle());
                i.putExtra("authors", bookInfo.getAuthors());
                i.putExtra("publisher", bookInfo.getPublisher());
                i.putExtra("publishedDate", bookInfo.getPublishedDate());
                i.putExtra("description", bookInfo.getDescription());
                i.putExtra("pageCount", bookInfo.getPageCount());
                i.putExtra("thumbnail", bookInfo.getThumbnail());
                i.putExtra("previewLink", bookInfo.getPreviewLink());
                i.putExtra("infoLink", bookInfo.getInfoLink());
                i.putExtra("buyLink", bookInfo.getBuyLink());

                // after passing that data we are
                // starting our new intent.
                mcontext.startActivity(i);
            }
        });
    }

    @Override
    public int getItemCount() {
        // inside get item count method we
        // are returning the size of our array list.
        return mBooksData.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView textView;

        //SE LLAMA ASI PORQUE EL BINDING SE GENERA A PARTIR DEL NOMBRE DEL LAYOUT EN ESTE CASO ACTIVITYMAIN
        public ViewHolder(BookListElementBinding itemView) {
            super(itemView.getRoot());
            // Define click listener for the ViewHolder's View

            textView = (TextView) itemView.findViewById(R.id.vi);
        }

        public TextView getTextView() {
            return textView;
        }
    }


}
