package MegaPck;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

// a Base guarda a quantidade de vezes que a dezena foi sorteada.   

public class IdxCiclo extends AbstractIndice {
	int numeroCicloAtual;
	int numeroSorteiosNoCiclo;
	int numeroSorteiosSemAcertos;
	int qtdTotalSorteios;
	int[] controlaAcertos = new int[60];
	int[] controlaAcertosMin = new int[60];
	int[] controlaAcertosMax = new int[60];

	IdxCiclo() {
		super();
		numeroCicloAtual = 1;
		numeroSorteiosNoCiclo = 0;
		numeroSorteiosSemAcertos = 0;
		qtdTotalSorteios = 0;
		Arrays.fill(controlaAcertos, 0);
		Arrays.fill(controlaAcertosMin, 9999);
		Arrays.fill(controlaAcertosMax, 0);
	}

	/*
	 * public IdxCiclo clone() { IdxCiclo copia = new IdxCiclo();
	 * copia.numeroCicloAtual=this.numeroCicloAtual;
	 * copia.numeroSorteiosNoCiclo=this.numeroSorteiosNoCiclo;
	 * copia.numeroSorteiosSemAcertos=this.numeroSorteiosSemAcertos;
	 * copia.qtdTotalSorteios=this.qtdTotalSorteios;
	 * copia.controlaAcertos=this.controlaAcertos.clone();
	 * copia.controlaAcertosMin=this.controlaAcertosMin.clone();
	 * copia.controlaAcertosMax=this.controlaAcertosMax.clone();
	 * 
	 * copia = super.clone(this, copia) ; return copia; }
	 */
	@Override
	public void soAdd(Sorteio sorteio) {
		numeroSorteiosNoCiclo++;
		byte dezena;
		boolean houveAcerto = false;
		byte[] dezenasNoSorteio = sorteio.get();
		int qtdAcertosInicial = qtdAcertos();
		for (byte i = 0; i < 6; i++) {
			dezena = dezenasNoSorteio[i];
			if (getValorBaseDaDezena(dezena) == 0) {
				houveAcerto = true;
			}
			incrementaBaseDaDezena(dezena);
			if (verificaFimDeCiclo()) {
				if (i < 5) {
					numeroSorteiosNoCiclo = 1;
					qtdAcertosInicial = 0;
					houveAcerto = false;
				}
			}
		}
		if (houveAcerto) {
			for (int i = qtdAcertosInicial; i <= qtdAcertos(); i++) {
				controlaAcertos[i] = numeroSorteiosNoCiclo;
			}
			numeroSorteiosSemAcertos = 0;
		} else {
			numeroSorteiosSemAcertos++;
		}

	}

	private boolean verificaFimDeCiclo() {
		for (byte i = 1; i < 61; i++) {
			if (getValorBaseDaDezena(i) == 0) {
				return false;
			}
		}
		qtdTotalSorteios += numeroSorteiosNoCiclo;
		// System.out.println("Final do ciclo "+ numeroCicloAtual+" apos
		// "+numeroSorteiosNoCiclo + "sorteios, passando o total de sorteios geral para
		// "+ qtdTotalSorteios);

		numeroCicloAtual++;

		int temp;
		for (byte i = 1; i < 61; i++) {
			setValorBaseDaDezena(i, 0);
			temp = numeroSorteiosNoCiclo - controlaAcertos[i - 1];
			if (temp > controlaAcertosMax[i - 1]) {
				controlaAcertosMax[i - 1] = temp;
			}
			if (temp < controlaAcertosMin[i - 1]) {
				controlaAcertosMin[i - 1] = temp;
			}
			// System.out.println("Atingimos a qtd de "+(i-1)+" acertos no ciclo, no sorteio
			// "+controlaAcertos[i-1]+ " esperando
			// "+(numeroSorteiosNoCiclo-controlaAcertos[i-1])+
			// " para fechar o ciclo, e deixando o min em "+controlaAcertosMin[i-1]+" e o
			// maximo em "+controlaAcertosMax[i-1]);
			controlaAcertos[i - 1] = 0;
		}
		numeroSorteiosNoCiclo = 0;
		return true;
	}

	public int qtdAcertos() {
		int retorno = 60;
		for (byte i = 1; i < 61; i++) {
			if (getValorBaseDaDezena(i) == 0) {
				retorno--;
			}
		}

		return retorno;

	}

	public List<Byte> quaisDezenasFaltam() {
		List<Byte> dezenasFaltantes = new ArrayList<>();
		for (byte i = 1; i < 61; i++) {
			if (getValorBaseDaDezena(i) == 0) {
				dezenasFaltantes.add(i);
			}
		}

		return dezenasFaltantes;
	}

	@Override
	public void print(String nomeClasse) {
		super.print(nomeClasse);
		System.out.println("\nO ciclo " + numeroCicloAtual + " est� ativo a " + (numeroSorteiosNoCiclo)
				+ " sorteios e est� com " + qtdAcertos() + " acertos, portanto "
				+ String.format("%6.2f%%", (float) qtdAcertos() / 60 * 100) + " completo.");
		System.out.print("Faltam sair as dezenas : " + quaisDezenasFaltam().toString());
		System.out.println("\nO ciclo atual n�o � modificado ha :" + numeroSorteiosSemAcertos + " sorteios.");
		if ((numeroCicloAtual - 1) > 0) {
			System.out.println("A m�dia de dura��o dos ciclos � de " +

					String.format("%6.2f", (float) qtdTotalSorteios / (numeroCicloAtual - 1)) + " sorteios.");
			System.out.println(
					"\nA m�dia de dura��o para os ciclos fecharem quando tinham " + qtdAcertos() + " acertos � de "
							+ String.format("%6.2f",
									(float) (controlaAcertosMin[qtdAcertos()] + controlaAcertosMax[qtdAcertos()]) / 2.)
							+ " sorteios.");
		}
		System.out.println("\nHistoricamente variando neste limite de " + controlaAcertosMin[qtdAcertos()] + " at� "
				+ controlaAcertosMax[qtdAcertos()] + " sorteos para fechar o ciclo.");
	}

}
