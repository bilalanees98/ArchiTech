package com.architech.architech.contractor;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.text.format.DateFormat;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.architech.architech.Notifier;
import com.architech.architech.R;
import com.architech.architech.model.Meeting;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashMap;

public class MeetingRequestedContractorFragmentRvAdapter extends RecyclerView.Adapter<MeetingRequestedContractorFragmentRvAdapter.MyViewHolder> {
    ArrayList<Meeting> listOfRequestedMeetings;
    Context context;
    DatabaseReference database;

    public void setListOfRequestedMeetings(ArrayList<Meeting> listOfRequestedMeetings) {
        this.listOfRequestedMeetings = listOfRequestedMeetings;
    }

    public MeetingRequestedContractorFragmentRvAdapter(ArrayList<Meeting> listOfRequestedMeetings, Context context) {
        this.listOfRequestedMeetings = listOfRequestedMeetings;
        this.context = context;
        this.database = FirebaseDatabase.getInstance().getReference().child("Meetings");
    }
    @NonNull
    @Override
    public MeetingRequestedContractorFragmentRvAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemRow= LayoutInflater.from(parent.getContext()).inflate(R.layout.row_requested_meeting_contractor, parent,false);
        return new MeetingRequestedContractorFragmentRvAdapter.MyViewHolder(itemRow);
    }

    @Override
    public void onBindViewHolder(@NonNull MeetingRequestedContractorFragmentRvAdapter.MyViewHolder holder, int position) {
        Meeting m = listOfRequestedMeetings.get(position);
//        Log.i("MEETING_ADAPTER",m.getMeetingId());
        holder.contractorName.setText(m.getCustomerName());
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
//                            String message="Your request for meeting with "+m.getContractorName()+" sent on "+convertTimestamp(m.getTimestamp())+" has been declined";
//                            Notifier notifier=new Notifier();
//                            notifier.sendNotification(m.getCustomerEmail(),message,"meetingcancelledbycontractor");

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

        holder.meetingConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Activity activity = (Activity)context;
                final View dialogView = View.inflate(activity, R.layout.date_time_picker_dialog, null);
                final AlertDialog alertDialog = new AlertDialog.Builder(activity).create();

                alertDialog.setView(dialogView);
                alertDialog.show();
                Button setMeeting= dialogView.findViewById(R.id.date_time_set);

                setMeeting.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        DatePicker datePicker = (DatePicker) dialogView.findViewById(R.id.date_picker);
                        TimePicker timePicker = (TimePicker) dialogView.findViewById(R.id.time_picker);
                        datePicker.setMinDate(System.currentTimeMillis() - 1000);
                        Calendar calendar = new GregorianCalendar(datePicker.getYear(),
                                datePicker.getMonth(),
                                datePicker.getDayOfMonth(),
                                timePicker.getCurrentHour(),
                                timePicker.getCurrentMinute());
                        //Toast.makeText(getActivity(), "Day: "+datePicker.getDayOfMonth()+"\nMonth: " +datePicker.getMonth() + "\nYear" + datePicker.getYear()+ "\nTime:" +timePicker.getCurrentHour(), Toast.LENGTH_SHORT).show();
                        long time = calendar.getTimeInMillis();

                        java.text.DateFormat df = new SimpleDateFormat("dd/MM/yyyy HH:mm");
                        String dateTime= df.format(time);
                        Toast.makeText(activity, "Time and Date: " + dateTime + " ", Toast.LENGTH_SHORT).show();

                        HashMap<String,Object> hashMap = new HashMap();
                        hashMap.put("status","confirmed");
                        hashMap.put("timestamp",Long.toString(time));
                        FirebaseDatabase.getInstance().getReference().child("Meetings")
                                .child(m.getMeetingId())
                                .updateChildren(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if(task.isSuccessful()){
//                                    String message="Your meeting with "+m.getContractorName()+" has been scheduled on "+df.format(time);
//                                    Notifier notifier=new Notifier();
//                                    notifier.sendNotification(m.getCustomerEmail(),message,"meetingcancelledbycontractor");

                                    Toast.makeText(activity, "Time and Date: " + dateTime + " ", Toast.LENGTH_SHORT).show();

                                }
                                else{
                                    Toast.makeText(activity, "could not update status to confirm", Toast.LENGTH_SHORT).show();

                                }
                            }
                        });


                        alertDialog.dismiss();
                    }
                });

            }
        });


        //DEMODEMODEMO: BILAL LOOK HERE

//        //dialog declared
//        Activity activity = (Activity)context;
//        final View dialogView = View.inflate(activity, R.layout.date_time_picker_dialog, null);
//        final AlertDialog alertDialog = new AlertDialog.Builder(activity).create();
//
//        //opens dialog
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
//                java.text.DateFormat df = new SimpleDateFormat("dd/MM/yyyy HH:mm");
//                String dateTime= df.format(time);
//                Toast.makeText(getActivity(), "Time and Date: " + dateTime + " ", Toast.LENGTH_SHORT).show();
//                alertDialog.dismiss();
//            }
//        });





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

        TextView contractorName, meetingTime, meetingStatus;
        TextView meetingCancel,meetingConfirm;
        ProgressBar progressBar;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            contractorName = itemView.findViewById(R.id.nameContractorRequestedMeetingContractorRow);
            meetingTime = itemView.findViewById(R.id.timeRequestedMeetingContractorRow);
            meetingStatus = itemView.findViewById(R.id.statusRequestedMeetingContractorRow);
            meetingCancel = itemView.findViewById(R.id.cancelTextRequestedMeetingContractorRow);
            meetingConfirm = itemView.findViewById(R.id.confirmTextRequestedMeetingContractorRow);

        }

    }
}
