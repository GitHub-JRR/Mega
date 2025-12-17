package MegaPck;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;
import java.util.Set;
import java.util.concurrent.Callable;

public class SoProcessa implements Callable<Void> {

	public enum Tipo {
        ComDarhal(0), SemDarhal(1);
        
        private final int indice;
        
        Tipo(int indice) {
            this.indice = indice;
        }
        
        public int indice() {
            return indice;
        }
    }
	static List<Calibragem> rankCalibragemFinal;
	static Colecao colecao;
	static byte[] sorteioAtual;
	static short numeroSorteioAtual;
	static Set<Long> indexCalibragem;
	static byte[][] indice;
	// static byte[][] multiplicadorQuadra;
	// static short[][] multiplicadorQuina;
	// static short[][] multiplicadorSena;

	static final long[] baseHash = new long[61];
	static {
		for (byte contador = 1; contador < 61; contador++) {
			baseHash[contador] = 1L << (contador - 1);
		}
	}

	// ver 78 // static long[] baseHash;
	// static long prefixSorteio;
	// static long sufixSorteio;
	static long hashSorteio; // ver 78
	/*
	 * private static void ajustaNovo(final byte[] limiteDeBusca, final byte nivel,
	 * final float[] listaIndice2, final byte[] listaMaioresIndices2, final Colecao
	 * colecao){//, final byte[][] indice) { if (nivel == 13) { return; } for (byte
	 * i2 = limiteDeBusca[0]; i2 < (limiteDeBusca[1] + 1); i2++) {
	 * listaIndice2[listaMaioresIndices2[i2 - 1]] += indiceAlocacao((byte)
	 * (listaMaioresIndices2[i2 - 1] + 1), nivel, colecao, indice); }
	 * maiores(listaIndice2, limiteDeBusca, listaMaioresIndices2); final
	 * ArrayList<byte[]> limites = encontraLimites(limiteDeBusca, listaIndice2,
	 * listaMaioresIndices2); for (final byte[] limite : limites) {
	 * ajustaNovo(limite, (byte) (nivel + 1), listaIndice2, listaMaioresIndices2,
	 * colecao);//, indice); } }
	 */
	/*
	 * public static byte[][] defineDezenas(final Formula formula, final Colecao
	 * colecao) {//, //final byte[][] indice) { // 0 entra o resultado com aplicac�o
	 * da s�rie de Darhal // 1 tem o resultado genuino byte[] lstIndexMaiores; final
	 * float[] listaIndice = new float[60]; final float total =
	 * (formula.maiores.getPeso() + formula.ultSorteio.getPeso() + //
	 * pesoPontuacaoSemSorteio + // formula.ultPQtdSB1.getPeso()+
	 * //pesoPontuacaoMediaSorteio1 + formula.parImp.getPeso() + //
	 * pesoPontuacaoParImpar + formula.ciclo.getPeso() + //
	 * pesoPontuacaoMediaSorteioUltimoCiclo + formula.ultPQtd.getPeso() + //
	 * pesoPontuacaoMediaSorteio + formula.ultPQtdSB1.getPeso() + //
	 * pesoPontuacaoMediaSorteio + formula.ultSorteioSB1.getPeso() + //
	 * pesoPontuacaoSemSorteio1 + formula.ultSorteioSB2.getPeso() + //
	 * pesoPontuacaoSemSorteio2 + formula.monteCarlo.getPeso() + //
	 * pesoPontuacaomonteCarlo formula.drop.getPeso() // pesoPontuacaomonteCarlo );
	 * // if (total==0) { // return null; // } float totalGeral = 0; for (byte
	 * cursorDezena = 1; cursorDezena < 61; cursorDezena++) {
	 * listaIndice[cursorDezena - 1] = ((colecao.idxMaiores.getIndice(cursorDezena,
	 * formula.maiores.getTipo()) formula.maiores.getPeso() +
	 * colecao.idxUltSorteio.getIndice(cursorDezena, formula.ultSorteio.getTipo())
	 * formula.ultSorteio.getPeso() + colecao.idxUltPQtd.getIndice(cursorDezena,
	 * formula.ultPQtdSB1.getTipo(), 1) formula.ultPQtdSB1.getPeso() +
	 * colecao.idxParImp.getIndice(cursorDezena, formula.parImp.getTipo()) *
	 * formula.parImp.getPeso() + colecao.idxCiclo.getIndice(cursorDezena,
	 * formula.ciclo.getTipo()) * formula.ciclo.getPeso() +
	 * colecao.idxUltPQtd.getIndice(cursorDezena, formula.ultPQtd.getTipo())
	 * formula.ultPQtd.getPeso() + colecao.idxMonteCarlo.getIndice(cursorDezena,
	 * formula.monteCarlo.getTipo()) formula.monteCarlo.getPeso() +
	 * colecao.idxDrop.getIndice(cursorDezena, formula.drop.getTipo()) *
	 * formula.drop.getPeso() + colecao.idxUltSorteio.getIndice(cursorDezena,
	 * formula.ultSorteioSB1.getTipo(), 1) formula.ultSorteioSB1.getPeso() +
	 * colecao.idxUltSorteio.getIndice(cursorDezena,
	 * formula.ultSorteioSB2.getTipo(), 2) formula.ultSorteioSB2.getPeso()) /
	 * total); totalGeral += listaIndice[cursorDezena - 1]; } if (totalGeral == 0) {
	 * return null; }
	 * 
	 * lstIndexMaiores = maioresSoProc(listaIndice, (byte) 30);
	 * eliminaDuplicidadesNovo(listaIndice, lstIndexMaiores, colecao);//, indice);
	 * //final byte[][] apostaDezena = new byte[2][qtdDezenas]; final byte[][]
	 * apostaDezena = new byte[2][10]; final boolean[] testaDuplicidade = new
	 * boolean[20]; Arrays.fill(testaDuplicidade, false); apostaDezena[0][0] =
	 * (byte) (lstIndexMaiores[0] + 1); testaDuplicidade[(apostaDezena[0][0] - 1)/3]
	 * = true; byte qtdAlocada = 1; byte cursorDezena = 1; byte tmpDezena; //while
	 * (qtdAlocada < qtdDezenas) { while (qtdAlocada < 10) { tmpDezena =
	 * lstIndexMaiores[cursorDezena++]; if (!testaDuplicidade[tmpDezena/3]) {
	 * apostaDezena[0][qtdAlocada++] = (byte) (tmpDezena + 1);
	 * testaDuplicidade[tmpDezena/3] = true; } } //for (cursorDezena = 0;
	 * cursorDezena < qtdDezenas; cursorDezena++) { for (cursorDezena = 0;
	 * cursorDezena < 10; cursorDezena++) { apostaDezena[1][cursorDezena] = (byte)
	 * (lstIndexMaiores[cursorDezena] + 1); } return apostaDezena; }
	 */
	/*
	 * // byte[0][] � o resultado com a aplica��o do filtro de darhal // byte[1][] �
	 * o resultado puro, sem a aplica��o do filtro de darhal public static byte[][]
	 * defineDezenasCompletox(final Formula formula, final Colecao colecao, final
	 * byte qtdDezenas, final byte[][] indice) { byte[] lstIndexMaiores; final
	 * float[] listaIndice = new float[60]; final float total =
	 * (formula.maiores.getPeso() + formula.ultSorteio.getPeso() + //
	 * pesoPontuacaoSemSorteio + formula.ultPQtdSB1.getPeso() + //
	 * pesoPontuacaoMediaSorteio1 + formula.parImp.getPeso() + //
	 * pesoPontuacaoParImpar + formula.ciclo.getPeso() + //
	 * pesoPontuacaoMediaSorteioUltimoCiclo + formula.ultPQtd.getPeso() + //
	 * pesoPontuacaoMediaSorteio + formula.ultSorteioSB1.getPeso() + //
	 * pesoPontuacaoSemSorteio1 + formula.ultSorteioSB2.getPeso() + //
	 * pesoPontuacaoSemSorteio2 + formula.monteCarlo.getPeso() //
	 * pesoPontuacaomonteCarlo ); if (total == 0) { return null; } float totalGeral
	 * = 0; for (byte cursorDezena = 1; cursorDezena < 61; cursorDezena++) {
	 * listaIndice[cursorDezena - 1] = ((colecao.idxMaiores.getIndice(cursorDezena,
	 * formula.maiores.getTipo()) formula.maiores.getPeso() +
	 * colecao.idxUltSorteio.getIndice(cursorDezena, formula.ultSorteio.getTipo())
	 * formula.ultSorteio.getPeso() + colecao.idxUltPQtd.getIndice(cursorDezena,
	 * formula.ultPQtdSB1.getTipo(), 1) formula.ultPQtdSB1.getPeso() +
	 * colecao.idxParImp.getIndice(cursorDezena, formula.parImp.getTipo()) *
	 * formula.parImp.getPeso() + colecao.idxCiclo.getIndice(cursorDezena,
	 * formula.ciclo.getTipo()) * formula.ciclo.getPeso() +
	 * colecao.idxUltPQtd.getIndice(cursorDezena, formula.ultPQtd.getTipo())
	 * formula.ultPQtd.getPeso() + colecao.idxMonteCarlo.getIndice(cursorDezena,
	 * formula.monteCarlo.getTipo()) formula.monteCarlo.getPeso() +
	 * colecao.idxUltSorteio.getIndice(cursorDezena,
	 * formula.ultSorteioSB1.getTipo(), 1) formula.ultSorteioSB1.getPeso() +
	 * colecao.idxUltSorteio.getIndice(cursorDezena,
	 * formula.ultSorteioSB2.getTipo(), 2) formula.ultSorteioSB2.getPeso()) /
	 * total); totalGeral += listaIndice[cursorDezena - 1]; } if (totalGeral == 0) {
	 * return null; } lstIndexMaiores = maioresSoProc(listaIndice, (byte) 30);
	 * eliminaDuplicidadesNovo(listaIndice, lstIndexMaiores, colecao, indice); final
	 * byte[][] apostaDezena = new byte[2][qtdDezenas]; final boolean[]
	 * testaDuplicidade = new boolean[20]; Arrays.fill(testaDuplicidade, false);
	 * apostaDezena[0][0] = (byte) (lstIndexMaiores[0] + 1);
	 * testaDuplicidade[colecao.darhal.grupoDarhalDaDezena[apostaDezena[0][0] - 1]]
	 * = true; byte qtdAlocada = 1; byte cursorDezena = 1; byte tmpDezena; while
	 * (qtdAlocada < qtdDezenas) { tmpDezena = lstIndexMaiores[cursorDezena++]; if
	 * (!testaDuplicidade[colecao.darhal.grupoDarhalDaDezena[tmpDezena]]) {
	 * apostaDezena[0][qtdAlocada++] = (byte) (tmpDezena + 1);
	 * testaDuplicidade[colecao.darhal.grupoDarhalDaDezena[tmpDezena]] = true; } }
	 * for (cursorDezena = 0; cursorDezena < qtdDezenas; cursorDezena++) {
	 * apostaDezena[1][cursorDezena] = (byte) (lstIndexMaiores[cursorDezena] + 1); }
	 * return apostaDezena; }
	 */
	/*
	 * public static void eliminaDuplicidadesNovo(final float[] listaIndice2, final
	 * byte[] listaMaioresIndices2, final Colecao colecao) {//, final byte[][]
	 * indice) { ArrayList<byte[]> limites = encontraLimites(listaIndice2,
	 * listaMaioresIndices2); for (final byte[] limite : limites) {
	 * ajustaNovo(limite, (byte) 1, listaIndice2, listaMaioresIndices2, colecao);//,
	 * indice); } limites = encontraLimites(listaIndice2, listaMaioresIndices2); for
	 * (final byte[] limite : limites) { ajustaNovo(limite, (byte) 1, listaIndice2,
	 * listaMaioresIndices2, colecao);//, indice); } }
	 */
	/*
	 * private static ArrayList<byte[]> encontraLimites(final byte[] limiteDeBusca,
	 * final float[] listaIndice2, final byte[] listaMaioresIndices2) { final
	 * ArrayList<byte[]> retorno = new ArrayList<byte[]>(); float indiceAnterior =
	 * -1; final byte[] limite = new byte[2]; for (byte i2 = limiteDeBusca[0]; i2 <
	 * (limiteDeBusca[1] + 1); i2++) { if (listaIndice2[listaMaioresIndices2[i2 -
	 * 1]] == indiceAnterior) { while (listaIndice2[listaMaioresIndices2[i2 - 1]] ==
	 * indiceAnterior) { i2++; if (i2 == (limiteDeBusca[1] + 1)) { break; } }
	 * limite[1] = (byte) (i2 - 1); retorno.add(limite.clone()); } if (i2 <
	 * (limiteDeBusca[1] + 1)) { indiceAnterior =
	 * listaIndice2[listaMaioresIndices2[i2 - 1]]; limite[0] = i2; } } return
	 * retorno; }
	 */
	/*
	 * private static ArrayList<byte[]> encontraLimites(final byte[] limiteDeBusca,
	 * final float[] listaIndice2, final byte[] listaMaioresIndices2) { final
	 * ArrayList<byte[]> retorno = new ArrayList<byte[]>(); //if (limiteDeBusca[1] <
	 * limiteDeBusca[0]) return retorno; // Caso inv�lido
	 * 
	 * float indiceAnterior = -1; byte inicioIntervalo = limiteDeBusca[0]; final int
	 * limiteSuperior = limiteDeBusca[1] + 1;
	 * 
	 * for (byte i2 = limiteDeBusca[0]; i2 < limiteSuperior; i2++) { final float
	 * indiceAtual = listaIndice2[listaMaioresIndices2[i2 - 1]];// & 0xFF];
	 * 
	 * if (indiceAtual == indiceAnterior) { // Avan�a enquanto os �ndices forem
	 * iguais do { i2++; if (i2 >= limiteSuperior) break; } while
	 * (listaIndice2[listaMaioresIndices2[i2 - 1] & 0xFF] == indiceAnterior);
	 * 
	 * retorno.add(new byte[]{inicioIntervalo, (byte)(i2 - 1)}); }
	 * 
	 * if (i2 < limiteSuperior) { indiceAnterior = indiceAtual; inicioIntervalo =
	 * i2; } } return retorno; }
	 */
	/*
	 * private static ArrayList<byte[]> encontraLimites(final float[] listaIndice2,
	 * final byte[] listaMaioresIndices2) { final byte[] limite = { 1, (byte)
	 * listaMaioresIndices2.length }; return encontraLimites(limite, listaIndice2,
	 * listaMaioresIndices2); }
	 */

