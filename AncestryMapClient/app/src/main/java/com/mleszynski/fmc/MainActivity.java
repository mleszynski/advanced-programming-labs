package com.mleszynski.fmc;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import android.os.Bundle;
import com.joanzapata.iconify.Iconify;
import com.joanzapata.iconify.fonts.FontAwesomeModule;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Iconify.with(new FontAwesomeModule());
        setContentView(R.layout.activity_main);
        FragmentManager manager = getSupportFragmentManager();
        Fragment loginFrag = manager.findFragmentById(R.id.main_activity);
        Fragment mapFrag = manager.findFragmentById(R.id.main_activity);

        if (FamilyTree.getInstance().isLoggedIn()) {

            if (mapFrag == null) {
                mapFrag = new MapFragment();
                manager.beginTransaction().add(R.id.main_activity, mapFrag).commit();
            }
        } else {

            if (loginFrag == null) {
                loginFrag = LoginFragment.newInstance(this);
                manager.beginTransaction().add(R.id.main_activity, loginFrag).commit();
            }
        }
    }

    public void loginInit() {
        MapFragment mapFrag = new MapFragment();
        FragmentTransaction fragTrans = getSupportFragmentManager().beginTransaction();
        fragTrans.replace(R.id.main_activity, mapFrag);
        fragTrans.commit();
    }
}