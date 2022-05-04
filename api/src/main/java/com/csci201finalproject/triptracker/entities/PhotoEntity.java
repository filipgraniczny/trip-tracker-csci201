package com.csci201finalproject.triptracker.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.apache.catalina.User;

import javax.persistence.*;

@Entity
@Table(name = "photo", schema = "heroku_efbc5c1a3000eab")
public class PhotoEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "trip_id")
    private TripEntity trip;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "event_id")
    private EventEntity event;

    @Basic
    @Column(name = "caption")
    private String caption;

    @Basic
    @Column(name = "object_key_aws")
    private String objectKeyAws;
  
    @JsonIgnore
    @OneToOne(cascade = CascadeType.ALL)
    private UserEntity userEntity;

    @Column(name = "presigned_url")
    private String presignedUrl;

    public UserEntity getUserEntity() {
        return this.userEntity;
    }

    public void setUserEntity(UserEntity userEntity) {
        this.userEntity = userEntity;
    }

    public String getPresignedUrl() {
        return this.presignedUrl;
    }

    public void setPresignedUrl(String presignedUrl) {
        this.presignedUrl = presignedUrl;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public TripEntity getTrip() {
        return trip;
    }

    public void setTrip(TripEntity trip) {
        this.trip = trip;
    }

    public EventEntity getEvent() {
        return event;
    }

    public void setEvent(EventEntity event) {
        this.event = event;
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
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;

        PhotoEntity that = (PhotoEntity) o;

        if (id != that.id)
            return false;
        if (trip != null ? !trip.equals(that.trip) : that.trip != null)
            return false;
        if (event != null ? !event.equals(that.event) : that.event != null)
            return false;
        if (caption != null ? !caption.equals(that.caption) : that.caption != null)
            return false;
        return objectKeyAws != null ? objectKeyAws.equals(that.objectKeyAws) : that.objectKeyAws == null;
    }

    @Override
    public int hashCode() {
        int result = id;
        result = 31 * result + (trip != null ? trip.hashCode() : 0);
        result = 31 * result + (event != null ? event.hashCode() : 0);
        result = 31 * result + (caption != null ? caption.hashCode() : 0);
        result = 31 * result + (objectKeyAws != null ? objectKeyAws.hashCode() : 0);
        return result;
    }

}
