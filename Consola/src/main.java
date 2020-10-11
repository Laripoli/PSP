import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Scanner;

public class main {
	
	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub
		String os = System.getProperty("os.name");
		if (os.contains("Windows")) {
			Windows.solicitar_accion();
		}
		else {
			Linux.solicitar_accion();
		}
	}
}