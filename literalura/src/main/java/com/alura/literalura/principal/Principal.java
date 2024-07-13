package com.alura.literalura.principal;

import com.alura.literalura.model.Autor;
import com.alura.literalura.model.Datos;
import com.alura.literalura.model.DatosLibros;
import com.alura.literalura.model.Libro;
import com.alura.literalura.repository.LibrosRepository;
import com.alura.literalura.service.ConsumoAPI;
import com.alura.literalura.service.ConvierteDatos;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.Scanner;

@Service
public class Principal
{
    private final String URL_BASE = "https://gutendex.com/books/";
    private final ConsumoAPI consumoApi = new ConsumoAPI();
    private final ConvierteDatos conversor = new ConvierteDatos();
    private final Scanner entradaDelTeclado = new Scanner(System.in);
    private LibrosRepository librosRepository;

    @Autowired
    public Principal(LibrosRepository libroRepository) {
        this.librosRepository = libroRepository;
    }

    public void mostrarMenu()
    {
        int opcion = -1;

        while (opcion != 0)
        {
            System.out.println("*********** Menu ***********");
            System.out.println( "1- Buscar libro por título\n" +
                    "2- Lista de libros registrados\n" +
                    "3- Lista de autores registrados\n" +
                    "4- Lista de autores vivos en un año determinado\n" +
                    "5- Lista de libros por idioma\n" +
                    "6- Top 10 libros más descargados\n" +
                    "7- Estadísticas\n" +
                    "0- Salir");
            System.out.println("Elija una opción: ");
            opcion = entradaDelTeclado.nextInt();

            switch (opcion)
            {
                case 1:
                    buscarLibroPorTitulo();
                break;
                case 2:
                break;
                case 3:
                break;
                case 4:
                break;
                case 5:
                break;
                case 6:
                break;
                case 7:
                break;
                default:
                    System.out.println("Opcion no valida");
                break;
            }
        }

    }

    private DatosLibros getDatosLibros()
    {
        String tituloDelLibroABuscar = null;
        String json = null;
        Datos datos = null;
        Optional<DatosLibros> libro = null;
        System.out.println("Escribe el titulo del libro");
        entradaDelTeclado.nextLine();
        tituloDelLibroABuscar = entradaDelTeclado.nextLine();
        json = consumoApi.obtenerDatos(URL_BASE + "?search=" + tituloDelLibroABuscar.replace(" ", "+"));
        datos = conversor.obtenerDatos(json, Datos.class);
        if(!datos.resultados().isEmpty())
            return datos.resultados().get(0);

        return null;
    }

    private void comprabarExistenciaEnDB(Libro libro)
    {
        Libro libroBuscado = librosRepository.buscarLibro(libro.getTitulo());
        if(libroBuscado == null)
            System.out.println("No existe en la BD");
        else
            System.out.println("Existe en la BD");
    }

    private void buscarLibroPorTitulo()
    {
        var datosLibro = getDatosLibros();
        if(datosLibro != null)
        {
            Libro libro = new Libro(datosLibro,new Autor(datosLibro.autor().get(0)));
            comprabarExistenciaEnDB(libro);
            System.out.println(libro);
        }
        else
            System.out.println("Libro no encontrado");
    }
}
