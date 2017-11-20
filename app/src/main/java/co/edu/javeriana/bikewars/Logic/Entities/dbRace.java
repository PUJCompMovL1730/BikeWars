package co.edu.javeriana.bikewars.Logic.Entities;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Todesser on 29/10/2017.
 */

public class dbRace implements Serializable{
    private String id;
    private String name;
    private dbRouteMarker start;
    private dbRouteMarker end;
    private List<dbCompetitor> competitors;
    private long startDate;
    private long endDate;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public dbRouteMarker getStart() {
        return start;
    }

    public void setStart(dbRouteMarker start) {
        this.start = start;
    }

    public dbRouteMarker getEnd() {
        return end;
    }

    public void setEnd(dbRouteMarker end) {
        this.end = end;
    }

    public List<dbCompetitor> getCompetitors() {
        return competitors;
    }

    public void setCompetitors(List<dbCompetitor> competitors) {
        this.competitors = competitors;
    }

    public long getStartDate() {
        return startDate;
    }

    public void setStartDate(long startDate) {
        this.startDate = startDate;
    }

    public long getEndDate() {
        return endDate;
    }

    public void setEndDate(long endDate) {
        this.endDate = endDate;
    }

    public dbRace(String id, String name, dbRouteMarker start, dbRouteMarker end, List<dbCompetitor> competitors, long startDate, long endDate) {

        this.id = id;
        this.name = name;
        this.start = start;
        this.end = end;
        this.competitors = competitors;
        this.startDate = startDate;
        this.endDate = endDate;
    }

    public dbRace() {

    }
}
