package com.project.khayalipulao.model;
import jakarta.persistence.*;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import java.time.LocalDate;

@Entity
@Data
@Table(name = "menu_items")
public class MenuItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    
    private String itemName;
    private double price;
    private LocalDate dateAdded;

    @ManyToOne
    @JsonIgnore 
    @JoinColumn(name = "provider_id", nullable = false)
    private User provider;

    public MenuItem() {
        this.dateAdded = LocalDate.now(); // Auto-set date
    }

}
