package androidArmy.SmartKinder.ui;
import androidArmy.SmartKinder.R;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageButton;
import android.widget.TextView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class ViewProgressFragment extends Fragment {

    private Button datePickerButton;
    private TextView selectedDateTextView, recordTextView;
    private Calendar calendar;
    private ImageButton backButton;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_view_progress, container, false);

        // Find the views by the ID
        datePickerButton = view.findViewById(R.id.view_progress_date_picker);
        selectedDateTextView = view.findViewById(R.id.view_progress_selected_date);
        recordTextView = view.findViewById(R.id.view_progress_record);

        backButton = view.findViewById(R.id.view_progress_back_button);

        calendar = Calendar.getInstance();

        // Set OnClickListener for the buttons
        backButton.setOnClickListener(v -> handleBackButton());
        datePickerButton.setOnClickListener(v -> showDatePickerDialog());

        return view;
    }



    // Show the picker dialog calendar
    private void showDatePickerDialog() {
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(requireContext(),
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        calendar.set(year, monthOfYear, dayOfMonth);
                        updateSelectedDate();
                    }
                }, year, month, day);

        datePickerDialog.show();
    }

    // Show the selected date
    private void updateSelectedDate() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        String selectedDate = dateFormat.format(calendar.getTime());
        selectedDateTextView.setText("Selected Date: " + selectedDate);
        recordTextView.setText("Your child is progressing admirably in both their academic and social-emotional development, demonstrating a strong work ethic and positive engagement with their peers. ");
    }

    // Handle the back button
    private void handleBackButton() {
        FragmentManager fm = getParentFragmentManager();
        HomeFragment home = new HomeFragment();
        FragmentTransaction t = fm.beginTransaction();
        t.replace(R.id.nav_host_fragment_activity_main, home);
        t.commit();
    }
}
