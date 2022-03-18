package votos;

import java.util.Scanner;

/**
 * 
 * @author CFRdoCarmo
 *
 */
public class CalculaVotos {

	public static void main(String[] args) {

		Votos votos = new Votos();
		Scanner scanner = new Scanner(System.in);
		Boolean sair = false;
		
		while (sair != true) {
			
			System.out.println("Digite: ");
			System.out.println("1 - Para exibir o total de votos válidos: ");
			System.out.println("2 - Para exibir o total de votos brancos: ");
			System.out.println("3 - Para exibir o total de votos nulos: ");
			System.out.println("4 - Para sair: ");
			
			int opcao = scanner.nextInt();
			
			if(opcao == 1) {
				System.out.println("O percentual de votos válidos foi de: " + (votos.percentualVotosValidos(votos.getValidos(), votos.getTotalEleitores()) * 100) + "%\n" );
			}
			
			if(opcao == 2) {
				System.out.println("O percentual de votos brancos foi de: " + (votos.percentualVotosBrancos(votos.getVotosBrancos(), votos.getTotalEleitores()) * 100) + "%\n" );
			}
			
			if(opcao == 3) {
				System.out.println("O percentual de votos brancos foi de: " + (votos.percentualVotosNulos(votos.getNulos(), votos.getTotalEleitores()) * 100) + "%\n" );
			}
			
			if(opcao == 4) {
				System.out.println("Encerrano exibição de resultados!");
				break;
			}
		}
		scanner.close();
	}

}
