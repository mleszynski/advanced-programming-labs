package com.mleszynski.fmc;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import android.os.Bundle;

public class EventActivity extends AppCompatActivity {
    private String mEventID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event);
        this.mEventID = getIntent().getExtras().getString("eventID");
        FamilyTree tree = FamilyTree.getInstance();
        tree.setSelectedEventID(mEventID);
        FragmentManager manager = getSupportFragmentManager();
        MapFragment mapFrag = (MapFragment)manager.findFragmentById(R.id.event_activity_container);

        if (mapFrag == null) {
            mapFrag = new MapFragment();
            manager.beginTransaction().add(R.id.event_activity_container, mapFrag).commit();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        FamilyTree tree = FamilyTree.getInstance();
        tree.setFromPersonSearch(false);
    }
}