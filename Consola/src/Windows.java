import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Scanner;

/**
 * Clase Windows
 * Lleva a cabo todas las acciones de la aplicación adaptadas a sistemas operativos Windows
 * @author Laripoli
 *
 */
public class Windows {
	private static String accion = "";
	private static String comando = "";
	private static String resultado = "";
	private static Boolean conectado = false;
	private static String ruta;
	private static ProcessBuilder pb = new ProcessBuilder();
	
	/**
	 * Solcita acción al usuario y la lleva a cabo
	 * @throws IOException
	 */
	public static void solicitar_accion() throws IOException {
		
	accion = elegirAccion();
	
		switch(accion) {
		case "1":
			comando = "mkdir";
			ruta = leerRuta("ruta");
			resultado = "Carpeta creada!";
			crear(pb,ruta,comando,resultado);
			break;
			
		case "2":
			comando = "type NUL >";
			ruta = leerRuta("ruta");
			resultado = "Fichero creado!";
			crear(pb,ruta,comando,resultado);
			break;
			
		case "3":
			mostrar_interfaces(pb);
			break;
			
		case "4":
			ruta = leerRuta("interfaz");
			mostrar_ip(pb,ruta);
			break;
			
		case "5":
			ruta = leerRuta("interfaz");
			mostrar_mac(pb,ruta);
			break;
			
		case "6":
			conectado = check_conexion(pb);
			if (conectado) {
				System.out.println("Estas conectado!");
				System.out.println();
			}
			else {
				System.err.println("Estas desconectado!");
				System.out.println();
			}
			break;
			
		case "7":
			System.out.println("Hasta la proxima!");
			System.exit(0);
			break;
			
		default:
			System.err.println("Entrada invalida!");
			System.out.println("Introduce el numero que hay entre los [] del comando");
		}
		//Reinicio de solicitud de accion
		solicitar_accion();
	}
	
	/**
	 * Método para solicitar al usuario que acción realizar
	 * @return 
	 */
	private static String elegirAccion() {	
		Scanner sc = new Scanner(System.in);		
	
		System.out.println("Acciones: [1]Crear carpeta \t [2]Crear fichero \t [3]Mostrar interfaces de red");
		System.out.println("[4]Mostrar ip de interfaz en específico \t"
				+ " [5]Mostrar MAC de interfaz en específico");
		System.out.println( "[6]Comprobar consexión a internet \t [7]Salir): ");

		return (sc.nextLine());
	}
	
	/**
	 * Solicita y guarda ya sea una ruta o una interfaz elegida por el usuario
	 * @param accion		Podrá ser "ruta" o "interfaz"
	 * @return
	 */
	private static String leerRuta(String accion) {	
		Scanner sc = new Scanner(System.in);		
		
		System.out.println("Introduce la "+accion+": ");	
		return sc.nextLine();
	}
	
	/**
	 * Método que crea tanto carpetas como ficheros a eleccion del usuario
	 * @param pb
	 * @param ruta  			ruta donde crear el archivo
	 * @param comando			mkdir o type NUL > según elección de usuario
	 * @param resultado			Mensaje final
	 * @throws IOException
	 */
	private static void crear(ProcessBuilder pb,String ruta, String comando,String resultado) throws IOException {

		//Control de errores
	  if(comando == "type NUL >" && !ruta.contains(".")) {
		  System.err.println("No puedo crear un archivo sin su extension..");
	      System.out.println();
	      solicitar_accion();
	    }else if(!ruta.contains("\\")){
	    	System.err.println("Me parece que eso no es una ruta..");
	      System.out.println();
	      solicitar_accion();
	    }
	  
	  	//Ejecucion del comando y mensaje final
	    else {
			pb.command("cmd.exe", "/c", comando , "C:\\" + ruta);
			try {
				Process p = pb.start();
				System.out.println(resultado);
				System.out.println();
	
			} catch (Exception e) {
				// TODO: handle exception
			}
	    }
	  
	}
	
