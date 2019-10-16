package com.imie.a2dev.teamculte.readeo.Views;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;
import com.imie.a2dev.teamculte.readeo.DBManagers.UserDBManager;
import com.imie.a2dev.teamculte.readeo.Entities.DBEntities.PrivateUser;
import com.imie.a2dev.teamculte.readeo.R;
import com.imie.a2dev.teamculte.readeo.Utils.AppUtils;
import com.imie.a2dev.teamculte.readeo.Utils.HTTPRequestQueueSingleton;
import com.imie.a2dev.teamculte.readeo.Utils.PreferencesUtils;

/**
 * Fragment displaying the login screen, giving the ability for the user to perform usual log in actions.
 */
public final class LogInFragment extends Fragment implements View.OnClickListener, HTTPRequestQueueSingleton.HTTPRequestQueueListener {
    /**
     * Stores the email text edit.
     */
    private EditText email;

    /**
     * Stores the password text input.
     */
    private EditText password;

    /**
     * Stores the loading progress bar.
     */
    private ProgressBar progressLoading;

    /**
     * Defines if online or not.
     */
    private boolean offline = false;

    /**
     * LogInFragment's default constructor.
     */
    public LogInFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_log_in, container, false);

        this.init(view);
        this.email.setText("blabla@bla.fr");
        this.password.setText("DaDa12345");

        return view;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.txt_forgot:
                // TODO : The forgotten password event. 

                break;
            case R.id.txt_no_account:
                if (AppUtils.isOnline(this.getActivity())) {
                    FragmentManager fragmentManager = this.getActivity().getSupportFragmentManager();
                    FragmentTransaction transaction = fragmentManager.beginTransaction();

                    transaction.replace(R.id.content_fragment, new SignInFragment());
                    transaction.addToBackStack(null);
                    transaction.commit();
                } else {
                    Toast.makeText(this.getContext(), R.string.not_available_offline, Toast.LENGTH_SHORT).show();
                }

                break;
            case R.id.btn_sign_in:
                this.progressLoading.setVisibility(View.VISIBLE);

                if (this.validateData()) {
                    Intent intent = new Intent(this.getContext(), MainActivity.class);

                    this.getActivity().startActivity(intent);
                } else {
                    Toast.makeText(this.getContext(), R.string.login_error, Toast.LENGTH_SHORT).show();
                }

                break;
            default:

                break;
        }
    }

    /**
     * Initializes the fragment's view components.
     * @param view The fragment's view.
     */
    private void init(View view) {
        // TODO : See if getting the mail address and password from conf (auto complete fields).
        this.email = view.findViewById(R.id.edit_email);
        this.password = view.findViewById(R.id.edit_password);
        this.progressLoading = view.findViewById(R.id.progress_loading);

        view.findViewById(R.id.txt_forgot).setOnClickListener(this);
        view.findViewById(R.id.txt_no_account).setOnClickListener(this);
        view.findViewById(R.id.btn_sign_in).setOnClickListener(this);

        if (!AppUtils.isOnline(this.getActivity())) {
            this.offlineBehaviour();
        }

        PrivateUser internal = PreferencesUtils.loadUser();

        if (internal != null) {
            this.email.setText(internal.getEmail());
            this.password.setText(internal.getPassword());
        }
    }

    /**
     * Defines the behaviour if no network.
     */
    private void offlineBehaviour() {
        if (PreferencesUtils.loadUser() == null) {
            Toast.makeText(this.getContext(), R.string.offline_never_connected, Toast.LENGTH_LONG).show();
        } else {
            AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this.getContext());

            dialogBuilder.setMessage(R.string.offline_dialog_message);
            dialogBuilder.setPositiveButton(R.string.yes, (dialog, which) -> {
                this.offline = true;

                dialog.cancel();
            });
            dialogBuilder.setNegativeButton(R.string.no, ((dialog, which) -> {
                dialog.cancel();
                LogInFragment.this.getActivity().finish();
            }));

            dialogBuilder.show();
        }
    }

    /**
     * Checks the login info to connect the user.
     * @return True if success else false.
     */
    private boolean validateData() {
        boolean valid = false;

        if (this.offline) {
            PrivateUser internal = PreferencesUtils.loadUser();

            if (internal != null) {
                valid = true;
            }
        } else {
            PrivateUser loaded = new UserDBManager(this.getContext()).loadMySQL(this.email.getText().toString(),
                                                                                this.password.getText().toString(),
                                                                                null);

            if (loaded != null) {
                PreferencesUtils.saveUser(loaded);

                valid = true;
            }
        }

        return valid;
    }

    @Override
    public void onRequestsFinished() {
        this.progressLoading.setVisibility(View.GONE);
    }

    @Override public void onRequestFinished() {
        // Nothing to do.
    }

    @Override public void onRequestError() {
        // Nothing to do.
    }
}
