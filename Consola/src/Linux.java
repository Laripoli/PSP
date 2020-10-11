import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Scanner;

/**
 * Clase Linux
 * Lleva a cabo todas las acciones de la aplicación adaptadas a sistemas operativos Linux
 * @author Laripoli
 *
 */
public class Linux {
  private static String accion = "";
  private static String comando = "";
  private static String resultado = "";
  private static Boolean conectado = false;
  private static String ruta;
  private static ProcessBuilder pb = new ProcessBuilder();
  
  /**
   * Solicita acción y la lleva a cabo
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
        System.out.println("Estas desconectado!");
        System.out.println();
      }
      break;
      
    case "7":
      System.out.println("Hasta la proxima!");
      System.exit(0);
      break;
      
    default:
      System.err.println("Entrada invalida!");
      System.err.println("Introduce el numero que hay entre los [] del comando");
      System.out.println();
    }
    //Reinicio de solicitud de acción
    solicitar_accion();
  }
  
  /**
	* Método para solicitar al usuario que accion realizar
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
    //No cierren nunca el System.in si no quieren cargarse el flujo estandar (teclado)
    Scanner sc = new Scanner(System.in);    
    //System.out.println("Introduce la ruta a listar: ");    
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
    pb.command("bash", "-c", comando +" "+ ruta);
      try {
        Process p = pb.start();
        System.out.println(resultado);
        System.out.println();
  
      }catch (Exception e) {
      }
    }
  }

  /**
	 * Método que muestra todas las interfaces de red disponibles
	 * @param pb
	 * @throws IOException
	 */
  private static void mostrar_interfaces(ProcessBuilder pb) throws IOException {

	pb.command("bash", "-c","nmcli device status");
    Process p = pb.start();
    BufferedReader r = new BufferedReader(new InputStreamReader(p.getInputStream()));
    String line= r.readLine();
    boolean encontrado = false;
    
    while((line = r.readLine()) != null) {
      String[] trozos = line.split(" ");
      if (!trozos[0].equals("lo")) {
        System.out.println(trozos[0]);
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
	  
    pb.command("bash", "-c","ifconfig "+ interfaz);
    Process p = pb.start();
    boolean encontrado = false;
    BufferedReader r = new BufferedReader(new InputStreamReader(p.getInputStream()));
    String line= "";
    
    while((line = r.readLine()) != null) {
    	//Localizacion y formateo del string
	      if (line.contains("inet")) {
	        line = line.replaceAll("\\s{8}","");
	        String[] trozos = line.split(" ");
	        System.out.println(trozos[1]);
	        System.out.println();
	        encontrado = true;
	        break;
	      }
    }
    
    //Control de errores
    if(!encontrado) {
    	System.err.println("No se ha podido encontrar la ip de: "+interfaz);
    	System.out.println();
    }
  }
  
  /**
	 * Método que devuelve la dirección MAC en función de la ip pasada como parametro
	 * @param pb
	 * @param interfaz
	 * @throws IOException
	 */
  private static void mostrar_mac(ProcessBuilder pb,String interfaz) throws IOException {
    pb.command("bash", "-c","ip link | awk \'{print $2}\'");
    Process p = pb.start();
    BufferedReader r = new BufferedReader(new InputStreamReader(p.getInputStream()));
    String line= "";
    boolean encontrado = false;
    
    while((line = r.readLine()) != null) {
	      if(line.contains(interfaz)) {
	        System.out.println(r.readLine());
	        System.out.println();
	        encontrado = true;
	      }
    }
    //Control de errores
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
    pb.command("bash", "-c","ping -c 5 8.8.8.8");
    Process p = pb.start();
    BufferedReader r = new BufferedReader(new InputStreamReader(p.getInputStream()));
    String line= "";
    
    System.out.println("Comprobando..");
    
    //Si no devuelve mas que 1 linea se da por hecho que no puede hacer ping
    if(r.readLine() == null){
      return false;
    }
    return true;
  }
}