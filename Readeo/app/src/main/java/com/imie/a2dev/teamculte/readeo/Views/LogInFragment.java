package com.imie.a2dev.teamculte.readeo.Views;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.imie.a2dev.teamculte.readeo.R;

/**
 * Fragment displaying the login screen, giving the ability for the user to perform usual log in actions.
 */
public class LogInFragment extends Fragment {
    /**
     * LogInFragment's default constructor.
     */
    public LogInFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_log_in, container, false);
    }
}
