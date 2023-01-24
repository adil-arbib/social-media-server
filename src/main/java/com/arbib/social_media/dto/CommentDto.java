package com.arbib.social_media.dto;

import lombok.*;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class CommentDto {
    private Long id;
    private String content;
    private Long post_id;
    private Long user_id;
}
