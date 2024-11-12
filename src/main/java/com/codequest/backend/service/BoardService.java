package com.codequest.backend.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.codequest.backend.entity.Board;
import com.codequest.backend.repository.BoardRepository;

@Service
public class BoardService {

    @Autowired
    private BoardRepository boardRepository;

    public BoardService(BoardRepository boardRepository) {
        this.boardRepository = boardRepository;
    }

    // 검색어가 있는 경우 검색된 게시글을 반환
    public Page<Board> searchBoards(String search, Pageable pageable) {
        return boardRepository.findByTitleContainingOrContentContaining(search, search, pageable);
    }

    // 모든 게시글을 페이지네이션하여 반환
    public Page<Board> getAllBoards(Pageable pageable) {
        Pageable sortedByViews = PageRequest.of(
                pageable.getPageNumber(),
                pageable.getPageSize(),
                Sort.by(Sort.Direction.DESC, "views")
        );
        return boardRepository.findAll(sortedByViews);
    }
}
