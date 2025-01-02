package com.example.userLogin.controller;

import com.example.userLogin.dto.request.CommentRequestDto;
import com.example.userLogin.dto.response.CommentResponseDto;
import com.example.userLogin.service.CommentService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/comment")
public class CommentController {

  private final CommentService commentService;

  @Operation(summary = "댓글 생성")
  @PostMapping("/{schedule_id}")
  public ResponseEntity<String> createComment(@PathVariable Long schedule_id,
                                              @RequestBody CommentRequestDto commentRequestDto) {
    commentService.createComment(schedule_id, commentRequestDto);
    return ResponseEntity.ok("댓글 작성 완료");
  }

  @Operation(summary = "대댓글 생성")
  @PostMapping("/{schedule_id}/{comment_id}")
  public ResponseEntity<String> createReply(@PathVariable Long schedule_id,
                                            @PathVariable Long comment_id,
                                            @RequestBody CommentRequestDto commentRequestDto) {
    commentService.createReply(schedule_id, comment_id, commentRequestDto);
    return ResponseEntity.ok("대댓글 작성 완료");
  }

  @Operation(summary = "댓글 조회")
  @GetMapping("/{schedule_id}")
  public ResponseEntity<List<CommentResponseDto>> getComments(@PathVariable Long schedule_id) {
    List<CommentResponseDto> comments = commentService.getComments(schedule_id);
    return ResponseEntity.ok(comments);
  }

  @Operation(summary = "댓글 수정")
  @PutMapping("/{comment_id}")
  public ResponseEntity<String> updateComment(@PathVariable Long comment_id,
                                              @RequestBody CommentRequestDto commentRequestDto) {
    commentService.updateComment(comment_id, commentRequestDto);
    return ResponseEntity.ok("댓글 수정 완료");
  }

  @Operation(summary = "댓글 삭제")
  @DeleteMapping("/{comment_id}")
  public ResponseEntity<String> deleteComment(@PathVariable Long comment_id){
    commentService.deleteComment(comment_id);
    return ResponseEntity.ok("댓글 삭제 완료");
  }
}
