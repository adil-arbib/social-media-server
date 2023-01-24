package com.arbib.social_media.model.like;

import com.arbib.social_media.dto.LikeDto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface LikeRepository extends JpaRepository<Like, Long> {

    @Query(
            value = "select * from likes where post_id = ?1 and user_id = ?2 ",
            nativeQuery = true)
    Optional<Like> findByPostAndUser(Long id_post, Long id_user);

    @Query(value = "SELECT * FROM likes WHERE post_id = :id", nativeQuery = true)
    List<Like> getAssociatedLikes(@Param("id") Long id);

}
