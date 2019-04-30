package com.imie.a2dev.teamculte.readeo.Views;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.imie.a2dev.teamculte.readeo.R;

/**
 * Fragment displaying a specific book info and giving the ability to add it into the user book lists.
 */
public class BookFragment extends Fragment {
    /**
     * BookFragment's default constructor.
     */
    public BookFragment() {
        // Required empty public constructor
    }
    
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_book, container, false);
    }

}
