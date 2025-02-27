package com.example.elementsfoodapp.ui.favorites;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.elementsfoodapp.R;
import com.example.elementsfoodapp.db.Food;

import org.jetbrains.annotations.NotNull;

import java.util.List;

/**This class binds the app data to the views (UI).*/
public class FavoritesListAdapter extends RecyclerView.Adapter<FavoritesListAdapter.FoodViewHolder> {

    private final LayoutInflater mInflater;
    private List<Food> mFoods; // Cached copy of foods
    private OnItemClickListener listener;
    private OnItemLongClickListener longClickListener;
    private int mSelectedPos = RecyclerView.NO_POSITION;

    FavoritesListAdapter(Context context) {
        mInflater = LayoutInflater.from(context);
    }

    @NotNull
    @Override
    public FoodViewHolder onCreateViewHolder(@NotNull ViewGroup parent, int viewType) {
        View itemView = mInflater.inflate(R.layout.item_food, parent, false);
        return new FoodViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NotNull FoodViewHolder holder, int position) {
        if (mFoods != null) {
            Food current = mFoods.get(position);
            holder.foodItemView.setText(current.getFood());
            holder.itemView.setSelected(mSelectedPos == position);
        } else {
            // Covers the case of data not being ready yet.
            holder.foodItemView.setText("Keine Lebensmittel");
        }
    }

    void setFoods(List<Food> foods) {
        mFoods = foods;
        notifyDataSetChanged();
    }

    // getItemCount() is called many times, and when it is first called,
    // mFoods has not been updated (means initially, it's null, and we can't return null).
    @Override
    public int getItemCount() {
        if (mFoods != null)
            return mFoods.size();
        else return 0;
    }

    /*public Food getFoodAtPosition(int position) {
        return mFoods.get(position);
    }*/

    class FoodViewHolder extends RecyclerView.ViewHolder {

        private final TextView foodItemView;

        private FoodViewHolder(View itemView) {
            super(itemView);
            foodItemView = itemView.findViewById(R.id.foodTextView);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    if (listener != null && position != RecyclerView.NO_POSITION) {
                        listener.onItemClick(mFoods.get(position));
                    }
                }
            });

            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    int position = getAdapterPosition();
                    if (longClickListener != null && position != RecyclerView.NO_POSITION) {
                        longClickListener.onItemLongClick(mFoods.get(position));
                    }
                    notifyItemChanged(mSelectedPos);
                    mSelectedPos = getLayoutPosition();
                    notifyItemChanged(mSelectedPos);
                    return true;
                }
            });
        }
    }

    public interface OnItemClickListener {
        void onItemClick(Food food);
    }

    public interface OnItemLongClickListener {
        void onItemLongClick(Food food);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    public void setOnItemLongClickListener(OnItemLongClickListener longClickListener) {
        this.longClickListener = longClickListener;
    }

    public void setSelectedPos(int SelectedPos) {
        mSelectedPos = SelectedPos;
        notifyDataSetChanged();
    }
}
