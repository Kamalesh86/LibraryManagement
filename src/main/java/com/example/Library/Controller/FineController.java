package com.example.Library.Controller;


import com.example.Library.Entity.Book;
import com.example.Library.Entity.BorrowingTransaction;
import com.example.Library.Entity.Fine;
import com.example.Library.Entity.Member;
import com.example.Library.Service.BookService;
import com.example.Library.Service.BorrowingTransactionService;
import com.example.Library.Service.FineService;
import com.example.Library.Service.MemberService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.Optional;

@Controller
public class FineController {

    @Autowired
    FineService fineService;

    @Autowired
    BorrowingTransactionService borrowingTransactionService;

    @Autowired
    MemberService memberService;

    @Autowired
    BookService bookService;

    @GetMapping("/dashboard/return/pay-fine")
    public String payFine(@RequestParam("memberId") String memberId,
                          @RequestParam("transactionId") Long transactionId,
                          HttpSession session,
                          Model model) {
        // Example: Retrieve the transaction and calculate the fine
        if (session.getAttribute("loggedInUser") == null) {
            return "redirect:/user-login"; // or wherever your login page is
        }
        BorrowingTransaction transaction = borrowingTransactionService.getTransactionById(transactionId);
        if (transaction == null || !transaction.getMember().getLibraryId().equals(memberId)) {
            model.addAttribute("returnError", "Invalid transaction or member.");
            return "return"; // return the same view with error
        }

        long overdueDays = transaction.getOverdueDays();
        double fineAmount = overdueDays * 0.5; // For example, ₹0.5 per day

        fineService.createOrUpdateFine(transaction.getMember(), transaction, overdueDays);

        // Send values to frontend
        model.addAttribute("memberId", memberId);
        model.addAttribute("transaction", transaction);
        model.addAttribute("fineAmount", fineAmount);

        return "pay_fine"; // Create pay_fine.html Thymeleaf page
    }

    @PostMapping("/dashboard/return/pay-fine")
    public String payFine(@RequestParam("transactionId") Long transactionId,
                          @RequestParam("memberId") String memberId,
                          HttpSession session,
                          RedirectAttributes redirectAttributes,
                          Model model){
        if (session.getAttribute("loggedInUser") == null) {
            return "redirect:/user-login"; // or wherever your login page is
        }
            BorrowingTransaction transaction = borrowingTransactionService.getTransactionById(transactionId);

            // Mark as returned
            transaction.setStatus("RETURNED");
            transaction.setReturnDate(java.time.LocalDate.now());
            borrowingTransactionService.saveTransaction(transaction);

            // Update book stock
            Book book = transaction.getBook();
            book.setAvailableCopies(book.getAvailableCopies() + 1);
            bookService.addBook(book);// save updated book
            Optional<Fine> fines = fineService.getFineByTransaction(transaction);
            Fine fine = fines.get();
            System.out.println(fine.getStatus());
            fine.setStatus("PAID");
            fineService.saveFine(fine);
            System.out.println(fine.getStatus());
            System.out.println(fine);

            // ✅ Flash message and redirect
            redirectAttributes.addFlashAttribute("success", "Payment Successful and Book Returned.");
            return "redirect:/dashboard/return/pay-fine?transactionId=" + transactionId + "&memberId=" + memberId;
    }

    @GetMapping("/dashboard/payment")
    public String showFineInputPage(HttpSession session) {
        if (session.getAttribute("loggedInUser") == null) {
            return "redirect:/user-login";
        }
        return "payment"; // Initial form (no fine details)
    }

    @PostMapping("/dashboard/payment")
    public String ShowpayAllFine(@RequestParam("memberId") String memberId,
                          HttpSession session,
                                 RedirectAttributes redirectAttributes,
                          Model model) {
        if (session.getAttribute("loggedInUser") == null) {
            return "redirect:/user-login";
        }


        Optional<Member> member = memberService.getMemberById(memberId);
        if (member.isEmpty()) {
            redirectAttributes.addFlashAttribute("error", "Member Not Found.");
            return "redirect:/dashboard/payment";
        }
        List<BorrowingTransaction> overdueTransactions = borrowingTransactionService.getOverdueTransactionsByMember(memberId);

        double totalFine = 0.0;
        for (BorrowingTransaction tx : overdueTransactions) {
            long overdueDays = tx.getOverdueDays();
            double fineAmount = overdueDays * 0.5;
            fineService.createOrUpdateFine(tx.getMember(), tx, overdueDays);
            totalFine += fineAmount;
        }

        if(totalFine==0.0){
            model.addAttribute("memberId", memberId);
            model.addAttribute("success", "No OutStanding Fines");
            return "payment";
        }

        model.addAttribute("memberId", memberId);
        model.addAttribute("transactions", overdueTransactions);
        model.addAttribute("totalFineAmount", totalFine);
        model.addAttribute("showFineDetails",true);

        return "payment";
    }

    @GetMapping("/dashboard/payment/submit")
    public String payAllfine(@RequestParam("memberId") String memberId,
                             @RequestParam("totalfine") double totalfine,
                             HttpSession session,
                             Model model){
        if (session.getAttribute("loggedInUser") == null) {
            return "redirect:/user-login";
        }
        model.addAttribute("memberId",memberId);
        model.addAttribute("totalFine",totalfine);
        return "pay_Allfine";
    }

    @PostMapping("/dashboard/payment/submit")
    public String submitAllFines(@RequestParam("memberId") String memberId,
                                 @RequestParam("fineAmount") double fineAmount,
                                 HttpSession session,
                                 RedirectAttributes redirectAttributes) {

        if (session.getAttribute("loggedInUser") == null) {
            return "redirect:/user-login";
        }

        // 1. Get member
        Optional<Member> memberOpt = memberService.getMemberById(memberId);
//        if (memberOpt.isEmpty()) {
//            redirectAttributes.addFlashAttribute("error", "Member not found.");
//            return "redirect:/dashboard";
//        }

        Member member = memberOpt.get();

        // 2. Get all overdue transactions
        List<BorrowingTransaction> overdueTransactions = borrowingTransactionService.getOverdueTransactionsByMember(memberId);

        for (BorrowingTransaction tx : overdueTransactions) {
            // a. Set transaction as returned
            tx.setStatus("RETURNED");
            tx.setReturnDate(java.time.LocalDate.now());
            borrowingTransactionService.saveTransaction(tx);

            // b. Increase book stock
            Book book = tx.getBook();
            book.setAvailableCopies(book.getAvailableCopies() + 1);
            bookService.addBook(book);

            // c. Mark associated fine as PAID
            Optional<Fine> fineOpt = fineService.getFineByTransaction(tx);
            if (fineOpt.isPresent()) {
                Fine fine = fineOpt.get();
                fine.setStatus("PAID");
                fineService.saveFine(fine);
            }

            long overdueDays = tx.getOverdueDays();
            double fineAmounttemp = overdueDays * 0.5; // For example, ₹0.5 per day

            fineService.createOrUpdateFine(tx.getMember(), tx, overdueDays);
        }
        fineAmount=0.0;
        redirectAttributes.addFlashAttribute("success", "All fines paid and books returned successfully.");
        return "redirect:/dashboard/payment/submit?memberId=" + memberId + "&totalfine=" + fineAmount;
    }


}
