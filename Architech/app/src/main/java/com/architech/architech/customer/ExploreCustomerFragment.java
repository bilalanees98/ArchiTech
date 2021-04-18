package com.architech.architech.customer;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.architech.architech.R;
import com.architech.architech.model.Customer;
import com.architech.architech.model.FloorPlan;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class ExploreCustomerFragment extends Fragment {

//    ArrayList<FloorPlan> listOfFloorPlans;
//    ArrayList<Image> listOfImages;
    ArrayList<FloorPlan> listForRecycler;
    ArrayList<FloorPlan> filteredList;
    ArrayList<String> keysFloorPlanInFirebase;// keys stored each time listOfContacts populated
    DatabaseReference database;

    Customer myProfile;

    EditText roomsFilter,bathroomsFilter,carParkFilter,lengthFilter,widthFilter;
    LinearLayout filterMenu;
    Button applyFilterButton, removeAllButton;
    CircleImageView openFilterButton;
    ExploreCustomerFragmentRvAdapter adapter;
    RecyclerView rv;
    ProgressBar progressBar;


    public ExploreCustomerFragment(){}

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
         rv= (RecyclerView) view.findViewById(R.id.homeCustomerRecyclerView);
        RecyclerView.LayoutManager lm= new LinearLayoutManager(getActivity());
        rv.setLayoutManager(lm);
        adapter=new ExploreCustomerFragmentRvAdapter(listForRecycler, getActivity());
        rv.setAdapter(adapter);
        rv.setItemAnimator(new DefaultItemAnimator());
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view= inflater.inflate(R.layout.fragment_explore_customer, container, false);

        listForRecycler=new ArrayList<>();
        keysFloorPlanInFirebase = new ArrayList<>();
        filteredList=new ArrayList<>();


        populateMyProfile();

        roomsFilter=view.findViewById(R.id.rooms_filter_value);
        bathroomsFilter=view.findViewById(R.id.bathrooms_filter_value);
        carParkFilter=view.findViewById(R.id.car_park_filter_value);
        lengthFilter=view.findViewById(R.id.length_filter_value);
        widthFilter=view.findViewById(R.id.width_filter_value);
        progressBar= view.findViewById(R.id.progressBarHomeCustomerFragment);
        filterMenu=view.findViewById(R.id.filter_menu);

        openFilterButton=view.findViewById(R.id.open_filter_menu_button);
        applyFilterButton=view.findViewById(R.id.apply_filter_button);
        removeAllButton= view.findViewById(R.id.remove_all_filter_button);

        openFilterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //only show remove filter button when filter previously applied
                if(filteredList.size()==listForRecycler.size())
                {
                    removeAllButton.setVisibility(View.GONE);
                }
                else
                {
                    removeAllButton.setVisibility(View.VISIBLE);
                }


                //toggle opening menu
                if(filterMenu.getVisibility()==View.VISIBLE)
                {
                    hideKeyboard(getActivity());
                    filterMenu.setVisibility(View.GONE);
                }
                else {
                    filterMenu.setVisibility(View.VISIBLE);
                }
            }
        });

        removeAllButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //clearing text views
                roomsFilter.setText("");
                bathroomsFilter.setText("");
                carParkFilter.setText("");
                lengthFilter.setText("");
                widthFilter.setText("");

                //actual list resetting
                filteredList.clear();
                filteredList.addAll(listForRecycler);
                adapter.filteredList(filteredList);
                adapter.notifyDataSetChanged();
                filterMenu.setVisibility(View.GONE);
                Toast.makeText(getActivity(),"All filters removed",Toast.LENGTH_SHORT).show();

            }
        });


        applyFilterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hideKeyboard(getActivity());
                applyFilters(
                        roomsFilter.getText().toString().trim(),
                        bathroomsFilter.getText().toString().trim(),
                        carParkFilter.getText().toString().trim(),
                        lengthFilter.getText().toString().trim(),
                        widthFilter.getText().toString().trim()
                            );

            }
        });

        database = FirebaseDatabase.getInstance().getReference().child("Floorplans");
        database.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                FloorPlan newFp = snapshot.getValue(FloorPlan.class);
                String key = snapshot.getKey();
                keysFloorPlanInFirebase.add(key);
                listForRecycler.add(new FloorPlan(
                        newFp.getTitle(),
                         newFp.getOwner(),
                         newFp.getWidth(),
                         newFp.getLength(),
                         newFp.getNoOfCars(),
                         newFp.getBathrooms(),
                         newFp.getBedrooms(),
                         newFp.getId(),
                         newFp.getOwnerName(),
                         newFp.getCroppedWidth(),
                         newFp.getCroppedLength(),
                         newFp.getImageUrl(),
                         newFp.getPercentageCoveredArea(),
                         newFp.getCostEstimate()
                    ));

