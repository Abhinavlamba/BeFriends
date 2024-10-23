package com.facebooklogin.application.adapters;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

import com.bumptech.glide.Glide;
import com.facebooklogin.application.R;

import java.util.ArrayList;
import java.util.Objects;

public class ImageAdapter extends PagerAdapter {

    // Context object
    Context context;
    String userId;
    // Array of images
    ArrayList<String> images;

    // Layout Inflater
    LayoutInflater mLayoutInflater;


    // Viewpager Constructor
    public ImageAdapter(Context context, ArrayList<String> images, String userId) {
        this.context = context;
        this.images = images;
        this.userId = userId;
        mLayoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        // return the number of images
        return images.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == ((LinearLayout) object);
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, final int position) {
        // inflating the item.xml
        View itemView = mLayoutInflater.inflate(R.layout.item, container, false);

        // referencing the image view from the item.xml file
        ImageView imageView = (ImageView) itemView.findViewById(R.id.imageViewMain);

        // setting the image in the imageView
        if ((images.get(position)!=null) && (position != 0))
        Glide.with(context).load("https://bitsphere.in/Brig/user_image/"+ userId +"/" + images.get(position)).into(imageView);
        // Adding the View
        else if ((images.get(position)!=null) && (position == 0))
            Glide.with(context).load("https://bitsphere.in/Brig/profile/" + images.get(position)).into(imageView);
        Objects.requireNonNull(container).addView(itemView);

        return itemView;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {

        container.removeView((LinearLayout) object);
    }
}
