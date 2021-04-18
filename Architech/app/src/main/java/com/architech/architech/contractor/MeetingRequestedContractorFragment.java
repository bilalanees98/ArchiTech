package com.architech.architech.contractor;

import android.app.AlertDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
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

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

public class MeetingRequestedContractorFragment extends Fragment {
    ArrayList<Meeting> listOfRequestedMeetings;
    ArrayList<String> keysMessageInFirebase;
    DatabaseReference database;

    MeetingRequestedContractorFragmentRvAdapter rvAdapter;
    RecyclerView recyclerView;

    String uid;

    long time;

    public MeetingRequestedContractorFragment() {
        // Required empty public constructor
        this.listOfRequestedMeetings = new ArrayList<>();
        this.keysMessageInFirebase = new ArrayList<>();
//        Log.i("MEETING","empty constructor called");

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        uid= FirebaseAuth.getInstance().getUid();
        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerViewRequestMeetingContractor);
        LinearLayoutManager lm= new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(lm);
        rvAdapter =new MeetingRequestedContractorFragmentRvAdapter(listOfRequestedMeetings, getActivity());
        recyclerView.setAdapter(rvAdapter);
        recyclerView.setItemAnimator(new DefaultItemAnimator());





    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_requests_meeting_contractor, container, false);

        database = FirebaseDatabase.getInstance().getReference().child("Meetings");

        database.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()) {
                    listOfRequestedMeetings.clear();
                    for (DataSnapshot s : snapshot.getChildren()) {
                        Meeting newMeeting = s.getValue(Meeting.class);
                        if (newMeeting.getStatus().equals("pending")) {
                            if(newMeeting.getContractorId().equals(uid)) {
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


//
//        //DEMODEMODEMO: BILAL LOOK HERE
//
//        //dialog declared
//        final View dialogView = View.inflate(getActivity(), R.layout.date_time_picker_dialog, null);
//        final AlertDialog alertDialog = new AlertDialog.Builder(getActivity()).create();
//
//        //opens dialog
//        dateTimeDEMO= view.findViewById(R.id.dateTimeButton);
//        dateTimeDEMO.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//
//                alertDialog.setView(dialogView);
//                alertDialog.show();
//
//            }
//
//        });
//
//        //inside dialog set button
//        setMeetingDEMO= dialogView.findViewById(R.id.date_time_set);
//        setMeetingDEMO.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                DatePicker datePicker = (DatePicker) dialogView.findViewById(R.id.date_picker);
//                TimePicker timePicker = (TimePicker) dialogView.findViewById(R.id.time_picker);
//
//                Calendar calendar = new GregorianCalendar(datePicker.getYear(),
//                        datePicker.getMonth(),
//                        datePicker.getDayOfMonth(),
//                        timePicker.getCurrentHour(),
//                        timePicker.getCurrentMinute());
//                //Toast.makeText(getActivity(), "Day: "+datePicker.getDayOfMonth()+"\nMonth: " +datePicker.getMonth() + "\nYear" + datePicker.getYear()+ "\nTime:" +timePicker.getCurrentHour(), Toast.LENGTH_SHORT).show();
//                time = calendar.getTimeInMillis();
//
//               DateFormat df = new SimpleDateFormat("dd/MM/yyyy HH:mm");
//                String dateTime= df.format(time);
//                Toast.makeText(getActivity(), "Time and Date: " + dateTime + " ", Toast.LENGTH_SHORT).show();
//                alertDialog.dismiss();
//            }
//        });

        return view;
    }


}
