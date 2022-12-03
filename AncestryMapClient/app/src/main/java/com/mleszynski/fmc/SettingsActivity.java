package com.mleszynski.fmc;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.RelativeLayout;
import android.widget.Switch;

public class SettingsActivity extends AppCompatActivity {
    Context mContext = this;
    FamilyTree mTree = FamilyTree.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        Switch lifeEvent = (Switch)findViewById(R.id.settings_life_event_switch);

        if (mTree.isLifeStoryLines()) lifeEvent.setChecked(true);
        lifeEvent.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) mTree.setLifeStoryLines(true);
                else mTree.setLifeStoryLines(false);
            }
        });
        Switch familyEvent = (Switch)findViewById(R.id.settings_fam_event_switch);

        if (mTree.isFamilyTreeLines()) familyEvent.setChecked(true);
        familyEvent.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) mTree.setFamilyTreeLines(true);
                else mTree.setFamilyTreeLines(false);
            }
        });
        Switch spouseEvent = (Switch)findViewById(R.id.settings_spouse_event_switch);

        if (mTree.isSpouseLines()) spouseEvent.setChecked(true);
        spouseEvent.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) mTree.setSpouseLines(true);
                else mTree.setSpouseLines(false);
            }
        });
        Switch fatherEvent = (Switch)findViewById(R.id.settings_dad_event_switch);

        if (mTree.isFatherSide()) fatherEvent.setChecked(true);
        fatherEvent.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) mTree.setFatherSide(true);
                else mTree.setFatherSide(false);
            }
        });
        Switch motherEvent = (Switch)findViewById(R.id.settings_mom_event_switch);

        if (mTree.isMotherSide()) motherEvent.setChecked(true);
        motherEvent.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) mTree.setMotherSide(true);
                else mTree.setMotherSide(false);
            }
        });
        Switch maleEvent = (Switch)findViewById(R.id.settings_male_event_switch);

        if (mTree.isMaleEvents()) maleEvent.setChecked(true);
        maleEvent.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) mTree.setMaleEvents(true);
                else mTree.setMaleEvents(false);
            }
        });
        Switch femaleEvent = (Switch)findViewById(R.id.settings_fem_event_switch);

        if (mTree.isFemaleEvents()) femaleEvent.setChecked(true);
        femaleEvent.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) mTree.setFemaleEvents(true);
                else mTree.setFemaleEvents(false);
            }
        });
        RelativeLayout logout = (RelativeLayout)findViewById(R.id.logout_layout);
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FamilyTree tree = FamilyTree.getInstance();
                tree.clearClient();
                Intent intent = new Intent(mContext, MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });
    }
}