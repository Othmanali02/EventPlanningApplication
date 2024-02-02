package com.example.eventplanningapp;

public class Event {
    public int eventID;
    public String eventName;
    public int eventImage;
    public String eventDate;
    public String eventDescription;

    public String likeCount;
    public String eventLocation;
    public String eventPlanner;
    public String eventPricing;

    public Event() {
    }

    public Event(int eventID, String eventName, int eventImage, String eventDate, String eventDescription, String likeCount, String eventLocation, String eventPlanner, String eventPricing) {
        this.eventID = eventID;
        this.eventName = eventName;
        this.eventImage = eventImage;
        this.eventDate = eventDate;
        this.eventDescription = eventDescription;
        this.likeCount = likeCount;
        this.eventLocation = eventLocation;
        this.eventPlanner = eventPlanner;
        this.eventPricing = eventPricing;
    }

    public int getEventID() {
        return eventID;
    }

    public String getEventName() {
        return eventName;
    }

    public void setEventName(String eventName) {
        this.eventName = eventName;
    }

    public int getEventImage() {
        return eventImage;
    }

    public void setEventImage(int eventImage) {
        this.eventImage = eventImage;
    }

    public String getEventDate() {
        return eventDate;
    }

    public void setEventDescription(String eventDate) {
        this.eventDescription = eventDescription;
    }


    public String getEventDescription() {
        return eventDescription;
    }

    public void setEventDate(String eventDate) {
        this.eventDate = eventDate;
    }

    public String getLikeCount() {
        return likeCount;
    }

    public void setLikeCount(String likeCount) {
        this.likeCount = likeCount;
    }

    public String getEventLocation() {
        return eventLocation;
    }

    public void setEventLocation(String eventLocation) {
        this.eventLocation = eventLocation;
    }

    public String getEventPlanner() {
        return eventPlanner;
    }

    public void setEventPlanner(String eventPlanner) {
        this.eventPlanner = eventPlanner;
    }

    public String getEventPricing() {
        return eventPricing;
    }

    public void setEventPricing(String eventPricing) {
        this.eventPricing = eventPricing;
    }

    @Override
    public String toString() {
        return "Event{" +
                "eventName='" + eventName + '\'' +
                ", eventImage=" + eventImage +
                ", eventDate='" + eventDate + '\'' +
                ", likeCount='" + likeCount + '\'' +
                ", eventLocation='" + eventLocation + '\'' +
                ", eventPlanner='" + eventPlanner + '\'' +
                ", eventPricing='" + eventPricing + '\'' +
                '}';
    }


}
