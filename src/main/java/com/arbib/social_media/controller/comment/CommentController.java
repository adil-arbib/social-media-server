package com.arbib.social_media.controller.comment;

import com.arbib.social_media.dto.CommentDto;
import com.arbib.social_media.model.comment.Comment;
import com.arbib.social_media.model.comment.CommentRepository;
import com.arbib.social_media.model.post.Post;
import com.arbib.social_media.model.post.PostRepository;
import com.arbib.social_media.model.user.AppUser;
import com.arbib.social_media.model.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

@RestController
@RequestMapping("/api/v1/comments")
@RequiredArgsConstructor
public class CommentController {

    private final CommentRepository commentRepository;
    private final PostRepository postRepository;
    private final UserRepository userRepository;


    @GetMapping("/{id}")
    public ResponseEntity<Comment> getCommentById(@PathVariable Long id) {
        return ResponseEntity.ok(commentRepository.findById(id).get());
    }

    @GetMapping("/{post_id}/comments")
    public ResponseEntity<List<Comment>> getAllComments(@PathVariable Long post_id){
        Optional<Post> optionalPost = postRepository.findById(post_id);
        if(optionalPost.isEmpty()){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
        return ResponseEntity.ok(commentRepository.findComments(post_id));
    }


    @PostMapping("/add-comment")
    public ResponseEntity<Comment> addComment(@RequestBody CommentDto commentDto) {
        Optional<AppUser> optionalUser = userRepository.findById(commentDto.getUser_id());
        Optional<Post> optionalPost = postRepository.findById(commentDto.getPost_id());
        if(optionalPost.isEmpty() || optionalUser.isEmpty())
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);

        Comment comment = new Comment();
        comment.setContent(commentDto.getContent());
        comment.setPost(optionalPost.get());
        comment.setUser(optionalUser.get());
        comment.setCreate_at(new Date());
        return ResponseEntity.ok(commentRepository.save(comment));
    }

    @PutMapping("update-comment")
    public ResponseEntity<Comment> updateComment(@RequestBody CommentDto commentDto) {
        Optional<AppUser> optionalUser = userRepository.findById(commentDto.getUser_id());
        Optional<Post> optionalPost = postRepository.findById(commentDto.getPost_id());
        Optional<Comment> optionalComment = commentRepository.findById(commentDto.getId());
        if(optionalPost.isEmpty() || optionalUser.isEmpty() || optionalComment.isEmpty())
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);

        Comment comment = optionalComment.get();
        comment.setUpdate_at(new Date());
        comment.setContent(commentDto.getContent());
        return ResponseEntity.ok(commentRepository.save(comment));
    }



}
