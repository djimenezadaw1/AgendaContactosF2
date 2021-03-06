package agenda.io; 

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import java.util.Map;
import java.io.File;
import java.util.Scanner;

import agenda.modelo.*;
 
/**
 * Utilidades para cargar la agenda
 * 
 * @author Alex Calderón, Irune Arratibel, Daniel Jiménez
 * @version 1.0
 *
 
 */

/**
 * A partir de los datos obtenidos por el fichero "agenda.csv",
 * cargamos todos los contactos en el parametro agenda y contabilizamos los errores.
 * @param AgendaContactos agenda (donde meteremos los contactos)
 * 
 */
public class AgendaIO {

	public static int importar(AgendaContactos agenda) {

        int errores = 0;
        Scanner sc = null;
        try {
            sc = new Scanner(new File("agenda.csv"));
            while (sc.hasNextLine()) {
                try {
                    Contacto nuevo = parsearLinea(sc.nextLine());
                    agenda.añadirContacto(nuevo);
                }
                catch(IllegalArgumentException e) {
                    errores ++;
                }

            }
        } catch (IOException e) {
            System.out.println("Error al leer agenda.csv");
            errores ++;
        } finally {
            sc.close();
        }

        return errores;
    }
	

	/**
	 * De una linea crea un objeto dependiendo de que tipo de contacto sea y propagamos los errores.
	 * Los datos vienen separados por comas y tienen espacios al principio y al final.
	 * @param String linea (la linea con los datos)
	 * @return Contacto 
	 */
	private static Contacto parsearLinea(String linea){
		String[] datos = linea.split(",");
		String tipo = datos[0].trim();
		String nombre = datos[1].trim();
		String apellidos = datos[2].trim();
		String tel = datos[3].trim();
		String email = datos[4].trim();
		if(Integer.parseInt(tipo) == 1) {
			String empresa = datos[5].trim();
			Contacto prof = new Profesional(nombre, apellidos, tel, email, empresa);
			return prof;
		}
		if(Integer.parseInt(tipo) == 2) {
			String fecha = datos[5].trim();
			String relacion = datos[6].trim();
			Relacion rel = null;
			if(relacion.equalsIgnoreCase("PADRE")) {
				rel = Relacion.PADRE;
			}
			if(relacion.equalsIgnoreCase("MADRE")) {
				rel = Relacion.MADRE;
			}
			if(relacion.equalsIgnoreCase("AMIGOS")) {
				rel = Relacion.AMIGOS;
			}
			if(relacion.equalsIgnoreCase("PAREJA")) {
				rel = Relacion.PAREJA;
			}
			if(relacion.equalsIgnoreCase("HIJO")) {
				rel = Relacion.HIJO;
			}
			if(relacion.equalsIgnoreCase("HIJA")) {
				rel = Relacion.HIJA;
			}
			
			if(rel == null) {
				throw new IllegalArgumentException();
			}
			
			Contacto pers = new Personal(nombre, apellidos, tel, email, fecha, rel);
			return pers;
			
		}
		
		return null;
	}
	/**
	 * Escribe los contactos personales, agrupados por relacion, en un fichero llamado "personales-relacion".
	 * @param agenda
	 * @param ruta
	 */
	public static void exportarPersonales(AgendaContactos agenda,String ruta) {

		
		PrintWriter fsalida = null;
		try {
			fsalida = new PrintWriter(new BufferedWriter(new FileWriter(ruta)));
			fsalida.println(escribirBonito(agenda, ruta));
			System.out.println("Exportados personales agrupados por relación");
			
		} catch (IOException e) {
			
			System.out.println("Error al crear " + ruta);
		} finally {
			fsalida.close();
		}
		
	}
	/**
	 * Metodo para formatear el texto que introduciremos en el fichero "personales por relacion".
	 * @param agenda
	 * @param ruta
	 * @return String
	 */
	private static String escribirBonito(AgendaContactos agenda,String ruta) {
		Map<Relacion, List<String>> map = agenda.personalesPorRelacion();
		String resul ="";
		boolean metido = true;
		
		for(Relacion clave: map.keySet()) {
			resul += clave + "\n\t [" ;
			metido = false;
			for(String contenido: map.get(clave)) {
				if(!metido) {
					resul += contenido;
					metido = true;
				}
				else
					resul += ", " + contenido;

			}
			resul += "]\n";
		}
		
		return resul;
	}
	

}
