package SuperClases;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class Apropiativo {
	
	protected String PID;
	protected int entrada,rafaga,rafagaTotal,size;
	protected double ciclo,indice = 0;
	protected DecimalFormat formato = new DecimalFormat("#.###");//Formato de double con 3 cifras decimales
	protected ArrayList<Proceso> lista;
	protected ArrayList<Proceso> listaEnCola = new ArrayList<>();
	
	public Apropiativo(ArrayList<Proceso> lista) {
		
	this.lista=lista;
	this.size = this.lista.size();
	}
	
	/**
	 * Calcula el indice final de cada proceso
	 * @param proceso
	 * @return
	 */
	protected double calcularIndice(Proceso proceso) {
		double resultado;
		resultado = (proceso.getSalida()-proceso.getEntrada())/proceso.getRafagaTotal();
		return resultado;
	}
	
	/**
	 * Imprime en pantalla en indice de penalizacion del algoritmo
	 */
	protected void mostrarIndice() {
		System.out.println("Indice de penalización: "+formato.format(indice/size));
	}
	
	/**
	 * Comprueba que no haya ningun proceso nuevo en la cola con menor rafaga restante que el que se 
	 * esta ejecutando
	 * @param proceso
	 * @return
	 */
	protected boolean checkRafaga(Proceso proceso) {
			
		checkCola();
		
		for (int i = 0;i<listaEnCola.size();i++) {
			if(listaEnCola.get(i).getRafaga()<proceso.getRafaga()) {
				listaEnCola.remove(proceso);
				listaEnCola.add(proceso);
				return true;
			}
		}
		return false;
	}
	
	/**
	 * Comprueba si ha de añadir algun proceso a la cola
	 */
	protected void checkCola() {
		
		Proceso proceso = null;
		
		for (int i = 0;i<lista.size();i++) {
			if(lista.get(i).getEntrada()<=ciclo) {
				proceso = lista.get(i);
				lista.remove(proceso);
				listaEnCola.add(proceso);
			}
		}
	}
	
	/**
	 * Devuelve el proceso con menor rafaga restante
	 * @return
	 */
	protected Proceso getMin() {
		 Proceso p =  Collections.min(listaEnCola, Comparator.comparing(s -> s.getRafaga()));
		 return p;
	}
	
	/**
	 * Tareas a realizar durante cada ciclo sobre un proceso
	 * @param proceso
	 */
	protected void ciclosProceso(Proceso proceso) {
		for (int j= 0;j<proceso.getRafaga();j++) {
			if(!checkRafaga(proceso)) {
				ciclo++;		
				proceso.restarRafaga();
				System.out.println("Ciclo: " + (int)ciclo+", Proceso: "+proceso.getPID()
				+ ", Rafaga restante: "+(int)proceso.getRafaga());
			}
		}
		if(proceso.getRafaga() == 0) {
			listaEnCola.remove(proceso);
			proceso.setSalida(ciclo);
			indice += calcularIndice(proceso);
		}
	}
}
