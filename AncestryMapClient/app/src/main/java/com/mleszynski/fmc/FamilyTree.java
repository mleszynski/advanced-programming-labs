package com.mleszynski.fmc;

import com.google.android.gms.maps.model.LatLng;
import java.util.*;
import model.*;

public class FamilyTree {
    private static FamilyTree instance;
    private Person user;
    private String authtoken;
    private final Map<String, Person> personsMap = new HashMap<>();
    private final Map<String, Person> childrenMap = new HashMap<>();
    private final Set<String> eventTypes = new HashSet<>();
    private final Set<String> immFamMales = new HashSet<>();
    private final Set<String> immFamFemales = new HashSet<>();
    private final Set<String> dadSideMales = new HashSet<>();
    private final Set<String> momSideMales = new HashSet<>();
    private final Set<String> dadSideFemales = new HashSet<>();
    private final Set<String> momSideFemales = new HashSet<>();
    private boolean isLoggedIn = false;
    private boolean isFromPersonSearch = false;
    private boolean lifeStoryLines = true;
    private boolean familyTreeLines = true;
    private boolean spouseLines = true;
    private boolean fatherSide = true;
    private boolean motherSide = true;
    private boolean maleEvents = true;
    private boolean femaleEvents = true;
    private String selectedEventID = new String();
    private LatLng lastLoc = new LatLng(29.23, -99.9);
    private Map<String, Event> allEventIDs = new HashMap<>();
    private Map<String, ArrayList<Event>> allEvents = new HashMap<>();
    private Map<String, ArrayList<Event>> curEvents = new HashMap<>();

    private FamilyTree() {

    }

    public static FamilyTree getInstance() {
        if (instance == null) {
            instance = new FamilyTree();
        }
        return instance;
    }

    public void clearClient() {
        instance = null;
        user = null;
        authtoken = null;
        personsMap.clear();
        childrenMap.clear();
        eventTypes.clear();
        immFamMales.clear();
        immFamFemales.clear();
        dadSideMales.clear();
        momSideMales.clear();
        dadSideFemales.clear();
        momSideFemales.clear();
        isLoggedIn = false;
        isFromPersonSearch = false;
        lifeStoryLines = true;
        familyTreeLines = true;
        spouseLines = true;
        fatherSide = true;
        motherSide = true;
        maleEvents = true;
        femaleEvents = true;
        selectedEventID = null;
        lastLoc = null;
        allEventIDs.clear();
        allEvents.clear();
        curEvents.clear();
    }

    public void setPersons(Person[] persons) {
        setUser(persons[0]);

        for (int i = 0; i < persons.length; i++) {
            personsMap.put(persons[i].getPersonID(), persons[i]);
        }
        setImmFam();
        setRemainingFam();
    }

    public void setImmFam() {
        if (user.getGender().equals("m")) {
            immFamMales.add(user.getPersonID());
        } else {
            immFamFemales.add(user.getPersonID());
        }

        if (user.getSpouseID() != null) {
            Person spouse = personsMap.get(user.getSpouseID());

            if (spouse.getGender().equals("m")) {
                immFamMales.add(spouse.getPersonID());
            } else {
                immFamFemales.add(spouse.getPersonID());
            }
        }
    }

    public void setRemainingFam() {
        if (user.getFatherID() != null) {
            Person father = personsMap.get(user.getFatherID());
            dadSideMales.add(father.getPersonID());
            childrenMap.put(user.getFatherID(), user);
            setFamilySide(father, true);
        }

        if (user.getMotherID() != null) {
            Person mother = personsMap.get(user.getMotherID());
            momSideFemales.add(mother.getPersonID());
            childrenMap.put(user.getMotherID(), user);
            setFamilySide(mother, false);
        }
    }

    public void setFamilySide(Person curPerson, boolean isDadSide) {
        if (isDadSide) {

            if (curPerson.getFatherID() != null) {
                Person father = personsMap.get(curPerson.getFatherID());
                dadSideMales.add(father.getPersonID());
                childrenMap.put(curPerson.getFatherID(), curPerson);
                setFamilySide(father, true);
            }

            if (curPerson.getMotherID() != null) {
                Person mother = personsMap.get(curPerson.getMotherID());
                dadSideFemales.add(mother.getPersonID());
                childrenMap.put(curPerson.getMotherID(), curPerson);
                setFamilySide(mother, true);
            }
        } else {
            if (curPerson.getFatherID() != null) {
                Person father = personsMap.get(curPerson.getFatherID());
                momSideMales.add(father.getPersonID());
                childrenMap.put(curPerson.getFatherID(), curPerson);
                setFamilySide(father, false);
            }

            if (curPerson.getMotherID() != null) {
                Person mother = personsMap.get(curPerson.getMotherID());
                momSideFemales.add(mother.getPersonID());
                childrenMap.put(curPerson.getMotherID(), curPerson);
                setFamilySide(mother, false);
            }
        }
    }

    public String getRelation(Person curPerson, String personID) {
        String relation = new String();

        if (curPerson.getFatherID() != null) {
            if (curPerson.getFatherID().equals(personID)) relation = "Father";
        }

        if (curPerson.getMotherID() != null) {
            if (curPerson.getMotherID().equals(personID)) relation = "Mother";
        }

        if (curPerson.getSpouseID() != null) {
            if (curPerson.getSpouseID().equals(personID)) relation = "Spouse";
        }

        if (getChildrenMap().containsKey(personID)) {
            if (getChildrenMap().get(personID).getPersonID().equals(personID)) {
                relation = "Child";
            }
        }
        return relation;
    }

