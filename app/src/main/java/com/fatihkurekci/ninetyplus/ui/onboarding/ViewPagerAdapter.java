package com.fatihkurekci.ninetyplus.ui.onboarding;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

import com.fatihkurekci.ninetyplus.R;

public class ViewPagerAdapter extends PagerAdapter {

    Context context;

    int sliderAllImages[] = {
            R.drawable.onboarding_page1_img_bgsecondary,
            R.drawable.onboarding_page2_img_bgsecondary,
            R.drawable.onboarding_page3_img_bgsecondary,
    };

    int sliderAllTitle[] = {
            R.string.onboarding_screen1_title,
            R.string.onboarding_screen2_title,
            R.string.onboarding_screen3_title,
    };

    int sliderAllDesc[] = {
            R.string.onboarding_screen1_description,
            R.string.onboarding_screen2_description,
            R.string.onboarding_screen3_description,
    };

    public ViewPagerAdapter(Context context){
        this.context = context;
    }

    @Override
    public int getCount() {
        return sliderAllTitle.length;
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == (LinearLayout) object;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {

        LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
        View view = layoutInflater.inflate(R.layout.slider_screen,container,false);

        ImageView sliderImage = (ImageView) view.findViewById(R.id.sliderImage);
        TextView sliderTitle = (TextView) view.findViewById(R.id.sliderTitle);
        TextView sliderDesc = (TextView) view.findViewById(R.id.sliderDesc);

        sliderImage.setImageResource(sliderAllImages[position]);
        sliderTitle.setText(this.sliderAllTitle[position]);
        sliderDesc.setText(this.sliderAllDesc[position]);

        container.addView(view);
        return view;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((LinearLayout)object);
    }
}