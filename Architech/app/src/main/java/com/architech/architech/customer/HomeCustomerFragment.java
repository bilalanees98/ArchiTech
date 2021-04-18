package com.architech.architech.customer;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.architech.architech.R;


public class HomeCustomerFragment extends Fragment {

    LinearLayout profileTile, favouritesTile;
    RelativeLayout exploreTile, generateModelTile;

    public HomeCustomerFragment() {
        // Required empty public constructor
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_home_customer, container, false);


        exploreTile=view.findViewById(R.id.exploreFloorPlansTile);
        generateModelTile=view.findViewById(R.id.generateModelTile);
        favouritesTile=view.findViewById(R.id.favoritesTile);
        profileTile=view.findViewById(R.id.profileTile);



        exploreTile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((MainPageCustomer)getActivity()).switchFragmentOnTileClick(1);
            }
        });

        generateModelTile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((MainPageCustomer)getActivity()).switchFragmentOnTileClick(2);
            }
        });

        favouritesTile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((MainPageCustomer)getActivity()).switchFragmentOnTileClick(3);
            }
        });

        profileTile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((MainPageCustomer)getActivity()).switchFragmentOnTileClick(4);
            }
        });



        return view;
    }
}