package servidor;

import java.io.*;
import java.net.*;
import java.util.Scanner;

public class Servidor {

	private static int puerto;
	private static String mensajeRecibido;
	private static String mensajeEnviado;
	private static boolean hablando = false;
	private static boolean escuchando = true;
	private static boolean cierre = false;

	public static void main(String[] arg) throws IOException {

		while(puerto == 0) {
			puerto = puerto();
			try {
				ServerSocket test = new ServerSocket(puerto);
				test.close();
			}catch(BindException e) {
				System.err.println("Ya hay una sesion de chat en ese puerto...");
				puerto = 0;
			}
		}
		ServerSocket servidor = new ServerSocket(puerto);
		Socket clienteConectado = null;
		System.out.println("Esperando al cliente.....");
		clienteConectado = servidor.accept();

		// CREO FLUJO DE ENTRADA DEL CLIENTE
		InputStream entrada = null;
		entrada = clienteConectado.getInputStream();
		DataInputStream flujoEntrada = new DataInputStream(entrada);

		// CREO FLUJO DE SALIDA AL CLIENTE
		OutputStream salida = null;
		salida = clienteConectado.getOutputStream();
		DataOutputStream flujoSalida = new DataOutputStream(salida);

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
		entrada.close();
		salida.close();
		flujoSalida.close();
		clienteConectado.close();
		servidor.close();

	}
	
	/**
	 * Solicita al usuario introducir el puerto en la consola
	 * 
	 * @param sc
	 * @return
	 */
	public static int puerto() {
		Scanner sc = new Scanner(System.in);
		System.out.println("Introduce el puerto donde abrir la sesión de chat");
		try {
			puerto = Integer.valueOf(sc.nextLine());
		} catch (NumberFormatException e) {
			System.err.println("Eso no es un numero colegui");
		}
		return puerto;
	}

}