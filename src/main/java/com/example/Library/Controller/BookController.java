package com.example.Library.Controller;

import com.example.Library.Entity.Book;
import com.example.Library.Entity.Member;
import com.example.Library.Entity.Notification;
import com.example.Library.Service.BookService;
import com.example.Library.Service.MemberService;
import com.example.Library.Service.NotificationService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.Optional;

@Controller
public class BookController {

    @Autowired
    private BookService bookService;

    @Autowired
    private MemberService memberService;

    @Autowired
    private NotificationService notificationService;

    @GetMapping("/admin-dashboard/books")
    public String listBooks(HttpSession session,Model model) {
        if (session.getAttribute("adminUser") == null) {
            return "redirect:/admin_login";
        }
        model.addAttribute("books", bookService.getAllBooks());
        return "books";
    }

    // Show all books on the dashboard
    @GetMapping("/dashboard")
    public String getAllBooks(HttpSession session,Model model) {
        //System.out.println("Dashboard endpoint called.");
        if (session.getAttribute("loggedInUser") == null) {
            return "redirect:/user-login";
        }
        List<Book> books = bookService.getAllBooks();
        model.addAttribute("books", books);
        return "dashboard"; // points to templates/dashboard.html
    }

    // Show form to add a new book
    @GetMapping("/admin-dashboard/books/add")
    public String showAddBookForm(Model model, HttpSession session) {
        if (session.getAttribute("adminUser") == null) {
            return "redirect:/admin_login";
        }
        model.addAttribute("book", new Book());
        return "book-add";
    }

    // Process adding a new book
    @PostMapping("/admin-dashboard/books/add")
    public String addBook(@ModelAttribute("book") Book book, HttpSession session) {
        if (session.getAttribute("adminUser") == null) {
            return "redirect:/admin_login";
        }
        List<Member> allMembers = memberService.getAllMembers();
        for (Member member : allMembers) {
            notificationService.sendNotification(
                    member.getLibraryId(),
                    "New Book Added: " + book.getTitle(),
                    "GENERAL",
                    book.getBookId()
            );
        }
        bookService.addBook(book);
        return "redirect:/admin-dashboard/books";
    }

    // Show form to edit an existing book
    @GetMapping("/admin-dashboard/books/edit/{id}")
    public String showEditBookForm(@PathVariable Long id, Model model, HttpSession session) {
        if (session.getAttribute("adminUser") == null) {
            return "redirect:/admin_login";
        }
        Optional<Book> book = bookService.getBookById(id);
        if (book.isPresent()) {
            model.addAttribute("book", book.get());
            return "book-edit";
        } else {
            return "redirect:/admin-dashboard/books";
        }
    }

    // Process editing a book
    @PostMapping("/admin-dashboard/books/edit/{id}")
    public String updateBook(@PathVariable Long id, @ModelAttribute("book") Book updatedBook, HttpSession session) {
        if (session.getAttribute("adminUser") == null) {
            return "redirect:/admin_login";
        }
        bookService.updateBook(id, updatedBook);
        return "redirect:/admin-dashboard/books";
    }

    // Delete a book
    @GetMapping("/admin-dashboard/books/delete/{id}")
    public String deleteBook(@PathVariable Long id, RedirectAttributes redirectAttributes, HttpSession session) {
        if (session.getAttribute("adminUser") == null) {
            return "redirect:/admin_login";
        }
        boolean deleted = bookService.deleteBook(id);

        if (deleted) {
            redirectAttributes.addFlashAttribute("successMessage", "Member deleted successfully.");
        } else {
            redirectAttributes.addFlashAttribute("errorMessage", "Member not found or could not be deleted.");
        }
        return "redirect:/admin-dashboard/books";
    }

    // Search books
    @GetMapping("/dashboard/search")
    public String searchBooks(@RequestParam(name = "keyword", required = false) String query, Model model, HttpSession session) {
        if (session.getAttribute("loggedInUser") == null) {
            return "redirect:/user-login";
        }
        List<Book> books = bookService.searchBooks(query);
        model.addAttribute("books", books);
        return "dashboard";
    }

    @PostMapping("/admin-dashboard/books/{id}/copies/increment")
    public String incrementCopies(@PathVariable Long id, HttpSession session) {
        if (session.getAttribute("adminUser") == null) {
            return "redirect:/admin_login";
        }
        bookService.updateBookCopies(id, 1);
        return "redirect:/admin-dashboard/books";
    }

    // Decrement copies
    @PostMapping("/admin-dashboard/books/{id}/copies/decrement")
    public String decrementCopies(@PathVariable Long id, HttpSession session) {
        if (session.getAttribute("adminUser") == null) {
            return "redirect:/admin_login";
        }
        bookService.updateBookCopies(id, -1);
        return "redirect:/admin-dashboard/books";
    }

}
