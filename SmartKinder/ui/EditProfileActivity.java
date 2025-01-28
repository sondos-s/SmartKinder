package androidArmy.SmartKinder.ui;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidArmy.SmartKinder.R;
import androidArmy.SmartKinder.databinding.ActivityEditProfileBinding;


import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class EditProfileActivity extends AppCompatActivity {

    private  EditText usernameEditText;
    private EditText emailTextView;
    private EditText phoneTextView;
    private EditText addressTextView;
    private @NonNull ActivityEditProfileBinding binding;

    private Button cancelBtn;

    private Button saveBtn;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityEditProfileBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());


        Intent intent = getIntent();
        String username = intent.getStringExtra("username");
        String email = intent.getStringExtra("email");
        String phone = intent.getStringExtra("phone");
        String address = intent.getStringExtra("address");
        usernameEditText= findViewById(R.id.edit_username);
        emailTextView = findViewById(R.id.edit_email);
        phoneTextView = findViewById(R.id.edit_phone);
        addressTextView = findViewById(R.id.edit_address);
        // Pre fill with the user's data
        usernameEditText.setText(username);
        emailTextView.setText(email);
        phoneTextView.setText(phone);
        addressTextView.setText(address);

        // Handling the cancel button
        cancelBtn = binding.getRoot().findViewById(R.id.edit_cancel_btn);
        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

       // Set OnClickListener to the save button
        saveBtn = binding.getRoot().findViewById(R.id.edit_save_btn);
        saveBtn.setOnClickListener(this::saveProfileOnClick);

    }

    // Handling the save button
    private void saveProfileOnClick(View view) {
        // Create a Dialog object
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        // Inflate the custom layout
        View dialogView = getLayoutInflater().inflate(R.layout.custom_alert_dialog, null);

        // Set the inflated view as the dialog view
        builder.setView(dialogView);

        AlertDialog dialog = builder.create();
        dialog.dismiss();
        // Show the dialog
        dialog.show();
    }
}