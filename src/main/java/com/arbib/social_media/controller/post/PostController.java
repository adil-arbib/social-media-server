package com.arbib.social_media.controller.post;

import com.arbib.social_media.dto.PostDto;
import com.arbib.social_media.dto.PostRequest;
import com.arbib.social_media.model.post.Post;
import com.arbib.social_media.model.post.PostRepository;
import com.arbib.social_media.model.user.AppUser;
import com.arbib.social_media.model.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.*;

@RestController
@RequestMapping("/api/v1/posts")
@RequiredArgsConstructor
public class PostController {

    private final PostRepository postRepository;
    private final UserRepository userRepository;

    private final SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    @PostMapping("/add-post")
    public ResponseEntity<Post> addPost(@RequestBody PostDto postDto
                                     ) throws IOException {
        Optional<AppUser> optionalUser = userRepository.findById(postDto.getUser_id());
        if (optionalUser.isEmpty()) return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(null);
        Post post = new Post();
        post.setContent(postDto.getContent());
        post.setCreate_at(new Date());
        post.setUser(optionalUser.get());
        return ResponseEntity.ok(postRepository.save(post));
    }

    @PutMapping("/{id}/set-image")
    public ResponseEntity<Post> setPostPic(
            @PathVariable(name = "id") Long id,
            @RequestParam("image") MultipartFile file) throws IOException {

        Optional<Post> optionalAppUser = postRepository.findById(id);
        if(optionalAppUser.isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
        Post post = optionalAppUser.get();
        post.setImage(file.getBytes());
        return ResponseEntity.ok(postRepository.save(post));
    }



    @PutMapping("/update-post")
    public ResponseEntity<?> updatePost(@RequestBody PostDto postDto) {
        Optional<Post> optionalPost = postRepository.findById(postDto.getId());
        if(optionalPost.isEmpty())
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body("Post doesn't exist !!");


        Post post = optionalPost.get();
        post.setContent(postDto.getContent());
        post.setUpdate_at(new Date());
        return ResponseEntity.ok(postRepository.save(post));
    }



    @GetMapping("/{id}/get-posts")
    public ResponseEntity<?> getUserPosts(@PathVariable(name = "id") Long id) {
        if (userRepository.findById(id).isEmpty())
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("user not found !!");
        return ResponseEntity.ok().body(postRepository.getUserPosts(id));
    }


    @DeleteMapping("/remove-post")
    public ResponseEntity<?> removePost(@RequestBody Long id) {
        Optional<Post> optionalPost = postRepository.findById(id);
        if(optionalPost.isPresent()) {
            postRepository.delete(optionalPost.get());
            return ResponseEntity.ok().body("removed successfully !!");
        }
        return  ResponseEntity.status(HttpStatus.BAD_REQUEST).body("id not found !!");
    }


    @GetMapping("{id}/following-posts/{page}")
    public ResponseEntity<List<Post>> getFollowingPosts(@PathVariable(name = "id") Long id,
                                                        @PathVariable(name = "page") int page) {
        Optional<AppUser> user = userRepository.findById(id);
        if(user.isEmpty()) return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        return ResponseEntity.ok(postRepository.getFollowingPosts(id,page));
    }

    @GetMapping("{id}/likes-count")
    public ResponseEntity<Long> getPostLikesCount(@PathVariable(name = "id") Long id){
        if(!postRepository.existsById(id)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
        return ResponseEntity.ok(postRepository.getPostLikesCount(id));
    }

    @GetMapping("{id}/comments-count")
    public ResponseEntity<Long> getPostCommentsCount(@PathVariable(name = "id") Long id){
        if(!postRepository.existsById(id)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
        return ResponseEntity.ok(postRepository.getPostCommentsCount(id));
    }




}
