package com.example.Library.Controller;

import com.example.Library.Entity.Book;
import com.example.Library.Entity.BorrowingTransaction;
import com.example.Library.Entity.Member;
import com.example.Library.Service.BookService;
import com.example.Library.Service.BorrowingTransactionService;
import com.example.Library.Service.MemberService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Controller
public class BorrowingTransactionController {

    @Autowired
    BorrowingTransactionService borrowingTransactionService;

    @Autowired
    BookService bookService;

    @Autowired
    MemberService memberService;

    @GetMapping("/dashboard/borrow")
    public String showBorrowForm(Model model, HttpSession session) {
        if (session.getAttribute("loggedInUser") == null) {
            return "redirect:/user-login"; // or wherever your login page is
        }
        List<Book> availableBooks = bookService.getAllBooks().stream()
                .filter(book -> book.getAvailableCopies() > 0)
                .toList();
        model.addAttribute("books", availableBooks);
        model.addAttribute("borrowRequest", new BorrowingTransaction());
        return "borrow";
    }



    @PostMapping("/dashboard/borrow")
    public String borrowBook(
            @ModelAttribute("borrowRequest") BorrowingTransaction borrowingTransaction,
            @RequestParam("memberId") String memberId,
            Model model,HttpSession session) {

        if (session.getAttribute("loggedInUser") == null) {
            return "redirect:/user-login"; // or wherever your login page is
        }

        Book book = bookService.getBookById(borrowingTransaction.getBook().getBookId()).orElse(null);
        Member member = memberService.getMemberById(memberId).orElse(null);

        System.out.println(memberId);
        System.out.println(borrowingTransaction.getBook().getBookId());

        if (book == null || member == null || book.getAvailableCopies() <= 0) {
            model.addAttribute("error", "Invalid Book or Member ID, or Book not available.");
            model.addAttribute("books", bookService.getAllBooks());
            model.addAttribute("borrowRequest", new BorrowingTransaction());
            return "borrow";
        }

        LocalDate today = LocalDate.now();
        LocalDate dueDate = today.plusDays(1);
        borrowingTransaction.setBook(book);
        borrowingTransaction.setMember(member);
        borrowingTransaction.setBorrowDate(today);
        borrowingTransaction.setDueDate(dueDate);
        borrowingTransaction.setStatus("BORROWED");

        borrowingTransactionService.saveTransaction(borrowingTransaction);

        book.setAvailableCopies(book.getAvailableCopies() - 1);
        bookService.addBook(book);

        model.addAttribute("success", "Book borrowed successfully! Due Date: " + dueDate);
        model.addAttribute("books", bookService.getAllBooks());
        model.addAttribute("borrowRequest", new BorrowingTransaction());
        return "borrow";
    }

    @GetMapping("/dashboard/return")
    public String showReturnBookForm(HttpSession session) {
        if (session.getAttribute("loggedInUser") == null) {
            return "redirect:/user-login"; // or wherever your login page is
        }
        return "return";
    }

    @PostMapping("/dashboard/return")
    public String searchBorrowedBooks(@RequestParam("memberId") String memberId, HttpSession session, RedirectAttributes redirectAttributes, Model model) {
        if (session.getAttribute("loggedInUser") == null) {
            return "redirect:/user-login"; // or wherever your login page is
        }
        Optional<Member> member = memberService.getMemberById(memberId);
        if (member.isEmpty()) {
            redirectAttributes.addFlashAttribute("returnError", "Member Not Found.");
            return "redirect:/dashboard/return";
        }
        List<BorrowingTransaction> borrowedBooks = borrowingTransactionService.findBorrowedByMemberId(memberId);
        model.addAttribute("borrowedBooks", borrowedBooks);
        model.addAttribute("memberId", memberId);

        if (borrowedBooks.isEmpty()) {
            model.addAttribute("noBooks", true);
        }
        else {
            model.addAttribute("noBooks", false);
        }
        System.out.println("Returned");
        System.out.println(borrowedBooks);
        return "return";
    }

    @PostMapping("/dashboard/return/{id}")
    public String returnBook(
            @PathVariable("id") Long transactionId,
            @RequestParam("memberId") String memberId,
            HttpSession session,
            Model model
    ) {
        if (session.getAttribute("loggedInUser") == null) {
            return "redirect:/user-login"; // or wherever your login page is
        }
        BorrowingTransaction transaction = borrowingTransactionService.getTransactionById(transactionId);

        if (transaction == null || !"BORROWED".equals(transaction.getStatus())) {
            model.addAttribute("returnError", "Invalid transaction or book already returned.");
        } else {
            // Mark as returned
            transaction.setStatus("RETURNED");
            transaction.setReturnDate(java.time.LocalDate.now());
            borrowingTransactionService.saveTransaction(transaction);

            // Update book stock
            Book book = transaction.getBook();
            book.setAvailableCopies(book.getAvailableCopies() + 1);
            bookService.addBook(book); // save updated book
            model.addAttribute("returnSuccess", "Book returned successfully.");
        }

        // Reload updated borrowed books list
        List<BorrowingTransaction> borrowedBooks = borrowingTransactionService.findBorrowedByMemberId(memberId);
        model.addAttribute("borrowedBooks", borrowedBooks);
        model.addAttribute("memberId", memberId);
        if (borrowedBooks.isEmpty()) model.addAttribute("noBooks", true);

        return "return";
    }


    @GetMapping("/admin-dashboard/transactions")
    public String showAllTransactions(Model model,HttpSession session) {
        if (session.getAttribute("adminUser") == null) {
            return "redirect:/admin-login"; // or wherever your login page is
        }
        List<BorrowingTransaction> transactions = borrowingTransactionService.getAllTransactions();
        model.addAttribute("transactions", transactions);
        return "transactions";  // refers to transactions.html
    }

}
