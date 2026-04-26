package edu.psu.ist.registration.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name="registrations")
public class Registration {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="registrationId")
    private int registrationId;

    @ManyToOne
    @JoinColumn(name="memberId", nullable=false)
    private Member member;

    @ManyToOne
    @JoinColumn(name="eventId", nullable=false)
    private Event event;

    @Column(name="registrationDate")
    private LocalDateTime registrationDate;

    @Column(name="paid")
    private boolean paid;

    @Column(name="checkedIn")
    private boolean checkedIn;

    public Registration() {
        this.registrationDate = LocalDateTime.now();
    }

    public Registration(Member member, Event event) {
        this.member = member;
        this.event = event;
        this.registrationDate = LocalDateTime.now();
        this.paid = false;
        this.checkedIn = false;
    }

    // Getters/setters
    public int getRegistrationId() { return registrationId; }
    public void setRegistrationId(int registrationId) { this.registrationId = registrationId; }

    public Member getMember() { return member; }
    public void setMember(Member member) { this.member = member; }

    public Event getEvent() { return event; }
    public void setEvent(Event event) { this.event = event; }

    public LocalDateTime getRegistrationDate() { return registrationDate; }
    public void setRegistrationDate(LocalDateTime registrationDate) { this.registrationDate = registrationDate; }

    public boolean isPaid() { return paid; }
    public void setPaid(boolean paid) { this.paid = paid; }

    public boolean isCheckedIn() { return checkedIn; }
    public void setCheckedIn(boolean checkedIn) { this.checkedIn = checkedIn; }

    @Override
    public String toString() {
        return "Registration{registrationId=" + registrationId +
               ", member=" + (member != null ? member.getMemberId() : "null") +
               ", event=" + (event != null ? event.getEventId() : "null") + "}";
    }
}
