package com.pintogether.backend.service;

import com.pintogether.backend.entity.CollectionComment;
import com.pintogether.backend.repository.CollectionCommentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class CollectionCommentService {

    private final CollectionCommentRepository collectionCommentRepository;

    public CollectionComment getCollectionComment(Long commentId) {
        return collectionCommentRepository.findOneById(commentId).orElse(null);
    }

    public List<CollectionComment> getCollectionComments(Long collectionId) {
        return collectionCommentRepository.findByCollectionId(collectionId);
    }
}
