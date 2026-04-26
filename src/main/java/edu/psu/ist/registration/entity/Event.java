package edu.psu.ist.registration.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name="events")
public class Event {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="eventId")
    private int eventId;

    @Column(name="eventName")
    private String eventName;

    @Column(name="eventDate")
    private LocalDate eventDate;

    @Column(name="startTime")
    private String startTime;

    @Column(name="endTime")
    private String endTime;

    @Column(name="location")
    private String location;

    @Column(name="capacity")
    private int capacity;

    @Column(name="price")
    private BigDecimal price;

    @Column(name="description")
    private String description;

    @Column(name="isActive")
    private boolean isActive;

    @OneToMany(mappedBy="event", cascade=CascadeType.ALL)
    private List<Registration> registrations;

    public Event() {}

    // Getters/setters
    public int getEventId() { return eventId; }
    public void setEventId(int eventId) { this.eventId = eventId; }

    public String getEventName() { return eventName; }
    public void setEventName(String eventName) { this.eventName = eventName; }

    public LocalDate getEventDate() { return eventDate; }
    public void setEventDate(LocalDate eventDate) { this.eventDate = eventDate; }

    public String getStartTime() { return startTime; }
    public void setStartTime(String startTime) { this.startTime = startTime; }

    public String getEndTime() { return endTime; }
    public void setEndTime(String endTime) { this.endTime = endTime; }

    public String getLocation() { return location; }
    public void setLocation(String location) { this.location = location; }

    public int getCapacity() { return capacity; }
    public void setCapacity(int capacity) { this.capacity = capacity; }

    public BigDecimal getPrice() { return price; }
    public void setPrice(BigDecimal price) { this.price = price; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public boolean isActive() { return isActive; }
    public void setActive(boolean active) { isActive = active; }

    public List<Registration> getRegistrations() { return registrations; }
    public void setRegistrations(List<Registration> registrations) { this.registrations = registrations; }

    public int getRegistrationCount() {
        return registrations != null ? registrations.size() : 0;
    }

    public int getSpotsRemaining() {
        return capacity - getRegistrationCount();
    }

    @Override
    public String toString() {
        return "Event{eventId=" + eventId + ", eventName='" + eventName + "', eventDate=" + eventDate + "}";
    }
}
