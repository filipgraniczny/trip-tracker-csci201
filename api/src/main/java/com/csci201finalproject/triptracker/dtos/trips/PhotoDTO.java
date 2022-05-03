package com.csci201finalproject.triptracker.dtos.trips;

public class PhotoDTO {
    private String caption;
    private String object_key_aws;

    public String getCaption() {
        return caption;
    }

    public void setCaption(String caption) {
        this.caption = caption;
    }

    public String getObject_key_aws() {
        return object_key_aws;
    }

    public void setObject_key_aws(String object_key_aws) {
        this.object_key_aws = object_key_aws;
    }
}
