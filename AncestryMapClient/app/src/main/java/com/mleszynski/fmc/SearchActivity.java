package com.mleszynski.fmc;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.annotation.NonNull;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import java.util.ArrayList;
import com.joanzapata.iconify.IconDrawable;
import com.joanzapata.iconify.fonts.FontAwesomeIcons;

import model.*;

public class SearchActivity extends AppCompatActivity {
    private static final int PERSON_VIEW = 0;
    private static final int EVENT_VIEW = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        SearchView searchView = findViewById(R.id.search_view);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                RecyclerView recyclerView = findViewById(R.id.recycler_view);
                recyclerView.setLayoutManager(new LinearLayoutManager(SearchActivity.this));
                SearchAdapter adapter = new SearchAdapter(searchPerson(query.toLowerCase()),
                                                          searchEvent(query.toLowerCase()));
                recyclerView.setAdapter(adapter);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
    }

    private class SearchAdapter extends RecyclerView.Adapter<SearchViewHolder> {
        private final ArrayList<Person> mPersons;
        private final ArrayList<Event> mEvents;

        SearchAdapter(ArrayList<Person> persons, ArrayList<Event> events) {
            this.mPersons = persons;
            this.mEvents = events;
        }

        @NonNull
        @Override
        public SearchViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view;

            if (viewType == PERSON_VIEW) {
                view = getLayoutInflater().inflate(R.layout.item_person, parent, false);
            } else {
                view = getLayoutInflater().inflate(R.layout.item_event, parent, false);
            }
            return new SearchViewHolder(view, viewType);
        }

        @Override
        public void onBindViewHolder(@NonNull SearchViewHolder holder, int position) {
            if (position < mPersons.size()) {
                holder.bind(mPersons.get(position));
            } else {
                holder.bind(mEvents.get(position - mPersons.size()));
            }
        }

        @Override
        public int getItemCount() {
            return mPersons.size() + mEvents.size();
        }

        @Override
        public int getItemViewType(int position) {
            return position < mPersons.size() ? PERSON_VIEW : EVENT_VIEW;
        }
    }

    private class SearchViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private final int mViewType;
        private final TextView mName;
        private final TextView mEventType;
        private final TextView mLocation;
        private final ImageView mIcon;
        private Person mPerson;
        private Event mEvent;

        SearchViewHolder(View view, int viewType) {
            super(view);
            this.mViewType = viewType;
            itemView.setOnClickListener(this);

            if (viewType == PERSON_VIEW) {
                mIcon = itemView.findViewById(R.id.iconPersonImage);
                mName = itemView.findViewById(R.id.PersonName);
                mEventType = null;
                mLocation = null;
            } else {
                mIcon = itemView.findViewById(R.id.iconEventImage);
                mName = itemView.findViewById(R.id.EventPerson1);
                mEventType = itemView.findViewById(R.id.EventType1);
                mLocation = itemView.findViewById(R.id.EventTimePlace1);
            }
        }

        @Override
        public void onClick(View v) {
            if (mViewType == PERSON_VIEW) {
                Intent intent = new Intent(SearchActivity.this, PersonActivity.class);
                intent.putExtra("personID", mPerson.getPersonID());
                startActivity(intent);
            } else {
                FamilyTree tree = FamilyTree.getInstance();
                tree.setFromPersonSearch(true);
                Intent intent = new Intent(SearchActivity.this, EventActivity.class);
                intent.putExtra("eventID", mEvent.getEventID());
                startActivity(intent);
            }
        }

        private void bind(Person person) {
            this.mPerson = person;

            if (mPerson.getGender().equals("m")) {
                Drawable genderIcon = new IconDrawable(SearchActivity.this, FontAwesomeIcons.fa_male)
                        .colorRes(R.color.blue).sizeDp(50);
                mIcon.setImageDrawable(genderIcon);
            } else {
                Drawable genderIcon = new IconDrawable(SearchActivity.this, FontAwesomeIcons.fa_female)
                        .colorRes(R.color.pink).sizeDp(50);
                mIcon.setImageDrawable(genderIcon);
            }
            String name = person.getFirstName() + " " + person.getLastName();
            mName.setText(name);
        }

        private void bind(Event event) {
            this.mEvent = event;
            FamilyTree tree = FamilyTree.getInstance();
            Person person = tree.getPersonsMap().get(event.getPersonID());
            Drawable markerIcon = new IconDrawable(SearchActivity.this, FontAwesomeIcons.fa_map_marker)
                    .colorRes(R.color.dark_green).sizeDp(50);
            mIcon.setImageDrawable(markerIcon);
            String name = person.getFirstName() + " " + person.getLastName();
            mName.setText(name);
            mEventType.setText(event.getEventType());
            String location = event.getCity() + ", " + event.getCountry() + " (" + event.getYear() + ")";
            mLocation.setText(location);
        }
    }

    public ArrayList<Event> searchEvent(String query) {
        FamilyTree tree = FamilyTree.getInstance();
        ArrayList<Event> result = new ArrayList<Event>();

        for (String key : tree.getCurEvents().keySet()) {
            Person person = tree.getPersonsMap().get(key);

            if (person.getFirstName().toLowerCase().contains(query) ||
                person.getLastName().toLowerCase().contains(query)) {
                result.addAll(tree.getCurEvents().get(key));
            } else {

                for (int i = 0; i < tree.getCurEvents().get(key).size(); i++) {
                    Event curEvent = tree.getCurEvents().get(key).get(i);

                    if (curEvent.getCountry().toLowerCase().contains(query)) {
                        result.add(curEvent);
                    } else if (curEvent.getCity().toLowerCase().contains(query)) {
                        result.add(curEvent);
                    } else if (curEvent.getEventType().toLowerCase().contains(query)) {
                        result.add(curEvent);
                    } else if (Integer.toString(curEvent.getYear()).contains(query)) {
                        result.add(curEvent);
                    }
                }
            }
        }
        return result;
    }

    public ArrayList<Person> searchPerson(String query) {
        FamilyTree tree = FamilyTree.getInstance();
        ArrayList<Person> result = new ArrayList<Person>();

        for (String key : tree.getPersonsMap().keySet()) {
            Person curPerson = tree.getPersonsMap().get(key);

            if (curPerson.getFirstName().toLowerCase().contains(query)) {
                result.add(curPerson);
            } else if (curPerson.getLastName().toLowerCase().contains(query)) {
                result.add(curPerson);
            }
        }
        return result;
    }
}