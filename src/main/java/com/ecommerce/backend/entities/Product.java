package com.ecommerce.backend.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "products")

public class Product extends BaseEntity {

    @Column(nullable = false)
    private String title;

    @Column(length = 2000)
    private String description;

    @Column(nullable = false)
    private Double price;

    @Column(nullable = false)
    private int stock;

    @ManyToOne
    @JoinColumn(name = "category_id", nullable = false)
    private Category category;




    private String brand;
    private String sku;

    private Double rating;
    private Double discountPercentage;
    private Integer weight;

     @Column(name = "size")
     private String size;
    /* ---------- EMBEDDED ---------- */
    @Embedded
    private Dimensions dimensions;

    @Embedded
    private Meta meta;


    /* ---------- COLLECTIONS ---------- */
    @ElementCollection
    @CollectionTable(name = "product_images", joinColumns = @JoinColumn(name = "product_id"))
    @Column(name = "images", columnDefinition = "TEXT") // Karakter sınırını kaldırmak için TEXT tipi tanımladık
    private List<String> images;

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL)
    private List<Review> reviews;

    /* ---------- ORDERS ---------- */
    @ManyToMany
    @JoinTable(
            name = "order_items",
            joinColumns = @JoinColumn(name = "product_id"),
            inverseJoinColumns = @JoinColumn(name = "order_id")
    )
    private List<Order> orders;

    @Version
    private Integer version = 0;

    /* @ManyToMany(mappedBy = "products")
    private List<Wishlist> wishlist; */
    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL)
    private List<WishlistProduct> wishlistProducts;

}
