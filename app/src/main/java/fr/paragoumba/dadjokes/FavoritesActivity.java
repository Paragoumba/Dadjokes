package fr.paragoumba.dadjokes;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class FavoritesActivity extends AppCompatActivity {

    List<Joke> jokes;
    JokeAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState){

        Intent intent = getIntent();

        Bundle extra = intent.getExtras();

        if (extra != null){

            jokes = (List<Joke>) extra.get(MainActivity.FAVORITES);

        } else {

            jokes = new ArrayList<>();

        }

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorites);

        ActionBar actionBar = getSupportActionBar();

        if (actionBar != null){

            actionBar.setDisplayHomeAsUpEnabled(true);

        }

        adapter = new JokeAdapter(jokes);
        RecyclerView recyclerView = findViewById(R.id.jokeList);
        SwipeController swipeController = new SwipeController(adapter);
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(swipeController);

        itemTouchHelper.attachToRecyclerView(recyclerView);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

    }

    @Override
    public boolean onSupportNavigateUp(){

        finish();

        return true;

    }
}
