package com.arbib.social_media.controller.auth;

import com.arbib.social_media.dto.LoginDto;
import com.arbib.social_media.dto.UserDto;
import com.arbib.social_media.model.role.Role;
import com.arbib.social_media.model.role.RoleRepository;
import com.arbib.social_media.model.user.AppUser;
import com.arbib.social_media.model.user.UserRepository;
import com.arbib.social_media.security.PasswordEncoder;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final RoleRepository roleRepository;


    @PostMapping("/login-user")
    public ResponseEntity<AppUser> login(@RequestBody LoginDto loginDto) {
        Optional<AppUser> optionalUser = userRepository.findByEmail(loginDto.getEmail());
        if(optionalUser.isEmpty()) return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(null);
        AppUser user = optionalUser.get();
        if(!passwordEncoder.bCryptPasswordEncoder().matches(
                loginDto.getPassword(),
                user.getPassword()
        )) return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(null);
        return ResponseEntity.ok(user);
    }


    @PostMapping("/register-user")
    public ResponseEntity<AppUser> register(@RequestBody UserDto userDto) {
        if(userRepository.existsByEmail(userDto.getEmail())) return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(null);
        AppUser user = new AppUser();
        user.setFirstName(userDto.getFirstName());
        user.setEmail(userDto.getEmail());
        user.setLastName(userDto.getLastName());
        user.setPassword(passwordEncoder.bCryptPasswordEncoder().encode(userDto.getPassword()));
        user.setBirthday(userDto.getBirthday());
        Role role = roleRepository.findByName("ROLE_USER");
        user.setRole(role);
        return ResponseEntity.ok(userRepository.save(user));
    }

}
