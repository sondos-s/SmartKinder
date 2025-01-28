package androidArmy.SmartKinder.ui;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidArmy.SmartKinder.R;
import androidArmy.SmartKinder.databinding.FragmentProfileBinding;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

public class ProfileFragment extends Fragment {

    private FragmentProfileBinding binding;
    private  View root;

    private static final int REQ_CODE = 1;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
//        ProfileViewModel profileViewModel =
//                new ViewModelProvider(this).get(ProfileViewModel.class);
        binding = FragmentProfileBinding.inflate(inflater, container, false);
        root = binding.getRoot();

        //  Set OnClickListener to edit button
        Button editBtn = root.findViewById(R.id.edit_profile_btn);
        editBtn.setOnClickListener(this::editProfileOnClick);
        Button logout = root.findViewById(R.id.logout_btn);
        logout.setOnClickListener(v -> handleLogoutButton());

        return root;
    }


    // Handling the edit profile button
    private void editProfileOnClick(View view) {
        // Start the edit profile activity
        Intent intent = new Intent(getActivity(), EditProfileActivity.class);
        TextView username = root.findViewById(R.id.profile_username);
        TextView email = root.findViewById(R.id.profile_email);
        TextView phone = root.findViewById(R.id.profile_phone);
        TextView address = root.findViewById(R.id.profile_address);

        intent.putExtra("username", username.getText());
        intent.putExtra("email", email.getText());
        intent.putExtra("address", address.getText());
        intent.putExtra("phone", phone.getText());
        startActivity(intent);
    }

    private void handleLogoutButton() {
        getActivity().finish();
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}