package jpabook.jpashop.service;

import jpabook.jpashop.domain.Member;
import jpabook.jpashop.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor //만들어 져 있는 필드를 이용하여 생성자 생성
public class MemberService {
    private final MemberRepository memberRepository; // final로 해놓아야 컴파일 과정에서 실수를 잡을 수 있다.
    @Transactional() // 따로 해놓은면 따로 해놓은게 우선 적용된다. read 가능 write 가능
    public Long join(Member member){
        validateDuplicateMmeber(member);
        memberRepository.save(member);
        return member.getId();
    }

    @Transactional // 변경감지
    public void update(Long id,String name){
        Member member = memberRepository.findOne(id);
        member.setName(name);
        // return을 void가 아닌 Member로 하면 SELECT 조회를 하게 된다.
        // update문 + SELECT 문까지 같이 묶여 동작하므로 return을 void하거나 id만 반환해주어 분리해주는 것이 좋다.
    }

    private void validateDuplicateMmeber(Member member) {
        List<Member> findMembers = memberRepository.findByName(member.getName());
        if(!findMembers.isEmpty()) {
            throw new IllegalStateException("이미 존재하는 회원입니다.");
        }
    }

    public List<Member> findMember(){
        return memberRepository.findAll();
    }

    public Member findOne (Long memberId){
        return memberRepository.findOne(memberId);
    }

    //회원 전체 조회
}
