package com.alura.literalura.repository;

import com.alura.literalura.entity.Libro;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LibrosRepository extends JpaRepository<Libro, Long>
{
    @Query("SELECT l FROM Libro l WHERE LOWER(l.idioma) = LOWER(:idioma) ")
    List<Libro> buscarLibrosPorIdioma(String idioma);

    @Query("SELECT l FROM Libro l WHERE LOWER(l.titulo) = LOWER(:titulo) ")
    Libro buscarLibro(String titulo);
}
