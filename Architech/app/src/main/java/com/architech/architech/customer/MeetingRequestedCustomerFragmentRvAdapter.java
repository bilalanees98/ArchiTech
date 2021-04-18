package com.architech.architech.customer;

import android.content.Context;
import android.text.format.DateFormat;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.architech.architech.R;
import com.architech.architech.model.Meeting;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;

public class MeetingRequestedCustomerFragmentRvAdapter extends RecyclerView.Adapter<MeetingRequestedCustomerFragmentRvAdapter.MyViewHolder> {
    ArrayList<Meeting> listOfRequestedMeetings;
    Context context;
    DatabaseReference database;

    public void setListOfRequestedMeetings(ArrayList<Meeting> listOfRequestedMeetings) {
        this.listOfRequestedMeetings = listOfRequestedMeetings;
    }

    public MeetingRequestedCustomerFragmentRvAdapter(ArrayList<Meeting> listOfRequestedMeetings, Context context) {
        this.listOfRequestedMeetings = listOfRequestedMeetings;
        this.context = context;
        this.database = FirebaseDatabase.getInstance().getReference().child("Meetings");
    }

    @NonNull
    @Override
    public MeetingRequestedCustomerFragmentRvAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemRow= LayoutInflater.from(parent.getContext()).inflate(R.layout.row_requested_meeting_customer, parent,false);
        return new MeetingRequestedCustomerFragmentRvAdapter.MyViewHolder(itemRow);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Meeting m = listOfRequestedMeetings.get(position);
//        Log.i("MEETING_ADAPTER",m.getMeetingId());
        holder.customerName.setText(m.getContractorName());
        holder.meetingTime.setText(String.format("Requested on %s", convertTimestamp(m.getTimestamp())));
        holder.meetingStatus.setText(String.format("Status: %s", m.getStatus()));

        holder.meetingCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseDatabase.getInstance().getReference().child("Meetings")
                        .child(m.getMeetingId()).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()){
                            Toast.makeText(context, "Request cancelled", Toast.LENGTH_SHORT).show();
                            if(listOfRequestedMeetings.size()!=0) {
                                listOfRequestedMeetings.remove(position);
                            }
                            notifyDataSetChanged();
                        }
                        else{
                            Toast.makeText(context, "Could not cancel request", Toast.LENGTH_SHORT).show();

                        }
                    }
                });
            }
        });


    }
    public String convertTimestamp(String timestamp){
        Calendar cal = Calendar.getInstance(((AppCompatActivity)context).getResources().getConfiguration().locale);
        cal.setTimeInMillis(Long.parseLong(timestamp) * 1000L);

        String dateString;
        if(DateUtils.isToday(Long.parseLong(timestamp) * 1000L)){
            dateString = DateFormat.format("h:mm a", cal).toString();
        }
        else {
            dateString = DateFormat.format("dd-MM-yyyy h:mm a", cal).toString();
        }

        return dateString;
    }

    @Override
    public int getItemCount() {
        if(listOfRequestedMeetings==null)
        {
            return 0;
        }
        return listOfRequestedMeetings.size();



    }
    //what data is in tile
    public class MyViewHolder extends RecyclerView.ViewHolder{

        TextView customerName, meetingTime, meetingStatus;
        TextView meetingCancel;
        ProgressBar progressBar;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            customerName = itemView.findViewById(R.id.nameContractorRequestedMeetingCustomerRow);
            meetingTime = itemView.findViewById(R.id.timeRequestedMeetingCustomerRow);
            meetingStatus = itemView.findViewById(R.id.statusRequestedMeetingCustomerRow);
            meetingCancel = itemView.findViewById(R.id.cancelTextRequestedMeetingCustomerRow);



        }

    }
}

