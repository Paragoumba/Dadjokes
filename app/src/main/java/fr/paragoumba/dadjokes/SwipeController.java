package fr.paragoumba.dadjokes;

import static androidx.recyclerview.widget.ItemTouchHelper.END;
import static androidx.recyclerview.widget.ItemTouchHelper.START;

import android.content.res.Resources;
import android.graphics.Canvas;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.core.content.res.ResourcesCompat;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

public class SwipeController extends ItemTouchHelper.Callback {

    private boolean swipeBack = false;
    private final ItemTouchHelperAdapter adapter;

    public SwipeController(ItemTouchHelperAdapter adapter){
        this.adapter = adapter;
    }

    @Override
    public int getMovementFlags(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder){
        return makeMovementFlags(0, START | END);
    }

    @Override
    public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target){
        return true;
    }

    @Override
    public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction){
        //adapter.onItemDismiss(viewHolder.getAdapterPosition());
        adapter.onItemDismiss(viewHolder.getAbsoluteAdapterPosition());
    }

    @Override
    public int convertToAbsoluteDirection(int flags, int layoutDirection){
        if (swipeBack){
            swipeBack = false;
            return 0;
        }

        return super.convertToAbsoluteDirection(flags, layoutDirection);
    }

    @Override
    public void onChildDraw(@NonNull Canvas canvas, @NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive){
        Resources resources = recyclerView.getResources();

        LinearLayout fgLayout = viewHolder.itemView.findViewById(R.id.rowFG);
        LinearLayout bgLayout = viewHolder.itemView.findViewById(R.id.rowBG);
        RecyclerView.ViewHolder newViewHolder = new RecyclerView.ViewHolder(fgLayout){};

        ImageView trashLeft = bgLayout.findViewById(R.id.img_delete_left);
        ImageView trashRight = bgLayout.findViewById(R.id.img_delete_right);

        if (dX > 0){
            trashLeft.setVisibility(View.VISIBLE);
            fgLayout.setBackground(ResourcesCompat.getDrawable(resources, R.drawable.rounded_layout_left, null));
            //fgLayout.setBackgroundColor(android.R.attr.colorBackground);
        } else if (dX < 0){
            trashRight.setVisibility(View.VISIBLE);
            fgLayout.setBackground(ResourcesCompat.getDrawable(resources, R.drawable.rounded_layout_right, null));
            //fgLayout.setBackgroundColor(android.R.attr.colorBackground);
        } else {
            trashLeft.setVisibility(View.INVISIBLE);
            trashRight.setVisibility(View.INVISIBLE);
            Log.d("DadJokes", "background");
        }

        super.onChildDraw(canvas, recyclerView, newViewHolder, dX, dY, actionState, isCurrentlyActive);
    }
}
