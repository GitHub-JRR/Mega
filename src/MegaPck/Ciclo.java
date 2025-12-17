package MegaPck;

public class Ciclo {
	static int sequencia = 0;
	int numero;
	int sorteioInicial;
	int sorteioFinal;
	int cicloRelativo;

	public Ciclo() {
		numero = 0;
		sorteioInicial = 0;
		sorteioFinal = 0;
	}

	public void zeraCiclo() {
		sequencia = 0;
	}

	public Ciclo(int inicio, int fim) {
		numero = ++sequencia;
		sorteioInicial = inicio;
		sorteioFinal = fim;
	}

	public boolean fechado() {

		return (sorteioFinal != 0);
	}

	public int duracao() {

		return (fechado() ? sorteioFinal - sorteioInicial + 1 : 0);
	}

}
