package fr.paragoumba.dadjokes;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.Serializable;
import java.net.URL;
import java.net.URLConnection;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Scanner;

public class MainActivity extends AppCompatActivity {

    public static final String JOKE = "joke_info";
    public static final String FAVORITES = "favorites";

    private Joke joke;
    private List<Joke> favorites;


    @Override
    protected void onCreate(Bundle savedInstanceState){

        super.onCreate(savedInstanceState);

        Intent intent = getIntent();
        Bundle extra = intent.getExtras();

        if (extra != null){

            joke = (Joke) extra.get(JOKE);
            favorites = (List<Joke>) extra.get(FAVORITES);

        }

        if (favorites == null){

            favorites = Saver.loadJokes(getApplicationContext());

        }

        setContentView(R.layout.activity_main);

        Button refreshButton = findViewById(R.id.refreshButton);

        if (joke == null){

            randomJoke(refreshButton);

        }

        loadJoke(joke);


        RotateAnimation rotate = new RotateAnimation(
                0, 360,
                Animation.RELATIVE_TO_SELF, 0.5f,
                Animation.RELATIVE_TO_SELF, 0.5f);
        rotate.setDuration(1000);

        refreshButton.setOnClickListener(v -> {

            refreshButton.startAnimation(rotate);
            randomJoke(v);

        });
    }

    public void setFavorite(View view){

        if (joke != null){

            if (favorites.contains(joke)){

                favorites.remove(joke);

            } else {

                favorites.add(joke);

            }

            setFavoriteButtonMode(joke);
            Saver.saveJokes(favorites, getApplicationContext());

        } else {

            Button favoriteButton = findViewById(R.id.favoriteButton);

            favoriteButton.setEnabled(false);

        }
    }

    public void loadJoke(Joke joke){

        TextView textView = findViewById(R.id.jokeView);

        runOnUiThread(() -> {

            textView.setText(joke != null ? joke.getText() : "No joke found.\n(This is not a joke)");
            setFavoriteButtonMode(joke);

        });
    }

    public void randomJoke(View view){

        new Thread(() -> {

            try {

                URL url = new URL("https://icanhazdadjoke.com/");
                StringBuilder builder = new StringBuilder();
                URLConnection connection = url.openConnection();

                connection.setRequestProperty("User-Agent", "DadJokes Android App");
                connection.setRequestProperty("Accept", "application/json");

                try(Scanner scanner = new Scanner(connection.getInputStream())){

                    while (scanner.hasNext()){

                        builder.append(scanner.nextLine()).append('\n');

                    }
                }

                JSONObject json = new JSONObject(builder.toString());

                joke = new Joke(
                        json.getString("id"),
                        json.getString("joke"),
                        json.getInt("status"),
                        ZonedDateTime.now());

            } catch (IOException | JSONException e){

                if (view != null){

                    runOnUiThread(() -> {

                        Toast toast = Toast.makeText(view.getContext(),
                                "Could not connect to the Internet.", Toast.LENGTH_LONG);
                        toast.show();

                    });

                } else {

                    Log.d("DadJokes", "view null, could not toast");

                }

                return;

            }

            loadJoke(joke);

        }).start();

    }

    public void displayList(View view){

        Intent intent = new Intent(MainActivity.this, FavoritesActivity.class);

        intent.putExtra(FAVORITES, (Serializable) favorites);
        startActivity(intent);

    }

    private void setFavoriteButtonMode(Joke joke){

        Button favoriteButton = findViewById(R.id.favoriteButton);

        favoriteButton.setEnabled(true);
        favoriteButton.setBackgroundResource(
                favorites.contains(joke) ?
                        R.drawable.ic_favorite_selected :
                        R.drawable.ic_favorite);

    }
}
