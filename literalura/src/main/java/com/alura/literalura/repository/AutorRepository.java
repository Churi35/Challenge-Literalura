package com.alura.literalura.repository;

import com.alura.literalura.entity.Autor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AutorRepository extends JpaRepository<Autor, Long>
{
    @Query("SELECT a FROM Autor a WHERE LOWER(a.nombre) LIKE LOWER(:nombre)")
    Autor buscarAutorPorNombre(String nombre);

    @Query("SELECT a FROM Autor a WHERE (a.fechaDeMuerte > :fecha OR a.fechaDeMuerte IS NULL) AND a.fechaDeNacimiento < :fecha")
    List<Autor> buscarAutoresVivosEnUnAñoElegido(Integer fecha);
}
