package com.architech.architech.customer;

import android.content.Context;
import android.content.Intent;
import android.renderscript.Sampler;
import android.text.format.DateFormat;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.architech.architech.Notifier;
import com.architech.architech.R;
import com.architech.architech.model.Contractor;
import com.architech.architech.model.Customer;
import com.architech.architech.model.Favourite;
import com.architech.architech.model.FloorPlan;
import com.architech.architech.model.FloorPlanRecyclerObject;
import com.architech.architech.model.Meeting;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Calendar;

public class ContractorsPageCustomerRvAdapter extends RecyclerView.Adapter<ContractorsPageCustomerRvAdapter.MyViewHolder> {
    Context context;
    FloorPlanRecyclerObject floorplan;
    ArrayList<Contractor> listOfcontractors;
    String uid;
    Customer myProfile;
    public ContractorsPageCustomerRvAdapter(ArrayList<Contractor> listOfFloorPlans,FloorPlanRecyclerObject floorplan, Context context) {
        this.listOfcontractors=listOfFloorPlans;
        this.floorplan=floorplan;
        this.context= context;
        this.uid = FirebaseAuth.getInstance().getCurrentUser().getUid();

        FirebaseDatabase.getInstance().getReference().child("Users").child(uid)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if(snapshot.exists()){
                            myProfile = snapshot.getValue(Customer.class);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
    }

    public void setListOfFloorPlans(ArrayList<Contractor> listOfFloorPlans) {
        this.listOfcontractors = listOfFloorPlans;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemRow= LayoutInflater.from(parent.getContext()).inflate(R.layout.row_contractor_page_customer,parent,false);
        return new ContractorsPageCustomerRvAdapter.MyViewHolder(itemRow);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Contractor c = listOfcontractors.get(position);
        holder.name.setText(c.getName());
        holder.estimate.setText(estimateCost(c));

        holder.button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatabaseReference database = FirebaseDatabase.getInstance().getReference().child("Meetings");
                database.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {

                            Boolean meetingPresentCheck = false;
                            for(DataSnapshot s: snapshot.getChildren()){
                                Meeting tempmMeeting = s.getValue(Meeting.class);
                                if(tempmMeeting.getContractorId().equals(c.getUid())
                                        && tempmMeeting.getCustomerId().equals(uid)){
                                    meetingPresentCheck = true;
                                }
                            }
                            if(meetingPresentCheck){
                                Toast.makeText(context,"Meeting already requested/confirmed"
                                        , Toast.LENGTH_SHORT).show();
                            }
                            else{
                                String key=database.push().getKey();
                                Long tsLong = System.currentTimeMillis()/1000;
                                String timestamp = tsLong.toString();
                                Meeting newMeeting=new Meeting(key,uid,c.getUid(),timestamp,"pending",
                                        c.getName(),c.getPhone(),myProfile.getPhone(),myProfile.getName(),myProfile.getEmail(),c.getEmail());
                                database.child(key).setValue(newMeeting).addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        Toast.makeText(context,"Meeting request sent",Toast.LENGTH_SHORT).show();
                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Toast.makeText(context,"Meeting request could not be sent",Toast.LENGTH_SHORT)
                                        .show();
                                    }
                                });

                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }
        });
    }


    public String estimateCost(Contractor contractor){
        String result="PKR ";

        int length=Integer.parseInt(floorplan.getLength());
        int width=Integer.parseInt(floorplan.getWidth());

        int highestRate=Integer.parseInt(contractor.getRateOne());
        int lowestRate=Integer.parseInt(contractor.getRateThree());


        int lowestEstimate=length*width*lowestRate;
        int highestEstimate=length*width*highestRate;


        result+=String.valueOf(lowestEstimate);
        result+=" - ";
        result+=String.valueOf(highestEstimate);




        return result;
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
    public int getItemCount()
    {
        if(listOfcontractors!=null){
            return this.listOfcontractors.size();
        }
        return 0;

    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView name, estimate,button;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            name=itemView.findViewById(R.id.nameContractorRequestedMeetingCustomerRow);
            estimate=itemView.findViewById(R.id.estimateContractorPageCustomerRow);
            button=itemView.findViewById(R.id.requestMeetingContractorPageCustomerRow);



        }
    }
}
