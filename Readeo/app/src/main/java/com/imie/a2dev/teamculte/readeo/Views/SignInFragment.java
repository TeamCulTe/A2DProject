package com.imie.a2dev.teamculte.readeo.Views;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import com.imie.a2dev.teamculte.readeo.Views.CountrySpinnerAdapter;
import com.imie.a2dev.teamculte.readeo.DBManagers.CityDBManager;
import com.imie.a2dev.teamculte.readeo.DBManagers.CountryDBManager;
import com.imie.a2dev.teamculte.readeo.DBManagers.ProfileDBManager;
import com.imie.a2dev.teamculte.readeo.DBManagers.UserDBManager;
import com.imie.a2dev.teamculte.readeo.Entities.DBEntities.City;
import com.imie.a2dev.teamculte.readeo.Entities.DBEntities.Country;
import com.imie.a2dev.teamculte.readeo.Entities.DBEntities.PrivateUser;
import com.imie.a2dev.teamculte.readeo.Entities.DBEntities.Profile;
import com.imie.a2dev.teamculte.readeo.R;
import com.imie.a2dev.teamculte.readeo.Utils.Enums.InputError;
import com.imie.a2dev.teamculte.readeo.Utils.InputUtils;

/**
 * Fragment giving the ability to create a new account (by proving the needed info).
 */
public class SignInFragment extends Fragment implements View.OnClickListener {
    /**
     * Stores the pseudo edit field.
     */
    private EditText editPseudo;

    /**
     * Stores the email edit field.
     */
    private EditText editEmail;

    /**
     * Stores the password edit field.
     */
    private EditText editPassword;

    /**
     * Stores the password confirmation edit field.
     */
    private EditText editConfirm;

    /**
     * Stores the city edit field.
     */
    private EditText editCity;

    /**
     * Stores the country spinner.
     */
    private Spinner spinnerCountry;

    /**
     * Stores the pseudo error message.
     */
    private TextView pseudoErrorMessage;

    /**
     * Stores the email error message.
     */
    private TextView emailErrorMessage;

    /**
     * Stores the city error message.
     */
    private TextView cityErrorMessage;
    
    /**
     * Stores the password error message.
     */
    private TextView passwordErrorMessage;

    /**
     * Stores the confirm password error message.
     */
    private TextView confirmErrorMessage;

    /**
     * Stores the spinner's adapter.
     */
    private CountrySpinnerAdapter adapter;
    
