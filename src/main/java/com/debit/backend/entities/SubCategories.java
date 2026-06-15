package com.debit.backend.entities;


import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
    @Table(name = "sub_categories")
    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public class SubCategories {

        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Long id;

        private String name;

        @ManyToOne(fetch = FetchType.LAZY)
        @JoinColumn(name = "category_id")
        private Category category;

    @OneToMany(mappedBy = "subCategory")
    private List<Product> products;
    }
