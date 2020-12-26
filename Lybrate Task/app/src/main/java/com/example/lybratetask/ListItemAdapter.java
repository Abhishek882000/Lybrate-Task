package com.example.lybratetask;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class ListItemAdapter extends RecyclerView.Adapter<ListItemAdapter.ViewHolder> implements Filterable {
    private ArrayList<ListItem> mListItems;
    private ArrayList<ListItem> mListItemsFull;
    private Context context;

    public ListItemAdapter(ArrayList<ListItem> mListItems, Context context) {
        this.mListItems = mListItems;
        this.context = context;
        this.mListItemsFull = new ArrayList<>(mListItems);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.example_item, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ListItem currentItems = mListItems.get(position);
        holder.mRestaurantName.setText(currentItems.getName());
        holder.mCuisinesName.setText(currentItems.getCuisines());
        holder.mAverageCost.setText(currentItems.getAverageCost());
        holder.mTimings.setText(currentItems.getTimings());
    }

    @Override
    public int getItemCount() {
        return mListItems.size();
    }

    @Override
    public Filter getFilter() {
        return exampleFilter;
    }

    private Filter exampleFilter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            List<ListItem> filteredList = new ArrayList<>();

            if (constraint == null || constraint.length() == 0) {
                filteredList.addAll(mListItemsFull);
            } else {
                String filterPattern = constraint.toString().toLowerCase().trim();

                for (ListItem item : mListItemsFull) {
                    if (item.getCuisines().toLowerCase().contains(filterPattern)) {
                        filteredList.add(item);
                    }
                }
            }
            FilterResults results = new FilterResults();
            results.values = filteredList;

            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            mListItems.clear();
            mListItems.addAll((List) results.values);
            notifyDataSetChanged();
        }
    };


    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView mRestaurantName;
        public TextView mCuisinesName;
        public TextView mAverageCost;
        public TextView mTimings;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            mRestaurantName = itemView.findViewById(R.id.textView_Restaurant_name);
            mCuisinesName = itemView.findViewById(R.id.textView_cuisines_name);
            mAverageCost = itemView.findViewById(R.id.textView_averageCost_name);
            mTimings = itemView.findViewById(R.id.textView_timings_name);
        }
    }
}
