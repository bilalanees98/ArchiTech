package com.architech.architech.customer;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.architech.architech.R;
import com.architech.architech.model.FloorPlan;
import com.architech.architech.model.FloorPlanRecyclerObject;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class ProfileCustomerFragmentRvAdapter extends RecyclerView.Adapter<ProfileCustomerFragmentRvAdapter.MyViewHolder>{


    ArrayList<FloorPlan> listOfFloorPlans;
    Context context;

    public ProfileCustomerFragmentRvAdapter(ArrayList<FloorPlan> listOfFloorPlans, Context context)
    {
        this.listOfFloorPlans=listOfFloorPlans;
        this.context= context;
        //System.out.println("size: " + this.listOfFloorPlans.size());
        //this.listOfFloorPlans.get(0).display();
        //this.listOfFloorPlans.get(1).display();
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemRow= LayoutInflater.from(parent.getContext()).inflate(R.layout.floorplan_row,parent,false);
        return new ProfileCustomerFragmentRvAdapter.MyViewHolder(itemRow);
    }

    @Override
    public void onBindViewHolder(@NonNull ProfileCustomerFragmentRvAdapter.MyViewHolder holder, int position) {
        holder.title.setText(listOfFloorPlans.get(position).getTitle());
        holder.ownerName.setText("You"); //in profile page we dont need this view. Dont have to write owner name


        final String siz= listOfFloorPlans.get(position).getLength() + "x" + listOfFloorPlans.get(position).getWidth() + " ft";
        holder.size.setText(siz);


        final String bed= listOfFloorPlans.get(position).getBedrooms();
        holder.bedrooms.setText(bed);

        final String bath= listOfFloorPlans.get(position).getBathrooms();
        holder.bathrooms.setText(bath);

        final String car= listOfFloorPlans.get(position).getNoOfCars();
        holder.noOfCars.setText(car);

//        //Image Stuff
//        int resIdCoverImage =  context.getResources().getIdentifier(listOfFloorPlans.get(position).getPaths().get(0), null, context.getPackageName());
//        holder.coverImage.setImageResource(resIdCoverImage);
        if(listOfFloorPlans.get(position).getImageUrl()!=null) {
            Picasso.get().load(listOfFloorPlans.get(position).getImageUrl()).fit().centerCrop().placeholder(R.drawable.progress_animation).into(holder.coverImage);
        }

        holder.rowItself.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(v.getContext(),Integer.toString(position)+"", Toast.LENGTH_SHORT).show();
                //TODO: my floor plan detail/edit page
            }
        });
    }

    @Override
    public int getItemCount() {
        if(listOfFloorPlans!=null) {
            return listOfFloorPlans.size();
        }
        return 0;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView title, ownerName, size, bedrooms, bathrooms, noOfCars;
        ImageView coverImage;
        LinearLayout rowItself;


        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            title=itemView.findViewById(R.id.row_title);
            ownerName=itemView.findViewById(R.id.row_owner_name);
            size=itemView.findViewById(R.id.row_size);
            bedrooms=itemView.findViewById(R.id.row_bedrooms);
            bathrooms= itemView.findViewById(R.id.row_bathrooms);
            noOfCars= itemView.findViewById(R.id.row_cars);
            coverImage= itemView.findViewById(R.id.row_cover_image);
            rowItself= itemView.findViewById(R.id.floorPlanRow);


        }
    }
}
