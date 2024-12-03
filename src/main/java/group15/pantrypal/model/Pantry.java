package group15.pantrypal.model;

import jakarta.persistence.*;

@Entity
@Table(name = "pantry_table")
public class Pantry {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "pantry_id") // Maps the field to the pantry_id column in the database
    private Long id;

    @Column(name = "pantry_name")
    private String pantryName;

    // Getters and setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getPantryName() {
        return pantryName;
    }

    public void setPantryName(String pantryName) {
        this.pantryName = pantryName;
    }
}
