package com.usf_mobile_dev.filmfriend.ui.history;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.usf_mobile_dev.filmfriend.HistoryRecyclerViewAdapter;
import com.usf_mobile_dev.filmfriend.Movie_temp;
import com.usf_mobile_dev.filmfriend.R;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

public class HistoryFragment extends Fragment {

    private HistoryViewModel historyViewModel;
    private RecyclerView historyRecyclerView;
    private List<Movie_temp> listMovie;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        historyViewModel =
                new ViewModelProvider(this).get(HistoryViewModel.class);
        View root = inflater.inflate(R.layout.fragment_history, container, false);
        final TextView textView = root.findViewById(R.id.text_history);
        historyViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        });


        return root;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        listMovie = new ArrayList<>();
        listMovie.add(new Movie_temp("Shrek"));
        listMovie.add(new Movie_temp("Shrek 2"));
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        historyRecyclerView = (RecyclerView) view.findViewById(R.id.history_recyclerview);
        HistoryRecyclerViewAdapter adapter  = new HistoryRecyclerViewAdapter(getContext(), listMovie);
        historyRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        historyRecyclerView.setAdapter(adapter);
    }
}