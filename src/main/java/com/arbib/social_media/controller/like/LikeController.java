package com.arbib.social_media.controller.like;

import com.arbib.social_media.dto.LikeDto;
import com.arbib.social_media.model.like.Like;
import com.arbib.social_media.model.like.LikeRepository;
import com.arbib.social_media.model.post.Post;
import com.arbib.social_media.model.post.PostRepository;
import com.arbib.social_media.model.user.AppUser;
import com.arbib.social_media.model.user.UserRepository;
import com.arbib.social_media.utils.ImageUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/likes")
@RequiredArgsConstructor
public class LikeController {

    private final LikeRepository likeRepository;
    private final UserRepository userRepository;
    private final PostRepository postRepository;

    @PostMapping("/add-like")
    public ResponseEntity<Like> addLikeToPost(@RequestBody LikeDto likeDto) {
        Optional<AppUser> optionalUser = userRepository.findById(likeDto.getUser_id());
        Optional<Post> optionalPost = postRepository.findById(likeDto.getPost_id());
        if (optionalPost.isEmpty() || optionalUser.isEmpty())
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(null);
        Optional<Like> optionalLike = likeRepository.findByPostAndUser(likeDto.getPost_id(), likeDto.getUser_id());
        if (optionalLike.isPresent() && optionalLike.get().getType().equals(likeDto.getType())){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(null);
        }else if (optionalLike.isPresent()) {
            Like like = optionalLike.get();
            like.setType(likeDto.getType());
            return ResponseEntity.ok(likeRepository.save(like));
        }
        Like like = new Like();
        like.setType(likeDto.getType());
        like.setPost(optionalPost.get());
        like.setUser(optionalUser.get());
        return ResponseEntity.ok(likeRepository.save(like));
    }

    @PostMapping("/cancel-like")
    public ResponseEntity<Like> cancelLikeFromPost(@RequestBody LikeDto likeDto) {
        Optional<AppUser> optionalUser = userRepository.findById(likeDto.getUser_id());
        Optional<Post> optionalPost = postRepository.findById(likeDto.getPost_id());
        if (optionalPost.isEmpty() || optionalUser.isEmpty())
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(null);
        Optional<Like> optionalLike = likeRepository.findByPostAndUser(likeDto.getPost_id(), likeDto.getUser_id());
        if (optionalLike.isEmpty())
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(null);

        likeRepository.delete(optionalLike.get());
        return ResponseEntity.ok().body(null);
    }

    @GetMapping("/check/{post_id}&{user_id}")
    public ResponseEntity<Like> checkLiked(@PathVariable Long post_id,
                                              @PathVariable Long user_id){
        Optional<Like> optionalLike = likeRepository.findByPostAndUser(post_id, user_id);
        return ResponseEntity.ok(optionalLike.isEmpty() ?
                null : optionalLike.get());
    }



    @GetMapping("/{post_id}")
    public ResponseEntity<List<Like>> getPostLike(@PathVariable Long post_id) {
        List<Like> list = likeRepository.getAssociatedLikes(post_id);
        list.forEach( like -> {
            AppUser user = like.getUser();
            user.setPicture(null);
        });
        return ResponseEntity.ok(list);
    }

}
