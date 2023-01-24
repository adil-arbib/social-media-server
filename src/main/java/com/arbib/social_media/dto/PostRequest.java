package com.arbib.social_media.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
public class PostRequest {
    private Long userID;
    private int page;
}
