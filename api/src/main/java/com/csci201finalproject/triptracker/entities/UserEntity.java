package com.csci201finalproject.triptracker.entities;

import javax.persistence.*;

@Entity
@Table(name = "user", schema = "heroku_efbc5c1a3000eab")
public class UserEntity {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "email", unique = true, nullable = false)
    private String email;

    @Column(name = "password", nullable = false)
    private String password;

    @OneToOne
    @Column(name = "pfp_id")
    private PhotoEntity profilePhotoEntity;

    public int getId() {
        return this.id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return this.email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return this.password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public PhotoEntity getProfilePhotoEntity() {
        return this.profilePhotoEntity;
    }

    public void setProfilePhotoEntity(PhotoEntity profilePhotoEntity) {
        this.profilePhotoEntity = profilePhotoEntity;
    }
}
