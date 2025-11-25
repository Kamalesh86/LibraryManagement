package com.example.Library.Repository;

import com.example.Library.Entity.BorrowingTransaction;
import com.example.Library.Entity.Fine;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface FineRepository extends JpaRepository<Fine,Long> {
    Optional<Fine> findByBorrowingTransaction(BorrowingTransaction transaction);

}
