package com.example.cinema.entities;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.*;
import java.util.Collection;

@Entity
@Data @AllArgsConstructor @NoArgsConstructor @ToString
public class Cinema {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private double longitude,latitude,altitude;
    private int nombreSalles;
    @OneToMany(mappedBy = "cinema")
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private Collection<Salle> salles;
    @ManyToOne
    private Ville ville;
}
