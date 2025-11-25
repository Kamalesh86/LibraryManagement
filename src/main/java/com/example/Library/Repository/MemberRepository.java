package com.example.Library.Repository;

import com.example.Library.Entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MemberRepository extends JpaRepository<Member,Long> {
    long count();

    boolean existsByEmail(String email);

    Optional<Member> findByEmail(String email);

    boolean existsByLibraryId(String libraryId);

    Optional<Member> findByLibraryId(String libraryId);

}
