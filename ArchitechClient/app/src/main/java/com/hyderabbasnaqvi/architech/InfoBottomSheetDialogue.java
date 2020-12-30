package com.hyderabbasnaqvi.architech;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import java.util.ArrayList;
import java.util.Hashtable;

public class InfoBottomSheetDialogue extends BottomSheetDialogFragment {

    TextView bedroomsField, pcaField, bathroomsField, costEstimateField;
    String pca, costEstimate;
    ArrayList<String> roomCounts,roomTypes;
    Hashtable<String,String> roomDictionary;
//            # color map - from python code - here just for reference
//    self.floorplan_map = {
//        (255, 255, 255): 'background - white',  # background
//                (192, 192, 224): 'closet - violet',  # closet
//                (192, 255, 255): 'bathroom/washroom - cyan',  # bathroom/washroom
//                (224, 255, 192): 'livingroom/kitchen/dining room - light green',
//                (255, 224, 128): 'bedroom - yellow',  # bedroom
//                (255, 160, 96): 'hall - orange',  # hall
//                (255, 224, 224): 'balcony',  # balcony
//                (255, 60, 128): 'door & window - pink',  # door & window
//                (0,  0,  0): 'wall - black'  # wall
//    }

//    data that is to be shown in bottom dialogue is received and instantiated in constructor
    InfoBottomSheetDialogue(String pArea, ArrayList<String> rc, ArrayList<String> rt,String costEstimate)
    {
        this.pca = pArea;
        this.roomCounts= rc;
        this.roomTypes=rt;
        this.costEstimate = costEstimate;
//        dictionary to mimic the one in python server
        roomDictionary = new Hashtable<String,String>();
        for(int i = 0; i<roomCounts.size();i++){
            roomDictionary.put(roomTypes.get(i),roomCounts.get(i));
        }
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view= inflater.inflate(R.layout.bottom_sheet_info, container, false);

        //join with ids
        pcaField = view.findViewById(R.id.informationPercentageCoveredArea);
        bedroomsField = view.findViewById(R.id.informationNumberOfBedrooms);
        bathroomsField = view.findViewById(R.id.informationNumberOfBathrooms);
        costEstimateField = view.findViewById(R.id.informationCostEstimate);

        pcaField.setText(pca + "%");
        bedroomsField.setText(roomDictionary.get("bedroom - yellow"));
        bathroomsField.setText(roomDictionary.get("bathroom/washroom - cyan"));
        costEstimateField.setText(" PKR " + costEstimate);

        return view;
    }
}
