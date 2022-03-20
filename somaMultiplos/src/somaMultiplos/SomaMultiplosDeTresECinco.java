package somaMultiplos;

import java.util.Scanner;

public class SomaMultiplosDeTresECinco {

	public static void main(String[] args) {
		
		int a = 3;
		int b = 5;
		int somaMultiplos = 0;
		
		Scanner scanner = new Scanner(System.in);
		Boolean sair = false;
		
		while(sair == false) {
			System.out.println("Digite: ");
			System.out.println("1 - para digitar um número: ");
			System.out.println("2 - para sair");
			int numero = scanner.nextInt();
			
			if(numero == 1) {
				System.out.println("Digite o número para qual deseja saber o somatório dos multiplos de 3 e 5: ");
				int num = scanner.nextInt();
				
				for(int i = 0; i < num; i++){
				    if(i % a == 0 || i % b == 0){
				    	somaMultiplos += i;
				    }
				}
				System.out.println("A soma dos multiplos de 3 e 5: " + somaMultiplos);
			}
			else if (numero == 2) {
				System.out.println("Encerrano exibição de resultados!");
				break;
			}
		}
		scanner.close();
	}
}
