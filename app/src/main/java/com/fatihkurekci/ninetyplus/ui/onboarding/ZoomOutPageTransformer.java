package com.fatihkurekci.ninetyplus.ui.onboarding;

import android.view.View;

import androidx.viewpager.widget.ViewPager;

public class ZoomOutPageTransformer implements ViewPager.PageTransformer {
    private static final float MIN_SCALE = 0.5f; // Daha küçük bir ölçek
    private static final float MIN_ALPHA = 0.1f; // Daha düşük bir alfa değeri
    private static final int ANIMATION_DURATION = 3000; // Animasyon süresi (milisaniye cinsinden)

    public void transformPage(View view, float position) {
        int pageWidth = view.getWidth();
        int pageHeight = view.getHeight();

        if (position < -1) { // Solunda olan sayfa
            view.setAlpha(0);
        } else if (position <= 1) { // [-1,1] arasındaki sayfa
            // Ölçek ve alfa ayarları
            float scaleFactor = Math.max(MIN_SCALE, 1 - Math.abs(position));
            float vertMargin = pageHeight * (1 - scaleFactor) / 2;
            float horzMargin = pageWidth * (1 - scaleFactor) / 2;
            if (position < 0) {
                view.setTranslationX(horzMargin - vertMargin / 2);
            } else {
                view.setTranslationX(-horzMargin + vertMargin / 2);
            }

            // Ölçekleme
            view.setScaleX(scaleFactor);
            view.setScaleY(scaleFactor);

            // Alfa ayarı
            view.setAlpha(MIN_ALPHA + (scaleFactor - MIN_SCALE) / (1 - MIN_SCALE) * (1 - MIN_ALPHA));
        } else { // Sağında olan sayfa
            view.setAlpha(0);
        }

        // Animasyonun süresini ayarla
        view.animate().setDuration(ANIMATION_DURATION).start();
    }
}