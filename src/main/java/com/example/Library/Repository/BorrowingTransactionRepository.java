package com.example.Library.Repository;

import com.example.Library.Entity.BorrowingTransaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface BorrowingTransactionRepository extends JpaRepository<BorrowingTransaction,Long> {
    List<BorrowingTransaction> findByMember_LibraryIdAndStatus(String memberId, String status);

    @Query("SELECT COUNT(bt) FROM BorrowingTransaction bt WHERE bt.status = 'BORROWED'")
    long countCurrentlyBorrowed();

    @Query("SELECT COUNT(bt) FROM BorrowingTransaction bt WHERE bt.status = 'RETURNED'")
    long countReturned();

    @Query("SELECT t FROM BorrowingTransaction t " +
            "WHERE t.member.libraryId = :memberId AND t.dueDate < :today AND t.status = 'BORROWED'")
    List<BorrowingTransaction> findOverdueTransactionsByMember(@Param("memberId") String memberId,
                                                               @Param("today") LocalDate today);
}