	public static long geraHash(final byte[] serie) {
		return (baseHash[serie[0]] | baseHash[serie[1]] | baseHash[serie[2]] | baseHash[serie[3]] | baseHash[serie[4]]
				| baseHash[serie[5]] | baseHash[serie[6]] | baseHash[serie[7]] | baseHash[serie[8]]
				| baseHash[serie[9]]);
	}

	/*
	 * private static float indiceAlocacao(final byte cursorDezena, final byte
	 * nivel, final Colecao colecao, final byte[][] indice) { final byte
	 * pesoPontuacaoColuna = 1; final byte pesoPontuacaoLinha = 1; final byte
	 * pesoPontuacaoQuadrante = 1; return ((colecao.idxCol.getIndice(cursorDezena,
	 * pesoPontuacaoColuna) * indice[nivel - 1][2] +
	 * colecao.idxLin.getIndice(cursorDezena, pesoPontuacaoLinha) * indice[nivel -
	 * 1][1] + colecao.idxQuad.getIndice(cursorDezena, pesoPontuacaoQuadrante) *
	 * indice[nivel - 1][0]) / (indice[nivel - 1][2] + indice[nivel - 1][1] +
	 * indice[nivel - 1][0])) / 1000; }
	 */
	private static float indiceAlocacaoNovo(final int cursorDezena, final int nivel, final Colecao colecao,
			final byte[][] indice) {
		// final byte pesoPontuacaoColuna = 1;
		// final byte pesoPontuacaoLinha = 1;
		// final byte pesoPontuacaoQuadrante = 1;
		return ((colecao.idxCol.getIndice((byte) cursorDezena, (byte) 1) * indice[nivel - 1][2]
				+ colecao.idxLin.getIndice((byte) cursorDezena, (byte) 1) * indice[nivel - 1][1]
				+ colecao.idxQuad.getIndice((byte) cursorDezena, (byte) 1) * indice[nivel - 1][0])
				/ (indice[nivel - 1][2] + indice[nivel - 1][1] + indice[nivel - 1][0])) / 1000;
	}

