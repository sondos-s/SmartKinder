package androidArmy.SmartKinder.ui;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;


import androidArmy.SmartKinder.backend.InfoKid;
import androidArmy.SmartKinder.backend.MyInfoManager;
import androidArmy.SmartKinder.backend.User;
import androidArmy.SmartKinder.databinding.ActivityLoginBinding;
import androidArmy.SmartKinder.ui.MainActivity;


public class LoginActivity extends AppCompatActivity {

    ActivityLoginBinding binding;
    //DataBaseHelper databaseHelper;
    private MyInfoManager infoManager;

    private User user;
    //User user;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        infoManager = MyInfoManager.getInstance(this);
        //Add your kid objects here
        //InfoKid kid1 = new InfoKid(6,"Emma Johnson", "1", "1", "2016-01-01", "Needs1");
        //InfoKid kid2 = new InfoKid(7,"Noah Williams", "2", "2", "2020-03-15", "Needs2");
         //InfoKid kid3 = new InfoKid(3,"Sophia Davis", "3", "3", "2019-03-15", "Needs2");
         //InfoKid kid4 = new InfoKid(4,"Liam Davis", "3", "3", "2019-03-15", "Needs2");
        //InfoKid kid5 = new InfoKid(5,"Ava Wilson", "4", "4", "2019-03-15", "Needs2");
         //Add the kid objects to the database
        //infoManager.addInfoKid(kid1);
        //infoManager.addInfoKid(kid2);
        //  infoManager.addInfoKid(kid3);
         //infoManager.addInfoKid(kid4);
         //infoManager.addInfoKid(kid5);
       // infoManager.deleteAllAttendance();
        //infoManager.deleteAllKids();
 


        binding.loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = binding.loginUsername.getText().toString();
                String password = binding.loginPassword.getText().toString();

                if(username.equals("") || password.equals("")){
                    Toast.makeText(LoginActivity.this, "All fields are mandatory", Toast.LENGTH_SHORT).show();
                } else {
                    //Set the user object
                    user = new User(username, password);
                    MyInfoManager.setUser(user);

                    if(username.equals("Admin") && password.equals("admin")){
                        Toast.makeText(LoginActivity.this, "Login Successfully", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                        startActivity(intent);
                    } else {
                        if(username.equals("Teacher") && password.equals("teacher")){
                            Toast.makeText(LoginActivity.this, "Login Successfully", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                            startActivity(intent);
                        } else {
                            if(username.equals("Parent") && password.equals("parent")){
                                Toast.makeText(LoginActivity.this, "Login Successfully", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
                            } else {
                                Toast.makeText(LoginActivity.this, "Invalid Credentials", Toast.LENGTH_SHORT).show();
                            }
            }
                    }
                }
            }
        });

    }
}