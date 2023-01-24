package com.arbib.social_media.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;


import java.util.Date;

@Data
@AllArgsConstructor
public class PostDto {
    private Long id;
    private String content;
    private Long user_id;
}
