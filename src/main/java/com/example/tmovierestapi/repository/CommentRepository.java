package com.example.tmovierestapi.repository;

import com.example.tmovierestapi.model.Comment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {
    Optional<Comment> findCommentById(Long id);

    @Query(value = "SELECT * FROM comment c "
            + " WHERE c.movie_id = :id AND c.parent_id IS NULL \n#pageable\n", nativeQuery = true)
    Page<Comment> findAllRoots(@Param("id") Long movieID, Pageable pageable);

    @Query("SELECT comment FROM Comment comment"
            + " WHERE comment.parentComment.id IN :rootIds ")
    List<Comment> findAllSubCategoriesInRoot(@Param("rootIds") List<Long> rootIds);

    @Query(value = "SELECT * FROM comment c"
            + " WHERE c.movie_id = :movieId AND c.parent_id = :parentId \n#pageable\n", nativeQuery = true)
    Page<Comment> findAllChildCommentsByParentId(@Param("movieId") Long movieId ,@Param("parentId") Long parentId, Pageable pageable);

    @Query(
            value = "WITH RECURSIVE CommentCTE(id, parent_id, content) AS(" +
                    "    SELECT c.id, c.parent_id, c.content FROM Comment c WHERE c.parent_id IS NULL" +
                    "    UNION ALL" +
                    "    SELECT c2.id, c2.parent_id, c2.content FROM Comment c2 " +
                    "    INNER JOIN CommentCTE cte on c2.parent_id = cte.id WHERE c2.parent_id IS NOT NULL" +
                    ")" +
                    "SELECT id, parent_id, content FROM CommentCTE"
            , nativeQuery = true)
    List<Comment> getAllComments();

}