	/*
	 * public static void maiores(final float[] original, final byte[] limites,
	 * final byte[] indice) { final float[] myOriginal = original.clone(); final
	 * byte[] numeros = new byte[limites[1] - limites[0] + 1]; float maior = -1;
	 * byte indiceMaior; byte indiceDeAlocacao = 0; byte i, j; for (i = (byte)
	 * (limites[0] - 1); i < limites[1]; i++) { maior = -1; indiceMaior = (byte)
	 * (limites[0] - 1); for (j = (byte) (limites[0] - 1); j < limites[1]; j++) { if
	 * (myOriginal[indice[j]] > maior) { maior = myOriginal[indice[j]]; indiceMaior
	 * = j; } ; } numeros[indiceDeAlocacao++] = indice[indiceMaior];
	 * myOriginal[indice[indiceMaior]] = -1; } indiceDeAlocacao = 0; for (i = (byte)
	 * (limites[0] - 1); i < limites[1]; i++) { indice[i] =
	 * numeros[indiceDeAlocacao++]; } return; }
	 */

	public static void maiores(final float[] original, final byte[] limites, final byte[] indice) {
		final int start = limites[0] - 1;
		final int end = limites[1];
		final int size = end - start;

		// Create a local copy of the relevant portion of the original array
		final float[] values = new float[size];
		for (int i = 0; i < size; i++) {
			values[i] = original[indice[start + i]];
		}

		// Selection sort to find the largest elements in descending order
		for (int i = 0; i < size; i++) {
			int maxIndex = i;
			float maxValue = values[i];

			for (int j = i + 1; j < size; j++) {
				if (values[j] > maxValue) {
					maxValue = values[j];
					maxIndex = j;
				}
			}

			// Swap elements
			if (maxIndex != i) {
				values[maxIndex] = values[i];
				values[i] = maxValue;

				byte temp = indice[start + maxIndex];
				indice[start + maxIndex] = indice[start + i];
				indice[start + i] = temp;
			}
		}
	}

	/*
	 * public static byte[] maioresSoProc(final float[] original, final byte qtd) {
	 * final float[] myOriginal = original.clone(); final byte[] numeros = new
	 * byte[qtd]; float maior; byte indiceMaior = 0, i, j; for (i = 0; i < qtd; i++)
	 * { maior = -1; for (j = 0; j < 60; j++) { if (myOriginal[j] > maior) { maior =
	 * myOriginal[j]; indiceMaior = j; } } numeros[i] = indiceMaior;
	 * myOriginal[indiceMaior] = -1; } return numeros; }
	 */
	public static byte[] maioresSoProc(final float[] original, final byte qtd) {
		// No need to clone the entire array - we'll work with indices
		final byte[] numeros = new byte[qtd];
		// final boolean[] used = new boolean[original.length];
		final boolean[] used = new boolean[60];

		for (byte i = 0; i < qtd; i++) {
			float maior = -1;// Float.MAX_VALUE; // More reliable than -1 for general cases
			byte indiceMaior = -1;

			// for (byte j = 0; j < original.length; j++) {
			for (byte j = 0; j < 60; j++) {
				if (!used[j] && original[j] > maior) {
					maior = original[j];
					indiceMaior = j;
				}
			}

			// if (indiceMaior == -1) break; // No more elements to process
			numeros[i] = indiceMaior;
			used[indiceMaior] = true;
		}

		return numeros;
	}

