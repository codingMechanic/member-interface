package de.lsvn.model;

public class Event {

    private int id;
    private int mitgliederId;
    private String name;
    private String start;
    private String end;
    
    public int getId() {
        return id;
    }
    
    public void setId(int eventid) {
        this.id = eventid;
    }

    public int getMitgliederId() {
        return mitgliederId;
    }

    public void setMitgliederId(int mitgliederId) {
        this.mitgliederId = mitgliederId;
    }
    
    public String getName() {
        return name;
    }
    
    public void setName(String lastName) {
        this.name = lastName;
    }
    
    public String getStart() {
        return start;
    }
    
    public void setStart(String start) {
        this.start = start;
    }
    
    public String getEnd() {
        return end;
    }
    
    public void setEnd(String end) {
        this.end = end;
    }
}