package com.example.cinema.dao;

import com.example.cinema.entities.Categorie;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.web.bind.annotation.CrossOrigin;

@CrossOrigin("*")
@RepositoryRestResource // means that all methods used in this class or added later are accessible by the REST API
public interface CategorieRepository extends JpaRepository<Categorie,Long> {
}
