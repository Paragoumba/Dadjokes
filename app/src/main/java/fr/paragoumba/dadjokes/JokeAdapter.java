package fr.paragoumba.dadjokes;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.List;

public class JokeAdapter extends RecyclerView.Adapter<JokeAdapter.ViewHolder> implements ItemTouchHelperAdapter {

    private final List<Joke> favorites;

    JokeAdapter(List<Joke> favorites){

        super();

        this.favorites = favorites;

    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {

        View v;
        TextView jokeText;
        TextView titleText;
        TextView dateText;

        ViewHolder(View v){

            super(v);

            this.v = v;
            jokeText = v.findViewById(R.id.jokeText);
            dateText = v.findViewById(R.id.dateText);
            titleText = v.findViewById(R.id.titleText);

            v.setOnClickListener(this);
            v.setOnLongClickListener(this);

        }

        @Override
        public void onClick(View view){

            int position = getAdapterPosition();
            Joke joke = favorites.get(position);
            Context context = view.getContext();
            Intent intent = new Intent(context, MainActivity.class);

            intent.putExtra(MainActivity.JOKE, joke);
            intent.putExtra(MainActivity.FAVORITES, (Serializable) favorites);
            context.startActivity(intent);

        }

        @Override
        public boolean onLongClick(View v){

            return true;

        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType){

        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View contactView = inflater.inflate(R.layout.recycler_view_item, parent, false);

        return new ViewHolder(contactView);

    }

    @Override
    public void onBindViewHolder(@NonNull JokeAdapter.ViewHolder viewHolder, int position){

        Joke joke = favorites.get(position);
        Resources resources = viewHolder.v.getResources();
        ZonedDateTime zonedDateTime = joke.getDate();

        viewHolder.jokeText.setText(joke.getText());
        viewHolder.dateText.setText(String.format(resources.getString(R.string.date), zonedDateTime.getMonthValue(), zonedDateTime.getDayOfMonth(), zonedDateTime.getYear()));
        viewHolder.titleText.setText(String.format(resources.getString(R.string.dadjoke_nb), position + 1));

    }

    @Override
    public int getItemCount(){

        return favorites.size();

    }

    @Override
    public void onItemDismiss(int position){

        favorites.remove(position);
        notifyItemRemoved(position);

    }
}