    /**
     * SignInFragment's default constructor.
     */
    public SignInFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_sign_in, container, false);
        
        this.init(view);
        
        return view;
    }

    @Override
    public void onClick(View view) {
        if (this.checkInputs()) {
            this.saveData();
            
            FragmentManager fragmentManager = this.getActivity().getSupportFragmentManager();
            FragmentTransaction transaction = fragmentManager.beginTransaction();
            
            transaction.replace(R.id.content_fragment, new LogInFragment());
            transaction.commit();
        }
    }

    /**
     * Initializes the fragment's view components.
     * @param view The fragment's view.
     */
    private void init(View view) {
        this.editPseudo = view.findViewById(R.id.edit_pseudo);
        this.editEmail = view.findViewById(R.id.edit_email);
        this.editPassword = view.findViewById(R.id.edit_password);
        this.editConfirm = view.findViewById(R.id.edit_confirm);
        this.editCity = view.findViewById(R.id.edit_city);
        this.spinnerCountry = view.findViewById(R.id.spinner_country);
        this.pseudoErrorMessage = view.findViewById(R.id.txt_pseudo_error_message);
        this.emailErrorMessage = view.findViewById(R.id.txt_email_error_message);
        this.cityErrorMessage = view.findViewById(R.id.txt_city_error_message);
        this.passwordErrorMessage = view.findViewById(R.id.txt_password_error_message);
        this.confirmErrorMessage = view.findViewById(R.id.txt_confirm_error_message);
        this.adapter = new CountrySpinnerAdapter(this.getContext(),
                new CountryDBManager(this.getContext()).queryAllSQLite());
        
        this.spinnerCountry.setAdapter(this.adapter);
        
        view.findViewById(R.id.btn_sign_in).setOnClickListener(this);
        
        this.editPseudo.setText("DADADA");
        this.editEmail.setText("blabla@bla.fr");
        this.editPassword.setText("Frk7xet3g5pny");
        this.editConfirm.setText("Frk7xet3g5pny");
        this.editCity.setText("seetee");
    }

    /**
     * Hide all the error messages.
     */
    private void hideErrorMessages() {
        if (this.pseudoErrorMessage.getVisibility() == View.VISIBLE) {
            this.pseudoErrorMessage.setVisibility(View.GONE);
        }

        if (this.emailErrorMessage.getVisibility() == View.VISIBLE) {
            this.emailErrorMessage.setVisibility(View.GONE);
        }

        if (this.cityErrorMessage.getVisibility() == View.VISIBLE) {
            this.cityErrorMessage.setVisibility(View.GONE);
        }

        if (this.passwordErrorMessage.getVisibility() == View.VISIBLE) {
            this.passwordErrorMessage.setVisibility(View.GONE);
        }

        if (this.confirmErrorMessage.getVisibility() == View.VISIBLE) {
            this.confirmErrorMessage.setVisibility(View.GONE);
        }
    }

    /**
     * Checks if the input values are correct (email format, password...) and displays the error messages if needed.
     * @return True if the inputs are valid else false.
     */
    private boolean checkInputs() {
        boolean valid = true;
        InputError error;
        
        this.hideErrorMessages();
        
        error = InputUtils.validatePseudo(this.editPseudo.getText().toString());
        
        if (error != InputError.NO_ERROR) {
            this.pseudoErrorMessage.setText(error.getMessage());
            this.pseudoErrorMessage.setVisibility(View.VISIBLE);
            
            valid = false;
        }
        
        error = InputUtils.validateEmail(this.editEmail.getText().toString());

        if (error != InputError.NO_ERROR) {
            this.emailErrorMessage.setText(error.getMessage());
            this.emailErrorMessage.setVisibility(View.VISIBLE);

            valid = false;
        }

        error = InputUtils.validateCity(this.editCity.getText().toString());

        if (error != InputError.NO_ERROR) {
            this.cityErrorMessage.setText(error.getMessage());
            this.cityErrorMessage.setVisibility(View.VISIBLE);

            valid = false;
        }
        
        error = InputUtils.validatePassword(this.editPassword.getText().toString(),
                this.editConfirm.getText().toString());

        if (error != InputError.NO_ERROR) {
            if (error == InputError.PASSWORD_FORMAT) {
                this.passwordErrorMessage.setText(error.getMessage());
                this.passwordErrorMessage.setVisibility(View.VISIBLE);
            } else if (error == InputError.PASSWORD_MATCH) {
                this.confirmErrorMessage.setText(error.getMessage());
                this.confirmErrorMessage.setVisibility(View.VISIBLE);
            }
            
            valid = false;
        }
        
        return valid;
    }

    /**
     * Save the newly registered user in the database.
     */
    private void saveData() {
        CityDBManager cityDBManager = new CityDBManager(this.getContext());
        String pseudo = this.editPseudo.getText().toString();
        String email = this.editEmail.getText().toString();
        String password = this.editPassword.getText().toString();
        String cityName = this.editCity.getText().toString();
        Profile profile = new Profile();
        City city = cityDBManager.loadMySQL(cityName);
        Country country = new CountryDBManager(this.getContext()).loadMySQL(
                        this.adapter.getItem(this.spinnerCountry.getSelectedItemPosition()).getName());
        
        if (city == null) {
            city = new City(cityName);
            
            cityDBManager.createMySQL(city);
        }
        
        new ProfileDBManager(this.getContext()).createMySQL(profile);
        
        PrivateUser usr = new PrivateUser(pseudo, password, email, profile, country, city, null, null);
        
        new UserDBManager(this.getContext()).createMySQL(usr);
    }
}
