
package com.messboard.model;

import jakarta.persistence.*;

@Entity
@Table(
    name = "votes",
    uniqueConstraints = {
        @UniqueConstraint(
            columnNames = {"post_id", "voter_id"}
        )
    }
)
public class Vote {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "post_id", nullable = false)
    private Long postId;

    @Column(name = "voter_id", nullable = false, length = 100)
    private String voterId;

    @Column(nullable = false, length = 4)
    private String value;

    public Vote() {}

    public Vote(Long postId, String voterId, String value) {
        this.postId = postId;
        this.voterId = voterId;
        this.value = value;
    }

    public Long getId() {
        return id;
    }

    public Long getPostId() {
        return postId;
    }

    public void setPostId(Long postId) {
        this.postId = postId;
    }

    public String getVoterId() {
        return voterId;
    }

    public void setVoterId(String voterId) {
        this.voterId = voterId;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
