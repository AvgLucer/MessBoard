
package com.messboard.repository;

import com.messboard.model.Post;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostRepository
        extends JpaRepository<Post, Long> {
}
