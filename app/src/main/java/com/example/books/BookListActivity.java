package com.example.books;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.MenuItemCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.SearchView;
import android.widget.TextView;

import java.net.URL;
import java.util.ArrayList;

public class BookListActivity extends AppCompatActivity implements SearchView.OnQueryTextListener {
    private ProgressBar mLoadingProgress;
    private RecyclerView rvBooks;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_list);
        mLoadingProgress = (ProgressBar) findViewById(R.id.pb_loading);
        rvBooks = (RecyclerView) findViewById(R.id.rv_books);
        LinearLayoutManager booksLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL,false);
        rvBooks.setLayoutManager(booksLayoutManager);
//        Intent intent = getIntent();
//        String query = intent.getStringExtra("Query");
        URL bookURL;
        try{
            if(ApiUtil.SEARCH_ACTIVTY_URL == null){
                bookURL = ApiUtil.buildURL("cooking");
            }else {
                 bookURL = ApiUtil.SEARCH_ACTIVTY_URL;
            }
             String jsonResult = ApiUtil.getJson(bookURL);
            new BookQueryTask().execute(bookURL);
        }catch (Exception ex){
            Log.d("Error",ex.getMessage());
        }

    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        try {
            URL bookURL = ApiUtil.buildURL(query);
            new BookQueryTask().execute(bookURL);
        }catch (Exception ex){
            Log.d("Error", ex.getMessage());
        }
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        return false;
    }

    public class BookQueryTask extends AsyncTask<URL,Void,String>{

        @Override
        protected String doInBackground(URL... urls) {
            URL searchURL = urls[0];
            String result = null;
            try {
                result = ApiUtil.getJson(searchURL);
            }catch (Exception ex){
                Log.d("Error",ex.getMessage());

            }
            return result;

        }

        @Override
        protected void onPostExecute(String result) {
            mLoadingProgress.setVisibility(View.INVISIBLE);
            TextView tvError = (TextView) findViewById(R.id.tvError);
            if (result == null){
                rvBooks.setVisibility(View.INVISIBLE);
                tvError.setVisibility(View.VISIBLE);
            }else {
                rvBooks .setVisibility(View.VISIBLE);
                tvError.setVisibility(View.INVISIBLE);
                ArrayList<Book> books = ApiUtil.getBooksfromJson(result);
                String resultString = "";
                BooksAdapter adapter = new BooksAdapter(books);
                rvBooks.setAdapter(adapter);
            }

        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mLoadingProgress.setVisibility(View.VISIBLE );
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.book_list_menu,menu);
        final MenuItem searchItem = menu.findItem(R.id.actoin_Search);
        final SearchView searchView = (SearchView) searchItem.getActionView();
        searchView.setOnQueryTextListener(this);
        ArrayList<String> recentList = SpUtil.getQueryList(getApplicationContext());
        int itemNum = recentList.size();
        MenuItem recentMenu;
        for(int i = 0; i <itemNum; i++ ){
            recentMenu = menu.add(Menu.NONE,i, Menu.NONE,recentList.get(i));

        }

        return true;
    }


    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.action_advanced_search:
                startActivity(new Intent(this, SearchActivity.class));
                return true;
            default:
                int position = item.getItemId() + 1;
                String preferenceName = SpUtil.QUERY + String.valueOf(position);
                String query = SpUtil.getPreferenceString(getApplicationContext(),preferenceName);
                String[] prefParams = query.split("\\,");
                String[] queryParams = new String[4];
                for(int i = 0; i < prefParams.length; i++){
                    queryParams[i] = prefParams[i];
                }
                URL bookUrl = ApiUtil.buildURL(
                        (queryParams[0] == null) ? "":queryParams[0],
                        (queryParams[1] == null) ? "":queryParams[1],
                        (queryParams[2] == null) ? "":queryParams[2],
                        (queryParams[3] == null) ? "":queryParams[3]
                );
                new BookQueryTask().execute(bookUrl);
                return super.onOptionsItemSelected(item);
        }

    }
}
