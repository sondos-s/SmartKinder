package androidArmy.SmartKinder.ui;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidArmy.SmartKinder.R;
import androidArmy.SmartKinder.backend.MyInfoManager;
import androidArmy.SmartKinder.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener , HomeFragment.TitleChangeListener,AdminDataTables.TitleChangeListener {

    private MenuItem actionIconMenuItem;

    private ActivityMainBinding binding;

    private MyInfoManager infoManager;

    String type;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        infoManager = MyInfoManager.getInstance(this);

        if (infoManager.getUser() != null) {
            String userName = infoManager.getUser().getUserName();
            if (userName != null) {
                if (userName.equals("Admin")) {
                    type = "admin";
                } else if (userName.equals("Parent")) {
                    type = "parent";
                } else {
                    type = "teacher";
                }
            } else {
                // Handle the case when userName is null
            }
        } else {
            // Handle the case when user is null
        }


       /* Passing each menu ID as a set of Ids because each
           menu should be considered as top level destinations.*/
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_home, R.id.navigation_messaging, R.id.navigation_notifications, R.id.navigation_profile, R.id.navigation_admin)
                .build();

        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_activity_main);
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(binding.navView, navController);
        binding.navView.setOnNavigationItemSelectedListener(this);

    }

    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_activity_main);

        switch (item.getItemId()) {
            case R.id.navigation_home:
                navController.popBackStack(R.id.navigation_home, false);
                return true;
            case R.id.navigation_messaging:
                navController.navigate(R.id.navigation_messaging);
                return true;
            case R.id.navigation_notifications:
                navController.navigate(R.id.navigation_notifications);
                return true;
            case R.id.navigation_profile:
                navController.navigate(R.id.navigation_profile);
                return true;
            case R.id.navigation_admin:
                navController.navigate(R.id.navigation_admin);
                return true;
        }

        return false;
    }


    // When the user is teacher or parent
    private void notAdmin() {

        // To make the menu item invisible
        if (type != null && (type.equals("teacher") || type.equals("parent"))) {
            actionIconMenuItem.setVisible(false);

            // To disable the menu item
            actionIconMenuItem.setEnabled(false);
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.admin_menu, menu);
        actionIconMenuItem = menu.findItem(R.id.navigation_admin);
        notAdmin();
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.navigation_admin) {
            // Handle the click event for the admin menu item
            NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_activity_main);
            navController.navigate(R.id.navigation_admin);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onTitleChanged(String newTitle) {
        getSupportActionBar().setTitle(newTitle);
    }

}