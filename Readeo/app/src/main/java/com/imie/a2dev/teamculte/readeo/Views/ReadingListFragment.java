package com.imie.a2dev.teamculte.readeo.Views;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.imie.a2dev.teamculte.readeo.R;

/**
 * Fragment displaying the list of books from a user reading list.
 */
public class ReadingListFragment extends Fragment {
    /**
     * ReadingListFragment's default constructor.
     */
    public ReadingListFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_reading_list, container, false);
    }
}