    public void setEvents(Event[] events) {
        setLoggedIn(true);
        setSelectedEventID(events[0].getEventID());
        Map<String, ArrayList<Event>> eventMap = new HashMap<>();

        for (int i = 0; i < events.length; i++) {
            allEventIDs.put(events[i].getEventID(), events[i]);
            eventTypes.add(events[i].getEventType().toLowerCase());

            if (!eventMap.containsKey(events[i].getPersonID())) {
                eventMap.put(events[i].getPersonID(), new ArrayList<Event>());
            }
            eventMap.get(events[i].getPersonID()).add(events[i]);
        }

        for (String key : eventMap.keySet()) {
            Set birthSet = new HashSet<>();
            Set deathSet = new HashSet<>();
            ArrayList<Event> tempList = new ArrayList<Event>();

            for (int i = 0; i < eventMap.get(key).size(); i++) {
                Event curEvent = eventMap.get(key).get(i);

                if (curEvent.getEventType().toLowerCase().equals("birth")) {
                    birthSet.add(curEvent);
                } else if (curEvent.getEventType().toLowerCase().equals("death")) {
                    deathSet.add(curEvent);
                } else {

                    if (tempList.size() > 0) {

                        if (curEvent.getYear() < tempList.get(0).getYear()) {
                            tempList.add(0, curEvent);
                        } else if (curEvent.getYear() >= tempList.get(tempList.size() - 1).getYear()) {
                            tempList.add(curEvent);
                        } else {
                            for (int j = 0; j < tempList.size() - 1; j++) {
                                if (tempList.get(j).getYear() <= curEvent.getYear() &&
                                    tempList.get(j + 1).getYear() > curEvent.getYear()) {
                                    tempList.add(j + 1, curEvent);
                                }
                            }
                        }
                    } else {
                        tempList.add(curEvent);
                    }
                }
            }
            ArrayList<Event> finalList = new ArrayList<Event>();
            finalList.addAll(birthSet);
            finalList.addAll(tempList);
            finalList.addAll(deathSet);
            allEvents.put(key, finalList);
        }
    }

    public void remakeCurEvents() {
        curEvents.clear();
        ArrayList<String> curPersonsList = new ArrayList<String>();

        if (maleEvents) curPersonsList.addAll(immFamMales);
        if (femaleEvents) curPersonsList.addAll(immFamFemales);
        if (maleEvents && fatherSide) curPersonsList.addAll(dadSideMales);
        if (maleEvents && motherSide) curPersonsList.addAll(momSideMales);
        if (femaleEvents && fatherSide) curPersonsList.addAll(dadSideFemales);
        if (femaleEvents && motherSide) curPersonsList.addAll(momSideFemales);

        for (int i = 0; i < curPersonsList.size(); i++) {
            String personID = curPersonsList.get(i);
            ArrayList<Event> events = allEvents.get(personID);
            curEvents.put(personID, events);
        }
    }

    public void setUser(Person user) {
        this.user = user;
    }

    public String getAuthtoken() {
        return authtoken;
    }

    public void setAuthtoken(String authtoken) {
        this.authtoken = authtoken;
    }

    public Map<String, Person> getPersonsMap() {
        return personsMap;
    }

    public Map<String, Person> getChildrenMap() {
        return childrenMap;
    }

    public Set<String> getEventTypes() {
        return eventTypes;
    }

    public boolean isLoggedIn() {
        return isLoggedIn;
    }

    public void setLoggedIn(boolean loggedIn) {
        isLoggedIn = loggedIn;
    }

    public boolean isFromPersonSearch() {
        return isFromPersonSearch;
    }

    public void setFromPersonSearch(boolean fromPersonSearch) {
        isFromPersonSearch = fromPersonSearch;
    }

    public boolean isLifeStoryLines() {
        return lifeStoryLines;
    }

    public void setLifeStoryLines(boolean lifeStoryLines) {
        this.lifeStoryLines = lifeStoryLines;
    }

    public boolean isFamilyTreeLines() {
        return familyTreeLines;
    }

    public void setFamilyTreeLines(boolean familyTreeLines) {
        this.familyTreeLines = familyTreeLines;
    }

    public boolean isSpouseLines() {
        return spouseLines;
    }

    public void setSpouseLines(boolean spouseLines) {
        this.spouseLines = spouseLines;
    }

    public boolean isFatherSide() {
        return fatherSide;
    }

    public void setFatherSide(boolean fatherSide) {
        this.fatherSide = fatherSide;
    }

    public boolean isMotherSide() {
        return motherSide;
    }

    public void setMotherSide(boolean motherSide) {
        this.motherSide = motherSide;
    }

    public boolean isMaleEvents() {
        return maleEvents;
    }

    public void setMaleEvents(boolean maleEvents) {
        this.maleEvents = maleEvents;
    }

    public boolean isFemaleEvents() {
        return femaleEvents;
    }

    public void setFemaleEvents(boolean femaleEvents) {
        this.femaleEvents = femaleEvents;
    }

    public String getSelectedEventID() {
        return selectedEventID;
    }

    public void setSelectedEventID(String selectedEventID) {
        this.selectedEventID = selectedEventID;
    }

    public LatLng getLastLoc() {
        return lastLoc;
    }

    public void setLastLoc(LatLng lastLoc) {
        this.lastLoc = lastLoc;
    }

    public Map<String, Event> getAllEventIDs() {
        return allEventIDs;
    }

    public Map<String, ArrayList<Event>> getCurEvents() {
        return curEvents;
    }
}
