package com.pintogether.backend.repository;

import com.pintogether.backend.entity.CollectionComment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CollectionCommentRepository extends JpaRepository<CollectionComment, Long> {
    Optional<CollectionComment> findOneById(Long id);

    List<CollectionComment> findByCollectionId(Long collectionId);

}
