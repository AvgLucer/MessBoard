
package com.messboard.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "posts")
public class Post {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100)
    private String messName;

    @Column(nullable = false, length = 100)
    private String foodName;

    @Column(nullable = false, length = 1000)
    private String comment;

    @Column(nullable = false)
    private int rating;

    @Column(nullable = false)
    private String imageUrl;

    @Column(nullable = false)
    private LocalDateTime createdAt;

    public Post() {}

    @PrePersist
    public void beforeSave() {
        createdAt = LocalDateTime.now();
    }

    public Long getId() { return id; }

    public String getMessName() { return messName; }
    public void setMessName(String value) { messName = value; }

    public String getFoodName() { return foodName; }
    public void setFoodName(String value) { foodName = value; }

    public String getComment() { return comment; }
    public void setComment(String value) { comment = value; }

    public int getRating() { return rating; }
    public void setRating(int value) { rating = value; }

    public String getImageUrl() { return imageUrl; }
    public void setImageUrl(String value) { imageUrl = value; }

    public LocalDateTime getCreatedAt() { return createdAt; }
}
