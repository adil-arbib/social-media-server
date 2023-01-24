package com.arbib.social_media.controller.user;

import com.arbib.social_media.dto.Follow;
import com.arbib.social_media.model.user.AppUser;
import com.arbib.social_media.model.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.Entity;
import java.io.IOException;
import java.net.URI;
import java.util.*;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UserController {

    private final UserRepository userRepository;



    @PutMapping("/{id}/update-picture")
    public ResponseEntity<String> updateProfilePicture(
            @PathVariable(name = "id") Long id,
            @RequestParam("image") MultipartFile file) throws IOException {
        Optional<AppUser> optionalAppUser = userRepository.findById(id);
        if(optionalAppUser.isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("user not found !!");
        }
        AppUser user = optionalAppUser.get();
        user.setPicture(file.getBytes());
        userRepository.save(user);
        return ResponseEntity.ok("uploaded successfully");
    }

    @GetMapping("/{id}")
    public ResponseEntity<AppUser> getUserById(@PathVariable Long id) {
        Optional<AppUser> optionalAppUser = userRepository.findById(id);
        if(optionalAppUser.isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
        AppUser user = optionalAppUser.get();
        return ResponseEntity.ok(user);

    }



    @PostMapping("/add-following")
    public ResponseEntity<?> addFollowing(@RequestBody Follow follow) {
        if(userRepository.checkFollowing(follow.getPersonID(), follow.getFollowerID()).isPresent()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("you are already following this account !!");
        }
        userRepository.addFollower(follow.getPersonID(),follow.getFollowerID());
        return ResponseEntity.ok().body("followed successfully !!");
    }


    @PostMapping("/remove-following")
    public ResponseEntity<?> removeFollowing(@RequestBody Follow follow) {
        if(userRepository.checkFollowing(follow.getPersonID(), follow.getFollowerID()).isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("your not following this account !!");
        }
        userRepository.removeFollowing(follow.getPersonID(),follow.getFollowerID());
        return ResponseEntity.ok().body("removed successfully !!");
    }

    @GetMapping("/{id}/followers")
    public ResponseEntity<?> getFollowers(@PathVariable(name = "id") Long id) {
        Optional<AppUser> optionalAppUser = userRepository.findById(id);
        if(optionalAppUser.isEmpty()) return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body("user with id "+id+" not found !!");
        return ResponseEntity.ok()
                .body(userRepository.getFollowers(id));
    }


    @GetMapping("/{id}/following")
    public ResponseEntity<?> getFollowing(@PathVariable(name = "id") Long id) {
        Optional<AppUser> optionalAppUser = userRepository.findById(id);
        if(optionalAppUser.isEmpty()) return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body("user with id "+id+" not found !!");
        return ResponseEntity.ok()
                .body(userRepository.getFollowings(id));
    }


    @GetMapping("/{id}/suggestions/{page}")
    public ResponseEntity<List<AppUser>> suggestedUsers(@PathVariable(name = "id") Long id,
                                                        @PathVariable(name = "page") int page) {

        return ResponseEntity.ok(userRepository.suggestFriends(id,page));
    }







}
