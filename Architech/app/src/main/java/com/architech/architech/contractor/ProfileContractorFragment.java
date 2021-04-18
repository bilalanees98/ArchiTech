package com.architech.architech.contractor;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.firebase.auth.FirebaseAuth;
import com.architech.architech.MainActivity;
import com.architech.architech.R;
import com.architech.architech.model.Contractor;

import de.hdodenhof.circleimageview.CircleImageView;

public class ProfileContractorFragment extends Fragment {

    TextView name, email, phone, rateOne, rateTwo, rateThree;
    CircleImageView profileImage;
    Button editButton, logoutButton;
    final int REQUEST_CODE_EDIT_PROFILE= 20;
    FirebaseAuth mAuth;
    Contractor myProfile;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile_contractor,container,false);
        myProfile= getArguments().getParcelable("MYPROFILE");

        mAuth=FirebaseAuth.getInstance();
        name= view.findViewById(R.id.nameContractorProfileFragment);
        email= view.findViewById(R.id.emailContractorProfileFragment);
        phone= view.findViewById(R.id.phoneNoContractorProfileFragment);
        rateOne= view.findViewById(R.id.rateOneContractorProfileFragment);
        rateTwo= view.findViewById(R.id.rateTwoContractorProfileFragment);
        rateThree= view.findViewById(R.id.rateThreeContractorProfileFragment);
        profileImage= view.findViewById(R.id.imageContractorProfileFragment);
        editButton= view.findViewById(R.id.editButtonContractorProfileFragment);
        logoutButton= view.findViewById(R.id.logoutButtonContractor);

        populateUiFromMyProfile();




        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getActivity(), "Logged out", Toast.LENGTH_SHORT).show();
                mAuth.signOut();


                Intent goToMain = new Intent(getActivity(), MainActivity.class);
                goToMain.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(goToMain);
            }
        });


        editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getActivity(), "go to edit", Toast.LENGTH_SHORT).show();
                Intent goToEdit= new Intent(getActivity(), EditProfileContractor.class);
                goToEdit.putExtra("MYPROFILE", myProfile);
                startActivityForResult(goToEdit, REQUEST_CODE_EDIT_PROFILE);
            }
        });




        return view;

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == REQUEST_CODE_EDIT_PROFILE)
        {
            //RESULT_OK
            if (resultCode== -1)
            {
                myProfile= data.getParcelableExtra("MY_PROFILE_FROM_EDIT");
                populateUiFromMyProfile();
            }
        }

    }

    private void populateUiFromMyProfile() {
        name.setText(myProfile.getName());
        email.setText(myProfile.getEmail());
        phone.setText(myProfile.getPhone());
        rateOne.setText(String.format("Rs. %s/sq ft", myProfile.getRateOne()));
        rateTwo.setText(String.format("Rs. %s/sq ft", myProfile.getRateTwo()));
        rateThree.setText(String.format("Rs. %s/sq ft", myProfile.getRateThree()));

        //TODO: piccasso image
    }
}
