package com.solvd.model;

import lombok.Builder;
import lombok.Getter;

@Getter
public class Review {
    // when adding setter remember to
    // check if rating is between 1 and 5 (inclusive)
    private final int rating;
    private final String userNickname;
    private final String summary;
    private final String reviewContent;

    @Builder
    public Review(int rating, String userNickname, String summary, String reviewContent) {
        if (rating < 1 || rating > 5) {
            throw new IllegalArgumentException("Rating (%d) must be between 1 and 5 (inclusive)"
                    .formatted(rating));
        }
        this.rating = rating;
        this.userNickname = userNickname;
        this.summary = summary;
        this.reviewContent = reviewContent;
    }

}
