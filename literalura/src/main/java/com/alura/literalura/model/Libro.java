package com.alura.literalura.model;

import jakarta.persistence.*;

@Entity
@Table(name = "libros")
public class Libro
{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String titulo;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "autor_id")
    private Autor autor;
    private String idioma;
    private Double numeroDeDescargas;

    public Libro() {}

    public Libro(DatosLibros datosLibro, Autor autor) {
        this.titulo = datosLibro.titulo();
        this.idioma = (String) datosLibro.idiomas().get(0);  // Ajuste aquí
        this.numeroDeDescargas = Double.valueOf(datosLibro.numeroDeDescargas());
        this.autor = autor;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public Autor getAutor() {
        return autor;
    }

    public void setAutor(Autor autor) {
        this.autor = autor;
    }

    public String getIdioma() {
        return idioma;
    }

    public void setIdioma(String idioma) {
        this.idioma = idioma;
    }

    public Double getNumeroDeDescargas() {
        return numeroDeDescargas;
    }

    public void setNumeroDeDescargas(Double numeroDeDescargas) {
        this.numeroDeDescargas = numeroDeDescargas;
    }

    @Override
    public String toString() {

        return "*********** Libro ***********\n" +
                "Titulo: " + titulo + "\n" +
                "Autor: " + autor.getNombre() + "\n" +
                "Idioma: " + idioma + "\n" +
                "Número de descargas: " + numeroDeDescargas  + "\n";
    }
}
