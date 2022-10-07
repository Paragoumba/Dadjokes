package fr.paragoumba.dadjokes;

import androidx.annotation.Nullable;

import java.io.Serializable;
import java.time.ZonedDateTime;

class Joke implements Serializable {

    public Joke(String id, String joke, int status, ZonedDateTime date){

        this.id = id;
        this.joke = joke;
        this.status = status;
        this.date = date;

    }

    private String id;
    private String joke;
    private int status;
    private ZonedDateTime date;


    public String getId(){

        return id;

    }

    public String getText(){

        return joke;

    }

    public int getStatus(){

        return status;

    }

    public ZonedDateTime getDate(){

        return date;

    }

    @Override
    public boolean equals(@Nullable Object obj){

        if (obj instanceof Joke){

            return ((Joke) obj).id.equals(id);

        }

        return false;

    }
}