	public static byte[][] maioresSoProcUltra(final float[] original) {
		// No need to clone the entire array - we'll work with indices
		final byte[][] numeros = new byte[2][10];
		// final boolean[] used = new boolean[original.length];
		final boolean[] used = new boolean[60];
		int inicio = 0;
		int totDarhal = 0;
		final boolean[] grupoocupado = new boolean[20];
		while (totDarhal < 10) {
			// for (byte i = 0; i < 60; i++) {
			float maior = -1;// Float.MAX_VALUE; // More reliable than -1 for general cases
			byte indiceMaior = -1;

			// for (byte j = 0; j < original.length; j++) {
			for (byte j = 0; j < 60; j++) {
				if (!used[j] && original[j] > maior) {
					maior = original[j];
					indiceMaior = j;
				}
			}

			// if (indiceMaior == -1) break; // No more elements to process
			if (inicio < 10) {
				//numeros[1][inicio] = (byte) (indiceMaior + 1);
				numeros[Tipo.SemDarhal.indice()][inicio] = (byte) (indiceMaior + 1);
			}
			used[indiceMaior] = true;
			if (!grupoocupado[indiceMaior / 3]) {
				//numeros[0][totDarhal++] = (byte) (indiceMaior + 1);
				numeros[Tipo.ComDarhal.indice()][totDarhal++] = (byte) (indiceMaior + 1);
				grupoocupado[indiceMaior / 3] = true;
			}
			inicio++;
		}

		return numeros;
	}

	int indiceRank;

	@SuppressWarnings("static-access")
	public SoProcessa(final Colecao colecao, final List<Calibragem> rankCalibragemFinal, final byte[] sorteioAtual,
			final short numeroSorteioAtual, final Set<Long> indexCalibragem, final byte[][] indice) {
		super();
		this.colecao = colecao;
		this.rankCalibragemFinal = rankCalibragemFinal;
		this.indexCalibragem = indexCalibragem;
		this.indice = indice;
		this.sorteioAtual = sorteioAtual.clone();
		Arrays.sort(this.sorteioAtual);
		this.numeroSorteioAtual = numeroSorteioAtual;
		// this.multiplicadorQuadra = multiplicadorQuadra;
		// this.multiplicadorQuina = multiplicadorQuina;
		// this.multiplicadorSena = multiplicadorSena;

		/*
		 * Ver 78 baseHash = new long[61]; for (byte contador = 1; contador < 61;
		 * contador++) { baseHash[contador] = (1L << (contador - 1)); }
		 */
		/*
		 * prefixSorteio = baseHash[sorteioAtual[0]] + baseHash[sorteioAtual[1]] +
		 * baseHash[sorteioAtual[2]]; sufixSorteio = baseHash[sorteioAtual[3]] +
		 * baseHash[sorteioAtual[4]] + baseHash[sorteioAtual[5]];
		 */
		hashSorteio = baseHash[sorteioAtual[0]] | baseHash[sorteioAtual[1]] | baseHash[sorteioAtual[2]]
				| baseHash[sorteioAtual[3]] | baseHash[sorteioAtual[4]] | baseHash[sorteioAtual[5]];
	}

	public SoProcessa(final int indiceRank) {
		super();
		this.indiceRank = indiceRank;
	}

