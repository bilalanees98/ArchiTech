package com.architech.architech.customer;

import android.content.Context;
import android.net.Uri;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

import com.architech.architech.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class AddFloorPlanViewPagerAdapter extends PagerAdapter {
    private Context context;
    ArrayList<Uri> imageUrls;

    AddFloorPlanViewPagerAdapter(Context context, ArrayList<Uri> imageUrls)
    {
        this.context= context;
        this.imageUrls= imageUrls;
    }

    public void setImageUrls(ArrayList<Uri> imageUrls) {
        this.imageUrls = imageUrls;
    }

    @Override
    public int getCount() {
        if(imageUrls!=null)
        {
            return imageUrls.size();
        }
        return 0;
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view==object;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        ImageView imageView = new ImageView(context);
        imageView.setImageURI(imageUrls.get(position));
        container.addView(imageView);
        return imageView;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((View) object);
    }
}
