package com.example.Library.Service;

import com.example.Library.Entity.Member;
import com.example.Library.Repository.MemberRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class MemberService {
    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    /**
     * Registers a new member.
     * @param member The member object to register.
     * @return The registered member, or null if email already exists.
     */


    @Transactional
    public Member registerMember(Member member) {
        if (memberRepository.existsByEmail(member.getEmail())) {
            return null; // Email already registered
        }

        // Generate unique library ID (e.g., LIB123456)
        String libraryId;
        do {
            libraryId = "LIB" + (int)(Math.random() * 1_000_000);
        } while (memberRepository.existsByLibraryId(libraryId));

        member.setLibraryId(libraryId);
        member.setMembershipStatus("ACTIVE");
        member.setRole("USER");
        member.setLastBorrowDate(LocalDate.now());

        String encodedPassword = passwordEncoder.encode(member.getPassword());
        member.setPassword(encodedPassword);

        return memberRepository.save(member);
    }


    public Optional<Member> authenticateMemberByLibraryId(String libraryId, String password) {
        Optional<Member> optionalMember = memberRepository.findByLibraryId(libraryId);
        if (optionalMember.isPresent()) {
            Member member = optionalMember.get();
            if (passwordEncoder.matches(password, member.getPassword())) {
                return Optional.of(member);
            }
        }
        return Optional.empty();
    }



    public List<Member> getAllMembers() {
        return memberRepository.findAll();
    }


    public Optional<Member> getMemberById(Long id) {
        return memberRepository.findById(id);
    }

    public long getMembersCount() {
        return memberRepository.count();
    }

//
//    @Transactional
//    public Optional<Member> updateMember(Integer id, Member memberDetails) {
//        return memberRepository.findById(id)
//                .map(existingMember -> {
//                    existingMember.setName(memberDetails.getName());
//
//                    if (!existingMember.getEmail().equals(memberDetails.getEmail()) &&
//                            memberRepository.existsByEmail(memberDetails.getEmail())) {
//                        return null; // Conflict
//                    }
//
//                    existingMember.setEmail(memberDetails.getEmail());
//                    existingMember.setPhone(memberDetails.getPhone());
//                    existingMember.setAddress(memberDetails.getAddress());
//                    existingMember.setMembershipStatus(memberDetails.getMembershipStatus());
//
//                    if (memberDetails.getPassword() != null && !memberDetails.getPassword().isEmpty()) {
//                        existingMember.setPassword(memberDetails.getPassword()); // plain
//                    }
//
//                    existingMember.setRole(memberDetails.getRole());
//
//                    if (memberDetails.getLastBorrowDate() != null) {
//                        existingMember.setLastBorrowDate(memberDetails.getLastBorrowDate());
//                    }
//
//                    return memberRepository.save(existingMember);
//                });
//    }
//
    public boolean deleteMemberById(Long id) {
        if (memberRepository.existsById(id)) {
            memberRepository.deleteById(id);
            return true;
        }
        return false;
    }

    public Optional<Member> getMemberById(String id) {
        return memberRepository.findByLibraryId(id);
    }
//
//    @Transactional
//    public Optional<Member> updateMemberLastBorrowDate(Integer memberId, LocalDate newDate) {
//        return memberRepository.findById(memberId)
//                .map(member -> {
//                    member.setLastBorrowDate(newDate);
//                    return memberRepository.save(member);
//                });
//    }
//
//    @Scheduled(cron = "0 0 0 * * ?")
//    @Transactional
//    public void updateInactiveMembers() {
//        LocalDate threeMonthsAgo = LocalDate.now().minusMonths(3);
//        List<Member> activeMembers = memberRepository.findAll();
//
//        for (Member member : activeMembers) {
//            if ("ACTIVE".equalsIgnoreCase(member.getMembershipStatus()) &&
//                    member.getLastBorrowDate() != null &&
//                    member.getLastBorrowDate().isBefore(threeMonthsAgo)) {
//
//                member.setMembershipStatus("INACTIVE");
//                memberRepository.save(member);
//                System.out.println("Member " + member.getEmail() + " (ID: " + member.getMemberId() + ") set to INACTIVE due to inactivity.");
//            }
//        }
//    }
}
