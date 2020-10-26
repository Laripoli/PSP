package SuperClases;

public class Proceso {
	
	protected String PID;
	protected double entrada;
	protected double rafaga;
	protected double rafagaTotal;
	protected double salida = 0;
	
	public Proceso(String pID, int entrada, int rafaga) {
		
		this.PID = pID;
		this.entrada = entrada;
		this.rafaga = rafaga;
		this.rafagaTotal = rafaga;
	}
	public String getTodo() {
		return PID + " " + entrada + " " + rafaga;
	}
	public String getPID() {
		return PID;
	}
	public void setPID(String pID) {
		PID = pID;
	}
	public double getSalida() {
		return salida;
	}
	public void setSalida(double salida) {
		this.salida = salida;
	}
	public double getEntrada() {
		return entrada;
	}
	public void setEntrada(int entrada) {
		this.entrada = entrada;
	}
	public double getRafaga() {
		return rafaga;
	}
	public double getRafagaTotal() {
		return rafagaTotal;
	}
	public void setRafaga(int rafaga) {
		this.rafaga = rafaga;
	}
	public void restarRafaga() {
		this.rafaga--;
	}
}
