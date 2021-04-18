package com.architech.architech.customer;

import android.app.Activity;
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
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class ComparisonListPageRvAdapter extends RecyclerView.Adapter<ComparisonListPageRvAdapter.MyViewHolder>{
    ArrayList<FloorPlan> listOfFloorPlans;
    Context context;
    FloorPlan floorPlanOne;

    public ComparisonListPageRvAdapter(ArrayList<FloorPlan> listOfFloorPlans, FloorPlan floorPlanOne, Context context)
    {
        this.listOfFloorPlans=listOfFloorPlans;
        this.context= context;
        this.floorPlanOne= floorPlanOne;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemRow= LayoutInflater.from(context).inflate(R.layout.floorplan_row,parent,false);
        return new MyViewHolder(itemRow);
    }


    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, final int position) {
        holder.title.setText(listOfFloorPlans.get(position).getTitle());
        holder.ownerName.setText(listOfFloorPlans.get(position).getOwnerName());


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
        Picasso.get()
                .load(listOfFloorPlans.get(position).getImageUrl()).fit().centerCrop()
                .placeholder(R.drawable.progress_animation)
                .into(holder.coverImage);

        holder.rowItself.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context,Integer.toString(position)+"", Toast.LENGTH_SHORT).show();
                Intent intent= new Intent(context, ComparisonPage.class);
                intent.putExtra("FLOORPLANONE", floorPlanOne);
                intent.putExtra("FLOORPLANTWO", listOfFloorPlans.get(position));
                context.startActivity(intent);
                ((Activity)context).finish();
            }
        });
    }

    @Override
    public int getItemCount()
    {
        if(listOfFloorPlans!=null){
            return this.listOfFloorPlans.size();
        }
        return 0;

    }

    public void filteredList(ArrayList<FloorPlan> list){
        listOfFloorPlans=new ArrayList(list);


    }


    //what data is in tile
    public class MyViewHolder extends RecyclerView.ViewHolder{


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
