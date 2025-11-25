package com.example.Library.Controller;

import com.example.Library.Entity.Book;
import com.example.Library.Entity.Member;
import com.example.Library.Service.BookService;
import com.example.Library.Service.MemberService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.Optional;

@Controller
public class MemberController {
    @Autowired
    private MemberService memberService;


    @GetMapping("/admin-dashboard/members")
    public String listMembers(HttpSession session,Model model) {
        if (session.getAttribute("adminUser") == null) {
            return "redirect:/admin_login";
        }
        List<Member> members = memberService.getAllMembers();
        model.addAttribute("members", members);
        return "members"; // members.html
    }


    @GetMapping("/user-login")
    public String loginPage() {
        return "login";
    }

    @GetMapping("/user-register")
    public String showRegistrationForm(Model model) {
        model.addAttribute("member", new Member());
        return "register";
    }

    @PostMapping("/user-register")
    public String registerMember(@ModelAttribute("member") Member member, Model model) {
        Member saved = memberService.registerMember(member);
        if (saved == null) {
            model.addAttribute("error", "Email already registered.");
            return "register";
        }
        model.addAttribute("success", "Registration successful!\nYour Library ID is: " + saved.getLibraryId()+" Save it for Future Reference");
        //model.addAttribute("libraryId", saved.getLibraryId());
        return "login";
    }


    @PostMapping("/user-login")
    public String loginMember(@RequestParam String libraryId,
                              @RequestParam String password,
                              HttpSession session,
                              Model model) {
        Optional<Member> omember = memberService.authenticateMemberByLibraryId(libraryId, password);
        if (omember.isPresent()) {
            Member member = omember.get();
            session.setAttribute("loggedInUser", member.getLibraryId());
            return "redirect:/dashboard";
        } else {
            model.addAttribute("error", "Invalid Library ID or password.");
            return "login";
        }
    }




    @GetMapping("/dashboard/profile")
    public String showUserProfile(HttpSession session, Model model) {
        String memberId = (String) session.getAttribute("loggedInUser");
        if (memberId == null) {
            return "redirect:/login"; // or appropriate error page
        }
        Optional<Member> optionalMember = memberService.getMemberById(memberId);
        if(optionalMember.isPresent()) {
            Member member = optionalMember.get();
            model.addAttribute("user", member);
            // Pass user to Thymeleaf
            return "user_profile"; // returns userprofile.html
        }
        else{
            return "redirect:/login";
        }
    }


    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate(); // Clears the session
        return "redirect:/";  // Redirects to home
    }

    @GetMapping("/admin-dashboard/members/edit/{id}")
    public String editMemberForm(@PathVariable Long id, HttpSession session, Model model) {
        if (session.getAttribute("adminUser") == null) {
            return "redirect:/admin_login";
        }
        Optional<Member> optionalMember = memberService.getMemberById(id);
        if (optionalMember.isEmpty()) {
            model.addAttribute("errorMessage", "Member not found");
            return "redirect:/admin-dashboard/members"; // fallback to members list
        }

        model.addAttribute("successMessage", "Member Deleted Successfully");
        return "redirect:/admin-dashboard/members";
    }

    @GetMapping("/admin-dashboard/members/delete/{id}")
    public String deleteMember(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        boolean deleted = memberService.deleteMemberById(id);

        if (deleted) {
            redirectAttributes.addFlashAttribute("successMessage", "Member deleted successfully.");
        } else {
            redirectAttributes.addFlashAttribute("errorMessage", "Member not found or could not be deleted.");
        }

        return "redirect:/admin-dashboard/members";
    }

}
