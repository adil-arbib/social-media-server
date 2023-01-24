package com.arbib.social_media;

import com.arbib.social_media.dto.Note;
import com.arbib.social_media.model.post.Post;
import com.arbib.social_media.model.post.PostRepository;
import com.arbib.social_media.model.role.Role;
import com.arbib.social_media.model.role.RoleRepository;
import com.arbib.social_media.model.user.AppUser;
import com.arbib.social_media.model.user.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

@SpringBootTest
class SocialMediaApplicationTests {

	@Autowired
	private  RoleRepository roleRepository;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private PostRepository postRepository;






}
