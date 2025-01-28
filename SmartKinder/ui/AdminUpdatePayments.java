package androidArmy.SmartKinder.ui;
import androidArmy.SmartKinder.R;
import androidArmy.SmartKinder.backend.InfoKid;
import androidArmy.SmartKinder.backend.MyInfoManager;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class AdminUpdatePayments extends Fragment {
    // ...

    private Spinner childNameSpinner;
    private RadioGroup firstPaymentRadioGroup, secondPaymentRadioGroup, thirdPaymentRadioGroup;
    RadioButton paidBtn1,paidBtn2 ,paidBtn3, notPaidBtn1, notPaidBtn2, notPaidBtn3;
    private Button saveButton;
    private ImageButton backButton;
    private View view;
    private Button yesButton;
    private Button noButton;
    private  AlertDialog dialog;
    private InfoKid selectedChild;
    private String firstPaymentOption, secondPaymentOption, thirdPaymentOption;
    ArrayList<InfoKid> kidList;

    private MyInfoManager infoManager;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_admin_update_payments, container, false);

        // Retrieve the MyInfoManager instance from MainActivity
        infoManager = MyInfoManager.getInstance(requireContext());

        // Initialize views
        childNameSpinner = view.findViewById(R.id.admin_child_name_spinner);
        paidBtn1 = view.findViewById(R.id.admin_paid_radio_btn1);
        paidBtn2 = view.findViewById(R.id.admin_paid_radio_btn2);
        paidBtn3 = view.findViewById(R.id.admin_paid_radio_btn3);
        notPaidBtn1 = view.findViewById(R.id.admin_not_paid_radio_btn1);
        notPaidBtn2 = view.findViewById(R.id.admin_not_paid_radio_btn2);
        notPaidBtn3= view.findViewById(R.id.admin_not_paid_radio_btn3);
        firstPaymentRadioGroup = view.findViewById(R.id.admin_payment_radio_group1);
        secondPaymentRadioGroup = view.findViewById(R.id.admin_payment_radio_group2);
        thirdPaymentRadioGroup = view.findViewById(R.id.admin_payment_radio_group3);
        saveButton = view.findViewById(R.id.admin_payment_save_button);
        backButton = view.findViewById(R.id.admin_payment_back_button);

        // Get all the children from db
        kidList = new ArrayList<>(infoManager.getAllKids());


        // Set up child names in spinner
        List<String> childNames = getChildNames();
        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<>(requireContext(),
                android.R.layout.simple_spinner_item, childNames);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        childNameSpinner.setAdapter(spinnerAdapter);

        // Handle child name selection
        childNameSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedChildName = parent.getItemAtPosition(position).toString();
                selectedChild = getKidByName(selectedChildName);
                initialState();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Do nothing
            }
        });

        // Set OnClickListener for the buttons
         backButton.setOnClickListener(v -> handleBackButton());
         saveButton.setOnClickListener(v -> saveButtonClickListener());

        return view;
    }



    // Get a list of child names (ID and name) for the spinner
    private List<String> getChildNames() {
        List<String> childNames = new ArrayList<>();
        for (InfoKid kid : kidList) {
            String childName = "ID- " + kid.getId() + " , " + kid.getName();
            childNames.add(childName);
        }
        return childNames;
    }


    // Get the InfoKid object based on the selected child name
    private InfoKid getKidByName(String childName) {
        for (InfoKid kid : kidList) {
            String formattedChildName = "ID- " + kid.getId() + " , " + kid.getName();
            if (formattedChildName.equals(childName)) {
                return kid;
            }
        }
        return null;
    }


    // Initial the radio buttons with the current payment status ( db )
    private void initialState() {
        // Replace with your data from db using the selectedChild to get his payments
        boolean payment1 = infoManager.getFirstPayment(selectedChild.getName(), "2023");
        boolean payment2 = infoManager.getSecondPayment(selectedChild.getName(), "2023");
        boolean payment3 = infoManager.getThirdPayment(selectedChild.getName(), "2023");

        Log.d("m",""+payment1);
        Log.d("m",""+payment2);
        Log.d("m",""+payment3);
        // Clear the checked state of all radio buttons
        firstPaymentRadioGroup.clearCheck();
        secondPaymentRadioGroup.clearCheck();
        thirdPaymentRadioGroup.clearCheck();

        // Check the appropriate radio button based on the payment values
        if (payment1) {
            paidBtn1.setChecked(true);
        } else {
            notPaidBtn1.setChecked(true);
        }

        if (payment2) {
            paidBtn2.setChecked(true);
        } else {
            notPaidBtn2.setChecked(true);
        }

        if (payment3) {
            paidBtn3.setChecked(true);
        } else {
            notPaidBtn3.setChecked(true);
        }
    }


    // Handle save button click
    private void saveButtonClickListener() {
        // Validate the required fields
        if (selectedChild == null) {
            showRequiredFieldsError();
            return;
        }
        // Build the alert dialog
        buildAlertDialog();
        dialog.show();
        // Set click listener for "Yes" button
        yesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                boolean firstPaymentValue = paidBtn1.isChecked();
                boolean secondPaymentValue = paidBtn2.isChecked();
                boolean thirdPaymentValue = paidBtn3.isChecked();

                boolean success1 = infoManager.updateFirstPayment(selectedChild.getName(), "2023", firstPaymentValue);
                boolean success2 = infoManager.updateSecondPayment(selectedChild.getName(), "2023", secondPaymentValue);
                boolean success3 = infoManager.updateThirdPayment(selectedChild.getName(), "2023", thirdPaymentValue);

                if (success1 && success2 && success3) {
                    // Display selected options
                    String message = "Child Name: " + selectedChild.getName() + "\n"
                            + "First Payment: " + (firstPaymentValue ? "Paid" : "Not Paid") + "\n"
                            + "Second Payment: " + (secondPaymentValue ? "Paid" : "Not Paid") + "\n"
                            + "Third Payment: " + (thirdPaymentValue ? "Paid" : "Not Paid");

                    Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(requireContext(), "Failed to update payments", Toast.LENGTH_SHORT).show();
                }

                dialog.dismiss();
            }
        });

        // Set click listener for "No" button
        noButton.setOnClickListener(v -> {
            dialog.dismiss();
            initialState();});

    }



    // Show an alert dialog indicating that required fields are empty
    private void showRequiredFieldsError() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Error");
        builder.setMessage("Please fill in all required fields.");
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }


    // Building the alert dialog
    private void buildAlertDialog()
    {
        // Create a Dialog object
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());

        // Inflate the custom layout
        View dialogView = getLayoutInflater().inflate(R.layout.custom_alert_dialog, null);

        // Set the inflated view as the dialog view
        builder.setView(dialogView);

        dialog = builder.create();


        // Find the "Yes" and "No" buttons from the dialog view
        yesButton = dialogView.findViewById(R.id.dialog_btn_yes);
        noButton = dialogView.findViewById(R.id.dialog_btn_no);
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
