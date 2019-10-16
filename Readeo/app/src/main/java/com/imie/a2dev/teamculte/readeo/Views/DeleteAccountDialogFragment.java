package com.imie.a2dev.teamculte.readeo.Views;

import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.imie.a2dev.teamculte.readeo.Entities.DBEntities.Book;
import com.imie.a2dev.teamculte.readeo.Entities.DBEntities.BookList;
import com.imie.a2dev.teamculte.readeo.Entities.DBEntities.PrivateUser;
import com.imie.a2dev.teamculte.readeo.R;
import com.imie.a2dev.teamculte.readeo.Utils.HTTPRequestQueueSingleton;
import com.imie.a2dev.teamculte.readeo.Utils.ManagerHolderUtils;
import com.imie.a2dev.teamculte.readeo.Utils.PreferencesUtils;

/**
 * Fragment displaying the delete account dialog.
 */
public final class DeleteAccountDialogFragment extends DialogFragment
        implements View.OnClickListener,
                   HTTPRequestQueueSingleton.HTTPRequestQueueListener {
    /**
     * Stores the password text edit.
     */
    private EditText editTxtPassword;

    /**
     * Stores the progress loading bar.
     */
    private ProgressBar progressLoading;

    /**
     * DeleteAccountDialogFragment's default constructor.
     */
    public DeleteAccountDialogFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.dialog_fragment_delete_account, container, false);

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
        this.deleteUser();
        this.getActivity().finishAffinity();
    }

    @Override public void onRequestError() {
        this.getActivity().runOnUiThread(() -> {
            Toast.makeText(this.getContext(), R.string.login_error, Toast.LENGTH_SHORT).show();
            DeleteAccountDialogFragment.this.progressLoading.setVisibility(View.GONE);
        });
    }

    @Override
    public void onClick(View view) {
        if (this.editTxtPassword.getText().toString().isEmpty()) {
            Toast.makeText(this.getContext(), R.string.need_password_for_account_deletion,
                           Toast.LENGTH_SHORT).show();
        } else {
            this.progressLoading.setVisibility(View.VISIBLE);

            ManagerHolderUtils.getInstance().getUserDBManager()
                              .loadMySQL(PreferencesUtils.loadUser().getEmail(),
                                         this.editTxtPassword
                                                 .getText()
                                                 .toString(), this);
        }
    }

    /**
     * Initializes the fragment's view components.
     * @param view The fragment's view.
     */
    private void init(View view) {
        this.editTxtPassword = view.findViewById(R.id.edit_txt_password);
        this.progressLoading = view.findViewById(R.id.progress_loading);

        view.findViewById(R.id.btn_delete).setOnClickListener(this);
    }

    /**
     * Deletes the current user and associated data.
     */
    private void deleteUser() {
        PrivateUser user = PreferencesUtils.loadUser();

        user.setPassword(this.editTxtPassword.getText().toString());

        ManagerHolderUtils.getInstance().getBookListDBManager().deleteUserMySQL(user.getId());

        for (BookList bookList : user.getBookLists().values()) {
            for (Book book : bookList.getBooks()) {
                ManagerHolderUtils.getInstance().getBookListDBManager().deleteSQLite(user.getId(),
                                                                                     bookList.getType()
                                                                                             .getId(),
                                                                                     book.getId());
            }
        }

        ManagerHolderUtils.getInstance().getQuoteDBManager().deleteUserMySQL(user.getId());
        ManagerHolderUtils.getInstance().getReviewDBManager().deleteUserMySQL(user.getId());
        
        ManagerHolderUtils.getInstance().getQuoteDBManager().deleteUserSQLite(user.getId());
        ManagerHolderUtils.getInstance().getReviewDBManager().deleteUserSQLite(user.getId());

        ManagerHolderUtils.getInstance().getUserDBManager().deleteMySQL(user);
        ManagerHolderUtils.getInstance().getUserDBManager().deleteSQLite(user.getId());
        
        ManagerHolderUtils.getInstance().getProfileDBManager().deleteMySQL(user.getProfile().getId());
        ManagerHolderUtils.getInstance().getProfileDBManager().deleteSQLite(user.getProfile().getId());
    }

}
