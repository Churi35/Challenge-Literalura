package com.alura.literalura.principal;

import com.alura.literalura.entity.Autor;
import com.alura.literalura.model.Datos;
import com.alura.literalura.model.DatosLibros;
import com.alura.literalura.entity.Libro;
import com.alura.literalura.repository.AutorRepository;
import com.alura.literalura.repository.LibrosRepository;
import com.alura.literalura.service.ConsumoAPI;
import com.alura.literalura.service.ConvierteDatos;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
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
    private AutorRepository autorRepository;

    @Autowired
    public Principal(LibrosRepository libroRepository, AutorRepository autorRepository) {
        this.librosRepository = libroRepository;
        this.autorRepository = autorRepository;
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
                    "0- Salir");
            System.out.println("Elija una opción: ");
            opcion = entradaDelTeclado.nextInt();

            switch (opcion)
            {
                case 1:
                    buscarLibroPorTitulo();
                break;
                case 2:
                    obtenerListaDeLibrosRegistrados();
                break;
                case 3:
                    obtenerListaDeAutoresRegistrados();
                break;
                case 4:
                    obtenerListaDeAutoresVivosEnAñoDeterminado();
                break;
                case 5:
                    buscarLibroPorIdioma();
                break;
                case 0:
                break;
                default:
                    System.out.println("Opcion no valida");
                break;
            }
        }

    }

    private void buscarLibroPorIdioma()
    {
        List<String> idiomas = new ArrayList<>() {{add("es");add("en");add("fr");add("pt");}};
        List<Libro> librosBuscados = null;
        String idiomaABuscar = null;
        System.out.println("***** Idioma ******");
        System.out.println("Idiomas disponibles: ");
        System.out.println("es - Español");
        System.out.println("en - Ingles");
        System.out.println("fr - Frances");
        System.out.println("pt - Portugues");
        System.out.println("Seleccione un idioma: ");
        entradaDelTeclado.nextLine();
        idiomaABuscar = entradaDelTeclado.nextLine();
        if(idiomas.contains(idiomaABuscar))
        {
            librosBuscados = librosRepository.buscarLibrosPorIdioma(idiomaABuscar);

            if (librosBuscados.isEmpty())
            {
                System.out.println(
                        "\n**********************************************\n" +
                        "*No existen libros registrados con ese idioma\n*" +
                        "**********************************************\n");
            }
            else
            {
                System.out.println(
                        "\n****************************\n" +
                        "*Lista de libros encontrada*\n" +
                        "****************************\n");
                librosBuscados.forEach(System.out::println);
            }

        }
        else
        {
            System.out.println("Opcion invalida");
        }
    }

    private void obtenerListaDeAutoresVivosEnAñoDeterminado()
    {
        System.out.println("Ingrese el año que desea buscar: ");
        List<Autor> autores = autorRepository.buscarAutoresVivosEnUnAñoElegido(entradaDelTeclado.nextInt());
        if(autores.isEmpty())
        {
            System.out.println("\n************************\n" +
                               "*Autores no encontrados*\n" +
                               "************************\n");
        }
        else
        {
            System.out.println("\n************************\n" +
                               "* Autores  encontrados *\n" +
                               "************************\n");
            autores.forEach(System.out::println);
        }


    }

    private void obtenerListaDeLibrosRegistrados()
    {
        List<Libro> libros = librosRepository.findAll();
        if(libros.isEmpty())
        {
            System.out.println("\n**************************\n" +
                               "*No hay libros registrados*\n" +
                               "***************************\n");
        }
        else
        {
            System.out.println("\n*****************************\n" +
                               "*Lista de libros registrados*\n" +
                               "*****************************\n");
            libros.forEach(System.out::println);
        }

    }

    private void obtenerListaDeAutoresRegistrados()
    {
        List<Autor> autores = autorRepository.findAll();
        if(autores.isEmpty())
        {
            System.out.println("\n***************************\n" +
                    "*No hay autores registrados*\n" +
                    "****************************\n");
        }
        else
        {
            System.out.println("\n******************************\n" +
                    "*Lista de autores registrados*\n" +
                    "******************************\n");
            autores.forEach(System.out::println);
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
        Autor autorBuscado = autorRepository.buscarAutorPorNombre(libro.getAutor().getNombre());
        String textoEnPantallaLibroNoEncontrado = "\n*****************************\n" +
                "*No existe el libro en la BD*\n" +
                "*****************************\n";
        String textoEnPantallaLibroEncontrado =
                "\n**************************\n" +
                        "*El libro existe en la BD*\n" +
                        "**************************\n";

        if(autorBuscado == null)
        {
            System.out.println(
                    "\n*****************************\n" +
                    "*No existe el autor en la BD\n*" +
                    "*****************************\n");
            autorRepository.save(libro.getAutor());
            System.out.println(
                    "\n*****************************\n" +
                    "*Autor  guardado  en  la  BD*\n" +
                    "*****************************\n");
        }
        else
        {
            System.out.println(
                    "\n**************************\n" +
                    "*El autor existe en la BD*\n" +
                    "**************************\n");

            if(libroBuscado == null)
            {
                System.out.println(textoEnPantallaLibroNoEncontrado);
                libro.setAutor(autorBuscado);
                librosRepository.save(libro);
                System.out.println(
                        "\n*****************************\n" +
                                "*Libro  guardado  en  la  BD*\n"+
                                "*****************************\n");
            }
            else
                System.out.println(
                        "\n**************************\n" +
                        "*El libro existe en la BD*\n" +
                        "**************************\n");
            return;
        }


        if(libroBuscado == null)
        {
            System.out.println(textoEnPantallaLibroNoEncontrado);
            librosRepository.save(libro);
            System.out.println(
                    "\n*****************************\n" +
                    "*Libro  guardado  en  la  BD*\n"+
                    "*****************************\n");
        }
        else
            System.out.println(textoEnPantallaLibroEncontrado);
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
            System.out.println(
                    "\n*********************\n" +
                    "*Libro no encontrado*\n" +
                    "*********************\n");
    }
}