	/*
	 * Ver 78 private short[] ajustaResultado(final short[] resultado, final byte
	 * qtdNumJogado) { // * // * //Apostas 6 n�meros 5 n�meros 4 n�meros Quantidade
	 * de n� jogados Sena Quina // * Quadra Quina Quadra Quadra 6 1 0 0 1 0 1 7 1 6
	 * 0 2 5 3 8 1 12 15 3 15 6 9 1 // * 18 45 4 30 10 10 1 24 90 5 50 15 11 1 30
	 * 150 6 75 21 12 1 36 225 7 105 28 13 // * 1 42 315 8 140 36 14 1 48 420 9 180
	 * 45 15 1 54 540 10 225 55 // * resultado[2] *=
	 * multiplicadorQuadra[qtdNumJogado - 6][1]; resultado[2] += resultado[1] *
	 * multiplicadorQuina[qtdNumJogado - 6][2]; resultado[2] += resultado[0] *
	 * multiplicadorSena[qtdNumJogado - 6][2]; resultado[1] *=
	 * multiplicadorQuina[qtdNumJogado - 6][1]; resultado[1] += resultado[0] *
	 * multiplicadorSena[qtdNumJogado - 6][1]; return resultado; }
	 */
	/*
	 * public static List<Calibragem> cloneList(List<Calibragem> list) {
	 * List<Calibragem> clone = new ArrayList<>(list.size()); for (Calibragem item :
	 * list) clone.add(item); return clone; }
	 */
	@Override
	public Void call() {
		Formula formulaAtual = new Formula(rankCalibragemFinal.get(this.indiceRank).baseFormula);
		final byte[][] apostaDezena = defineDezenasUltra(formulaAtual, colecao);// , indice);
		formulaAtual = null;
		/// Ver 78
		/// if (apostaDezena == null) {
		/// return null;
		/// }

		// byte qtdDezenasNoTeste = 10;
		// short[] resultado;
		// short[] resultadoTotal;
		long tempHash;
		// short qtdAcertos;
		// byte n;
		for (byte filtroLocal = 0; filtroLocal < 2; filtroLocal++) {

			tempHash = geraHash(apostaDezena[filtroLocal]);

			/*
			 * retirado controle de aposta sem sorteio // O indexCalibragem serve para
			 * verificar se a aposta j� foi testeada e a resposta foi zero acertos. if
			 * (indexCalibragem.contains(tempHash)) { continue; }
			 * 
			 * //// ----- Apura Resultados resultado = this.testaSorteio(sorteioAtual,
			 * apostaDezena[filtroLocal], qtdDezenasNoTeste, tempHash);
			 * 
			 * 
			 * //// ----- Testo resultado - se inv�lido, vou ao proximo /// Verifica a
			 * quantidade de acertos if (resultado[6] == 0) { indexCalibragem.add(tempHash);
			 * continue; }
			 */
			/*
			 * qtdAcertos = 0; resultadoTotal = resultado.clone(); do { qtdDezenasNoTeste--;
			 * tempHash -= baseHash[apostaDezena[filtroLocal][qtdDezenasNoTeste]]; if
			 * (qtdDezenasNoTeste < 6) { break; } qtdAcertos++; resultado =
			 * this.testaSorteio(sorteioAtual, apostaDezena[filtroLocal], qtdDezenasNoTeste,
			 * tempHash); for (n = 0; n < 6; n++) { resultadoTotal[n] += resultado[n]; } }
			 * while ((resultado[0] + resultado[1] + resultado[2]) != 0);
			 * qtdDezenasNoTeste++;
			 */
			long intersecao = hashSorteio & tempHash;
			if (intersecao == 0) {
				continue;
			}
			final int numeroDeAcertos = Long.bitCount(intersecao);

			//if (filtroLocal == 1) {
			if (filtroLocal == SoProcessa.Tipo.SemDarhal.indice()) {
				
				if ((rankCalibragemFinal.get(this.indiceRank).baseFormula &~(1L << 50)) != (rankCalibragemFinal.get(this.indiceRank+1).baseFormula)) {
					System.out.println("\n*********************************************************");
					System.out.println("***********************  URGENTE ************************");
					System.out.println("*********************************************************");
					System.out.println("No registro numero "+this.indiceRank +" do Rank calibragem, a formula base "+
					        rankCalibragemFinal.get(this.indiceRank).baseFormula+
							"com filtro, e que sem o filtro é "+(rankCalibragemFinal.get(this.indiceRank).baseFormula &~(1L << 50))+
							" esta diferente da formula base seguinte sem o filtro de Darhal: "+
							(rankCalibragemFinal.get(this.indiceRank+1).baseFormula ));
					
					System.out.println("No registro numero "+this.indiceRank +" do Rank calibragem, a formula:");
					Formula formPrt=new Formula(rankCalibragemFinal.get(this.indiceRank).baseFormula);
					System.out.println("" + String.format("%1$14s", formPrt.base) + " => " + formPrt.toString());
					
					System.out.println("No registro numero "+(this.indiceRank+1) +" do Rank calibragem, a formula:");
					formPrt=new Formula(rankCalibragemFinal.get(this.indiceRank+1).baseFormula);
					System.out.println("" + String.format("%1$14s", formPrt.base) + " => " + formPrt.toString());
					
					System.out.println("Teclar ENTER para encerrar");
					Scanner scanner = new Scanner(System.in);
					scanner.nextLine(); // Aguarda o Enter
					scanner.close();
					throw new IllegalStateException("Programa interrompido devido a: " + "Diferença de formula na sequencia do Rank Calibragem");
				}
				this.indiceRank++; // Sem filtro de Darhal
			}

			/*
			 * if (numeroDeAcertos == 1 ) {
			 * rankCalibragemFinal.get(this.indiceRank).solos++; } else if (numeroDeAcertos
			 * == 2 ) { rankCalibragemFinal.get(this.indiceRank).duplas++; } else if
			 * (numeroDeAcertos == 3 ) { rankCalibragemFinal.get(this.indiceRank).ternos++ ;
			 * } else if (numeroDeAcertos == 4 ) {
			 * rankCalibragemFinal.get(this.indiceRank).quadras++; } else if
			 * (numeroDeAcertos == 5 ) { rankCalibragemFinal.get(this.indiceRank).quinas++;
			 * //} else if (numeroDeAcertos == 6 ) { } else {
			 * rankCalibragemFinal.get(this.indiceRank).senas++; }
			 */
			switch (numeroDeAcertos) {
			case 1:
				rankCalibragemFinal.get(this.indiceRank).solos++;
				break;
			case 2:
				rankCalibragemFinal.get(this.indiceRank).duplas++;
				break;
			case 3:
				rankCalibragemFinal.get(this.indiceRank).ternos++;
				break;
			case 4:
				rankCalibragemFinal.get(this.indiceRank).quadras++;
				break;
			case 5:
				rankCalibragemFinal.get(this.indiceRank).quinas++;
				break;
			default:
				rankCalibragemFinal.get(this.indiceRank).senas++; // 6 ou mais (se aplic�vel)
			}
			/*
			 * if (rankCalibragemFinal.get(this.indiceRank).qtdDezenas > qtdDezenasNoTeste)
			 * { rankCalibragemFinal.get(this.indiceRank).qtdDezenas = qtdDezenasNoTeste; }
			 */
			if (rankCalibragemFinal.get(this.indiceRank).ultimoSorteioComAcerto < numeroSorteioAtual) {
				rankCalibragemFinal.get(this.indiceRank).ultimoSorteioComAcerto = numeroSorteioAtual;
			}
			/*
			 * rankCalibragemFinal.get(this.indiceRank).numSorteiosComAcerto += qtdAcertos;
			 */
		}
		return null;
	}

	// public short[] testaSorteio(final byte[] sorteio, final byte[] apostaDezena,
	// final byte qtdDezenasParaTeste,
	// final long hashApostaInt) {
	// public short[] testaSorteio(final byte[] sorteio, final byte[] apostaDezena,
	// final long hashApostaInt) {
	// final short[] retorno = new short[7];
	// final int numeroDeAcertos = Long.bitCount(hashSorteio & hashApostaInt);
	// if (numeroDeAcertos == 0) {
	// return retorno;
	// }
	/*
	 * if ((prefixSorteio & hashAposta) == 0) { return retorno; } if ((sufixSorteio
	 * & hashAposta) == 0) { return retorno; } byte n = 0; for (byte cursorSorteio =
	 * 0; cursorSorteio < 6; cursorSorteio++) { if
	 * (((baseHash[sorteio[cursorSorteio]]) & hashAposta) != 0) { n++; } } if (n >
	 * 3) { retorno[0] = (short) (n == 6 ? 1 : 0); retorno[1] = (short) (n == 5 ? 1
	 * : 0); retorno[2] = (short) (n == 4 ? 1 : 0); retorno[3] = retorno[0];
	 * retorno[4] = retorno[1]; retorno[5] = retorno[2]; return
	 * this.ajustaResultado(retorno, qtdDezenasParaTeste); } else { return retorno;
	 * }
	 */

