package com.csci201finalproject.triptracker.entities;

import javax.persistence.*;

@Entity
@Table(name = "photo", schema = "heroku_efbc5c1a3000eab")
public class PhotoEntity {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "id")
    private int id;
    @Basic
    @Column(name = "trip_id")
    private Integer tripId;
    @Basic
    @Column(name = "event_id")
    private Integer eventId;
    @Basic
    @Column(name = "caption")
    private String caption;
    @Basic
    @Column(name = "object_key_aws")
    private String objectKeyAws;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Integer getTripId() {
        return tripId;
    }

    public void setTripId(Integer tripId) {
        this.tripId = tripId;
    }

    public Integer getEventId() {
        return eventId;
    }

    public void setEventId(Integer eventId) {
        this.eventId = eventId;
    }

    public String getCaption() {
        return caption;
    }

    public void setCaption(String caption) {
        this.caption = caption;
    }

    public String getObjectKeyAws() {
        return objectKeyAws;
    }

    public void setObjectKeyAws(String objectKeyAws) {
        this.objectKeyAws = objectKeyAws;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        PhotoEntity that = (PhotoEntity) o;

        if (id != that.id) return false;
        if (tripId != null ? !tripId.equals(that.tripId) : that.tripId != null) return false;
        if (eventId != null ? !eventId.equals(that.eventId) : that.eventId != null) return false;
        if (caption != null ? !caption.equals(that.caption) : that.caption != null) return false;
        if (objectKeyAws != null ? !objectKeyAws.equals(that.objectKeyAws) : that.objectKeyAws != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = id;
        result = 31 * result + (tripId != null ? tripId.hashCode() : 0);
        result = 31 * result + (eventId != null ? eventId.hashCode() : 0);
        result = 31 * result + (caption != null ? caption.hashCode() : 0);
        result = 31 * result + (objectKeyAws != null ? objectKeyAws.hashCode() : 0);
        return result;
    }
}
