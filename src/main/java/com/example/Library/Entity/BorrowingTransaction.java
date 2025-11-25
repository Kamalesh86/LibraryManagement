package com.example.Library.Entity;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "borrowing_transactions")
public class BorrowingTransaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "transaction_id")
    private Long transactionId;

    @ManyToOne
    @JoinColumn(name = "book_id", nullable = false)
    private Book book;

    @ManyToOne
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    @Column(name = "borrow_date", nullable = false)
    private LocalDate borrowDate;

    @Column(name = "return_date")
    private LocalDate returnDate;

    @Column(name = "due_date", nullable = false)
    private LocalDate dueDate;

    @Column(name = "status", nullable = false)
    private String status;

    public BorrowingTransaction() {
    }

    // Corrected constructor with parameters
    public BorrowingTransaction(Book book, Member member, LocalDate borrowDate, LocalDate dueDate, String status) {
        this.book = book;
        this.member = member;
        this.borrowDate = borrowDate;
        this.dueDate = dueDate;
        this.status = status;
    }

    // --- Getters ---
    public Long getTransactionId() {
        return transactionId;
    }

    public Book getBook() {
        return book;
    }

    public Member getMember() {
        return member;
    }

    public LocalDate getBorrowDate() {
        return borrowDate;
    }

    public LocalDate getReturnDate() {
        return returnDate;
    }

    public LocalDate getDueDate() {
        return dueDate;
    }

    public String getStatus() {
        return status;
    }

    // --- Setters ---
    public void setTransactionId(Long transactionId) {
        this.transactionId = transactionId;
    }

    public void setBook(Book book) {
        this.book = book;
    }

    public void setMember(Member member) {
        this.member = member;
    }

    public void setBorrowDate(LocalDate borrowDate) {
        this.borrowDate = borrowDate;
    }

    public void setReturnDate(LocalDate returnDate) {
        this.returnDate = returnDate;
    }

    public void setDueDate(LocalDate dueDate) {
        this.dueDate = dueDate;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BorrowingTransaction that = (BorrowingTransaction) o;
        return transactionId != null && transactionId.equals(that.transactionId);
    }

    @Override
    public int hashCode() {
        return transactionId != null ? transactionId.hashCode() : 0;
    }

    @Override
    public String toString() {
        return "BorrowingTransaction{" +
                "transactionId=" + transactionId +
                ", book=" + (book != null ? book.getTitle() : "null") +
                ", member=" + (member != null ? member.getName() : "null") +
                ", borrowDate=" + borrowDate +
                ", returnDate=" + returnDate +
                ", dueDate=" + dueDate +
                ", status='" + status + '\'' +
                '}';
    }

    @Transient
    public boolean isOverdue() {
        if (this.returnDate != null) {
            return false; // returned already
        }
        return LocalDate.now().isAfter(this.dueDate);
    }

    public long getOverdueDays() {
        if (isOverdue()) {
            return java.time.temporal.ChronoUnit.DAYS.between(dueDate, LocalDate.now());
        }
        return 0;
    }
}