	/**
	 * Método que muestra todas las interfaces de red disponibles
	 * @param pb
	 * @throws IOException
	 */
	private static void mostrar_interfaces(ProcessBuilder pb) throws IOException {

		pb.command("cmd.exe", "/c","ipconfig");
		Process p = pb.start();
		BufferedReader r = new BufferedReader(new InputStreamReader(p.getInputStream()));
		String line= "";
		boolean encontrado = false;
		
		//Localización del string deseado y formateo del mismo
		while((line = r.readLine()) != null) {
			if (line.contains("Adaptador de Ethernet")) {
				line = line.replaceAll("Adaptador de Ethernet ","");
				line = line.replaceAll(":","");
				System.out.println(line);

				encontrado = true;
			}
		}
		//Detección de errores
		if(!encontrado) {
			System.err.println("No se ha encontrado ninguna interfaz de red");
		}
		System.out.println();
		
	}
	
	/**
	 * Método que muestra la ip de la interfaz dada a través de parametro en forma de string
	 * @param pb
	 * @param interfaz
	 * @throws IOException
	 */
	private static void mostrar_ip(ProcessBuilder pb,String interfaz) throws IOException {
		int contador = 0;
		pb.command("cmd.exe", "/c","netsh interface ipv4 show address \""+interfaz+"\"");
		Process p = pb.start();
		BufferedReader r = new BufferedReader(new InputStreamReader(p.getInputStream()));
		String line= "";
		
		while((line = r.readLine()) != null) {
			//Control de errores
			if (line.contains("no son correctos")) {
				System.err.println("Interfaz incorrecta!");
				System.err.println("Prueba el [3] para ver las interfaces actuales");
				System.out.println();
				break;
			}
	       if (contador > 1) {
	    	   String[] trozos = line.split(" ");
		        if (line.contains("IP")) {
		        	System.out.println(trozos[trozos.length-1]);
		        	System.out.println();
		        }
	       }
        contador++;
		}
	}
	
	/**
	 * Método que devuelve la dirección MAC en función de la ip pasada como parametro
	 * @param pb
	 * @param interfaz
	 * @throws IOException
	 */
	private static void mostrar_mac(ProcessBuilder pb,String interfaz) throws IOException {
		pb.command("cmd.exe", "/c","getmac /V /FO CSV");
		Process p = pb.start();
		BufferedReader r = new BufferedReader(new InputStreamReader(p.getInputStream()));
		String line= r.readLine();
		boolean encontrado = false;
		
		while((line = r.readLine()) != null) {
			
			//Separación del String
    	   String[] trozos = line.split(",");
    	   trozos[0] = trozos[0].replaceAll("\"","");//Al pedirlo en formato csv se han de eliminar las comillas dobles
    	   trozos[0] = trozos[0].toLowerCase();
    	   
    	   //Ambos string pasados a minusculas para evitar errores de usuario
    	   if (trozos[0].equals(interfaz.toLowerCase())){
    		   encontrado = true;
    		   System.out.println(trozos[2].replaceAll("\"",""));
    		   System.out.println();
    		   break;
    	   } 
		}
		
		//Detección de fallo
		if(!encontrado) {
			System.err.println("Interfaz incorrecta!");
			System.err.println("Prueba el [3] para ver las interfaces actuales");
			System.out.println();
		}
		
	}
	
	/**
	 * Método que comprueba la conexión a internet.
	 * @param pb
	 * @return bool
	 * @throws IOException
	 */
	private static boolean check_conexion(ProcessBuilder pb) throws IOException {
		pb.command("cmd.exe", "/c","ping 8.8.8.8");
		Process p = pb.start();
		BufferedReader r = new BufferedReader(new InputStreamReader(p.getInputStream()));
		String line= "";
		
		System.out.println("Comprobando..");
		
		while((line = r.readLine()) != null) {
			if(line.contains("error")) {//Si devolviese algún error el programa nos dirá que estamos desconectados
				return false;
			}    
		}
		return true;
	}
}
