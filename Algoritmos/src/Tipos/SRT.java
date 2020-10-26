package Tipos;

import java.util.ArrayList;

import SuperClases.Apropiativo;
import SuperClases.Proceso;

public class SRT extends Apropiativo{

	
	public SRT(ArrayList<Proceso> lista) {
		super(lista);
	}

	public void run() {
		
		Proceso proceso = null;
		checkCola();
		while(listaEnCola.size() != 0) {
			proceso = getMin();
			ciclosProceso(proceso);
		}
		mostrarIndice();
	}
}
