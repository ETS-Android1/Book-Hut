/*
 * Author: Asad Jawaid
 * */
package com.example.bookapplication;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.textfield.TextInputLayout;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


public class SearchFragment extends Fragment {
    private Button searchBtn;
    private TextInputLayout userInput;
    private String URL = "https://www.googleapis.com/books/v1/volumes?q=";
    private RecyclerView recyclerView;
    private List<BookInfo> books;
    private Adapter adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_search, container, false);

        recyclerView = view.findViewById(R.id.list_of_books);
        books = new ArrayList<>();
        searchBtn = (Button) view.findViewById(R.id.search_book_btn);
        userInput = (TextInputLayout) view.findViewById(R.id.book_container);

        searchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setUp();
            }
        });

        return view;
    }

    private void setUp() {
        String userInputBook = userInput.getEditText().getText().toString().trim(); // convert user input to string

        if(userInputBook.isEmpty()) {
            Toast.makeText(getActivity(), "Error! Please try again!", Toast.LENGTH_SHORT).show();
            return;
        }

        // this is used when user searches a new book after they have already searched for a previous book
        if(!books.isEmpty()) {
            adapter.clear(); // remove all books
            recyclerView.removeAllViewsInLayout(); // remove all books from views
        }

        String finalUrl = URL + userInputBook;

        RequestQueue queue = Volley.newRequestQueue(getActivity().getApplicationContext());
        StringRequest stringRequest = new StringRequest(Request.Method.GET, finalUrl, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    JSONArray jsonArray = jsonObject.getJSONArray("items");

                    // iterate over each book, fetch information, create book object then append to array list
                    for(int i = 0; i < jsonArray.length(); i++) {
                        JSONObject currentObject = jsonArray.getJSONObject(i); // get the current object or book

                        // volume info contains book title, authors name, page count, book category, and preview link
                        JSONObject volumeInfo = currentObject.getJSONObject("volumeInfo");
                        String bookTitle = volumeInfo.getString("title");
                        String authorName = volumeInfo.getJSONArray("authors").getString(0); // get author name
                        int pageCount = volumeInfo.getInt("pageCount"); // number of pages
                        String bookCategory = volumeInfo.getJSONArray("categories").getString(0); // book category
                        String previewBookLink = volumeInfo.getString("previewLink"); // link to preview book
                        String imageLink = volumeInfo.getJSONObject("imageLinks").getString("thumbnail"); // thumbnail

                        //BookInfo currentBook = new BookInfo(bookTitle, authorName, bookCategory, pageCount, previewBookLink); // create object
                        BookInfo currentBook = new BookInfo();
                        currentBook.setBookTitle(bookTitle);
                        currentBook.setBookAuthorName(authorName);
                        currentBook.setBookPageCount(String.valueOf(pageCount));
                        currentBook.setBookCategory(bookCategory);
                        currentBook.setPreviewLink(previewBookLink);
                        currentBook.setThumbnailLink(imageLink);
                        books.add(currentBook); // add book to array list
                    }

                }
                catch(JSONException e) {
                    e.printStackTrace();
                }

                recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
                adapter = new Adapter(getActivity(), books);
                recyclerView.setAdapter(adapter);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getActivity(), "Error! Try Again!", Toast.LENGTH_SHORT).show();
            }
        });
        queue.add(stringRequest);
    }
}