//                            keysFloorPlanInFirebase.add(key);
                adapter.notifyDataSetChanged();

                //making filtered list same as listforrecycler initially
                filteredList.clear();
                filteredList.addAll(listForRecycler);


                progressBar.setVisibility(View.GONE);

            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                FloorPlan newFp = snapshot.getValue(FloorPlan.class);
                String key = snapshot.getKey();
                int index = keysFloorPlanInFirebase.indexOf(key);
                listForRecycler.set(index,new FloorPlan(
                        newFp.getTitle(),
                        newFp.getOwner(),
                        newFp.getWidth(),
                        newFp.getLength(),
                        newFp.getNoOfCars(),
                        newFp.getBathrooms(),
                        newFp.getBedrooms(),
                        newFp.getId(),
                        newFp.getOwnerName(),
                        newFp.getCroppedWidth(),
                        newFp.getCroppedLength(),
                        newFp.getImageUrl(),
                        newFp.getPercentageCoveredArea(),
                        newFp.getCostEstimate()
                ));

                adapter.notifyDataSetChanged();



            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {
                FloorPlan newFp = snapshot.getValue(FloorPlan.class);
                String key = snapshot.getKey();
                int index = keysFloorPlanInFirebase.indexOf(key);
                listForRecycler.remove(index);
                keysFloorPlanInFirebase.remove(index);
                adapter.notifyDataSetChanged();

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        return view;



    }

    public void applyFilters(String rooms,String bathRooms,String carPark,String length,String width){
        filteredList.clear();
        //filteredList.addAll(listForRecycler);

        boolean shouldBeAdded;
        for(int i=0; i<listForRecycler.size(); i++)
        {
            shouldBeAdded=true;
            if(!rooms.isEmpty())
            {
                //if rooms main kuch likha wa hai, and it is not equal to rooms of this floorplan, then we set flag to false
                if(!listForRecycler.get(i).getBedrooms().equals(rooms)){
                    shouldBeAdded=false;
                }
            }
            if(!bathRooms.isEmpty() && shouldBeAdded)
            {
                if(!listForRecycler.get(i).getBathrooms().equals(bathRooms)){
                    shouldBeAdded=false;
                }
            }
            if(!carPark.isEmpty() && shouldBeAdded)
            {
                if(!listForRecycler.get(i).getNoOfCars().equals(carPark)){
                    shouldBeAdded=false;
                }
            }
            if(!width.isEmpty() && shouldBeAdded)
            {
                if(!listForRecycler.get(i).getWidth().equals(width)){
                    shouldBeAdded=false;
                }
            }
            if(!length.isEmpty() && shouldBeAdded)
            {
                if(!listForRecycler.get(i).getLength().equals(length)){
                    shouldBeAdded=false;
                }
            }
            if(shouldBeAdded)
            {
                filteredList.add(listForRecycler.get(i));
            }

        }

        //filtering has been done
        if(rooms.isEmpty() && bathRooms.isEmpty() && carPark.isEmpty() && length.isEmpty() && width.isEmpty()){
            Toast.makeText(getActivity(),"Please add a Filter first",Toast.LENGTH_SHORT).show();
        }
        else if(filteredList.size()!=0){
            adapter.filteredList(filteredList);
            adapter.notifyDataSetChanged();
            filterMenu.setVisibility(View.GONE);
            Toast.makeText(getActivity(),"Results found: " + filteredList.size() + " ",Toast.LENGTH_SHORT).show();
        }
        else{
            Toast.makeText(getActivity(),"No results found for Filters",Toast.LENGTH_SHORT).show();
            filteredList.clear();
            filteredList.addAll(listForRecycler);
        }




    }

    private void populateMyProfile() {
        String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        DatabaseReference database = FirebaseDatabase.getInstance().getReference().child("Users").child(uid);
//        create listener that will listen for any changes in current user table
        ValueEventListener myProfileListener;
        myProfileListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    myProfile = snapshot.getValue(Customer.class);
//                    myProfile = new Customer(myProfileContact);
                    /*if (!myProfile.getImageUrl().isEmpty()) {
                        Picasso.get().load(myProfile.getImageUrl()).into(searchBarProfileIcon);//to set image from firebase storage with picasso
                    }*/

                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        };
//        add listener to that database
        database.addValueEventListener(myProfileListener);


    }


    void hideKeyboard(Context c)
    {
        InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Activity.INPUT_METHOD_SERVICE);
        //Find the currently focused view, so we can grab the correct window token from it.
        View view = getActivity().getCurrentFocus();
        //If no view currently has focus, create a new one, just so we can grab a window token from it
        if (view == null) {
            view = new View(c);
        }
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

}
