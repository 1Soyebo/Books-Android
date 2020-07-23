package com.example.books;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.net.URL;

public class SearchActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        final EditText etTitle = (EditText) findViewById(R.id.etTitle);
        final EditText etAuthor = (EditText) findViewById(R.id.etAuthor);
        final EditText etPublisher = (EditText) findViewById(R.id.etPublisher);
        final EditText etISBN = (EditText) findViewById(R.id.etISBN);
        Button btnSearch = (Button) findViewById(R.id.btnSearch);
        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String title = etTitle.getText().toString().trim();
                String author = etAuthor.getText().toString().trim();
                String publisher = etPublisher.getText().toString().trim();
                String isbn = etISBN.getText().toString().trim();

                if(title.isEmpty() && author.isEmpty() && publisher.isEmpty() && isbn.isEmpty()){
                    String message = getString(R.string.no_search_data);
                    Toast.makeText(getApplicationContext(),message,Toast.LENGTH_SHORT).show();

                }else
                {
                    ApiUtil.SEARCH_ACTIVTY_URL = ApiUtil.buildURL(title,author,publisher,isbn);
                    Context context = getApplicationContext();
                    int position = SpUtil.getPreferenceINt(context, SpUtil.POSITION);
                    if (position == 0 || position == 5){
                        position = 1;
                    }else {
                        position++;
                    }
                    String key = SpUtil.QUERY + String.valueOf(position);
                    String value = title + "," + author + "," + publisher + "," + isbn;
                    SpUtil.setPreferenceString(context,key,value);
                    SpUtil.setPreferenceInt(context,SpUtil.POSITION,position);

                    Intent intent =  new Intent(getApplicationContext(), BookListActivity.class);
                    //intent.putExtra("Query",queryUrl);
                    startActivity(intent);
                }


            }
        });



    }
}
