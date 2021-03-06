package iducs.springboot.boot.service;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.dsl.BooleanExpression;
import iducs.springboot.boot.domain.Member;
import iducs.springboot.boot.domain.PageRequestDTO;
import iducs.springboot.boot.domain.PageResultDTO;
import iducs.springboot.boot.entity.MemberEntity;
import iducs.springboot.boot.entity.MemoEntity;
import iducs.springboot.boot.entity.QMemberEntity;
import iducs.springboot.boot.repository.MemberRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;

@Service
public class MemberServiceImpl implements MemberService {

    private final MemberRepository memberRepository;

    public MemberServiceImpl(MemberRepository memberRepository) { // 생성자 주입
        this.memberRepository = memberRepository;
    }

    private MemberEntity dtoToEntity(Member member) {
        return MemberEntity.builder()
                .seq(member.getSeq())
                .id(member.getId())
                .pw(member.getPw())
                .name(member.getName())
                .email(member.getEmail())
                .phone(member.getPhone())
                .address(member.getAddress())
                .build();
    }

    private Member entityToDto(MemberEntity entity) {
        return Member.builder()
                .seq(entity.getSeq())
                .id(entity.getId())
                .pw(entity.getPw())
                .name(entity.getName())
                .email(entity.getEmail())
                .phone(entity.getPhone())
                .address(entity.getAddress())
                .build();
    }

    @Override
    public void create(Member member) {
        MemberEntity entity = dtoToEntity(member);
        memberRepository.save(entity);
    }

    @Override
    public Member readById(Long seq) {
        Member member = null;

        Optional<MemberEntity> entity = memberRepository.findById(seq);
        if(entity.isPresent()) {
            member = entityToDto(entity.get());
        }

//        // Other Solution
//        MemberEntity entity = memberRepository.getById(seq);
//        member = entityToDto(entity);

        return member;
    }

    @Override
    public List<Member> readAll() {
        List<Member> members = new ArrayList<>();
        List<MemberEntity> entities = memberRepository.findAll();
        for (MemberEntity entity : entities) {
            members.add(entityToDto(entity));
        }
        return members;
    }

    @Override
    public void update(Member member) {
        MemberEntity entity = dtoToEntity(member);
        memberRepository.save(entity);
    }

    @Override
    public void delete(Member member) {
        MemberEntity entity = dtoToEntity(member);
        memberRepository.deleteById(entity.getSeq());
    }

    @Override
    public Member readByName(Member member) {
        return null;
    }

    @Override
    public Member readByEmail(String email) {
        return null;
    }

    @Override
    public PageResultDTO<Member, MemberEntity> readListBy(PageRequestDTO pageRequestDTO) {
        Pageable pageable = pageRequestDTO.getPageable(Sort.by("seq").ascending());
        // 조건을 추가한 findAll
        BooleanBuilder booleanBuilder = findByCondition(pageRequestDTO);
        Page<MemberEntity> result = memberRepository.findAll(booleanBuilder, pageable);
        // 모두 가져오는 findAll
//        Page<MemberEntity> result = memberRepository.findAll(pageable);
        Function<MemberEntity, Member> fn = (entity -> entityToDto(entity));
        return new PageResultDTO<>(result, fn);
    }

    private BooleanBuilder findByCondition(PageRequestDTO pageRequestDTO) {
        String type = pageRequestDTO.getType();
        BooleanBuilder booleanBuilder = new BooleanBuilder();

        QMemberEntity qMemberEntity = QMemberEntity.memberEntity;

        BooleanExpression expression = qMemberEntity.seq.gt(0L); // where seq > 0 and title == "ti"
        booleanBuilder.and(expression);

        if(type == null || type.trim().length() == 0) {
            return booleanBuilder;
        }

        String keyword = pageRequestDTO.getKeyword();

        BooleanBuilder conditionBuilder = new BooleanBuilder();
        if(type.contains("e")) // email로 검색
            conditionBuilder.or(qMemberEntity.email.contains(keyword));
        if(type.contains("p")) // phone로 검색
            conditionBuilder.or(qMemberEntity.phone.contains(keyword));
        if(type.contains("a")) // address로 검색
            conditionBuilder.or(qMemberEntity.address.contains(keyword));
        booleanBuilder.and(conditionBuilder);
        return booleanBuilder; // 완성된 조건 or 술어(predicate)
    }


}
