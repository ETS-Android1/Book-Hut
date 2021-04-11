/*
 * Author: Asad Jawaid
 * */
package com.example.bookapplication;

import android.Manifest;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.File;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;


public class HomeFragment extends Fragment {

    private ArrayList<BookInfo> books = new ArrayList<>();;
    private RecyclerView recyclerView;
    private HomeAdapter homeAdapter;
    private FirebaseAuth mAuth;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        mAuth = FirebaseAuth.getInstance();

        recyclerView = view.findViewById(R.id.recycler_view_fav);

        FirebaseDatabase rootNode = FirebaseDatabase.getInstance(); // root node
        DatabaseReference reference = rootNode.getReference("Users");
        String uId = FirebaseAuth.getInstance().getCurrentUser().getUid(); // get current user id
        DatabaseReference booksBD = reference.child(uId).child("Books"); // reference to books node that contains every books this current users likes


        booksBD.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Iterator<DataSnapshot> totalBooks = snapshot.getChildren().iterator();
                // iterate over all the books and get each books data and add it to the array list
                while(totalBooks.hasNext()) {
                    DataSnapshot currentBookDS = totalBooks.next(); // get next
                    String authorName = currentBookDS.child("author").getValue().toString();
                    String imageLink = currentBookDS.child("image").getValue().toString();
                    String previewLink = currentBookDS.child("preview").getValue().toString();
                    String bookTitle = currentBookDS.child("title").getValue().toString();

                    BookInfo currentBook = new BookInfo();
                    currentBook.setBookTitle(bookTitle);
                    currentBook.setBookAuthorName(authorName);
                    currentBook.setThumbnailLink(imageLink);
                    currentBook.setPreviewLink(previewLink);
                    books.add(currentBook);
                }

                recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
                homeAdapter = new HomeAdapter(books);
                recyclerView.setAdapter(homeAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        return view;
    }
}