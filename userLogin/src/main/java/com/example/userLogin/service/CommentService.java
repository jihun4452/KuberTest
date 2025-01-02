package com.example.userLogin.service;

import com.example.userLogin.dto.request.CommentRequestDto;
import com.example.userLogin.dto.response.CommentResponseDto;
import jakarta.servlet.http.HttpServletRequest;

import java.util.List;

public interface CommentService {
  // 댓글 생성
  void createComment(Long review_id, CommentRequestDto commentRequestDto);
  // 대댓글 생성
  void createReply(Long reviewId, Long comment_id, CommentRequestDto commentRequestDto);
  // 댓글 조회
  List<CommentResponseDto> getComments(Long review_id);
  // 대댓글 조회
  List<CommentResponseDto> getReplies(Long comment_id);
  // 댓글 수정
  void updateComment(Long comment_id, CommentRequestDto commentRequestDto);
  // 댓글 삭제
  void deleteComment(Long comment_id);
}
