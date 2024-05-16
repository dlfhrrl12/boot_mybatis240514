package com.ezen.www.service;

import com.ezen.www.domain.CommentVO;
import com.ezen.www.domain.PagingVO;
import com.ezen.www.handler.PagingHandler;
import com.ezen.www.repository.CommmentMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.beans.Transient;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Service
public class CommentServiceImpl implements CommentService{
    private final CommmentMapper commmentMapper;

    @Override
    public int post(CommentVO cvo) {
        return commmentMapper.post(cvo);
    }

    @Transactional
    @Override
    public PagingHandler getList(long bno, PagingVO pgvo) {
        //totalCount
        int totalCount = commmentMapper.bnoTotalCount(bno);
        //Comment List
        List<CommentVO> cmtList = commmentMapper.getList(bno, pgvo);
        return new PagingHandler(pgvo, totalCount, cmtList);
    }

    @Override
    public int modify(CommentVO cvo) {
        return commmentMapper.update(cvo);
    }

    @Override
    public int delete(long cno) {
        return commmentMapper.delete(cno);
    }


}
