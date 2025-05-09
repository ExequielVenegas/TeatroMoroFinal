package com.douc.teatromoro;

import java.util.*;

public class TeatroMoroFinal {
    static Scanner scanner = new Scanner(System.in);

    static int totalIngresos = 0;
    static int totalEntradasVendidas = 0;
    static int entradasDisponibles = 100;

    static Map<Integer, Entrada> entradas = new HashMap<>();
    static int contadorEntradas = 1;

    public static void main(String[] args) {
        int opcion;
        do {
            System.out.println("\n*** Menú Teatro Moro ***");
            System.out.println("1. Reservar entrada");
            System.out.println("2. Comprar entrada");
            System.out.println("3. Modificar una venta");
            System.out.println("4. Imprimir boleto");
            System.out.println("5. Ver estadísticas");
            System.out.println("6. Pruebas y debug");
            System.out.println("0. Salir");
            System.out.print("Seleccione una opción: ");

            opcion = leerEntero();

            switch (opcion) {
                case 1 -> reservarEntrada();
                case 2 -> venderEntrada();
                case 3 -> modificarVenta();
                case 4 -> imprimirBoleta();
                case 5 -> verEstadisticas();
                case 6 -> menuDebug();
                case 0 -> System.out.println("Saliendo del sistema...");
                default -> System.out.println("Opción inválida.");
            }
        } while (opcion != 0);
    }

    static void reservarEntrada() {
        if (entradasDisponibles <= 0) {
            System.out.println("No hay entradas disponibles.");
            return;
        }
        System.out.print("Ingrese ubicación (Platea/Palco/VIP): ");
        String ubicacion = scanner.nextLine();

        Entrada entrada = new Entrada(contadorEntradas++, ubicacion, 0, "Reservada", "general");
        entradas.put(entrada.numero, entrada);
        entradasDisponibles--;
        System.out.println("Entrada reservada con número: " + entrada.numero);
    }

    static void venderEntrada() {
        if (entradasDisponibles <= 0) {
            System.out.println("No hay entradas disponibles.");
            return;
        }
        System.out.print("Ingrese tipo de cliente (general/estudiante/tercera edad): ");
        String tipoCliente = scanner.nextLine();
        System.out.print("Ingrese ubicación (Platea/Palco/VIP): ");
        String ubicacion = scanner.nextLine();

        int precio;
        switch (ubicacion.toLowerCase()) {
            case "vip" -> precio = 15000;
            case "palco" -> precio = 10000;
            default -> precio = 7000;
        }

        int descuento = switch (tipoCliente.toLowerCase()) {
            case "estudiante" -> 2000;
            case "tercera edad" -> 3000;
            default -> 0;
        };

        int precioFinal = precio - descuento;

        Entrada entrada = new Entrada(contadorEntradas++, ubicacion, precioFinal, "Vendida", tipoCliente);
        entradas.put(entrada.numero, entrada);
        entradasDisponibles--;
        totalIngresos += precioFinal;
        totalEntradasVendidas++;

        System.out.println("Entrada vendida con número: " + entrada.numero + ", precio final: $" + precioFinal);
    }

    static void modificarVenta() {
        System.out.print("Ingrese número de entrada a modificar: ");
        int numero = leerEntero();
        Entrada entrada = entradas.get(numero);

        if (entrada == null || !entrada.estado.equals("Vendida")) {
            System.out.println("Entrada no encontrada o no es modificable.");
            return;
        }

        System.out.print("Ingrese nueva ubicación (Platea/Palco/VIP): ");
        String nuevaUbicacion = scanner.nextLine();
        entrada.ubicacion = nuevaUbicacion;
        System.out.println("Ubicación actualizada.");
    }

    static void imprimirBoleta() {
        System.out.print("Ingrese número de entrada: ");
        int numero = leerEntero();
        Entrada entrada = entradas.get(numero);

        if (entrada == null) {
            System.out.println("Entrada no encontrada.");
            return;
        }

        System.out.println("\n--- BOLETA ---");
        System.out.println("Número: " + entrada.numero);
        System.out.println("Ubicación: " + entrada.ubicacion);
        System.out.println("Cliente: " + entrada.tipoCliente);
        System.out.println("Estado: " + entrada.estado);
        System.out.println("Precio: $" + entrada.precioFinal);
    }

    static void verEstadisticas() {
        System.out.println("\n*** Estadísticas ***");
        System.out.println("Total ingresos: $" + totalIngresos);
        System.out.println("Total entradas vendidas: " + totalEntradasVendidas);
        System.out.println("Entradas disponibles: " + entradasDisponibles);
    }

    static void menuDebug() {
        System.out.println("\n*** PRUEBAS Y DEBUG ***");
        System.out.println("1. Listar todas las entradas");
        System.out.println("2. Validar asientos ocupados");
        System.out.println("3. Ver estructura interna");
        System.out.println("4. Buscar por tipo de cliente");
        System.out.print("Seleccione opción de debug: ");
        int opcion = leerEntero();

        switch (opcion) {
            case 1 -> entradas.values().forEach(System.out::println);
            case 2 -> {
                long ocupadas = entradas.values().stream()
                    .filter(e -> e.estado.equals("Vendida") || e.estado.equals("Reservada")).count();
                System.out.println("Asientos ocupados: " + ocupadas);
            }
            case 3 -> System.out.println("[DEBUG] Entradas almacenadas en HashMap con clave numérica.");
            case 4 -> {
                System.out.print("Ingrese tipo de cliente (general/estudiante/tercera edad): ");
                String tipo = scanner.nextLine();
                entradas.values().stream()
                        .filter(e -> e.tipoCliente.equalsIgnoreCase(tipo))
                        .forEach(System.out::println);
            }
            default -> System.out.println("Opción de debug inválida.");
        }
    }

    static int leerEntero() {
        try {
            return Integer.parseInt(scanner.nextLine());
        } catch (NumberFormatException e) {
            System.out.println("Entrada inválida. Ingrese un número.");
            return -1;
        }
    }

    static class Entrada {
        int numero;
        String ubicacion;
        int precioFinal;
        String estado;
        String tipoCliente;

        Entrada(int numero, String ubicacion, int precioFinal, String estado, String tipoCliente) {
            this.numero = numero;
            this.ubicacion = ubicacion;
            this.precioFinal = precioFinal;
            this.estado = estado;
            this.tipoCliente = tipoCliente;
        }

        @Override
        public String toString() {
            return "Entrada{" +
                    "numero=" + numero +
                    ", ubicacion='" + ubicacion + '\'' +
                    ", precioFinal=" + precioFinal +
                    ", estado='" + estado + '\'' +
                    ", tipoCliente='" + tipoCliente + '\'' +
                    '}';
        }
    }
}
