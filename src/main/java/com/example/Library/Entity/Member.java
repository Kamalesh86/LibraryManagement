package com.example.Library.Entity;

import jakarta.persistence.*;
import java.time.LocalDate; // For last_borrow_date

@Entity
@Table(name = "members")
public class Member {



    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "member_id")
    private Long memberId;

    @Column(name = "library_id", unique = true)
    private String libraryId;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "email", unique = true, nullable = false)
    private String email;

    @Column(name = "phone")
    private String phone;

    @Column(name = "address")
    private String address;

    @Column(name = "membership_status", nullable = false)
    private String membershipStatus; // e.g., "ACTIVE", "INACTIVE"

    @Column(name = "password", nullable = false)
    private String password; // Hashed password

    @Column(name = "role", nullable = false)
    private String role; // e.g., "USER", "ADMIN"

    @Column(name = "last_borrow_date")
    private LocalDate lastBorrowDate;




    // Default constructor
    public Member() {
    }

    // Constructor for registration (without ID and status)
    public Member(String name, String email, String phone, String address, String password, String role) {
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.address = address;
        this.password = password;
        this.membershipStatus = "ACTIVE"; // Default to active on registration
        this.role = role;
        this.lastBorrowDate = LocalDate.now(); // Set current date on registration
    }

     //Getters and Setters
    public Long getMemberId() {
        return memberId;
    }

    public void setMemberId(Long memberId) {
        this.memberId = memberId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getMembershipStatus() {
        return membershipStatus;
    }

    public void setMembershipStatus(String membershipStatus) {
        this.membershipStatus = membershipStatus;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public LocalDate getLastBorrowDate() {
        return lastBorrowDate;
    }

    public void setLastBorrowDate(LocalDate lastBorrowDate) {
        this.lastBorrowDate = lastBorrowDate;
    }

    public void setLibraryId(String libraryId) {
        this.libraryId = libraryId;
    }

    public String getLibraryId(){
        return libraryId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Member member = (Member) o;
        return memberId != null && memberId.equals(member.memberId);
    }

    @Override
    public int hashCode() {
        return libraryId != null ? libraryId.hashCode() : 0;
    }

    @Override
    public String toString() {
        return "Member{" +
                "memberId=" + libraryId +
                ", name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", phone='" + phone + '\'' +
                ", address='" + address + '\'' +
                ", membershipStatus='" + membershipStatus + '\'' +
                ", role='" + role + '\'' +
                ", lastBorrowDate=" + lastBorrowDate +
                '}';
    }
}
