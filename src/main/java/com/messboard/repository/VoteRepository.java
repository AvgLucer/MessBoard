
package com.messboard.repository;

import com.messboard.model.Vote;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface VoteRepository extends JpaRepository<Vote, Long> {

    Optional<Vote> findByPostIdAndVoterId(
        Long postId,
        String voterId
    );

    long countByPostIdAndValue(
        Long postId,
        String value
    );
}