	// retorno[0] = (short) (numeroDeAcertos == 6 ? 1 : 0);
	// retorno[1] = (short) (numeroDeAcertos == 5 ? 1 : 0);
	// retorno[2] = (short) (numeroDeAcertos == 4 ? 1 : 0);
	// retorno[3] = (short) (numeroDeAcertos == 3 ? 1 : 0);
	// retorno[4] = (short) (numeroDeAcertos == 2 ? 1 : 0);
	// retorno[5] = (short) (numeroDeAcertos == 1 ? 1 : 0);
	// retorno[6] = (short) numeroDeAcertos ;
	// return retorno;
	// }
	/*
	 * import java.util.Arrays; import java.util.Collections; import
	 * java.util.ArrayList; import java.util.List;
	 * 
	 * public class FloatArrayProcessor {
	 */
	public static byte[] processArray(float[] array, int j, final Colecao colecao, final byte[][] indice) {
		// Valida��o dos par�metros de entrada
		// if (array == null || array.length != 10 || j <= 0 || j > 10) {
		// throw new IllegalArgumentException("Par�metros inv�lidos. Array deve ter 10
		// elementos e quantidade entre 1 e 10.");
		// }

		// Cria lista de elementos com valores e posi��es originais
		List<Element> elements = new ArrayList<>();
		for (int i = 0; i < array.length; i++) {
			elements.add(new Element(array[i], i));
		}

		int indiceProcessamento = 1;

		while (indiceProcessamento <= 12) {
			// Ordena em ordem descendente
			Collections.sort(elements, Collections.reverseOrder());

			// Lista para armazenar �ndices das duplicatas
			List<Integer> duplicateIndices = new ArrayList<>();
			boolean hasDuplicates = false;

			// Verifica duplicatas nos primeiros 'quantidade' elementos
			for (int i = 0; i < j - 1; i++) {
				if (elements.get(i).value == elements.get(i + 1).value) {
					hasDuplicates = true;
					duplicateIndices.add(i); // Armazena posi��o da duplicata
				}
			}

			// Se n�o h� duplicatas, retorna as posi��es originais
			if (!hasDuplicates) {
				return getOriginalPositions(elements, (byte) j);
			}

			// Aplica incrementos apenas nas duplicatas detectadas
			for (int i : duplicateIndices) {
				Element first = elements.get(i);
				Element second = elements.get(i + 1);

				// Aplica incrementos diferentes para cada elemento
				first.value += indiceAlocacaoNovo(first.originalIndex + 1, indiceProcessamento, colecao, indice);
				second.value += indiceAlocacaoNovo(second.originalIndex + 1, indiceProcessamento, colecao, indice);
			}

			indiceProcessamento++;
		}

		// Caso atinja o �ndice m�ximo (11), retorna as posi��es originais
		return getOriginalPositions(elements, (byte) j);
	}

	public static byte[][] processArrayNovo(float[] array) {// , int tamanho, final Colecao colecao,
		// final byte[][] indice) {
		// Valida��o dos par�metros de entrada
		// if (array == null || array.length != 10 || j <= 0 || j > 10) {
		// throw new IllegalArgumentException("Par�metros inv�lidos. Array deve ter 10
		// elementos e quantidade entre 1 e 10.");
		// }
		byte[][] retorno = new byte[2][10];

		int inicio = 0;
		int totDarhal = 0;
		boolean[] grupoocupado = new boolean[20];
		// Cria lista de elementos com valores e posi��es originais
		List<Element> elements = new ArrayList<>();
		for (int i = 0; i < array.length; i++) {
			elements.add(new Element(array[i], i));
		}

		// int indiceProcessamento = 1;

		// while (indiceProcessamento <= 12) {
		// Ordena em ordem descendente
		Collections.sort(elements, Collections.reverseOrder());

		// Lista para armazenar �ndices das duplicatas
		// List<Integer> duplicateIndices = new ArrayList<>();
		// boolean hasDuplicates = false;

		// Verifica duplicatas nos primeiros 'quantidade' elementos
		// for (int i = inicio; i < tamanho - 1; i++) {
		while (totDarhal < 10) {
			/*
			 * if (elements.get(inicio).value == elements.get(inicio + 1).value) {
			 * //hasDuplicates = true; //duplicateIndices.add(inicio); // Armazena posi��o
			 * da duplicata elements.get(inicio).value +=
			 * indiceAlocacaoNovo(elements.get(inicio).originalIndex+1, indiceProcessamento,
			 * colecao, indice); elements.get(inicio+1).value +=
			 * indiceAlocacaoNovo(elements.get(inicio+1).originalIndex+1,
			 * indiceProcessamento, colecao, indice); indiceProcessamento++; } else {
			 */
			if (inicio < 10) {
				retorno[1][inicio] = (byte) (elements.get(inicio).originalIndex + 1);
			}
			// if
			// (!grupoocupado[colecao.darhal.grupoDarhalDaDezena[elements.get(inicio).originalIndex]])
			// {
			// retorno[0][totDarhal++]=(byte) elements.get(inicio).originalIndex;
			// grupoocupado[colecao.darhal.grupoDarhalDaDezena[elements.get(inicio).originalIndex]]=true;
			// }
			if (!grupoocupado[elements.get(inicio).originalIndex / 3]) {
				retorno[0][totDarhal++] = (byte) (elements.get(inicio).originalIndex + 1);
				grupoocupado[elements.get(inicio).originalIndex / 3] = true;
			}
			inicio++;
			// }
		}
		return retorno;
		// }
		/*
		 * // Se n�o h� duplicatas, retorna as posi��es originais if (!hasDuplicates) {
		 * return getOriginalPositions(elements, (byte) tamanho); }
		 * 
		 * // Aplica incrementos apenas nas duplicatas detectadas for (int i :
		 * duplicateIndices) { Element first = elements.get(i); Element second =
		 * elements.get(i + 1);
		 * 
		 * // Aplica incrementos diferentes para cada elemento first.value +=
		 * indiceAlocacaoNovo(first.originalIndex+1, indiceProcessamento, colecao,
		 * indice); second.value += indiceAlocacaoNovo(second.originalIndex+1,
		 * indiceProcessamento,colecao, indice); }
		 * 
		 * indiceProcessamento++; }
		 * 
		 * // Caso atinja o �ndice m�ximo (11), retorna as posi��es originais return
		 * getOriginalPositions(elements, (byte) tamanho);
		 */
	}

	//// private static float incrementa(int posicaoOriginal, int
	//// indiceProcessamento) {
	// // F�rmula do incremento: (posi��o + 1) * (�ndice + 1) * 0.001
	// return (posicaoOriginal + 1) * (indiceProcessamento + 1) * 0.001f;
	// }

	private static byte[] getOriginalPositions(List<Element> elements, byte quantidade) {
		byte[] result = new byte[quantidade];
		for (int i = 0; i < quantidade; i++) {
			result[i] = (byte) elements.get(i).originalIndex;
		}
		return result;
	}

	// Classe auxiliar para armazenar valor e posi��o original
	static class Element implements Comparable<Element> {
		float value;
		int originalIndex;

		Element(float value, int originalIndex) {
			this.value = value;
			this.originalIndex = originalIndex;
		}

		@Override
		public int compareTo(Element other) {
			// Ordena��o natural (ascendente) - usada com reverseOrder para obter
			// descendente
			return Float.compare(this.value, other.value);
		}
	}

