package fr.paragoumba.dadjokes;

import android.content.Context;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InvalidClassException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;

class Saver {

    private static final String fileName = "jokes.bin";

    static List<Joke> loadJokes(Context context){

        List<Joke> favorites = new ArrayList<>();
        File jokesFile = new File(context.getFilesDir(), fileName);

        try(ObjectInputStream in = new ObjectInputStream(new FileInputStream(jokesFile))){

            Object object = in.readObject();

            if (object instanceof List){

                favorites = (List<Joke>) object;

            }

        } catch (FileNotFoundException | InvalidClassException e){

            favorites.clear();

        } catch (IOException | ClassNotFoundException e){

            e.printStackTrace();

        }

        return favorites;

    }

    static void saveJokes(List<Joke> favorites, Context context){

        File jokesFile = new File(context.getFilesDir(), fileName);

        try(ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(jokesFile))){

            out.writeObject(favorites);

        } catch (IOException e){

            e.printStackTrace();

        }
    }
}
