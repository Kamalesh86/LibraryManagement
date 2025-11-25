package com.example.Library.Repository;


import com.example.Library.Entity.Notification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NotificationRepository extends JpaRepository<Notification,Long> {

    List<Notification> findByMemberIdOrderByDateSentDesc(String memberId);
}
