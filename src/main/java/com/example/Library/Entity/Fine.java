package com.example.Library.Entity;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "fines")
public class Fine {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "fine_id")
    private Long fineId;

    @ManyToOne
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    @OneToOne // One fine per borrowing transaction
    @JoinColumn(name = "borrow_transaction_id", nullable = false, unique = true) // Unique constraint to ensure one fine per transaction
    private BorrowingTransaction borrowingTransaction;

    @Column(name = "amount", nullable = false)
    private BigDecimal amount;

    @Column(name = "fine_date", nullable = false)
    private LocalDate fineDate; // Date the fine was assessed

    @Column(name = "status", nullable = false)
    private String status; // "PENDING", "PAID", "WAIVED"

    // Default constructor
    public Fine() {
    }

    // Constructor for creating a new fine
    public Fine(Member member, BorrowingTransaction borrowingTransaction, BigDecimal amount, LocalDate fineDate, String status) {
        this.member = member;
        this.borrowingTransaction = borrowingTransaction;
        this.amount = amount;
        this.fineDate = fineDate;
        this.status = status;
    }

    // Getters and Setters
    public Long getFineId() {
        return fineId;
    }

    public void setFineId(Long fineId) {
        this.fineId = fineId;
    }

    public Member getMember() {
        return member;
    }

    public void setMember(Member member) {
        this.member = member;
    }

    public BorrowingTransaction getBorrowingTransaction() {
        return borrowingTransaction;
    }

    public void setBorrowingTransaction(BorrowingTransaction borrowingTransaction) {
        this.borrowingTransaction = borrowingTransaction;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public LocalDate getFineDate() {
        return fineDate;
    }

    public void setFineDate(LocalDate fineDate) {
        this.fineDate = fineDate;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Fine fine = (Fine) o;
        return fineId != null && fineId.equals(fine.fineId);
    }

    @Override
    public int hashCode() {
        return fineId != null ? fineId.hashCode() : 0;
    }

    @Override
    public String toString() {
        return "Fine{" +
                "fineId=" + fineId +
                ", member=" + (member != null ? member.getName() : "null") +
                ", borrowingTransactionId=" + (borrowingTransaction != null ? borrowingTransaction.getTransactionId() : "null") +
                ", amount=" + amount +
                ", fineDate=" + fineDate +
                ", status='" + status + '\'' +
                '}';
    }
}
