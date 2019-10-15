package com.imie.a2dev.teamculte.readeo.Views;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.imie.a2dev.teamculte.readeo.DBSchemas.UserDBSchema;
import com.imie.a2dev.teamculte.readeo.R;
import com.imie.a2dev.teamculte.readeo.Utils.HTTPRequestQueueSingleton;
import com.imie.a2dev.teamculte.readeo.Utils.ManagerHolderUtils;
import com.imie.a2dev.teamculte.readeo.Utils.PreferencesUtils;

/**
 * Fragment displaying the change password dialog.
 */
public final class ChangePasswordDialogFragment extends DialogFragment
        implements View.OnClickListener,
                   HTTPRequestQueueSingleton.HTTPRequestQueueListener {
    /**
     * Stores the old password text edit.
     */
    private EditText editTxtOldPassword;

    /**
     * Stores the new password text edit.
     */
    private EditText editTxtNewPassword;

    /**
     * Stores the confirmation text edit.
     */
    private EditText editTxtConfirmPassword;

    /**
     * Stores the error text field.
     */
    private TextView txtError;

    /**
     * Stores the progress loading bar.
     */
    private ProgressBar progressLoading;

    /**
     * ChangePasswordDialogFragment's default constructor.
     */
    public ChangePasswordDialogFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.dialog_fragment_change_password, container, false);

        this.init(view);

        return view;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        this.setStyle(DialogFragment.STYLE_NORMAL, R.style.FullScreenDialogStyle);

        super.onCreate(savedInstanceState);
    }

    @Override
    public void onStart() {
        super.onStart();

        Dialog dialog = this.getDialog();

        if (dialog != null) {
            int width = ViewGroup.LayoutParams.MATCH_PARENT;
            int height = ViewGroup.LayoutParams.MATCH_PARENT;

            dialog.getWindow().getAttributes().gravity = Gravity.CENTER;
            dialog.getWindow().setLayout(width, height);
        }
    }

    @Override public void onRequestsFinished() {
        // Nothing to do.
    }

    @Override public void onRequestFinished() {
        ManagerHolderUtils.getInstance().getUserDBManager()
                          .updateFieldMySQL(PreferencesUtils.loadUser().getId(),
                                            UserDBSchema.PASSWORD,
                                            this.editTxtNewPassword.getText().toString());

        this.dismiss();
    }

    @Override public void onRequestError() {
        this.getActivity().runOnUiThread(() -> {
            ChangePasswordDialogFragment.this.txtError.setVisibility(View.VISIBLE);
            ChangePasswordDialogFragment.this.progressLoading.setVisibility(View.GONE);
        });
    }

    @Override
    public void onClick(View view) {
        this.txtError.setVisibility(View.GONE);
        
        if (!this.editTxtNewPassword.getText().toString()
                                    .equals(this.editTxtConfirmPassword.getText().toString())) {
            Toast.makeText(this.getContext(), R.string.password_confirmation_doesnt_match,
                           Toast.LENGTH_SHORT).show();
        } else {
            this.progressLoading.setVisibility(View.VISIBLE);

            ManagerHolderUtils.getInstance().getUserDBManager()
                              .loadMySQL(PreferencesUtils.loadUser().getEmail(),
                                         this.editTxtOldPassword
                                                 .getText()
                                                 .toString(), this);
        }
    }


    /**
     * Initializes the fragment's view components.
     * @param view The fragment's view.
     */
    private void init(View view) {
        this.editTxtOldPassword = view.findViewById(R.id.edit_txt_old_password);
        this.editTxtNewPassword = view.findViewById(R.id.edit_txt_new_password);
        this.editTxtConfirmPassword = view.findViewById(R.id.edit_txt_confirm_password);
        this.txtError = view.findViewById(R.id.txt_error);
        this.progressLoading = view.findViewById(R.id.progress_loading);

        view.findViewById(R.id.btn_update).setOnClickListener(this);
    }

}
