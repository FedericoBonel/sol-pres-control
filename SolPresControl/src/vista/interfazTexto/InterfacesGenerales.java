package vista.interfazTexto;

import controlador.abstraccionNegocio.*;
import controlador.herramientas.Verificadores;
import vista.TextosConstantes;

import java.util.Hashtable;
import java.util.Scanner;

/**
 * Clase que contiene interfaces de objetivo general
 */
public class InterfacesGenerales {

    /**
     * Interfaz que muestra categoria actual, pide una nueva categoria y la devuelve
     */
    public static int pedirCategoria(){
        Scanner in = new Scanner(System.in);
        // Intenta pedir la categoria
        while (true) {
            try {
                System.out.println(TextosConstantes.MUNICIPIOS_PEDIR_CATEGORIA);
                String input = in.nextLine();
                // Si el input es vacio el usuario desea salir
                if (input.isEmpty()) return -1;
                return Integer.parseInt(input);
            } catch (NumberFormatException e) {
                // Si usuario ingresa algo incorrecto informale y vuelve a pedirle categoria
                System.out.println(TextosConstantes.ERROR_SOLO_ENTEROS);
                System.out.println(TextosConstantes.INGRESE_VACIO_TERMINAR);
            }
        }
    }

    /**
     * Pide una fecha y la devuelve como string en formato "anio-mes-dia"
     */

    public static String pedirFecha(){
        String[] opciones = {TextosConstantes.INGRESE_NUEVA_FECHA_YEAR,
                TextosConstantes.INGRESE_NUEVA_FECHA_MONTH,
                TextosConstantes.INGRESE_NUEVA_FECHA_DAY};
        StringBuilder fechaIngresada = new StringBuilder();
        String[] input = interfazPedirDatosYEsperar(opciones);
        // Concatena y devuelve la fecha
        fechaIngresada.append(input[0].replaceAll("\\s+",""))
                        .append("-").append(input[1].replaceAll("\\s+",""))
                        .append("-").append(input[2].replaceAll("\\s+",""));
        return fechaIngresada.toString();
    }

    /**
     * Interfaz de agregar o borrar
     */
    public static int agregarOBorrar() {
        int input;
        input = interfazImprimirTodoYPedirEntero(new String[]{TextosConstantes.AGREGAR_OPCION,
                TextosConstantes.BORRAR_OPCION,
                TextosConstantes.MENU_TEXT_SALIR});
        return input;
    }

    /**
     * Interfaz que imprime documentos, pide nuevos, y los devuelve como string
     */
    public static String pedirDocumento(Hashtable<String, Boolean> documentos) {
        Scanner in = new Scanner(System.in);
        String input;
        // Imprime el estado de los documentos del evento (Convocatoria o presentacion)
        System.out.println(TextosConstantes.DOCUMENTOS);
        imprimirHashTableObjetoValor(documentos);
        System.out.println(TextosConstantes.PEDIR_DOCUMENTO);
        input = in.nextLine();
        return input;
    }

    /**
     * Interfaz de imprimir cuentadantes de entidad y pedir un nuevo cuentadante
     */
    public static String pedirCuentadante(Entidad entidad) {
        Scanner in = new Scanner(System.in);
        // Si es un municipio cuentadante es 1
        if (entidad instanceof Municipio objeto) {
            if (objeto.hasCuentadante()) {
                Cuentadante cuentadante = objeto.getCuentadante();
                System.out.println(TextosConstantes.CUENTADANTES_ACTUALES + "\n" + cuentadante.getId());
            }
        // Si es un fiscal posee varios cuentadantes
        } else {
            Fiscal objeto = (Fiscal) entidad;
            Hashtable<String, Cuentadante> cuentadantes = objeto.getCuentadantes();
            if (!cuentadantes.isEmpty()) {
                System.out.println(TextosConstantes.CUENTADANTES_ACTUALES);
                for (String cuentadante : cuentadantes.keySet()) System.out.println(cuentadante);
            }
        }
        //TODO QUITAR ESTA PARTE CUANDO TENGAMOS pedirYValidarIdUsuario
        // Move la parte de if municipio a gestor municipios y else a fiscal en sus propios metodos
        return pedirIdentificador(TextosConstantes.INGRESE_IDENTIFICADOR_CUENTADANTE);
    }

    /**
     * Interfaz que pide identificador y lo devuelve
     */

    public static String pedirIdentificador(String textoAImprimir) {
        Scanner in = new Scanner(System.in);
        String input;
        // Toma la entrada y devuelvelo acordemente
        System.out.println(TextosConstantes.INGRESE_VACIO_TERMINAR);
        System.out.println(textoAImprimir);
        input = in.nextLine().replaceAll("\\s+","");
        return input;
    }

    /**
     * Interfaz que pide las opciones en el array y espera por el input en cada una
     * devuelve el input como un array de strings
     */
    public static String[] interfazPedirDatosYEsperar(String[] opciones) {
        Scanner in = new Scanner(System.in);
        String[] input = new String[opciones.length];
        int contador = 0;
        // Itera por las opciones pidiendolas al usuario y asignandolas a sus respectivos valores
        for (String opcion : opciones) {
            System.out.println(opcion);
            input[contador] = in.nextLine();
            contador ++;
        }
        return input;
    }

    /**
     * Interfaz que imprime todas las opciones en el array y espera por un input entero en un rango
     * especifico
     */
    public static int interfazImprimirTodoYPedirEntero(String[] opciones){
        Scanner in = new Scanner(System.in);
        int input;
        while (true) {
            // Itera por las opciones imprimiendolas al usuario
            for (String opcion : opciones) {
                System.out.println(opcion);
            }
            // Pide el input en rango
            input = Verificadores.verificarInputEnteroEnRango(in, 0, opciones.length-1);
            // Si es invalido pidelo de nuevo
            if (input == -1) {
                System.out.println(TextosConstantes.ERROR_SOLO_ENTEROS);
                continue;
            }
            break;
        }
        return input;
    }

    /**
     * Interfaz que verifica alguna operacion con respuesta si o no
     */
    public static boolean interfazSiONo(String textoAImprimir) {
        //Itera por los documentos imprimiendo cada uno de ellos y su estado
        System.out.println(textoAImprimir);
        int input = interfazImprimirTodoYPedirEntero(new String[]{TextosConstantes.SI, TextosConstantes.NO});
        return input == 1;
    }

    /**
     * Interfaz que imprime los registros del hashtable y sus valores con flechas
     */
    public static void imprimirHashTableObjetoValor(Hashtable<String, Boolean> hashtable) {
        //Itera por las llaves imprimiendo cada uno de ellos y su estado
        for (String llave : hashtable.keySet()) {
            System.out.println(llave + " -> " + hashtable.get(llave));
        }
        System.out.println("--------------------------------");
    }

    /**
     * Hashtable<String, Boolean> toma registros de la tabla verdaderos y los transforma a una String "bonita"
     */
    public static String tablaHashVerdaderoAString(Hashtable<String, Boolean> ht) {
        //Inicializa el builder con capacidad inicial de 200 caracteres
        StringBuilder output = new StringBuilder(200);
        for (String llave : ht.keySet()) {
            if (ht.get(llave)) output.append("/").append(llave);
        }
        return output.toString();
    }
}
