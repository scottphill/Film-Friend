package com.usf_mobile_dev.filmfriend;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.usf_mobile_dev.filmfriend.ui.match.MatchPreferences;
import com.usf_mobile_dev.filmfriend.ui.qr.QRGenerateActivity;
import com.usf_mobile_dev.filmfriend.ui.qr.QrActivity;

import java.util.List;

import static com.usf_mobile_dev.filmfriend.ui.qr.QrActivity.QR_REQUEST;

public class PreferenceRecyclerViewAdapter extends RecyclerView.Adapter
        <PreferenceRecyclerViewAdapter.PreferenceViewHolder>{

    Context mContext;
    List<MatchPreferences> mMatchPreferences;
    View.OnClickListener openOnClickListener;
    View.OnClickListener deleteOnClickListener;

    public PreferenceRecyclerViewAdapter(Context mContext,
                                         View.OnClickListener openOnClickListener,
                                         View.OnClickListener deleteOnClickListener
    ) {
        this.mContext = mContext;
        this.openOnClickListener = openOnClickListener;
        this.deleteOnClickListener = deleteOnClickListener;
    }

    // Changes the list of MatchPreferences objects and updates the recyclerview
    public void setmMatchPreferences(List<MatchPreferences> matchPreferences) {
        this.mMatchPreferences = matchPreferences;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public PreferenceViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View v;
        v = LayoutInflater.from(mContext).inflate(R.layout.item_saved_preference, parent, false);
        PreferenceViewHolder vHolder = new PreferenceViewHolder(v);

        return vHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull PreferenceViewHolder holder, int position) {
        if (mMatchPreferences != null) {
            MatchPreferences current = mMatchPreferences.get(position);
            holder.preferenceItemView.setText(current.getPreference_title());

            holder.itemView.findViewById(R.id.preference_open_btn).setTag(current);
            holder.itemView.findViewById(R.id.preference_open_btn)
                    .setOnClickListener(openOnClickListener);

            holder.itemView.findViewById(R.id.preference_qr_gen_btn).setOnClickListener(
                    new View.OnClickListener(){
                        @Override
                        public void onClick(View view) {
                            Intent intent_qr = new Intent(mContext, QRGenerateActivity.class);
                            // Pass MoviePreferences object to intent
                            intent_qr.putExtra(
                                    QRGenerateActivity.INTENT_EXTRAS_MATCH_PREFERENCES,
                                    mMatchPreferences.get(position)
                            );
                            mContext.startActivity(intent_qr);
                        }
                    });

            holder.itemView.findViewById(R.id.preference_delete_btn).setTag(current);
            holder.itemView.findViewById(R.id.preference_delete_btn)
                    .setOnClickListener(deleteOnClickListener);
        } else {
            // Covers the case of data not being ready yet.
            holder.preferenceItemView.setText("No Preferences atm");
        }
    }

    @Override
    public int getItemCount() {
        if (mMatchPreferences != null)
            return mMatchPreferences.size();
        else return 0;
    }

    public static class PreferenceViewHolder extends RecyclerView.ViewHolder {

        private TextView preferenceItemView;

        public PreferenceViewHolder(View itemView) {
            super(itemView);

            preferenceItemView = (TextView) itemView.findViewById((R.id.preference_name));
        }
    }
}
