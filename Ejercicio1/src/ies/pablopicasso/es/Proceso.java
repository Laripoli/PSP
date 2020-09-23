package ies.pablopicasso.es;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;

public class Proceso {
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		String ruta;
		ruta = args.length > 0 ? args[0] : "C:\\" ;
		ProcessBuilder pb = new ProcessBuilder();
		pb.command("cmd.exe", "/c", "dir" , ruta);
		pb.directory(new File("C:\\users"));
		try {
			Process p = pb.start();

            BufferedReader reader =
                    new BufferedReader(new InputStreamReader(p.getInputStream()));

            String line;
            while ((line = reader.readLine()) != null) {
                System.out.println(line);
            }
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
	}

}
