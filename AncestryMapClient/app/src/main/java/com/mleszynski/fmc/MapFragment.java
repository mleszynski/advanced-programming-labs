package com.mleszynski.fmc;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import java.util.ArrayList;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.joanzapata.iconify.IconDrawable;
import com.joanzapata.iconify.Iconify;
import com.joanzapata.iconify.fonts.FontAwesomeIcons;
import com.joanzapata.iconify.fonts.FontAwesomeModule;

import model.*;

public class MapFragment extends Fragment implements OnMapReadyCallback {
    private View mView;
    private GoogleMap mMap;
    private String mPersonID = new String();
    private ArrayList<Polyline> mPolylines = new ArrayList<Polyline>();

    public MapFragment() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        super.onCreate(savedInstanceState);
        Iconify.with(new FontAwesomeModule());
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        FamilyTree tree = FamilyTree.getInstance();

        if (tree.isFromPersonSearch()) {
            setHasOptionsMenu(false);
        } else {
            inflater.inflate(R.menu.main_menu, menu);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem menu) {
        switch(menu.getItemId()) {
            case R.id.search_menu_icon:
                Intent intent = new Intent(getActivity(), SearchActivity.class);
                startActivity(intent);
                return true;
            case R.id.settings_menu_icon:
                Intent intent1 = new Intent(getActivity(), SettingsActivity.class);
                startActivity(intent1);
                return true;
            default:
                return super.onOptionsItemSelected(menu);
        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater layoutInflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(layoutInflater, container, savedInstanceState);
        this.mView = layoutInflater.inflate(R.layout.fragment_map, container, false);
        SupportMapFragment mapFrag = (SupportMapFragment)getChildFragmentManager().findFragmentById(R.id.map);
        mapFrag.getMapAsync(this);
        return mView;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        this.mMap = googleMap;
        FamilyTree tree = FamilyTree.getInstance();
        tree.remakeCurEvents();

        if (!tree.isFromPersonSearch()) {
            mMap.moveCamera(CameraUpdateFactory.newLatLng(tree.getLastLoc()));
        }

        if (tree.isFromPersonSearch()) {
            float lat = tree.getAllEventIDs().get(tree.getSelectedEventID()).getLatitude();
            float lng = tree.getAllEventIDs().get(tree.getSelectedEventID()).getLongitude();
            LatLng latLng = new LatLng(lat, lng);
            mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
        }

        for (String key : tree.getCurEvents().keySet()) {

            for (int i = 0; i < tree.getCurEvents().get(key).size(); i++) {
                Event curEvent = tree.getCurEvents().get(key).get(i);
                LatLng location = new LatLng(curEvent.getLatitude(), curEvent.getLongitude());
                MarkerOptions options = new MarkerOptions().position(location);
                float color = getColor(curEvent.getEventType());
                options.icon(BitmapDescriptorFactory.defaultMarker(color));
                this.mMap.addMarker(options).setTag(curEvent);
            }
        }

        this.mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                FamilyTree tree = FamilyTree.getInstance();
                if (!tree.isFromPersonSearch()) {
                    tree.setLastLoc(marker.getPosition());
                }
                doClick(marker);
                return false;
            }
        });
        LinearLayout eventDisplay = (LinearLayout) mView.findViewById(R.id.EventDisplay);
        eventDisplay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), PersonActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.putExtra("personID", getmPersonID());
                startActivity(intent);
            }
        });
    }

    public void doClick(Marker marker) {
        FamilyTree tree = FamilyTree.getInstance();

        for (Polyline line : mPolylines) {
            line.remove();
        }
        mPolylines.clear();
        Event selectedEvent = (Event)marker.getTag();
        Person selectedPerson = tree.getPersonsMap().get(selectedEvent.getPersonID());
        setmPersonID(selectedPerson.getPersonID());
        String name = selectedPerson.getFirstName() + " " + selectedPerson.getLastName();
        String eventType = selectedEvent.getEventType();
        String timePlace = selectedEvent.getCity() + ", " + selectedEvent.getCountry() + " ("
                + selectedEvent.getYear() + ")";
        ImageView icon = mView.findViewById(R.id.icon_image);
        TextView textName = mView.findViewById(R.id.person_event);
        TextView textEventType = mView.findViewById(R.id.event_type);
        TextView textTimePlace = mView.findViewById(R.id.event_location);

        if (selectedPerson.getGender().equals("m")) {
            Drawable maleIcon = new IconDrawable(getActivity(), FontAwesomeIcons.fa_male)
                    .colorRes(R.color.blue).sizeDp(50);
            icon.setImageDrawable(maleIcon);
        } else {
            Drawable femIcon = new IconDrawable(getActivity(), FontAwesomeIcons.fa_female)
                    .colorRes(R.color.pink).sizeDp(50);
            icon.setImageDrawable(femIcon);
        }
        textName.setText(name);
        textEventType.setText(eventType);
        textTimePlace.setText(timePlace);
        drawLines(marker.getPosition(), selectedPerson);
    }

    public void addLine(LatLng start, LatLng stop, int color, int width) {
        Polyline newLine = mMap.addPolyline(new PolylineOptions()
                .clickable(false)
                .add(new LatLng(start.latitude, start.longitude),
                        new LatLng(stop.latitude, stop.longitude))
                .width(width).color(color));
        this.mPolylines.add(newLine);
    }

    public void drawLines(LatLng source, Person person) {
        int curWidth = 16;
        FamilyTree tree = FamilyTree.getInstance();

        if (tree.isSpouseLines()) {

            if (tree.getCurEvents().containsKey(person.getSpouseID())) {
                LatLng destination = getLatLng(person.getSpouseID(), 0);
                addLine(source, destination, Color.RED, curWidth);
            }
        }

        if (tree.isLifeStoryLines()) {

            if (tree.getCurEvents().get(person.getPersonID()).size() > 1) {

                for (int i = 0; i < tree.getCurEvents().get(person.getPersonID()).size() - 1; i++) {
                    LatLng tempSource = getLatLng(person.getPersonID(), i);
                    LatLng tempDestination = getLatLng(person.getPersonID(), i + 1);
                    addLine(tempSource, tempDestination, Color.YELLOW, curWidth);
                }
            }
        }

        if (tree.isFamilyTreeLines()) {

            if (tree.getCurEvents().containsKey(person.getFatherID())) {
                LatLng destination = getLatLng(person.getFatherID(), 0);
                addLine(source, destination, Color.GREEN, curWidth);
                Person dad = tree.getPersonsMap().get(person.getFatherID());
                drawParentLines(destination, dad, curWidth);
            }

            if (tree.getCurEvents().containsKey(person.getMotherID())) {
                LatLng destination = getLatLng(person.getMotherID(), 0);
                addLine(source, destination, Color.GREEN, curWidth);
                Person mom = tree.getPersonsMap().get(person.getMotherID());
                drawParentLines(destination, mom, curWidth);
            }
        }
    }

    public void drawParentLines(LatLng source, Person person, int curWidth) {
        FamilyTree tree = FamilyTree.getInstance();

        if (curWidth > 4) {
            curWidth = (curWidth/2);
        }

        if (tree.getCurEvents().containsKey(person.getFatherID())) {
            LatLng destination = getLatLng(person.getFatherID(), 0);
            addLine(source, destination, Color.GREEN, curWidth);
            Person dad = tree.getPersonsMap().get(person.getFatherID());
            drawParentLines(destination, dad, curWidth);
        }

        if (tree.getCurEvents().containsKey(person.getMotherID())) {
            LatLng destination = getLatLng(person.getMotherID(), 0);
            addLine(source, destination, Color.GREEN, curWidth);
            Person mom = tree.getPersonsMap().get(person.getMotherID());
            drawParentLines(destination, mom, curWidth);
        }
    }

    public LatLng getLatLng(String personID, int location) {
        FamilyTree tree = FamilyTree.getInstance();
        LatLng result = new LatLng(tree.getCurEvents().get(personID).get(location).getLatitude(),
                                   tree.getCurEvents().get(personID).get(location).getLongitude());
        return result;
    }

    public float getColor(String eventType) {
        int location = 0;
        FamilyTree tree = FamilyTree.getInstance();

        for (String type : tree.getEventTypes()) {
            if (type.equals(eventType.toLowerCase())) break;
            location++;
        }
        float result = (360/tree.getEventTypes().size())*location;
        return result;
    }

    public String getmPersonID() {
        return mPersonID;
    }

    public void setmPersonID(String mPersonID) {
        this.mPersonID = mPersonID;
    }
}