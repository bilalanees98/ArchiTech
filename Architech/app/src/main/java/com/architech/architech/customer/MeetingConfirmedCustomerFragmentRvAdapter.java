package com.architech.architech.customer;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.architech.architech.R;
import com.architech.architech.model.Meeting;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

public class MeetingConfirmedCustomerFragmentRvAdapter extends RecyclerView.Adapter<MeetingConfirmedCustomerFragmentRvAdapter.MyViewHolder> {
    ArrayList<Meeting> listOfConfirmedMeetings;
    Context context;
    DatabaseReference database;

    public void setListOfConfirmedMeetings(ArrayList<Meeting> listOfConfirmedMeetings) {
        this.listOfConfirmedMeetings = listOfConfirmedMeetings;
    }

    public MeetingConfirmedCustomerFragmentRvAdapter(ArrayList<Meeting> listOfConfirmedMeetings, Context context) {
        this.listOfConfirmedMeetings = listOfConfirmedMeetings;
        this.context = context;
        this.database = FirebaseDatabase.getInstance().getReference().child("Meetings");
    }

    @NonNull
    @Override
    public MeetingConfirmedCustomerFragmentRvAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemRow= LayoutInflater.from(parent.getContext()).inflate(R.layout.row_confirmed_meeting_customer, parent,false);
        return new MeetingConfirmedCustomerFragmentRvAdapter.MyViewHolder(itemRow);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Meeting m = listOfConfirmedMeetings.get(position);
//        Log.i("MEETING_ADAPTER",m.getMeetingId());
        holder.customerName.setText(m.getContractorName());
        holder.meetingTime.setText(String.format("Scheduled for %s", convertTimestamp(m.getTimestamp())));
        holder.meetingStatus.setText(String.format("Status: %s", m.getStatus()));


        holder.meetingContact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//              implement intenting to phone app to make call
                Intent callIntent = new Intent(Intent.ACTION_DIAL);
                callIntent.setData(Uri.parse("tel:"+m.getContractorNumber()));
                context.startActivity(callIntent);

                Toast.makeText(context,"Open Dialer",Toast.LENGTH_SHORT).show();
            }
        });
    }
    public String convertTimestamp(String timestamp){

        Calendar cal = Calendar.getInstance(((AppCompatActivity)context).getResources().getConfiguration().locale);
        DateFormat df = new SimpleDateFormat("dd/MM/yyyy - hh:mm a");
        Long time= Long.parseLong(timestamp);
        String dateTime= df.format(time);

        /*cal.setTimeInMillis(Long.parseLong(timestamp) * 1000L);

        String dateString;
        if(DateUtils.isToday(Long.parseLong(timestamp) * 1000L)){
            dateString = DateFormat.format("h:mm a", cal).toString();
        }
        else {
            dateString = DateFormat.format("dd-MM-yyyy h:mm a", cal).toString();
        }*/
        return dateTime;
    }

    @Override
    public int getItemCount() {
        if(listOfConfirmedMeetings==null)
        {
            return 0;
        }
        return listOfConfirmedMeetings.size();



    }
    //what data is in tile
    public class MyViewHolder extends RecyclerView.ViewHolder{

        TextView customerName, meetingTime, meetingStatus;
        TextView meetingContact;
        ProgressBar progressBar;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            customerName = itemView.findViewById(R.id.nameContractorConfirmedMeetingCustomerRow);
            meetingTime = itemView.findViewById(R.id.timeConfirmedMeetingCustomerRow);
            meetingStatus = itemView.findViewById(R.id.statusConfirmedMeetingCustomerRow);
            meetingContact = itemView.findViewById(R.id.contactTextConfirmedMeetingCustomerRow);


        }

    }
}

