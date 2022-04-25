package com.csci201finalproject.triptracker.entities;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.Objects;

@Entity
@Table(name = "event", schema = "heroku_efbc5c1a3000eab")
public class EventEntity {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "id")
    private int id;
    @Basic
    @Column(name = "name")
    private String name;
    @Basic
    @Column(name = "category")
    private String category;
    @Basic
    @Column(name = "description")
    private String description;
    @Basic
    @Column(name = "location")
    private String location;
    @Basic
    @Column(name = "from_time")
    private Timestamp fromTime;
    @Basic
    @Column(name = "to_time")
    private Timestamp toTime;
    @Basic
    @Column(name = "trip_id")
    private Integer tripId;
    @ManyToOne
    @JoinColumn(name="trip_id")
    private TripEntity trip;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public Timestamp getFromTime() {
        return fromTime;
    }

    public void setFromTime(Timestamp fromTime) {
        this.fromTime = fromTime;
    }

    public Timestamp getToTime() {
        return toTime;
    }

    public void setToTime(Timestamp toTime) {
        this.toTime = toTime;
    }

    public Integer getTripId() {
        return tripId;
    }

    public void setTripId(Integer tripId) {
        this.tripId = tripId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        EventEntity that = (EventEntity) o;
        return id == that.id && Objects.equals(name, that.name) && Objects.equals(category, that.category) && Objects.equals(description, that.description) && Objects.equals(location, that.location) && Objects.equals(fromTime, that.fromTime) && Objects.equals(toTime, that.toTime) && Objects.equals(tripId, that.tripId) && Objects.equals(trip, that.trip);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, category, description, location, fromTime, toTime, tripId, trip);
    }

    public TripEntity getTrip() {
        return trip;
    }

    public void setTrip(TripEntity trip) {
        this.trip = trip;
    }
}
