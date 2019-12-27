package com.example.getmybus;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import com.example.getmybus.fragments.SliderFragment;
import com.github.paolorotolo.appintro.AppIntro;
import com.github.paolorotolo.appintro.AppIntroFragment;
import com.github.paolorotolo.appintro.model.SliderPage;
public class SplashActivity extends AppIntro implements SliderFragment.OnFragmentInteractionListener{
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SliderFragment fragment = new SliderFragment();
        SliderFragment fragment1 = new SliderFragment();
        SliderFragment fragment2 = new SliderFragment();
        addSlide(fragment);
        addSlide(fragment1);
        addSlide(fragment2);
        SliderPage sliderPage = new SliderPage();
        sliderPage.setTitle("My title");
        sliderPage.setDescription("My description");
        sliderPage.setImageDrawable(R.drawable.point_green);
        sliderPage.setBgColor(Color.WHITE);
        addSlide(AppIntroFragment.newInstance(sliderPage));
        setBarColor(Color.parseColor("#03c2fc"));
        setSeparatorColor(Color.parseColor("#03c2fc"));
        showSkipButton(true);
        setProgressButtonEnabled(true);
        setVibrate(true);
        setVibrateIntensity(30);
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }

    @Override
    public void onSkipPressed(Fragment currentFragment) {
        super.onSkipPressed(currentFragment);
        // Do something when users tap on Skip button.
    }

    @Override
    public void onDonePressed(Fragment currentFragment) {
        super.onDonePressed(currentFragment);
        // Do something when users tap on Done button.
    }

    @Override
    public void onSlideChanged(@Nullable Fragment oldFragment, @Nullable Fragment newFragment) {
        super.onSlideChanged(oldFragment, newFragment);
        // Do something when the slide changes.
    }
}
