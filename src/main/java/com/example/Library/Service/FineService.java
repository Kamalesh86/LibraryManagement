package com.example.Library.Service;


import com.example.Library.Entity.BorrowingTransaction;
import com.example.Library.Entity.Fine;
import com.example.Library.Entity.Member;
import com.example.Library.Repository.FineRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class FineService {

    @Autowired
    FineRepository fineRepository;

    public void createOrUpdateFine(Member member, BorrowingTransaction transaction, long overdueDays) {
        Optional<Fine> existingFine = fineRepository.findByBorrowingTransaction(transaction);
        BigDecimal fineAmount = BigDecimal.valueOf(overdueDays * 0.5);
        LocalDate today = LocalDate.now();

        if (existingFine.isPresent()) {
            Fine fine = existingFine.get();
            fine.setAmount(fineAmount);
            fine.setFineDate(today);
            if (!"PAID".equalsIgnoreCase(fine.getStatus())) {
                fine.setStatus("PENDING");
            }// Reset to pending if needed
            fineRepository.save(fine);
        } else {
            Fine newFine = new Fine(member, transaction, fineAmount, today, "PENDING");
            fineRepository.save(newFine);
        }
    }

    public Optional<Fine> getFineByTransaction(BorrowingTransaction transaction) {
        return fineRepository.findByBorrowingTransaction(transaction);
    }

    public void saveFine(Fine fine) {
        fineRepository.save(fine);
        //System.out.println(fine);
        fineRepository.flush();
    }
}
