package com.architech.architech.customer;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

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
import com.architech.architech.model.Meeting;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class MeetingRequestedCustomerFragment extends Fragment {
    ArrayList<Meeting> listOfRequestedMeetings;
    ArrayList<String> keysMessageInFirebase;
    DatabaseReference database;

    MeetingRequestedCustomerFragmentRvAdapter rvAdapter;
    RecyclerView recyclerView;
    String uid;
    public MeetingRequestedCustomerFragment() {
        // Required empty public constructor
        this.listOfRequestedMeetings = new ArrayList<>();
        this.keysMessageInFirebase = new ArrayList<>();
        Log.i("MEETING","empty constructor called");

    }
//    public MeetingRequestedCustomerFragment(ArrayList<Meeting> listOfRequestedMeetings){
//        Log.i("MEETING","non empty constructor called");
//        this.listOfRequestedMeetings = new ArrayList<>();
//    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        uid= FirebaseAuth.getInstance().getUid();

        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerViewRequestMeetingCustomer);
        LinearLayoutManager lm= new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(lm);
        rvAdapter =new MeetingRequestedCustomerFragmentRvAdapter(listOfRequestedMeetings, getActivity());
        recyclerView.setAdapter(rvAdapter);
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        database = FirebaseDatabase.getInstance().getReference().child("Meetings");
//        database.addChildEventListener(new ChildEventListener() {
//            @Override
//            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
//                Meeting newMeeting = snapshot.getValue(Meeting.class);
//                String key = snapshot.getKey();
//                if (newMeeting.getStatus().equals("pending")) {
//                    listOfRequestedMeetings.add(newMeeting);
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
//                if (newMeeting.getStatus().equals("pending")) {
//                    int index = keysMessageInFirebase.indexOf(key);
//                    listOfRequestedMeetings.set(index,newMeeting);
//                    rvAdapter.notifyDataSetChanged();
////                    Log.i("MEETING",newMeeting.getMeetingId());
//                }
//            }
//
//            @Override
//            public void onChildRemoved(@NonNull DataSnapshot snapshot) {
//                Meeting newMeeting = snapshot.getValue(Meeting.class);
//                String key = snapshot.getKey();
//                if (newMeeting.getStatus().equals("pending")) {
//                    int index = keysMessageInFirebase.indexOf(key);
//                    listOfRequestedMeetings.remove(index);
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
        database.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()) {
                    listOfRequestedMeetings.clear();
                    for (DataSnapshot s : snapshot.getChildren()) {
                        Meeting newMeeting = s.getValue(Meeting.class);
                        if (newMeeting.getStatus().equals("pending")) {
                            if(newMeeting.getCustomerId().equals(uid)) {
                                listOfRequestedMeetings.add(newMeeting);
                                Log.i("MEETING", newMeeting.getMeetingId());
                            }
                        }
                    }
//                    adapter.setListOfRequestedMeetings(listOfRequestedMeetings);
                    rvAdapter.notifyDataSetChanged();
                }
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
        View view= inflater.inflate(R.layout.fragment_requests_meeting_customer, container, false);
        return view;
    }

}
