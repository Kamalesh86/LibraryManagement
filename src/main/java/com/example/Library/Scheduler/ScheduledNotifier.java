package com.example.Library.Scheduler;

import com.example.Library.Entity.BorrowingTransaction;
import com.example.Library.Service.BorrowingTransactionService;
import com.example.Library.Service.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ScheduledNotifier {

    @Autowired
    private BorrowingTransactionService borrowingTransactionService;

    @Autowired
    private NotificationService notificationService;

    @Scheduled(fixedRate = 300000) // every 30 seconds
    public void sendDueDateReminders() {
        List<BorrowingTransaction> borrowed = borrowingTransactionService.getAllTransactions().stream()
                .filter(tx -> "BORROWED".equals(tx.getStatus()))
                .toList();

        for (BorrowingTransaction tx : borrowed) {
            String message = "Book is Dued on " + tx.getDueDate() + ". Please return to avoid fine.";
            notificationService.sendNotification(tx.getMember().getLibraryId(), message, "DUE_DATE_REMINDER", tx.getTransactionId());
        }
    }

    @Scheduled(fixedRate = 300000) // every 30 seconds
    public void sendFineNotifications() {
        List<BorrowingTransaction> overdued = borrowingTransactionService.getAllTransactions().stream()
                .filter(tx -> tx.getOverdueDays() > 0 && "BORROWED".equals(tx.getStatus()))
                .toList();

        for (BorrowingTransaction tx : overdued) {
            double fineAmount = tx.getOverdueDays() * 0.5;
            String message = "Due date is crossed. Pay fine of ₹" + fineAmount + " and return book.";
            notificationService.sendNotification(tx.getMember().getLibraryId(), message, "OVERDUE_FINE", tx.getTransactionId());
        }
    }
}
