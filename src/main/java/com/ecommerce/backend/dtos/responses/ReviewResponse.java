package com.ecommerce.backend.dtos.responses;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class ReviewResponse {
    private int rating;
    private String comment;
    private String date;
    private String reviewerName;
    private String reviewerEmail;

}