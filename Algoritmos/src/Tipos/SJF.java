package Tipos;
import java.util.ArrayList;

import SuperClases.NoApropiativo;
import SuperClases.Proceso;

public class SJF extends NoApropiativo{

	
	public SJF(ArrayList<Proceso> lista) {
		super(lista);
	}

	public void run() {
		
		Proceso proceso = null;

		for (int i = 0;i<size;i++) {
			checkCola(ciclo);
			proceso = getMin();
			ciclosProceso(proceso);
			listaEnCola.remove(proceso);
		}
		mostrarIndice();
	}
}