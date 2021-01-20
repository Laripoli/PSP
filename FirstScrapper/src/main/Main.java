package main;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class Main {
	
	private static final String fichero = "ibex35.txt";

	public static void main(String[] args) throws IOException {
		while(true) {
			Document doc = Jsoup.connect("https://www.bolsamadrid.es/esp/aspx/Mercados/Precios.aspx?indice=ESI100000000&punto=indice").get();
	
			Element tabla = doc.select("#ctl00_Contenido_tblÍndice").first();
			
			Element cuerpo = tabla.child(0);
			int contador = 0;
			
			int fecha = 0;
			int nombre = 0;
			int hora = 0;
			int valor = 0;
			
			BufferedWriter bw = new BufferedWriter(new FileWriter(fichero,true));
			String linea = "";
			
			for (Element fila : cuerpo.getElementsByTag("tr")) {
				for (Element columna : fila.getElementsByTag("th")) {
					contador++;
	
						switch(columna.text()) {
							case "Nombre":
								nombre = contador;
								break;
							case "Último":
								valor = contador;
								break;
							case "Hora" : 
								hora = contador;
								break;
							case "Fecha":
								fecha = contador;
								break;
						}
				
				}
				contador = 0;
				for (Element columna : fila.getElementsByTag("td")) {
						contador ++;
						if(contador == nombre)
							linea += columna.text() + ", ";
						if(contador == valor)
							linea += columna.text() + ", ";
						if(contador == fecha)
							linea += columna.text() + ", ";
						if(contador == hora)
							linea += columna.text();
					}
			}
			bw.append(linea + "\n");
			bw.close();
			
			try {
				Thread.sleep(60000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
	}
}
