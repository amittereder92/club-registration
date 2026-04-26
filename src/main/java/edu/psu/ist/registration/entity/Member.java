package edu.psu.ist.registration.entity;

import jakarta.persistence.*;

@Entity
@Table(name="members")
public class Member {

    // define fields
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="memberId")
    private int memberId;

    @Column(name="firstName")
    private String firstName;

    @Column(name="lastName")
    private String lastName;

    @Column(name="userName")
    private String userName;

    @Column(name="email")
    private String email;

    @Column(name="phoneNumber")
    private String phoneNumber;

    @Column(name="bestGame")
    private String bestGame;

    // Address fields
    @Column(name="address")
    private String address;

    @Column(name="city")
    private String city;

    @Column(name="state")
    private String state;

    @Column(name="zipCode")
    private String zipCode;

    // Registration info
    @Column(name="ageRange")
    private String ageRange;

    @Column(name="guardianName")
    private String guardianName;

    @Column(name="guardianPhone")
    private String guardianPhone;

    // PSU info
    @Column(name="collegeStudent")
    private String collegeStudent;

    @Column(name="interestedInPSU")
    private String interestedInPSU;

    @Column(name="gradeYear")
    private String gradeYear;

    // Gaming
    @Column(name="gamingSystem")
    private String gamingSystem;

    @Column(name="attendedBefore")
    private String attendedBefore;

    @Column(name="joinTournament")
    private String joinTournament;

    // Misc
    @Column(name="howHeard")
    private String howHeard;

    @Column(name="acceptedTerms")
    private boolean acceptedTerms;

    // define constructors
    public Member() {
    }

    public Member(String firstName, String lastName, String userName,
                  String email, String phoneNumber, String bestGame) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.userName = userName;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.bestGame = bestGame;
    }

    // define getters/setters

    public int getMemberId() {
        return memberId;
    }

    public void setMemberId(int memberId) {
        this.memberId = memberId;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getBestGame() {
        return bestGame;
    }

    public void setBestGame(String bestGame) {
        this.bestGame = bestGame;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getZipCode() {
        return zipCode;
    }

    public void setZipCode(String zipCode) {
        this.zipCode = zipCode;
    }

    public String getAgeRange() {
        return ageRange;
    }

    public void setAgeRange(String ageRange) {
        this.ageRange = ageRange;
    }

    public String getGuardianName() {
        return guardianName;
    }

    public void setGuardianName(String guardianName) {
        this.guardianName = guardianName;
    }

    public String getGuardianPhone() {
        return guardianPhone;
    }

    public void setGuardianPhone(String guardianPhone) {
        this.guardianPhone = guardianPhone;
    }

    public String getCollegeStudent() {
        return collegeStudent;
    }

    public void setCollegeStudent(String collegeStudent) {
        this.collegeStudent = collegeStudent;
    }

    public String getInterestedInPSU() {
        return interestedInPSU;
    }

    public void setInterestedInPSU(String interestedInPSU) {
        this.interestedInPSU = interestedInPSU;
    }

    public String getGradeYear() {
        return gradeYear;
    }

    public void setGradeYear(String gradeYear) {
        this.gradeYear = gradeYear;
    }

    public String getGamingSystem() {
        return gamingSystem;
    }

    public void setGamingSystem(String gamingSystem) {
        this.gamingSystem = gamingSystem;
    }

    public String getAttendedBefore() {
        return attendedBefore;
    }

    public void setAttendedBefore(String attendedBefore) {
        this.attendedBefore = attendedBefore;
    }

    public String getJoinTournament() {
        return joinTournament;
    }

    public void setJoinTournament(String joinTournament) {
        this.joinTournament = joinTournament;
    }

    public String getHowHeard() {
        return howHeard;
    }

    public void setHowHeard(String howHeard) {
        this.howHeard = howHeard;
    }

    public boolean isAcceptedTerms() {
        return acceptedTerms;
    }

    public void setAcceptedTerms(boolean acceptedTerms) {
        this.acceptedTerms = acceptedTerms;
    }

    // define toString
    @Override
    public String toString() {
        return "Member{" +
                "memberId=" + memberId +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", userName='" + userName + '\'' +
                ", email='" + email + '\'' +
                ", phoneNumber='" + phoneNumber + '\'' +
                ", bestGame='" + bestGame + '\'' +
                ", address='" + address + '\'' +
                ", city='" + city + '\'' +
                ", state='" + state + '\'' +
                ", zipCode='" + zipCode + '\'' +
                ", ageRange='" + ageRange + '\'' +
                ", guardianName='" + guardianName + '\'' +
                ", guardianPhone='" + guardianPhone + '\'' +
                ", collegeStudent='" + collegeStudent + '\'' +
                ", interestedInPSU='" + interestedInPSU + '\'' +
                ", gradeYear='" + gradeYear + '\'' +
                ", gamingSystem='" + gamingSystem + '\'' +
                ", attendedBefore='" + attendedBefore + '\'' +
                ", joinTournament='" + joinTournament + '\'' +
                ", howHeard='" + howHeard + '\'' +
                ", acceptedTerms=" + acceptedTerms +
                '}';
    }
}