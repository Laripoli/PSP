package Tipos;

import java.util.ArrayList;

import SuperClases.NoApropiativo;
import SuperClases.Proceso;

public class RoundRobin extends NoApropiativo {

	private int quantum;

	
	public RoundRobin(ArrayList<Proceso> lista,int quantum) {
		super(lista);
		this.quantum = quantum;
	}
	
	public void run() {
		int quantumRestante = quantum;
		
		Proceso proceso = null;
		checkCola(ciclo);
			while(listaEnCola.size() != 0) {
				checkCola(ciclo);
				proceso = listaEnCola.get(0);

				for (int j= 0;j<checkMenor(proceso.getRafaga(),quantumRestante);j++) {
					ciclo++;	
					quantumRestante--;
					proceso.restarRafaga();
					System.out.println("Ciclo: " + (int)ciclo+", Proceso: "+proceso.getPID()
					+ ", Rafaga restante: "+(int)proceso.getRafaga());
				}
				if(proceso.getRafaga() == 0) {
					listaEnCola.remove(proceso);
					proceso.setSalida(ciclo);
					indice += calcularIndice(proceso);
					quantumRestante = quantum;
				}
				if(quantumRestante==0) {
					empujar(proceso);
					proceso = listaEnCola.get(0);
					quantumRestante = quantum;
				}
			}
			mostrarIndice();
	}
}
