package jpabook.jpashop.service;

import jpabook.jpashop.domain.Member;
import jpabook.jpashop.repository.MemberRepository;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;

    //sign in
    @Transactional
    public Long join(Member member){
        validateDuplicatedMember(member); //member validation if it is duplicated or not
        memberRepository.save(member);
        return member.getId();
    }

    private void validateDuplicatedMember(Member member) {
        List<Member> findMembers = memberRepository.findByName(member.getName());
        if(!findMembers.isEmpty()){
            throw new IllegalStateException("The member already exists");
        }
    }


    //search all members
    @Transactional(readOnly = true)
    public List<Member> findMembers(){
        return memberRepository.findAll();
    }

    //search one member
    @Transactional(readOnly = true)
    public Member findOne(Long memberId){
        return memberRepository.findOne(memberId);
    }

    /*
        readOnly allows to database optimise itself
        Annotation Transactional has true as default for readOnly
     */
}
