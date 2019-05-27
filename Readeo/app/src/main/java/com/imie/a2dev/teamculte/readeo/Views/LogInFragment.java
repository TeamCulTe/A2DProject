package com.imie.a2dev.teamculte.readeo.Views;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.EditText;
import com.imie.a2dev.teamculte.readeo.DBManagers.UserDBManager;
import com.imie.a2dev.teamculte.readeo.Entities.DBEntities.PrivateUser;
import com.imie.a2dev.teamculte.readeo.R;

/**
 * Fragment displaying the login screen, giving the ability for the user to perform usual log in actions.
 */
public class LogInFragment extends Fragment implements View.OnClickListener {
    /**
     * Stores the email text edit.
     */
    private EditText email;

    /**
     * Stores the password text input.
     */
    private EditText password;
    
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
        
        return view;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.txt_forgot:
                // TODO : The forgotten password event. 
                
                break;
            case R.id.txt_no_account:
                FragmentManager fragmentManager = this.getActivity().getSupportFragmentManager();
                FragmentTransaction transaction = fragmentManager.beginTransaction();
                
                transaction.replace(R.id.content_fragment, new SignInFragment());
                transaction.commit();
                
                break;
            case R.id.btn_sign_in:
                if (this.validateData()) {
                    Intent intent = new Intent(this.getContext(), MainActivity.class);

                    this.getActivity().startActivity(intent);
                }
                
                break;
            default:
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
        
        view.findViewById(R.id.txt_forgot).setOnClickListener(this);
        view.findViewById(R.id.txt_no_account).setOnClickListener(this);
        view.findViewById(R.id.btn_sign_in).setOnClickListener(this);
    }

    /**
     * Checks the login info to connect the user.
     * @return True if success else false.
     */
    private boolean validateData() {
        PrivateUser loaded = new UserDBManager(this.getContext()).loadMySQL(this.email.getText().toString(),
                this.password.getText().toString());
        
        if (loaded != null) {
            
            return true;
        }
        
        return false;
    }
}
