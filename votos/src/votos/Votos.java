package votos;

/**
 * 
 * @author CFRdoCarmo
 *
 */
public class Votos {

	private double totalEleitores = 1000;
	private double validos = 800;
	private double votosBrancos = 150;
	private double nulos = 50;
	
	public double getTotalEleitores() {
		return totalEleitores;
	}
	public double getValidos() {
		return validos;
	}
	public double getVotosBrancos() {
		return votosBrancos;
	}
	public double getNulos() {
		return nulos;
	}
	
	public double percentualVotosValidos(double validos, double totalEleitors) {
		return (validos / totalEleitors);
	}
	
	public double percentualVotosBrancos(double votosBrancos, double totalEleitors) {
		return (votosBrancos / totalEleitors);
	}
	
	public double percentualVotosNulos(double nulos, double totalEleitors) {
		return (nulos / totalEleitors);
	}
	
}
