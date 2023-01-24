package com.arbib.social_media.model.user;

import com.arbib.social_media.dto.Follow;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<AppUser,Long> {

    Optional<AppUser> findById(Long id);

    Optional<AppUser> findByEmail(String email);


    Optional<AppUser> findByEmailAndPassword(String email, String password);

    Boolean existsByEmail(String email);


    @Modifying
    @Transactional
    @Query(value = "INSERT INTO followers VALUES(:person_id, :follower_id)",nativeQuery = true)
    void addFollower(@Param("person_id") Long person_id, @Param("follower_id") Long follower_id);

    @Modifying
    @Transactional
    @Query(value = "DELETE FROM followers WHERE person_id = :person_id AND follower_id = :follower_id ",
            nativeQuery = true)
    void removeFollowing(@Param("person_id") Long person_id, @Param("follower_id") Long follower_id);

    @Query(value = "SELECT u.* FROM user u " +
            "inner join followers f " +
            "on u.id = f.follower_id WHERE " +
            "f.person_id = :person_id AND f.follower_id = :follower_id",
            nativeQuery = true)
    Optional<AppUser> checkFollowing(@Param("person_id") Long person_id, @Param("follower_id") Long follower_id);


    @Query(value = "SELECT distinct u.* from user u INNER JOIN followers f on f.person_id = u.id and f.follower_id = :uid"
            ,nativeQuery = true)
    List<AppUser> getFollowers(@Param("uid") Long uid);



    @Query(value = "SELECT u.* from user u INNER JOIN followers f on f.follower_id = u.id and f.person_id = :uid"
            ,nativeQuery = true)
    List<AppUser> getFollowings(@Param("uid") Long uid);


    @Query(value = "SELECT * FROM user where id<>:uid AND id NOT IN " +
            "(SELECT follower_id FROM followers where person_id = :uid)" +
            "ORDER BY first_name LIMIT :page, 10"
            ,nativeQuery = true)
    List<AppUser> suggestFriends(@Param("uid") Long id, @Param("page") int page);

}
