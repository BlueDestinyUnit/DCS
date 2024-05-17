package com.scd.dcs.domains.vos;


import com.scd.dcs.domains.entities.BoardEntity;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class BoardVo extends BoardEntity {
   private String by;
   private String keyword;

   private int requestPage = 1; // 요청한 페이지 번호
   private int countPerPage  = 10; // 한 페이지당 표시할 게시글(줄)의 개수
   private int totalCount;
   private int minPage = 1;
   private int maxPage;
   private int offset;


}
