package com.ezen.www.security;

import com.ezen.www.domain.MemberVO;
import com.ezen.www.repository.MemberMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

@Slf4j
public class CustomUserService implements UserDetailsService {

    @Autowired
    private MemberMapper memberMapper;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        log.info("loginmvo {}",username);
        MemberVO loginMvo = memberMapper.selectEmail(username);
        log.info("loginmvo {}",loginMvo);
        loginMvo.setAuthList(memberMapper.selectAuth(username));
        //userDetails return
        return new AuthMember(loginMvo);
    }
}