	/*
	 * public static void main(String[] args) { // Exemplo de uso float[] array =
	 * {5.0f, 3.0f, 5.0f, 7.0f, 2.0f, 9.0f, 1.0f, 4.0f, 6.0f, 8.0f}; byte quantidade
	 * = 5;
	 * 
	 * byte[] posicoesOriginais = processArray(array, quantidade);
	 * 
	 * System.out.println("Array original: " + Arrays.toString(array));
	 * System.out.println("Posi��es originais dos " + quantidade +
	 * " primeiros ap�s processamento: " + Arrays.toString(posicoesOriginais)); } }
	 */
	/*
	 * public static byte[][] defineDezenasNovo(final Formula formula, final Colecao
	 * colecao, final byte[][] indice) { // 0 entra o resultado com aplicac�o da
	 * s�rie de Darhal // 1 tem o resultado genuino byte[] lstIndexMaiores; final
	 * float[] listaIndice = new float[60]; final float total =
	 * (formula.maiores.getPeso() + formula.ultSorteio.getPeso() + //
	 * pesoPontuacaoSemSorteio + // formula.ultPQtdSB1.getPeso()+
	 * //pesoPontuacaoMediaSorteio1 + formula.parImp.getPeso() + //
	 * pesoPontuacaoParImpar + formula.ciclo.getPeso() + //
	 * pesoPontuacaoMediaSorteioUltimoCiclo + formula.ultPQtd.getPeso() + //
	 * pesoPontuacaoMediaSorteio + formula.ultPQtdSB1.getPeso() + //
	 * pesoPontuacaoMediaSorteio + formula.ultSorteioSB1.getPeso() + //
	 * pesoPontuacaoSemSorteio1 + formula.ultSorteioSB2.getPeso() + //
	 * pesoPontuacaoSemSorteio2 + formula.monteCarlo.getPeso() + //
	 * pesoPontuacaomonteCarlo formula.drop.getPeso() // pesoPontuacaomonteCarlo );
	 * // if (total==0) { // return null; // } float totalGeral = 0; for (byte
	 * cursorDezena = 1; cursorDezena < 61; cursorDezena++) {
	 * listaIndice[cursorDezena - 1] = ((colecao.idxMaiores.getIndice(cursorDezena,
	 * formula.maiores.getTipo()) formula.maiores.getPeso() +
	 * colecao.idxUltSorteio.getIndice(cursorDezena, formula.ultSorteio.getTipo())
	 * formula.ultSorteio.getPeso() + colecao.idxUltPQtd.getIndice(cursorDezena,
	 * formula.ultPQtdSB1.getTipo(), 1) formula.ultPQtdSB1.getPeso() +
	 * colecao.idxParImp.getIndice(cursorDezena, formula.parImp.getTipo()) *
	 * formula.parImp.getPeso() + colecao.idxCiclo.getIndice(cursorDezena,
	 * formula.ciclo.getTipo()) * formula.ciclo.getPeso() +
	 * colecao.idxUltPQtd.getIndice(cursorDezena, formula.ultPQtd.getTipo())
	 * formula.ultPQtd.getPeso() + colecao.idxMonteCarlo.getIndice(cursorDezena,
	 * formula.monteCarlo.getTipo()) formula.monteCarlo.getPeso() +
	 * colecao.idxDrop.getIndice(cursorDezena, formula.drop.getTipo()) *
	 * formula.drop.getPeso() + colecao.idxUltSorteio.getIndice(cursorDezena,
	 * formula.ultSorteioSB1.getTipo(), 1) formula.ultSorteioSB1.getPeso() +
	 * colecao.idxUltSorteio.getIndice(cursorDezena,
	 * formula.ultSorteioSB2.getTipo(), 2) formula.ultSorteioSB2.getPeso()) /
	 * total); totalGeral += listaIndice[cursorDezena - 1]; } if (totalGeral == 0) {
	 * return null; }
	 * 
	 * lstIndexMaiores = processArray(listaIndice, 28, colecao, indice);
	 * //eliminaDuplicidadesNovo(listaIndice, lstIndexMaiores, colecao, indice);
	 * //final byte[][] apostaDezena = new byte[2][qtdDezenas]; final byte[][]
	 * apostaDezena = new byte[2][10]; final boolean[] testaDuplicidade = new
	 * boolean[20]; Arrays.fill(testaDuplicidade, false); apostaDezena[0][0] =
	 * (byte) (lstIndexMaiores[0] + 1); testaDuplicidade[(apostaDezena[0][0] - 1)/3]
	 * = true; byte qtdAlocada = 1; byte cursorDezena = 1; byte tmpDezena; //while
	 * (qtdAlocada < qtdDezenas) { while (qtdAlocada < 10) { tmpDezena =
	 * lstIndexMaiores[cursorDezena++]; if (!testaDuplicidade[tmpDezena/3]) {
	 * apostaDezena[0][qtdAlocada++] = (byte) (tmpDezena + 1);
	 * testaDuplicidade[tmpDezena/3] = true; } } //for (cursorDezena = 0;
	 * cursorDezena < qtdDezenas; cursorDezena++) { for (cursorDezena = 0;
	 * cursorDezena < 10; cursorDezena++) { apostaDezena[1][cursorDezena] = (byte)
	 * (lstIndexMaiores[cursorDezena] + 1); } return apostaDezena; }
	 */
	/*
	 * public static byte[][] defineDezenasNovissimo (final Formula formula, final
	 * Colecao colecao, final byte[][] indice) { // 0 entra o resultado com
	 * aplicac�o da s�rie de Darhal // 1 tem o resultado genuino //byte[]
	 * lstIndexMaiores; final float[] listaIndice = new float[60]; final float total
	 * = (formula.maiores.getPeso() + formula.ultSorteio.getPeso() + //
	 * pesoPontuacaoSemSorteio + // formula.ultPQtdSB1.getPeso()+
	 * //pesoPontuacaoMediaSorteio1 + formula.parImp.getPeso() + //
	 * pesoPontuacaoParImpar + formula.ciclo.getPeso() + //
	 * pesoPontuacaoMediaSorteioUltimoCiclo + formula.ultPQtd.getPeso() + //
	 * pesoPontuacaoMediaSorteio + formula.ultPQtdSB1.getPeso() + //
	 * pesoPontuacaoMediaSorteio + formula.ultSorteioSB1.getPeso() + //
	 * pesoPontuacaoSemSorteio1 + formula.ultSorteioSB2.getPeso() + //
	 * pesoPontuacaoSemSorteio2 + formula.monteCarlo.getPeso() + //
	 * pesoPontuacaomonteCarlo formula.drop.getPeso() // pesoPontuacaomonteCarlo );
	 * // if (total==0) { // return null; // } float totalGeral = 0; for (byte
	 * cursorDezena = 1; cursorDezena < 61; cursorDezena++) {
	 * listaIndice[cursorDezena - 1] = ((colecao.idxMaiores.getIndice(cursorDezena,
	 * formula.maiores.getTipo()) formula.maiores.getPeso() +
	 * colecao.idxUltSorteio.getIndice(cursorDezena, formula.ultSorteio.getTipo())
	 * formula.ultSorteio.getPeso() + colecao.idxUltPQtd.getIndice(cursorDezena,
	 * formula.ultPQtdSB1.getTipo(), 1) formula.ultPQtdSB1.getPeso() +
	 * colecao.idxParImp.getIndice(cursorDezena, formula.parImp.getTipo()) *
	 * formula.parImp.getPeso() + colecao.idxCiclo.getIndice(cursorDezena,
	 * formula.ciclo.getTipo()) * formula.ciclo.getPeso() +
	 * colecao.idxUltPQtd.getIndice(cursorDezena, formula.ultPQtd.getTipo())
	 * formula.ultPQtd.getPeso() + colecao.idxMonteCarlo.getIndice(cursorDezena,
	 * formula.monteCarlo.getTipo()) formula.monteCarlo.getPeso() +
	 * colecao.idxDrop.getIndice(cursorDezena, formula.drop.getTipo()) *
	 * formula.drop.getPeso() + colecao.idxUltSorteio.getIndice(cursorDezena,
	 * formula.ultSorteioSB1.getTipo(), 1) formula.ultSorteioSB1.getPeso() +
	 * colecao.idxUltSorteio.getIndice(cursorDezena,
	 * formula.ultSorteioSB2.getTipo(), 2) formula.ultSorteioSB2.getPeso()) /
	 * total); totalGeral += listaIndice[cursorDezena - 1]; } if (totalGeral == 0) {
	 * return null; }
	 * 
	 * //lstIndexMaiores = processArrayNovo(listaIndice, 28, colecao, indice);
	 * return processArrayNovo(listaIndice);//, 28, colecao, indice);
	 */
	/*
	 * //eliminaDuplicidadesNovo(listaIndice, lstIndexMaiores, colecao, indice);
	 * //final byte[][] apostaDezena = new byte[2][qtdDezenas]; final byte[][]
	 * apostaDezena = new byte[2][10]; final boolean[] testaDuplicidade = new
	 * boolean[20]; Arrays.fill(testaDuplicidade, false); apostaDezena[0][0] =
	 * (byte) (lstIndexMaiores[0] + 1);
	 * testaDuplicidade[colecao.darhal.grupoDarhalDaDezena[apostaDezena[0][0] - 1]]
	 * = true; byte qtdAlocada = 1; byte cursorDezena = 1; byte tmpDezena; //while
	 * (qtdAlocada < qtdDezenas) { while (qtdAlocada < 10) { tmpDezena =
	 * lstIndexMaiores[cursorDezena++]; if
	 * (!testaDuplicidade[colecao.darhal.grupoDarhalDaDezena[tmpDezena]]) {
	 * apostaDezena[0][qtdAlocada++] = (byte) (tmpDezena + 1);
	 * testaDuplicidade[colecao.darhal.grupoDarhalDaDezena[tmpDezena]] = true; } }
	 * //for (cursorDezena = 0; cursorDezena < qtdDezenas; cursorDezena++) { for
	 * (cursorDezena = 0; cursorDezena < 10; cursorDezena++) {
	 * apostaDezena[1][cursorDezena] = (byte) (lstIndexMaiores[cursorDezena] + 1); }
	 * return apostaDezena;
	 */
	/*
	 * }
	 */
	public static byte[][] defineDezenasUltra(final Formula formula, final Colecao colecao) {// ,
		// final byte[][] indice) {
		// 0 entra o resultado com aplicac�o da s�rie de Darhal
		// 1 tem o resultado genuino
		// byte[] lstIndexMaiores;
		final float[] listaIndice = new float[60];
		final float total = (formula.maiores.getPeso() + formula.ultSorteio.getPeso() + // pesoPontuacaoSemSorteio +
		// formula.ultPQtdSB1.getPeso()+ //pesoPontuacaoMediaSorteio1 +
				formula.parImp.getPeso() + // pesoPontuacaoParImpar +
				formula.ciclo.getPeso() + // pesoPontuacaoMediaSorteioUltimoCiclo +
				formula.ultPQtd.getPeso() + // pesoPontuacaoMediaSorteio +
				formula.ultPQtdSB1.getPeso() + // pesoPontuacaoMediaSorteio +
				formula.ultSorteioSB1.getPeso() + // pesoPontuacaoSemSorteio1 +
				formula.ultSorteioSB2.getPeso() + // pesoPontuacaoSemSorteio2 +
				formula.monteCarlo.getPeso() + // pesoPontuacaomonteCarlo
				formula.drop.getPeso() // pesoPontuacaomonteCarlo
		);
		// if (total==0) {
		// return null;
		// }
		float totalGeral = 0;
		for (byte cursorDezena = 1; cursorDezena < 61; cursorDezena++) {
			listaIndice[cursorDezena - 1] = ((colecao.idxMaiores.getIndice(cursorDezena, formula.maiores.getTipo())
					* formula.maiores.getPeso()
					+ colecao.idxUltSorteio.getIndice(cursorDezena, formula.ultSorteio.getTipo())
							* formula.ultSorteio.getPeso()
					+ colecao.idxUltPQtd.getIndice(cursorDezena, formula.ultPQtdSB1.getTipo(), 1)
							* formula.ultPQtdSB1.getPeso()
					+ colecao.idxParImp.getIndice(cursorDezena, formula.parImp.getTipo()) * formula.parImp.getPeso()
					+ colecao.idxCiclo.getIndice(cursorDezena, formula.ciclo.getTipo()) * formula.ciclo.getPeso()
					+ colecao.idxUltPQtd.getIndice(cursorDezena, formula.ultPQtd.getTipo()) * formula.ultPQtd.getPeso()
					+ colecao.idxMonteCarlo.getIndice(cursorDezena, formula.monteCarlo.getTipo())
							* formula.monteCarlo.getPeso()
					+ colecao.idxDrop.getIndice(cursorDezena, formula.drop.getTipo()) * formula.drop.getPeso()
					+ colecao.idxUltSorteio.getIndice(cursorDezena, formula.ultSorteioSB1.getTipo(), 1)
							* formula.ultSorteioSB1.getPeso()
					+ colecao.idxUltSorteio.getIndice(cursorDezena, formula.ultSorteioSB2.getTipo(), 2)
							* formula.ultSorteioSB2.getPeso())
					/ total);
			totalGeral += listaIndice[cursorDezena - 1];
		}
		if (totalGeral == 0) {
			return null;
		}

		// lstIndexMaiores = processArrayNovo(listaIndice, 28, colecao, indice);
		return maioresSoProcUltra(listaIndice);// , 28, colecao, indice);

	}
}
