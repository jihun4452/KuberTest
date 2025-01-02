package com.example.userLogin.entity;

import com.example.userLogin.dto.request.CommentRequestDto;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Comment {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column
  private Long id;
  @Column
  private String content;

//  @ManyToOne(fetch = FetchType.LAZY)
//  @JoinColumn(name = "userEntity_id")
//  private UserEntity userEntity;
//
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "scheduleEntity_id")
  private ScheduleEntity scheduleEntity;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "parent_id")
  private Comment parent;

  @OneToMany(mappedBy = "parent", cascade = CascadeType.ALL, orphanRemoval = true)
  private List<Comment> childrens = new ArrayList<>();

  public void update(CommentRequestDto dto) {
    this.content = dto.getContent();
  }

}
