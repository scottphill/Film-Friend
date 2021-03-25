package com.usf_mobile_dev.filmfriend;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.RadioButton;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.usf_mobile_dev.filmfriend.api.LanguageResponse;
import java.util.List;


public class LanguagesGridAdapter
        extends RecyclerView.Adapter<LanguagesGridAdapter.ViewHolder> {

    private List<LanguageResponse> languages;
    private String selectedLanguage;
    private CompoundButton.OnCheckedChangeListener checkedChangeListener;

    public LanguagesGridAdapter(
            CompoundButton.OnCheckedChangeListener checkedChangeListener
    ) {
        this.checkedChangeListener = checkedChangeListener;
    }

    // Updates the languages to a new list
    public void setLanguages(List<LanguageResponse> languages) {
        this.languages = languages;
        notifyDataSetChanged();
    }

    public void setSelectedLanguage(String selectedLanguage) {
        this.selectedLanguage = selectedLanguage;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public LanguagesGridAdapter.ViewHolder onCreateViewHolder(
            @NonNull ViewGroup parent,
            int viewType
    ) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_language_radio_button, parent, false);

        return new LanguagesGridAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(
            @NonNull LanguagesGridAdapter.ViewHolder holder,
            int position
    ) {
        if(selectedLanguage.equals(languages.get(position).english_name))
            holder.getRadioButton().setChecked(true);
        else
            holder.getRadioButton().setChecked(false);
        holder.getRadioButton().setText(languages.get(position).english_name);
        holder.getRadioButton().setOnCheckedChangeListener(this.checkedChangeListener);
    }

    @Override
    public int getItemCount() {
        return languages.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{
        private final RadioButton radioButton;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            this.radioButton = (RadioButton)itemView.findViewById(R.id.language_radio_button);
        }

        public RadioButton getRadioButton() {
            return this.radioButton;
        }
    }
}
