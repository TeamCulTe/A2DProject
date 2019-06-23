package com.imie.a2dev.teamculte.readeo.Views;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;
import com.imie.a2dev.teamculte.readeo.DBManagers.CityDBManager;
import com.imie.a2dev.teamculte.readeo.DBManagers.CountryDBManager;
import com.imie.a2dev.teamculte.readeo.DBManagers.ProfileDBManager;
import com.imie.a2dev.teamculte.readeo.DBManagers.UserDBManager;
import com.imie.a2dev.teamculte.readeo.Entities.DBEntities.City;
import com.imie.a2dev.teamculte.readeo.Entities.DBEntities.PrivateUser;
import com.imie.a2dev.teamculte.readeo.R;
import com.imie.a2dev.teamculte.readeo.Utils.PreferencesUtils;
import com.imie.a2dev.teamculte.readeo.Views.Adapters.CountrySpinnerAdapter;

import static com.imie.a2dev.teamculte.readeo.DBSchemas.UserDBSchema.CITY;
import static com.imie.a2dev.teamculte.readeo.DBSchemas.UserDBSchema.COUNTRY;
import static com.imie.a2dev.teamculte.readeo.DBSchemas.UserDBSchema.EMAIL;
import static com.imie.a2dev.teamculte.readeo.DBSchemas.UserDBSchema.PSEUDO;

/**
 * Activity displaying the user's account settings.
 */
public final class AccountActivity extends AppCompatActivity implements View.OnClickListener {
    /**
     * Stores the associated user.
     */
    private PrivateUser user;
    
    /**
     * Displays the user's avatar image.
     */
    private ImageView avatar;

    /**
     * Stores the user's pseudo edit text.
     */
    private EditText pseudo;

    /**
     * Stores the user's email edit text.
     */
    private EditText email;

    /**
     * Stores the user's description edit text.
     */
    private EditText description;

    /**
     * Stores the user's country spinner.
     */
    private Spinner country;

    /**
     * Stores the user's city edit text.
     */
    private EditText city;

    /**
     * Stores the country spinner's adapter.
     */
    private CountrySpinnerAdapter adapter;

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.img_avatar:
                // TODO: Start a gallery picking intent.
                
                break;
            case R.id.btn_change_password:
                // TODO: Start a dialog fragment asking for current password, new one and confirm.
                
                break;
            case R.id.btn_save_changes:
                if (this.validateFields()) {
                    this.saveData();
                    this.finish();
                } else {
                    Toast.makeText(this, R.string.form_fields_invalid, Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.txt_delete_account:
                // TODO: Start a dialog fragment asking to confirm and user's password.
                break;
            default:
                
                break;
        }
    }
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        setContentView(R.layout.activity_account);
        this.init();
    }

    /**
     * Initializes the activity's view components.
     */
    private void init() {
        this.user = PreferencesUtils.loadUser();
        this.avatar = this.findViewById(R.id.img_avatar);
        this.pseudo = this.findViewById(R.id.edit_pseudo);
        this.email = this.findViewById(R.id.edit_email);
        this.city = this.findViewById(R.id.edit_city);
        this.description = this.findViewById(R.id.edit_description);
        this.country = this.findViewById(R.id.spinner_country);
        this.adapter = new CountrySpinnerAdapter(this, new CountryDBManager(this).queryAllSQLite());
        
        this.pseudo.setText(this.user.getPseudo());
        this.email.setText(this.user.getEmail());
        this.city.setText(this.user.getCity().getName());
        this.description.setText(this.user.getProfile().getDescription());
        this.country.setAdapter(this.adapter);
        this.country.setSelection(this.adapter.getPosition(this.user.getCountry()));
        
        this.findViewById(R.id.btn_change_password).setOnClickListener(this);
        this.findViewById(R.id.btn_save_changes).setOnClickListener(this);
        this.findViewById(R.id.txt_delete_account).setOnClickListener(this);
        this.avatar.setOnClickListener(this);
    }

    /**
     * Checks the validity of the inputs.
     * @return True if valid else false.
     */
    private boolean validateFields() {
        // TODO: See how to validate the avatar and which fields are required.
        boolean valid;
        UserDBManager userDBManager = new UserDBManager(this);
        String pseudo = this.pseudo.getText().toString();
        String email = this.email.getText().toString();
        String city = this.city.getText().toString();
        
        valid = !pseudo.equals("") && !email.equals("") && !city.equals("");
        
        if (!pseudo.equals(this.user.getPseudo())) {
            valid = valid && userDBManager.isAvailableMySQL(PSEUDO, pseudo);
        }

        if (!email.equals(this.user.getEmail())) {
            valid = valid && userDBManager.isAvailableMySQL(EMAIL, email);
        }
        
        return valid;
    }

    /**
     * Saves the changes into the database.
     */
    private void saveData() {
        // TODO: Save the avatar.
        CityDBManager cityDBManager = new CityDBManager(this);
        ProfileDBManager profileDBManager = new ProfileDBManager(this);
        UserDBManager userDBManager = new UserDBManager(this);
        String cityName = this.city.getText().toString();

        this.user.setPseudo(this.pseudo.getText().toString());
        this.user.setEmail(this.email.getText().toString());
        this.user.getProfile().setDescription(this.description.getText().toString());

        City city = cityDBManager.loadMySQL(cityName);
        
        if (city == null) {
            city = new City(cityName);
            
            cityDBManager.createMySQL(city);
        }
        
        this.user.setCity(city);
        this.user.setCountry(this.adapter.getItem(this.country.getSelectedItemPosition()));
        
        profileDBManager.updateMySQL(this.user.getProfile());
        profileDBManager.updateSQLite(this.user.getProfile());
        
        userDBManager.updateFieldMySQL(this.user.getId(), PSEUDO, this.user.getPseudo());
        userDBManager.updateFieldMySQL(this.user.getId(), EMAIL, this.user.getEmail());
        userDBManager.updateFieldMySQL(this.user.getId(), CITY, String.valueOf(this.user.getCity().getId()));
        userDBManager.updateFieldMySQL(this.user.getId(), COUNTRY, String.valueOf(this.user.getCountry().getId()));
        userDBManager.updateSQLite(this.user);
        PreferencesUtils.saveUser(this.user);
    }
}
