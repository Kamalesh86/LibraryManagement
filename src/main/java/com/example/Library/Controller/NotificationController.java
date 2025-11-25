package com.example.Library.Controller;

import com.example.Library.Entity.Notification;
import com.example.Library.Service.NotificationService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
@RequestMapping("/dashboard")
public class NotificationController {

    @Autowired
    private NotificationService notificationService;

    @GetMapping("/notifications")
    public String viewNotifications(HttpSession session, Model model) {
        //Object user = session.getAttribute("loggedInUser");
        if (session.getAttribute("loggedInUser") == null) {
            return "redirect:/user-login"; // or wherever your login page is
        }
        String memberId = (String) session.getAttribute("loggedInUser"); // Assuming ID is stored here
        List<Notification> notifications = notificationService.getNotificationsForMember(memberId);
        long unreadCount = notifications.stream().filter(n -> !n.getIsRead()).count();

        model.addAttribute("notifications", notifications);
        model.addAttribute("unreadCount", unreadCount);

        return "notification"; // notification.html
    }

    @PostMapping("/notifications/mark-read")
    public String markAsRead(@RequestParam("notificationId") Long notificationId) {
        notificationService.markAsRead(notificationId);
        return "redirect:/dashboard/notifications";
    }
}
