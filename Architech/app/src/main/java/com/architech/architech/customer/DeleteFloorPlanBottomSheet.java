package com.architech.architech.customer;


import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.architech.architech.R;
import com.architech.architech.model.FloorPlan;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

public class DeleteFloorPlanBottomSheet extends BottomSheetDialogFragment {

    private BottomSheetListener bottomSheetListener;


    Button confirmButton;
    FloorPlan floorPlan;
    int position;
    DeleteFloorPlanBottomSheet(FloorPlan floorPlan, int position){
        this.floorPlan= floorPlan;
        this.position= position;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view= inflater.inflate(R.layout.bottom_sheet_confirm_delete, container, false);


        confirmButton= view.findViewById(R.id.confirmButtonBottomSheet);
        confirmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bottomSheetListener.onConfirmPressed(1, floorPlan, position);
                dismiss(); //dismiss the dialogue
            }
        });


        return view;
    }

    public interface BottomSheetListener{

        //this we will communicate to main activity
        void onConfirmPressed(int buttonPressedCode, FloorPlan order, int position);
    }


    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

        //context attaches the dialogue with the home activity

        //exception here so we remember to implement the interface above in activity. Just a good practice
        try {
            bottomSheetListener = (BottomSheetListener) context;
        }catch (ClassCastException e)
        {
            throw new ClassCastException(context.toString()
                    +"Space must implement bottom sheet listener");
        }

    }
}
