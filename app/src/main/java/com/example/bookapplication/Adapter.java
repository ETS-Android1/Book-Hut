/*
* Author: Asad Jawaid
* */
package com.example.bookapplication;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.List;

public class Adapter extends RecyclerView.Adapter<Adapter.ViewHolder> {

    LayoutInflater inflater;
    List<BookInfo> books;
    private FirebaseAuth mAuth;
    private static int counter = 0;

    public Adapter(Context context, List<BookInfo> books) {
        this.inflater = LayoutInflater.from(context);
        this.books = books;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.custom_list_book, parent, false);
        mAuth = FirebaseAuth.getInstance(); // ADDED NOW!
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.bookTitle.setText(books.get(position).getBookTitle()); // book title
        holder.bookAuthor.setText(books.get(position).getBookAuthorName()); // author name
        holder.bookCategory.setText(books.get(position).getBookCategory()); // book category
        holder.bookPageCount.setText(books.get(position).getBookPageCount()); // book page count

        // by click the preview button in any one of the list items inside the recycler view, it opens the link to the book
        holder.previewBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String url = books.get(position).getPreviewLink(); //
                Log.d("URL", url);
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse(url));
                holder.previewBtn.getContext().startActivity(intent);
            }
        });

        holder.addFavBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String bookTitle = books.get(position).getBookTitle();
                String authorName = books.get(position).getBookAuthorName();
                String imageLink = books.get(position).getThumbnailLink();
                String previewLink = books.get(position).getPreviewLink();

                HashMap<String, String> map = new HashMap<>();
                map.put("title", bookTitle);
                map.put("author", authorName);
                map.put("image", imageLink);
                map.put("preview", previewLink);

                FirebaseDatabase rootNode = FirebaseDatabase.getInstance(); // root node
                DatabaseReference reference = rootNode.getReference("Users"); // Users
                String uId = FirebaseAuth.getInstance().getCurrentUser().getUid(); // get current user id
                reference.child(uId).child("Books").push().setValue(map); // adds the books to the database for the current logged in user

            }
        });
    }

    @Override
    public int getItemCount() {
        return books.size();
    }

    // inner class
    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView bookTitle, bookAuthor, bookCategory, bookPageCount;
        Button previewBtn, addFavBtn;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            bookTitle = itemView.findViewById(R.id.book_title);
            bookAuthor = itemView.findViewById(R.id.author_name);
            bookCategory = itemView.findViewById(R.id.book_categories);
            bookPageCount = itemView.findViewById(R.id.page_count);
            previewBtn = itemView.findViewById(R.id.preview_btn);
            addFavBtn = itemView.findViewById(R.id.add_favourites);
        }
    }

    public void clear() {
        int size = books.size();
        if (size > 0) {
            for (int i = 0; i < size; i++) {
                books.remove(0);
            }
            notifyItemRangeRemoved(0, size);
        }
    }
}
