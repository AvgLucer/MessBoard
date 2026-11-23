
package com.messboard.controller;

import com.messboard.model.*;
import com.messboard.repository.*;
import com.messboard.service.FileStorageService;

import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.*;

@RestController
@RequestMapping("/api/posts")
public class PostController {

    private final PostRepository postRepository;
    private final VoteRepository voteRepository;
    private final FileStorageService fileStorageService;

    public PostController(
        PostRepository postRepository,
        VoteRepository voteRepository,
        FileStorageService fileStorageService
    ) {
        this.postRepository = postRepository;
        this.voteRepository = voteRepository;
        this.fileStorageService = fileStorageService;
    }

    // Response format used by the website

    private Map<String, Object> postResponse(Post post) {

        Map<String, Object> result = new LinkedHashMap<>();

        result.put("id", post.getId());
        result.put("messName", post.getMessName());
        result.put("foodName", post.getFoodName());
        result.put("comment", post.getComment());
        result.put("rating", post.getRating());
        result.put("imageUrl", post.getImageUrl());
        result.put("createdAt", post.getCreatedAt());

        result.put(
            "upvotes",
            voteRepository.countByPostIdAndValue(
                post.getId(), "UP"
            )
        );

        result.put(
            "downvotes",
            voteRepository.countByPostIdAndValue(
                post.getId(), "DOWN"
            )
        );

        return result;
    }

    // GET ALL POSTS

    @GetMapping
    public List<Map<String, Object>> getPosts() {

        return postRepository.findAll(
            org.springframework.data.domain.Sort
                .by(org.springframework.data.domain.Sort.Direction.DESC,
                    "createdAt")
        ).stream()
         .map(this::postResponse)
         .toList();
    }

    // CREATE A POST

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> createPost(

        @RequestParam String messName,
        @RequestParam String foodName,
        @RequestParam String comment,
        @RequestParam int rating,
        @RequestParam MultipartFile image

    ) {

        try {

            if (messName.isBlank() ||
                foodName.isBlank() ||
                comment.isBlank()) {

                return ResponseEntity.badRequest()
                    .body("All fields are required.");
            }

            if (messName.length() > 100 ||
                foodName.length() > 100 ||
                comment.length() > 1000 ||
                rating < 1 || rating > 5) {

                return ResponseEntity.badRequest()
                    .body("Invalid post details.");
            }

            String imageUrl = fileStorageService.save(image);

            Post post = new Post();

            post.setMessName(messName.trim());
            post.setFoodName(foodName.trim());
            post.setComment(comment.trim());
            post.setRating(rating);
            post.setImageUrl(imageUrl);

            Post saved = postRepository.save(post);

            return ResponseEntity.ok(postResponse(saved));

        } catch (IOException | IllegalArgumentException e) {

            return ResponseEntity.badRequest()
                .body(e.getMessage());

        }
    }

    // LIKE OR DISLIKE A POST

    @PostMapping("/{id}/vote")
    @Transactional
    public ResponseEntity<?> vote(

        @PathVariable Long id,
        @RequestBody Map<String, String> request

    ) {

        Optional<Post> post = postRepository.findById(id);

        if (post.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        String voterId = request.get("voterId");
        String value = request.get("value");

        if (voterId == null ||
            !voterId.matches("[a-zA-Z0-9-]{1,100}") ||
            !Set.of("UP", "DOWN").contains(value)) {

            return ResponseEntity.badRequest()
                .body("Invalid vote.");
        }

        Optional<Vote> existing =
            voteRepository.findByPostIdAndVoterId(id, voterId);

        Vote vote;

        if (existing.isPresent()) {

            vote = existing.get();
            vote.setValue(value);

        } else {

            vote = new Vote(id, voterId, value);
        }

        voteRepository.saveAndFlush(vote);

        Map<String, Object> result = new HashMap<>();
        result.put("value", vote.getValue());
        result.put("message", "Vote recorded");

        return ResponseEntity.ok(result);
    }
}
