package MegaPck;

import java.util.ArrayList;

public class BaseIndice {
	//// ---- top,centro,base;
	//// ---- 0 1 2
	int[] base;
	byte[] rank;
	byte[] classificacao;
	float[] indice;

	/*
	 * O Rank guarda a informacao de qual dezena ex: o rank[tipo][0] vai informar
	 * qual a dezena est� em primeiro lugar o rank[tipo][59] vai informar qual a
	 * dezena que esta em ultimo lugar
	 * 
	 * a classificao informa em qual posicao esta a dezena ex. classifica��o [0] vai
	 * informar em que lugar est� a dezena 01 classifica��o [59] vai informar qual a
	 * classificacao da dezena 60.
	 * 
	 * 
	 */
	BaseIndice() {
		base = new int[60];
		indice = new float[60];
		rank = new byte[60];
		classificacao = new byte[60];
	}

	/*
	 * public BaseIndice clone() { BaseIndice novo=new BaseIndice();
	 * novo.base=this.base.clone(); novo.rank=this.rank.clone();
	 * novo.classificacao=this.classificacao.clone();
	 * novo.indice=this.indice.clone(); return novo; }
	 */
	public void recalcule() {
		byte indiceMaior = 0;
		byte indiceEliminacao = 0;
		ArrayList<int[]> baseTrabalho = new ArrayList<int[]>(base.length);
		int[] temp = new int[2];
		for (byte i = 0; i < base.length; i++) {
			temp[0] = i;
			temp[1] = base[i];
			baseTrabalho.add(temp.clone());
		}
		for (byte cursorMaiorDezenas = 0; cursorMaiorDezenas < 60; cursorMaiorDezenas++) {
			indiceMaior = (byte) baseTrabalho.get(0)[0];
			indiceEliminacao = 0;
			for (byte cursorMaiores = 1; cursorMaiores < baseTrabalho.size(); cursorMaiores++) {
				if (baseTrabalho.get(indiceEliminacao)[1] < baseTrabalho.get(cursorMaiores)[1]) {
					indiceMaior = (byte) baseTrabalho.get(cursorMaiores)[0];
					indiceEliminacao = cursorMaiores;
				}
			}
			classificacao[indiceMaior] = cursorMaiorDezenas;

			rank[classificacao[indiceMaior]] = indiceMaior;

			baseTrabalho.remove(indiceEliminacao);
		}
		int tmp;
		for (byte i = 0; i < 60; i++) {

			tmp = (maiorValor() - (menorValor() < 0 ? menorValor() : 0));
			if (tmp != 0) {
				indice[i] = (float) (base[i] - menorValor()) / tmp;
			}

		}

	}

	public int menorValor() {
		return base[rank[59]];
	}

	public int maiorValor() {
		return base[rank[0]];
	}

	public void print(String nome) {
		print(nome, "Indice ");
	}

	public void print(String nome, String nomeVariavel) {
		System.out.println("\n" + nomeVariavel + ": Impressao do indice " + nome + " para conferencia.");
		System.out.println("             Top                      Base              Centro       ");
		System.out.println("------------------------------   ----------------   ----------------");
		System.out.println("Rank   Dados Dezena     Indice    Rank     Indice    Rank     Indice");
		System.out.println("==== ======= ====== ==========   ===== ==========   ===== ==========");
		// 01 238 05 0.00000000 60 0.00000000 30 0.00000000
		byte dezena;
		for (byte i = 0; i < 60; i++) {
			dezena = rank[i];
			System.out.println(String.format("%4d %7d %6d %10.8f   %5d %10.8f   %5d %10.8f", (i + 1), base[dezena],
					(dezena + 1), indice[dezena], (i + 1), 1 - indice[dezena],
					Math.abs((61 - (i + 1) * 2)) + (((61 - (i + 1) * 2)) <= 0 ? 1 : 0),
					Math.abs(2 * indice[dezena] - 1)));
		}
	}
}
