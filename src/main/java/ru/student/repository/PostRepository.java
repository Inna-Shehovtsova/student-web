package ru.student.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.student.entity.Post;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;


public interface PostRepository extends JpaRepository<Post, Long> {

    Optional<Post> findByTitle(String title);

    List<Post> findByDtCreatedBetween(LocalDateTime from, LocalDateTime to);

    List<Post> findByContent(String content);
    List<Post> findByContentContaining(String content);
    List<Post> findByTitleContaining(String content);

//    List<Post> findSubsting(String content){
//        List<Post> p = findByContentContaining(content);
//        List<Post> t = findByTitleContaining(content);
//        return p.addAll(t);
//    }

    @Query(value = """
            select p.*
                from post p
            join post_tag pt
                on p.post_id = pt.post_id
            group by p.post_id
            order by count(*) desc
            """, nativeQuery = true)
    List<Post> findSortedTagSorted();

    @Query(value = """
            select p.*
                from post p
            join users pt
                on p.user_id = pt.user_id
            """, nativeQuery = true)
    List<Post> findByUserId(Long userId);

}
