package com.architech.architech.customer;

import android.content.Context;
import android.content.Intent;
import android.view.ContextMenu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

import com.architech.architech.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class FloorPlanDetailsViewPagerAdapter extends PagerAdapter {
    private Context context;
    ArrayList<String> imageUrls;

    FloorPlanDetailsViewPagerAdapter (Context context, ArrayList<String> imageUrls)
    {
        this.context= context;
        this.imageUrls= imageUrls;
    }

    @Override
    public int getCount() {
        return imageUrls.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view==object;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        ImageView imageView = new ImageView(context);
        Picasso.get()
                .load(imageUrls.get(position))
                .fit()
                .centerInside()
                .placeholder(R.drawable.progress_animation)
                .into(imageView);
        container.addView(imageView);


        return imageView;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((View) object);
    }
}
