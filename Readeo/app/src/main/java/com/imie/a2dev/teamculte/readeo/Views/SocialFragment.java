package com.imie.a2dev.teamculte.readeo.Views;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.imie.a2dev.teamculte.readeo.R;

/**
 * Fragment containing the social functionality.
 */
public class SocialFragment extends Fragment {
    /**
     * SocialFragment's default constructor.
     */
    public SocialFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_social, container, false);
    }
}
