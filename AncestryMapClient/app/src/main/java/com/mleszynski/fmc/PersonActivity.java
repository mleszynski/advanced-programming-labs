package com.mleszynski.fmc;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.TextView;
import java.util.ArrayList;
import com.joanzapata.iconify.IconDrawable;
import com.joanzapata.iconify.fonts.FontAwesomeIcons;

import model.*;

public class PersonActivity extends AppCompatActivity {
    private static final int PERSON_POSITION = 0;
    private static final int EVENT_POSITION = 1;
    private String mPersonID = new String();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_person);
        FamilyTree tree = FamilyTree.getInstance();
        this.mPersonID = getIntent().getExtras().getString("personID");
        TextView fNameView = findViewById(R.id.fname_person);
        fNameView.setText(tree.getPersonsMap().get(mPersonID).getFirstName());
        TextView lNameView = findViewById(R.id.lname_person);
        lNameView.setText(tree.getPersonsMap().get(mPersonID).getLastName());
        TextView genderView = findViewById(R.id.gender_person);

        if (tree.getPersonsMap().get(mPersonID).getGender().equals("m")) {
            genderView.setText(R.string.male);
        } else {
            genderView.setText(R.string.female);
        }
        ExpandableListView expandableListView = findViewById(R.id.exp_list_view);
        expandableListView.setAdapter(new ExpandableListAdapter(getFamily(), getEvents()));
    }

    private class ExpandableListAdapter extends BaseExpandableListAdapter {
        private final ArrayList<Person> mFamList;
        private final ArrayList<Event> mEventList;

        ExpandableListAdapter(ArrayList<Person> famList, ArrayList<Event> eventList) {
            this.mFamList = famList;
            this.mEventList = eventList;
        }

        @Override
        public int getGroupCount() {
            return 2;
        }

        @Override
        public int getChildrenCount(int groupPosition) {
            switch (groupPosition) {
                case PERSON_POSITION:
                    return mFamList.size();
                case EVENT_POSITION:
                    return mEventList.size();
                default:
                    throw new IllegalArgumentException("Unrecognized group position " + groupPosition);
            }
        }

        @Override
        public Object getGroup(int groupPosition) {
            switch (groupPosition) {
                case PERSON_POSITION:
                    return "Family";
                case EVENT_POSITION:
                    return "Event";
                default:
                    throw new IllegalArgumentException("Unrecognized group position " + groupPosition);
            }
        }

        @Override
        public Object getChild(int groupPosition, int childPosition) {
            switch (groupPosition) {
                case PERSON_POSITION:
                    return mFamList.get(childPosition);
                case EVENT_POSITION:
                    return mEventList.get(childPosition);
                default:
                    throw new IllegalArgumentException("Unrecognized group position " + groupPosition);
            }
        }

        @Override
        public long getGroupId(int groupPosition) {
            return groupPosition;
        }

        @Override
        public long getChildId(int groupPosition, int childPosition) {
            return childPosition;
        }

        @Override
        public boolean hasStableIds() {
            return false;
        }

        @Override
        public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
            if (convertView == null) {
                convertView = getLayoutInflater().inflate(R.layout.item_group, parent, false);
            }

            TextView title = convertView.findViewById(R.id.listTitle);

            if (groupPosition == PERSON_POSITION) {
                title.setText(R.string.person_fam_title);
            } else {
                title.setText(R.string.person_event_title);
            }
            return convertView;
        }

        @Override
        public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
            View itemView;

            if (groupPosition == PERSON_POSITION) {
                itemView = getLayoutInflater().inflate(R.layout.item_person, parent, false);
                initFamView(itemView, childPosition);
            } else {
                itemView = getLayoutInflater().inflate(R.layout.item_event, parent, false);
                initEventView(itemView, childPosition);
            }
            return itemView;
        }

        @Override
        public boolean isChildSelectable(int groupPosition, int childPosition) {
            return true;
        }

        private void initFamView(View personView, final int childPosition) {
            FamilyTree tree = FamilyTree.getInstance();
            ImageView icon = personView.findViewById(R.id.iconPersonImage);

            if (mFamList.get(childPosition).getGender().equals("m")) {
                Drawable genderIcon = new IconDrawable(PersonActivity.this, FontAwesomeIcons.fa_male)
                        .colorRes(R.color.blue).sizeDp(50);
                icon.setImageDrawable(genderIcon);
            } else {
                Drawable genderIcon = new IconDrawable(PersonActivity.this, FontAwesomeIcons.fa_female)
                        .colorRes(R.color.pink).sizeDp(50);
                icon.setImageDrawable(genderIcon);
            }

            TextView personName = personView.findViewById(R.id.PersonName);
            String name = mFamList.get(childPosition).getFirstName() + " "
                    + mFamList.get(childPosition).getLastName();
            personName.setText(name);
            Person curPerson = tree.getPersonsMap().get(mPersonID);
            TextView personRelation = personView.findViewById(R.id.RelationTitle);
            personRelation.setText(tree.getRelation(curPerson, mFamList.get(childPosition).getPersonID()));
            personView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String tempID = mFamList.get(childPosition).getPersonID();
                    Intent intent = new Intent(PersonActivity.this, PersonActivity.class);
                    intent.putExtra("personID", tempID);
                    startActivity(intent);
                }
            });
        }

        private void initEventView(View eventView, final int childPosition) {
            ImageView icon = eventView.findViewById(R.id.iconEventImage);
            Drawable genderIcon = new IconDrawable(PersonActivity.this, FontAwesomeIcons.fa_map_marker)
                    .colorRes(R.color.dark_green).sizeDp(50);
            icon.setImageDrawable(genderIcon);
            TextView personName = eventView.findViewById(R.id.EventPerson1);
            personName.setText(mEventList.get(childPosition).getEventType());
            TextView space = eventView.findViewById(R.id.EventType1);
            space.setText(" ");
            TextView location = eventView.findViewById(R.id.EventTimePlace1);
            String timePlace = mEventList.get(childPosition).getCity() + ", "
                             + mEventList.get(childPosition).getCountry() + " ("
                             + mEventList.get(childPosition).getYear() + ")";
            location.setText(timePlace);
            eventView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    FamilyTree tree = FamilyTree.getInstance();
                    tree.setFromPersonSearch(true);
                    String newID = mEventList.get(childPosition).getEventID();
                    Intent intent = new Intent(PersonActivity.this, EventActivity.class);
                    intent.putExtra("eventID", newID);
                    startActivity(intent);
                }
            });
        }
    }

    public ArrayList<Person> getFamily() {
        FamilyTree tree = FamilyTree.getInstance();
        ArrayList<Person> result = new ArrayList<Person>();
        Person curPerson = tree.getPersonsMap().get(mPersonID);

        if (curPerson.getFatherID() != null) {
            result.add(tree.getPersonsMap().get(curPerson.getFatherID()));
        }

        if (curPerson.getMotherID() != null) {
            result.add(tree.getPersonsMap().get(curPerson.getMotherID()));
        }

        if (curPerson.getSpouseID() != null) {
            result.add(tree.getPersonsMap().get(curPerson.getSpouseID()));
        }

        if (tree.getChildrenMap().containsKey(curPerson.getPersonID())) {
            result.add(tree.getChildrenMap().get(curPerson.getPersonID()));
        }
        return result;
    }

    public ArrayList<Event> getEvents() {
        FamilyTree tree = FamilyTree.getInstance();
        ArrayList<Event> result = new ArrayList<Event>();

        if (tree.getCurEvents().containsKey(mPersonID)) {
            result.addAll(tree.getCurEvents().get(mPersonID));
        }
        return result;
    }
}