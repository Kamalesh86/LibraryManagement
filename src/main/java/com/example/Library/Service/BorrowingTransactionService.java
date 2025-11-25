package com.example.Library.Service;

import com.example.Library.Entity.BorrowingTransaction;
import com.example.Library.Repository.BorrowingTransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class BorrowingTransactionService {

    @Autowired
    BorrowingTransactionRepository borrowingTransactionRepository;

    public void saveTransaction(BorrowingTransaction borrowingTransaction) {
       borrowingTransactionRepository.save(borrowingTransaction);
    }

    public List<BorrowingTransaction> findBorrowedByMemberId(String memberId) {
        return borrowingTransactionRepository.findByMember_LibraryIdAndStatus(memberId,"BORROWED");
    }

    public BorrowingTransaction getTransactionById(Long transactionId) {
        return borrowingTransactionRepository.findById(transactionId).orElse(null);
    }

    public long getBorrowedCount() {
        return borrowingTransactionRepository.countCurrentlyBorrowed();
    }

    public long getReturnedCount() {
        return borrowingTransactionRepository.countReturned();
    }


    public List<BorrowingTransaction> getAllTransactions() {
        return borrowingTransactionRepository.findAll();
    }

    public List<BorrowingTransaction> getOverdueTransactionsByMember(String memberId) {
        LocalDate today = LocalDate.now();
        return borrowingTransactionRepository.findOverdueTransactionsByMember(memberId, today);
    }
}
