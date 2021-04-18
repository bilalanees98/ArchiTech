package com.architech.architech.customer;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

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
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.architech.architech.R;
import com.architech.architech.model.Meeting;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class MeetingConfirmedCustomerFragment extends Fragment {

    RecyclerView recyclerView;
    MeetingConfirmedCustomerFragmentRvAdapter rvAdapter;
    ArrayList<Meeting> listOfConfirmedMeetings;
    ArrayList<String> keysMessageInFirebase;

    DatabaseReference database;
    FirebaseAuth mAuth;

    ProgressBar progressBar;

    public MeetingConfirmedCustomerFragment() {
        // Required empty public constructor
        listOfConfirmedMeetings = new ArrayList<>();
        keysMessageInFirebase = new ArrayList<>();
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        progressBar= view.findViewById(R.id.progressBarConfirmedMeetingCustomer);
        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerViewConfirmedMeetingCustomer);
        LinearLayoutManager lm= new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(lm);
        rvAdapter =new MeetingConfirmedCustomerFragmentRvAdapter(listOfConfirmedMeetings, getActivity());
        recyclerView.setAdapter(rvAdapter);
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        mAuth=FirebaseAuth.getInstance();

        database = FirebaseDatabase.getInstance().getReference().child("Meetings");
//        database.addChildEventListener(new ChildEventListener() {
//            @Override
//            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
//                Meeting newMeeting = snapshot.getValue(Meeting.class);
//                String key = snapshot.getKey();
//                if (newMeeting.getStatus().equals("confirmed")) {
//                    listOfConfirmedMeetings.add(newMeeting);
//                    keysMessageInFirebase.add(key);
//                    rvAdapter.notifyDataSetChanged();
////                    Log.i("MEETING",newMeeting.getMeetingId());
//                }
//            }
//
//            @Override
//            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
//                Meeting newMeeting = snapshot.getValue(Meeting.class);
//                String key = snapshot.getKey();
//                if (newMeeting.getStatus().equals("confirmed")) {
//                    int index = keysMessageInFirebase.indexOf(key);
//                    listOfConfirmedMeetings.set(index,newMeeting);
//                    rvAdapter.notifyDataSetChanged();
////                    Log.i("MEETING",newMeeting.getMeetingId());
//                }
//            }
//
//            @Override
//            public void onChildRemoved(@NonNull DataSnapshot snapshot) {
//                Meeting newMeeting = snapshot.getValue(Meeting.class);
//                String key = snapshot.getKey();
//                if (newMeeting.getStatus().equals("confirmed")) {
//                    int index = keysMessageInFirebase.indexOf(key);
//                    listOfConfirmedMeetings.remove(index);
//                    keysMessageInFirebase.remove(index);
//                    rvAdapter.notifyDataSetChanged();
////                    Log.i("MEETING",newMeeting.getMeetingId());
//                }
//            }
//
//            @Override
//            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
//
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//
//            }
//        });

        Query query = database.orderByChild("customerId").equalTo(mAuth.getCurrentUser().getUid());
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()) {
                    listOfConfirmedMeetings.clear();
                    for (DataSnapshot s : snapshot.getChildren()) {
                        Meeting newMeeting = s.getValue(Meeting.class);
                        if (newMeeting.getStatus().equals("confirmed")) {
                            listOfConfirmedMeetings.add(newMeeting);
                            Log.i("MEETING",newMeeting.getMeetingId());
                        }
                    }
//                    adapter.setListOfRequestedMeetings(listOfRequestedMeetings);
                    rvAdapter.notifyDataSetChanged();
                }
                progressBar.setVisibility(View.GONE);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
//        listOfConfirmedMeetings= getArguments().getParcelableArrayList("CONFIRMEDMEETINGS");


        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_confirmed_meeting_customer, container, false);


        return view;
    }
}
