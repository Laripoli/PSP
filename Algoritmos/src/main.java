import Tipos.FIFO;
import Tipos.RoundRobin;
import Tipos.SJF;
import Tipos.SRT;

import java.util.ArrayList;
import SuperClases.Proceso;
import SuperClases.Apropiativo;
import SuperClases.NoApropiativo;

public class main {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		ArrayList<Proceso> lista = new ArrayList<>();
		
		lista.add(new Proceso("a",0,3));
		lista.add(new Proceso("b",2,6));
		lista.add(new Proceso("c",4,4));
		lista.add(new Proceso("d",6,5));
		lista.add(new Proceso("e",8,2));
		
		FIFO algoritmo = new FIFO(lista);
//		SJF algoritmo = new SJF(lista);
//		RoundRobin algoritmo = new RoundRobin(lista,4);
//		SRT algoritmo = new SRT(lista);
		algoritmo.run();

		
	}

}

