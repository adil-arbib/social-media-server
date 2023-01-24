package com.arbib.social_media.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Follow {
    private Long personID;
    private Long followerID;
}
