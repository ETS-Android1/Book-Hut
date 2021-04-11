/*
 * Author: Asad Jawaid
 * */
package com.example.bookapplication;

import android.content.Context;
import android.content.Intent;

import android.content.SharedPreferences;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Iterator;

public class HomeAdapter extends RecyclerView.Adapter<HomeAdapter.MyViewHolder> {
    private ArrayList<BookInfo> books;
    private Context context;
    private RequestOptions options;

    public HomeAdapter(ArrayList<BookInfo> books) {
        this.books = books;
        options = new RequestOptions().centerCrop().placeholder(R.drawable.book_logo).error(R.drawable.book_logo);
    }
    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.add_to_fav_layout, parent, false);
        context = parent.getContext();
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        BookInfo currentBook = books.get(position);
        holder.bookTitle.setText(currentBook.getBookTitle());
        holder.bookAuthor.setText(currentBook.getBookAuthorName());

        StringBuilder sb = new StringBuilder(currentBook.getThumbnailLink());
        sb.insert(4, 's'); // insert s into http otherwise image will not load

        // get image and insert it into the image view
        Glide.with(context).load(sb.toString()).apply(options).into(holder.imageLink); // if image does not load then use book logo

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
    }

    @Override
    public int getItemCount() {
        return books.size();
    }

    // inner class
    public class MyViewHolder extends  RecyclerView.ViewHolder {
        TextView bookTitle, bookAuthor;
        Button previewBtn;
        ImageView imageLink, deleteItem;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            bookTitle = itemView.findViewById(R.id.book_title_fav);
            bookAuthor = itemView.findViewById(R.id.author_name_fav);
            previewBtn = itemView.findViewById(R.id.preview_btn_fav);
            imageLink = itemView.findViewById(R.id.book_image_fav);
            deleteItem = itemView.findViewById(R.id.delete_item);

            FirebaseDatabase rootNode = FirebaseDatabase.getInstance(); // root node
            DatabaseReference reference = rootNode.getReference("Users");
            String uId = FirebaseAuth.getInstance().getCurrentUser().getUid(); // get current user id
            DatabaseReference booksBD = reference.child(uId).child("Books"); // reference to books node that contains every books this current users likes

            deleteItem.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int positionOfItem = getAdapterPosition();

                    booksBD.addListenerForSingleValueEvent(new ValueEventListener() {
                        int i = 0;

                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            DataSnapshot ds = snapshot;
                            for (DataSnapshot currentSnap : snapshot.getChildren()) {
                                if(i == positionOfItem) {
                                    ds = currentSnap;
                                    Log.d("POS =", String.valueOf(positionOfItem));
                                    Log.d("I = ", String.valueOf(i));
                                }
                                i++;
                            }
                            ds.getRef().removeValue();
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                    books.remove(positionOfItem);
                    notifyItemRemoved(positionOfItem);
                }
            });
        }
    }
}
