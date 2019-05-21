package com.imie.a2dev.teamculte.readeo.Views;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import com.imie.a2dev.teamculte.readeo.R;

/**
 * Activity displaying the user's account settings.
 */
public class AccountActivity extends AppCompatActivity implements View.OnClickListener {
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
                // TODO: Update the current user with the data (and sync distant db).
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
        this.avatar = this.findViewById(R.id.img_avatar);
        this.pseudo = this.findViewById(R.id.edit_pseudo);
        this.email = this.findViewById(R.id.edit_email);
        this.city = this.findViewById(R.id.edit_city);
        this.description = this.findViewById(R.id.edit_description);
        this.country = this.findViewById(R.id.spinner_country);
        
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
        
        String pseudo = this.pseudo.getText().toString();
        String email = this.email.getText().toString();
        String city = this.city.getText().toString();
        String country = this.country.getSelectedItem().toString(); 
        
        return (!pseudo.equals("") && !email.equals("") && !city.equals("") && !country.equals(""));
    }
}
