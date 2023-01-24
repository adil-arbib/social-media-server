package com.arbib.social_media.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class LikeDto {
    private String type;
    private Long post_id;
    private Long user_id;
}
