package com.architech.architech.contractor;

import android.content.Context;
import android.content.Intent;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import android.net.Uri;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.architech.architech.R;
import com.architech.architech.customer.MeetingConfirmedCustomerFragmentRvAdapter;
import com.architech.architech.model.Meeting;

import java.util.ArrayList;
import java.util.Calendar;

public class MeetingConfirmedContractorRvAdapter extends RecyclerView.Adapter<MeetingConfirmedContractorRvAdapter.MyViewHolder> {
    ArrayList<Meeting> listOfConfirmedMeetings;
    Context context;
    DatabaseReference database;


    public void setListOfConfirmedMeetings(ArrayList<Meeting> listOfConfirmedMeetings) {
        this.listOfConfirmedMeetings = listOfConfirmedMeetings;
    }

    public MeetingConfirmedContractorRvAdapter(ArrayList<Meeting> listOfConfirmedMeetings, Context context) {
        this.listOfConfirmedMeetings = listOfConfirmedMeetings;
        this.context = context;
        this.database = FirebaseDatabase.getInstance().getReference().child("Meetings");
    }


    @NonNull
    @Override
    public MeetingConfirmedContractorRvAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemRow= LayoutInflater.from(parent.getContext()).inflate(R.layout.row_confirmed_meeting_contractor, parent,false);
        return new MeetingConfirmedContractorRvAdapter.MyViewHolder(itemRow);
    }

    @Override
    public void onBindViewHolder(@NonNull MeetingConfirmedContractorRvAdapter.MyViewHolder holder, int position) {
        Meeting m = listOfConfirmedMeetings.get(position);
//        Log.i("MEETING_ADAPTER",m.getMeetingId());
        holder.contractorName.setText(m.getCustomerName());
        holder.meetingTime.setText(String.format("Scheduled for %s", convertTimestamp(m.getTimestamp())));
        holder.meetingStatus.setText(String.format("Status: %s", m.getStatus()));


        holder.meetingContact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//              implement intenting to phone app to make call
                Intent callIntent = new Intent(Intent.ACTION_DIAL);
                callIntent.setData(Uri.parse("tel:"+m.getCustomerNumber()));
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

        TextView contractorName, meetingTime, meetingStatus;
        TextView meetingContact;
        ProgressBar progressBar;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            contractorName = itemView.findViewById(R.id.nameContractorConfirmedMeetingContractorRow);
            meetingTime = itemView.findViewById(R.id.timeConfirmedMeetingContractorRow);
            meetingStatus = itemView.findViewById(R.id.statusConfirmedMeetingContractorRow);
            meetingContact = itemView.findViewById(R.id.contactTextConfirmedMeetingContractorRow);


        }

    }
}
