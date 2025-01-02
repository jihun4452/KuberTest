package com.example.userLogin.service;

import com.example.userLogin.dto.request.CommentRequestDto;
import com.example.userLogin.dto.response.CommentResponseDto;
import com.example.userLogin.entity.Comment;
import com.example.userLogin.entity.ScheduleEntity;
import com.example.userLogin.repository.CommentRepository;
import com.example.userLogin.repository.ScheduleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.webjars.NotFoundException;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService {

  private final ScheduleRepository scheduleRepository;
  private final CommentRepository commentRepository;

  @Override
  public void createComment(Long schedule_id, CommentRequestDto commentRequestDto) {
    ScheduleEntity scheduleEntity = scheduleRepository.findById(schedule_id)
        .orElseThrow(() ->new NotFoundException("찾을 수 없는 일정입니다."));

    Comment comment = Comment.builder()
        .scheduleEntity(scheduleEntity)
        .content(commentRequestDto.getContent())
        .build();

    commentRepository.save(comment);
  }

  @Override
  public void createReply(Long schedule_id, Long comment_id, CommentRequestDto commentRequestDto) {
    ScheduleEntity scheduleEntity = scheduleRepository.findById(schedule_id)
        .orElseThrow(() ->new NotFoundException("찾을 수 없는 일정입니다."));
    Comment parent = commentRepository.findById(comment_id)
        .orElseThrow(() ->new NotFoundException("찾을 수 없는 댓글입니다."));

    Comment comment = Comment.builder()
        .scheduleEntity(scheduleEntity)
        .parent(parent)
        .content(commentRequestDto.getContent())
        .build();

    commentRepository.save(comment);
  }

  @Override
  public List<CommentResponseDto> getComments(Long schedule_id) {
    ScheduleEntity scheduleEntity = scheduleRepository.findById(schedule_id)
        .orElseThrow(() ->new NotFoundException("찾을 수 없는 일정입니다."));

    List<Comment> comments = scheduleEntity.getComments();
    return comments.stream()
        .filter(comment -> comment.getParent() == null)
        .map(comment -> CommentResponseDto.builder()
            .id(comment.getId())
            .content(comment.getContent())
            .children(getReplies(comment.getId()))
            .build())
        .collect(Collectors.toList());
  }

  @Override
  public List<CommentResponseDto> getReplies(Long comment_id) {
    Comment parent = commentRepository.findById(comment_id)
        .orElseThrow(() ->new NotFoundException("찾을 수 없는 댓글입니다."));

    List<Comment> replies = parent.getChildrens();
    return replies.stream()
        .map(reply -> CommentResponseDto.builder()
            .parent_id(reply.getParent().getId())
            .id(reply.getId())
            .content(reply.getContent())
            .build())
        .collect(Collectors.toList());
  }

  @Override
  public void updateComment(Long comment_id, CommentRequestDto commentRequestDto) {
    Comment comment = commentRepository.findById(comment_id)
        .orElseThrow(()-> new NotFoundException("찾을 수 없는 댓글입니다."));

    comment.update(commentRequestDto);
  }

  @Override
  public void deleteComment(Long comment_id) {
    Comment comment = commentRepository.findById(comment_id)
        .orElseThrow(() -> new NotFoundException("찾을 수 없는 댓글 입니다."));

    commentRepository.delete(comment);
  }
}
