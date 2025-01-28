package androidArmy.SmartKinder.ui;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import java.util.ArrayList;
import java.util.List;

import androidArmy.SmartKinder.R;
import androidArmy.SmartKinder.databinding.FragmentPaymentBinding;

public class PaymentFragment extends Fragment {

    private ImageButton backButton;
    RadioGroup radioGroup1 ;
    RadioGroup radioGroup2 ;
    RadioGroup radioGroup3 ;
    RadioButton paidBtn1 ;
    RadioButton notPaidBtn1 ;
    RadioButton paidBtn2 ;
    RadioButton notPaidBtn2 ;
    RadioButton paidBtn3 ;
    RadioButton notPaidBtn3 ;
    private FragmentPaymentBinding binding;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_payment, container, false);

        // Inflate the fragment's layout using the provided inflater
        binding = FragmentPaymentBinding.inflate(inflater, container, false);

        // Find the views by their IDs
        backButton = view.findViewById(R.id.payment_back_button);
        radioGroup1 = view.findViewById(R.id.parent_payment_radio_group1);
        radioGroup2 = view.findViewById(R.id.parent_payment_radio_group2);
        radioGroup3 = view.findViewById(R.id.parent_payment_radio_group3);
        paidBtn1 = view.findViewById(R.id.parent_paid_radio_btn1);
        notPaidBtn1 =view.findViewById(R.id.parent_not_paid_radio_btn1);
        paidBtn2 = view.findViewById(R.id.parent_paid_radio_btn2);
        notPaidBtn2 = view.findViewById(R.id.parent_not_paid_radio_btn2);
        paidBtn3 = view.findViewById(R.id.parent_paid_radio_btn3);
        notPaidBtn3 = view.findViewById(R.id.parent_not_paid_radio_btn3);

        // Initial the radio buttons
        initialState();

        // Set an onClickListeners to handle button click events
        backButton.setOnClickListener(v -> handleBackButton());

        return view;
    }


    // Initial the radio buttons with the current payment status ( db )
    private void initialState() {
        boolean payment1 = true; // Your boolean value for payment 1
        boolean payment2 = true; // Your boolean value for payment 2
        boolean payment3 = false; // Your boolean value for payment 3

        // Set the initial checked state of the RadioButtons based on the boolean values
        // Replace with your data from db
        paidBtn1.setChecked(payment1);
        notPaidBtn1.setChecked(!payment1);

        paidBtn2.setChecked(payment2);
        notPaidBtn2.setChecked(!payment2);

        paidBtn3.setChecked(payment3);
        notPaidBtn3.setChecked(!payment3);

        // Disable user interaction with the RadioGroups
        paidBtn1.setClickable(false);
        paidBtn2.setClickable(false);
        paidBtn3.setClickable(false);
        notPaidBtn1.setClickable(false);
        notPaidBtn2.setClickable(false);
        notPaidBtn3.setClickable(false);
        paidBtn1.setFocusable(false);
        paidBtn2.setFocusable(false);
        paidBtn3.setFocusable(false);
        notPaidBtn1.setFocusable(false);
        notPaidBtn2.setFocusable(false);
        notPaidBtn3.setFocusable(false);
    }


    // Handle the back button
    private void handleBackButton() {
        FragmentManager fm = getParentFragmentManager();
        int backStackCount = fm.getBackStackEntryCount();
        // Return to the previous screen (choose child)
        if (backStackCount > 0)
        {
            fm.popBackStack();
        }
        // Return to the home screen
        else
        {
            HomeFragment home = new HomeFragment();
            FragmentTransaction t = fm.beginTransaction();
            t.replace(R.id.nav_host_fragment_activity_main, home);
            t.commit();
        }
    }
}
