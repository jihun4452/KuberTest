package com.example.userLogin.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.ArrayList;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
public class CommentResponseDto {
  private Long parent_id;
  private Long id;
  private String content;
  private List<CommentResponseDto> children = new ArrayList<>();
}
