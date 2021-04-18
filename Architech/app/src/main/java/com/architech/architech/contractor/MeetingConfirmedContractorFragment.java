package com.architech.architech.contractor;

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
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.architech.architech.R;
import com.architech.architech.model.Meeting;

import java.util.ArrayList;

public class MeetingConfirmedContractorFragment extends Fragment {

    RecyclerView recyclerView;
    MeetingConfirmedContractorRvAdapter rvAdapter;
    ArrayList<Meeting> listOfConfirmedMeetings;
    ArrayList<String> keysMessageInFirebase;
    ProgressBar progressBar;

    DatabaseReference database;

    String uid;


    public MeetingConfirmedContractorFragment() {
        // Required empty public constructor
        listOfConfirmedMeetings = new ArrayList<>();
        keysMessageInFirebase = new ArrayList<>();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        uid= FirebaseAuth.getInstance().getUid();
        progressBar= view.findViewById(R.id.progressBarConfirmedMeetingContractor);
        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerViewConfirmedMeetingContractor);
        LinearLayoutManager lm= new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(lm);
        rvAdapter =new MeetingConfirmedContractorRvAdapter(listOfConfirmedMeetings, getActivity());
        recyclerView.setAdapter(rvAdapter);
        recyclerView.setItemAnimator(new DefaultItemAnimator());


        database = FirebaseDatabase.getInstance().getReference().child("Meetings");
        database.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()) {

                    listOfConfirmedMeetings.clear();
                    for (DataSnapshot s : snapshot.getChildren()) {
                        Meeting newMeeting = s.getValue(Meeting.class);
                        if (newMeeting.getStatus().equals("confirmed")) {
                            //if(newMeeting.getContractorId().equals(myProfile))
                            if(newMeeting.getContractorId().equals(uid)) {
                                listOfConfirmedMeetings.add(newMeeting);
//                            Log.i("MEETING",newMeeting.getMeetingId());
                            }
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
        View view= inflater.inflate(R.layout.fragment_confirmed_meeting_contractor, container, false);
        return view;
    }
}
