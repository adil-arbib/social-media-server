package com.arbib.social_media.model.post;

import com.arbib.social_media.model.comment.Comment;
import com.arbib.social_media.model.user.AppUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PostRepository extends JpaRepository<Post,Long> {

    Optional<Post> findById(Long id);


    @Query(value = "SELECT * FROM post WHERE user_id = :uid"
            ,nativeQuery = true)
    Optional<List<Post>> getUserPosts(@Param("uid") Long udi);

    @Query(value = "SELECT * FROM post WHERE user_id IN " +
            "(SELECT follower_id FROM followers WHERE person_id = :uid) " +
            "ORDER BY create_at DESC LIMIT :page,10"
            ,nativeQuery = true)
    List<Post> getFollowingPosts(@Param("uid") Long udi, @Param("page") int page);


    @Query(value = "SELECT COUNT(id) FROM likes WHERE post_id = :id", nativeQuery = true)
    Long getPostLikesCount(@Param("id")Long id);


    @Query(value = "SELECT COUNT(id) FROM comment WHERE post_id = :id", nativeQuery = true)
    Long getPostCommentsCount(@Param("id")Long id);






}
