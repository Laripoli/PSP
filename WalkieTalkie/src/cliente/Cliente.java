package cliente;

import java.io.*;
import java.net.*;
import java.util.Scanner;

public class Cliente {

	private static String host;
	private static int puerto;
	private static Socket Cliente = null;
	private static String mensajeRecibido;
	private static String mensajeEnviado;
	private static boolean hablando = true;
	private static boolean escuchando = false;
	private static boolean cierre = false;

	public static void main(String[] args) throws Exception {

		while (Cliente == null) {
			Cliente = Conexion();
		}
		// CREO FLUJO DE SALIDA AL SERVIDOR
		DataOutputStream flujoSalida = new DataOutputStream(Cliente.getOutputStream());

		// CREO FLUJO DE ENTRADA AL SERVIDOR
		DataInputStream flujoEntrada = new DataInputStream(Cliente.getInputStream());

		System.err.println("Conectado a " + host + ":" + puerto + ", puedes empezar a chatear");

		try {
			while (!cierre) {
				mensajeRecibido = "";
				mensajeEnviado = "";

				while (hablando) {
					if (!mensajeEnviado.equals("Cambio")) {
						Scanner sc = new Scanner(System.in);
						mensajeEnviado = sc.nextLine();
						flujoSalida.writeUTF(mensajeEnviado);
					}
					if (mensajeEnviado.contains("Cambio y corto")) {
						cierre = true;
						escuchando = false;
						hablando = false;
						break;
					} else if (mensajeEnviado.equals("Cambio")) {
						hablando = false;
						escuchando = true;
						break;
					}
				}
				while (escuchando) {
					if (mensajeRecibido.contains("Cambio y corto")) {
						cierre = true;
						escuchando = false;
						hablando = false;
						break;
					} else if (!(mensajeRecibido = flujoEntrada.readUTF()).equals("Cambio")) {
						System.out.println("Cliente: " + mensajeRecibido);
					} else if (mensajeRecibido.equals("Cambio")) {
						System.err.println("Cliente: " + mensajeRecibido);
						escuchando = false;
						hablando = true;
					}
				}
			}
			System.err.println("Chat finalizado");

		} catch (Exception e) {
			System.err.println("Ha ocurrido un error");
    		System.err.println("Chat finalizado");
}

		// CERRAR STREAMS Y SOCKETS
		flujoEntrada.close();
		flujoSalida.close();
		Cliente.close();

	}

	/**
	 * Solicita al usuario la direccion y el puerto al que conectarse
	 * 
	 * @return
	 */
	public static Socket Conexion() {

		Scanner sca = new Scanner(System.in);
		System.out.println("Introduce la dirreción a la que conectarte");
		host = sca.nextLine();
		puerto = 0;
		while (puerto == 0) {
			puerto = puerto(sca);
		}
		try {
			Cliente = new Socket(host, puerto);
		} catch (UnknownHostException e) {
			System.err.println("No se puede reconocer la dirección");
			return null;
		} catch (IOException e) {
			System.err.println("No se puede reconocer la dirección");
			return null;
		}
		return Cliente;
	}

	/**
	 * Solicita al usuario introducir el puerto en la consola
	 * 
	 * @param sc
	 * @return
	 */
	public static int puerto(Scanner sc) {

		System.out.println("Introduce el puerto al que conectarte de " + host);
		try {
			puerto = Integer.valueOf(sc.nextLine());
		} catch (NumberFormatException e) {
			System.err.println("Eso no es un numero colegui");
		}
		return puerto;
	}
}