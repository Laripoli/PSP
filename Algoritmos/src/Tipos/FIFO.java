package Tipos;

import java.util.ArrayList;

import SuperClases.NoApropiativo;
import SuperClases.Proceso;

public class FIFO extends NoApropiativo{

	public FIFO(ArrayList<Proceso> lista) {
		super(lista);
	}

	public void run() {
		Proceso proceso = null;
		for (int i = 0;i<size;i++) {
			proceso = lista.get(i);
			ciclosProceso(proceso);
		}
		mostrarIndice();
	}
}
