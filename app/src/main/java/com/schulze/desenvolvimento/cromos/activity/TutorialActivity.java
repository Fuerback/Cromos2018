package com.schulze.desenvolvimento.cromos.activity;

import android.content.Intent;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.heinrichreimersoftware.materialintro.app.IntroActivity;
import com.heinrichreimersoftware.materialintro.slide.FragmentSlide;
import com.schulze.desenvolvimento.cromos.R;
import com.schulze.desenvolvimento.cromos.helper.PrefManager;

public class TutorialActivity extends IntroActivity {

    private PrefManager prefManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setButtonBackVisible(false);
        setButtonNextVisible(false);

        prefManager = new PrefManager(this);
        if (!prefManager.isFirstTimeLaunch()) {
            startActivity(new Intent(this, MainActivity.class));
            finish();
        }

        addSlide( new FragmentSlide.Builder()
                .background(android.R.color.white)
                .fragment(R.layout.intro_todas)
                .build());

        addSlide( new FragmentSlide.Builder()
                .background(android.R.color.white)
                .fragment(R.layout.intro_tenho)
                .build());

        addSlide( new FragmentSlide.Builder()
                .background(android.R.color.white)
                .fragment(R.layout.intro_falta)
                .build());

        addSlide( new FragmentSlide.Builder()
                .background(android.R.color.white)
                .fragment(R.layout.intro_repetidas)
                .build());

        addSlide( new FragmentSlide.Builder()
                .background(android.R.color.white)
                .fragment(R.layout.intro_barra)
                .canGoForward(false)
                .build());
    }

    public void btInicializaApp(View view){
        prefManager.setFirstTimeLaunch(false);
        startActivity(new Intent(getApplicationContext(), MainActivity.class));
        finish();
    }
}
