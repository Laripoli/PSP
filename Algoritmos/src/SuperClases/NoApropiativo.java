package SuperClases;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class NoApropiativo {

	protected String PID;
	protected int entrada;
	protected int rafaga,rafagaTotal;
	protected DecimalFormat formato = new DecimalFormat("#.###");
	protected double ciclo = 0;
	protected double indice = 0;
	protected int size = 0;
	protected ArrayList<Proceso> listaEnCola = new ArrayList<>();
	
	protected ArrayList<Proceso> lista;
	
	public NoApropiativo(ArrayList<Proceso> lista) {
		
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
	 * Tareas a realizar durante cada ciclo sobre un proceso
	 * @param proceso
	 */
	protected void ciclosProceso(Proceso proceso) {
		for (int j= 0;j<proceso.getRafagaTotal();j++) {
			ciclo++;		
			proceso.restarRafaga();
			System.out.println("Ciclo: " + (int)ciclo+", Proceso: "+proceso.getPID()
			+ ", Rafaga restante: "+(int)proceso.getRafaga());
		}
		proceso.setSalida(ciclo);
		indice += calcularIndice(proceso);
	}
	
	/**
	 * Imprime en pantalla en indice de penalizacion del algoritmo
	 */
	protected void mostrarIndice() {
		System.out.println("Indice de penalización: "+formato.format(indice/size));
	}
	
	/**
	 * Comprueba que es menor, si el quantum o la rafaga restante, para iterar sobre el
	 * @param rafaga
	 * @param quantum
	 * @return
	 */
	protected double checkMenor(double rafaga,int quantum) {
		if(rafaga>quantum) {
			return quantum;
		}
		else {
			return rafaga;
		}
	}
	
	/**
	 * Comprueba si ha de añadir algun proceso a la cola
	 * @param ciclo
	 */
	protected void checkCola(double ciclo) {
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
	 * Manda el proceso al final de la cola
	 * @param proceso
	 */
	protected void empujar(Proceso proceso) {
		listaEnCola.remove(proceso);
		listaEnCola.add(proceso);
		
	}
	
	/**
	 * Devuelve el proceso con menor rafaga total de la cola
	 * @return
	 */
	protected Proceso getMin() {
		 Proceso p =  Collections.min(listaEnCola, Comparator.comparing(s -> s.getRafagaTotal()));
		 return p;
	}
}
