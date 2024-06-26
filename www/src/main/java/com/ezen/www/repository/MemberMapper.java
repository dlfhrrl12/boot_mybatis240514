package com.ezen.www.repository;

import com.ezen.www.domain.AuthVO;
import com.ezen.www.domain.MemberVO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface MemberMapper {
    int insert(MemberVO mvo);

    int insertAuth(String email);

    MemberVO selectEmail(String username);

    List<AuthVO> selectAuth(String username);

    List<MemberVO> getList();

    List<AuthVO> selectAuths(String email);

    int updateNoPwd(MemberVO mvo);

    int update(MemberVO mvo);
}
