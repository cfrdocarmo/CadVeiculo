package fatorial;

import java.util.Scanner;


public class CalculaFatorial {

	public static void main(String[] args) {

		Scanner scanner = new Scanner(System.in);
		Boolean sair = false;
		
		while(sair == false) {
			System.out.println("Digite: ");
			System.out.println("1 - para calcular o fatorial de um número: ");
			System.out.println("2 - para sair");
			int numero = scanner.nextInt();
			
			if(numero == 1) {
				System.out.println("Digite o número para qual deseja saber o fatorial: ");
				int num = scanner.nextInt();
				
				int fatorial = 1;
				for( int i = 1; i <= num; i++) {
					fatorial *= i;
				}
				System.out.println(fatorial + "\n");
			}
			
			else if (numero == 2) {
				System.out.println("Encerrano exibição de resultados!");
				break;
			}
			
			
		}
		
		scanner.close();
	}

}
