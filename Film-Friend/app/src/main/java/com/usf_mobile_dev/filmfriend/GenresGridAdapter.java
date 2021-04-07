package com.usf_mobile_dev.filmfriend;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.usf_mobile_dev.filmfriend.api.GenreResponse;
import java.util.List;

public class GenresGridAdapter
        extends RecyclerView.Adapter<GenresGridAdapter.ViewHolder> {

    private List<GenreResponse.Genre> genres;
    private CompoundButton.OnCheckedChangeListener checkedChangeListener;

    public GenresGridAdapter(
            CompoundButton.OnCheckedChangeListener checkedChangeListener
    ) {
        this.checkedChangeListener = checkedChangeListener;
    }

    // Updates the genres to a new list
    public void setGenres(List<GenreResponse.Genre> genres) {
        this.genres = genres;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public GenresGridAdapter.ViewHolder onCreateViewHolder(
            @NonNull ViewGroup parent,
            int viewType
    ) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_genre_checkbox, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(
            @NonNull GenresGridAdapter.ViewHolder holder,
            int position
    ) {
        holder.getCheckBox().setText(genres.get(position).name);
        holder.getCheckBox().setChecked(false);
        holder.getCheckBox().setOnCheckedChangeListener(this.checkedChangeListener);
    }

    @Override
    public int getItemCount() {
        if(genres != null)
            return genres.size();
        else
            return 0;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{
        private final CheckBox checkBox;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            this.checkBox = (CheckBox)itemView.findViewById(R.id.checkbox_genre);
        }

        public CheckBox getCheckBox() {
            return this.checkBox;
        }
    }
}
