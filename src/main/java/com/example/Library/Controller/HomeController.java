package com.example.Library.Controller;


import com.example.Library.Service.BookService;
import com.example.Library.Service.BorrowingTransactionService;
import com.example.Library.Service.MemberService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Map;

@Controller
public class HomeController {

    @Autowired
    MemberService memberService;

    @Autowired
    BookService bookService;

    @Autowired
    BorrowingTransactionService borrowingTransactionService;

    @GetMapping("/")
    public String showHomePage() {
        return "home"; // points to templates/home.html
    }

    private final Map<String, String> validAdmins = Map.of(
            "admin1", "pass@1",
            "admin2", "pass@2",
            "admin3", "pass@3",
            "admin4", "pass@4",
            "admin5", "pass@5"
    );

    @GetMapping("/admin-login")
    public String showAdminLogin() {
        return "admin_login";
    }

    @PostMapping("/admin-login")
    public String processAdminLogin(@RequestParam String email,
                                    @RequestParam String password,
                                    Model model,
                                    HttpSession session) {
        if (validAdmins.containsKey(email) && validAdmins.get(email).equals(password)) {
            session.setAttribute("adminUser", email);
            return "redirect:/admin-dashboard";
        } else {
            model.addAttribute("error", "Invalid Library ID or Password.");
            return "admin_login";
        }
    }

    @GetMapping("/admin-dashboard")
    public String showAdminDashboard(Model model,HttpSession session) {
        if (session.getAttribute("adminUser") == null) {
            return "redirect:/admin-login"; // or wherever your login page is
        }
        model.addAttribute("membersCount", memberService.getMembersCount());
        model.addAttribute("borrowedCount", borrowingTransactionService.getBorrowedCount());
        model.addAttribute("booksCount", bookService.getBooksCount());
        model.addAttribute("returnedCount", borrowingTransactionService.getReturnedCount());
        return "admin_dash";
    }

    @GetMapping("/admin-dashboard/profile")
    public String showAdminProfile(Model model, HttpSession session) {
        String adminEmail = (String) session.getAttribute("adminUser");
        if (adminEmail == null) {
            return "redirect:/admin-login";
        }

        // For static admin, hardcode info or customize as needed
        model.addAttribute("adminEmail", adminEmail); // Using email as name for demo
        model.addAttribute("adminPass", validAdmins.get(adminEmail));
        model.addAttribute("adminRole", "Administrator");

        return "admin_profile";  // Thymeleaf template name without .html
    }




}
