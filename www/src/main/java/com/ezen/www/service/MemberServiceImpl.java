package com.ezen.www.service;

import com.ezen.www.domain.MemberVO;
import com.ezen.www.repository.MemberMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Service
public class MemberServiceImpl implements MemberService {
    private final MemberMapper memberMapper;

    @Override
    public int insert(MemberVO mvo) {
        int isOk = memberMapper.insert(mvo);

        return (isOk > 0 ? memberMapper.insertAuth(mvo.getEmail()) : 0 );
    }

    @Override
    public List<MemberVO> getList() {
        List<MemberVO> memberList = memberMapper.getList();
        for(MemberVO mvo : memberList){
            mvo.setAuthList(memberMapper.selectAuths(mvo.getEmail()));
        }
        return memberList;
    }

    @Override
    public int modifyNoPwd(MemberVO mvo) {
        return memberMapper.updateNoPwd(mvo);
    }

    @Override
    public int modify(MemberVO mvo) {
        return memberMapper.update(mvo);
    }


}
