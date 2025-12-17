package MegaPck;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

///*//  base da vers�o 67
// Baseada nas teoriad de Taufic Darhal
public class Darhal {
	/// *//CombinaNovo tempComb;
	/// *//int [][][] matrizComb;
	/// *//int [] qtd6Grupos;
	/// *//int indiceDoMaior;
	/// *//int qtdDeGrupos;
	byte[] grupoDarhalDaDezena;
	/// *//static int contadorSerie;
	List<Series> rankSeries = new ArrayList<>();
	byte[][] serieDeDarhal;
	boolean recalcula;
	static int qtdAcertos;

	public Darhal() {
		// tempComb = new CombinaNovo(60,3);
		/// *//matrizComb = geraMatriz();
		/// *//qtdDeGrupos=1900;
		/// *//qtd6Grupos = new int[1900];
		/// *//Arrays.fill(qtd6Grupos,0);
		/// *//indiceDoMaior=0;
		/// *//contadorSerie=0;
		grupoDarhalDaDezena = new byte[60];
		for (byte i = 0; i < 60; i++) {
			grupoDarhalDaDezena[i] = (byte) (i / 3);
		}
		/// *//recalcule();
		geraTodasSeries(rankSeries);
		serieDeDarhal = new byte[20][3];
		recalcula = false;
		qtdAcertos = 0;

	}

	private static void geraTodasSeries(List<Series> rankSeries) {
		Series retorno;
		byte i, j, k;
		for (i = 1; i <= 58; i++) {
			for (j = (byte) (i + 1); j <= 59; j++) {
				for (k = (byte) (j + 1); k <= 60; k++) {
					retorno = new Series(i, j, k); // NOPMD by mailj on 07/07/2019 20:33
					rankSeries.add(retorno);
				}
			}
		}

	}

	// public int[][] getSerieDeDarhal() {
	/// *//return matrizComb[indiceDoMaior];
	// return buscaSerieDeDarhal(rankSeries,grupoDarhalDaDezena);
	// }
	public byte[][] getSerieDeDarhalNova() {
		/// *//return matrizComb[indiceDoMaior];
		serieDeDarhal = buscaSerieDeDarhalSuperNova(rankSeries, grupoDarhalDaDezena);
		return serieDeDarhal;
	}

	private static byte[][] buscaSerieDeDarhalSuperNova(List<Series> rankSeries, byte[] grupoDarhalDaDezena) {
		Series.ordem = 1;
		Collections.sort(rankSeries);

		Series serie;
		boolean[] dezenaSelecionada = new boolean[60];
		Arrays.fill(dezenaSelecionada, false);
		byte[][] serieDarhal = new byte[20][3];
		byte[][] serieDarhalReserva = new byte[20][3];
		int[] indiceSerieDarhal = new int[20];
		int[] acertosSerieDarhal = new int[20];
		byte grupo = 0;
		int indice = 0;
		int totAcertoNaSerie = 0;
		int acertosNaSerieReserva = 99;
		byte grupoDeEntrada;

		while (temVaga(dezenaSelecionada) & indice < rankSeries.size()) {
			serie = rankSeries.get(indice++);
			if (!dezenaSelecionada[serie.dezena1 - 1]) {
				if (!dezenaSelecionada[serie.dezena2 - 1]) {
					if (!dezenaSelecionada[serie.dezena3 - 1]) {
						serieDarhal[grupo][0] = serie.dezena1;
						serieDarhal[grupo][1] = serie.dezena2;
						serieDarhal[grupo][2] = serie.dezena3;
						grupoDarhalDaDezena[serie.dezena1 - 1] = grupo;
						grupoDarhalDaDezena[serie.dezena2 - 1] = grupo;
						grupoDarhalDaDezena[serie.dezena3 - 1] = grupo;
						dezenaSelecionada[serie.dezena1 - 1] = true;
						dezenaSelecionada[serie.dezena2 - 1] = true;
						dezenaSelecionada[serie.dezena3 - 1] = true;
						totAcertoNaSerie += serie.acertos;
						acertosSerieDarhal[grupo] = serie.acertos;
						indiceSerieDarhal[grupo] = indice;
						grupo++;
						// if (serie.acertos>indiceMaximo) {
						// indiceMaximo=serie.acertos;
						// }
						// System.out.println("O grupo " +(grupo)+" vem com a serie "+ indice+ " com as
						// dezenas "+ Arrays.toString(serieDarhal[grupo-1])+" com "+serie.acertos+"
						// acertos. E tot acertos em: "+totAcertoNaSerie);
					}
				}
			}
		}
		if (totAcertoNaSerie == 0) {
			Series.ordem = 0;
			Collections.sort(rankSeries);
			// System.out.println("Retornando serie zerada que est� com: "+totAcertoNaSerie+
			// " acertos na serie.");
			qtdAcertos = 0;
			return serieDarhal;
		} else {
			if (totAcertoNaSerie < acertosNaSerieReserva) {
				// System.out.println("Vou tentar ajustar a serie que est� com:
				// "+totAcertoNaSerie+ " acertos na serie.");
				// System.out.println("Caso n�o encontre melhor solu��o vou guardar a solu��o
				// atual com: "+totAcertoNaSerie+ " acertos na serie. ");
				for (byte i = 0; i < 20; i++) {
					serieDarhalReserva[i][0] = serieDarhal[i][0];
					serieDarhalReserva[i][1] = serieDarhal[i][1];
					serieDarhalReserva[i][2] = serieDarhal[i][2];
				}
				acertosNaSerieReserva = totAcertoNaSerie;
			}
		}
		// vou encontar o grupo com acertos
		for (byte i = 0; i < 20; i++) {
			if (acertosSerieDarhal[i] > 0) {
				grupo = i;
				break;
			}
		}
		if (grupo > 0) {
			grupo--;
		}
		while (true) {
			grupoDeEntrada = grupo;
			// vou apagar o grupo anterior e posteriores e apontar o indice para o ponto
			// posterior ao do grupo anterior
			for (byte zera = grupo; zera < 20; zera++) {
				totAcertoNaSerie -= acertosSerieDarhal[zera];
				acertosSerieDarhal[zera] = 0;
				dezenaSelecionada[serieDarhal[zera][0] - 1] = false;
				dezenaSelecionada[serieDarhal[zera][1] - 1] = false;
				dezenaSelecionada[serieDarhal[zera][2] - 1] = false;
			}
			indice = indiceSerieDarhal[grupo] + 1;

			while (temVaga(dezenaSelecionada)) {
				serie = rankSeries.get(indice++);
				if ((serie.acertos + totAcertoNaSerie) >= acertosNaSerieReserva) {
					//
					if (grupoDeEntrada == 0) {
						Series.ordem = 0;
						Collections.sort(rankSeries);
						// System.out.println("Retornando serie reservada que est� com:
						// "+acertosNaSerieReserva+ " acertos na serie.");
						qtdAcertos = acertosNaSerieReserva;
						return serieDarhalReserva;
					} else {

						// System.out.println("Tentando fechar o grupo " +(grupo+1)+" que estava com a
						// serie "+ indice+ " com "+serie.acertos+" acertos. que � maior ou igual ao
						// acertos na serie reserva em: "+acertosNaSerieReserva+" vou retoceder para o
						// grupo anterior");
						if (grupo == grupoDeEntrada
								| (indiceSerieDarhal[grupoDeEntrada] + 1 + 19 - grupoDeEntrada) >= rankSeries.size()) {
							grupo = (byte) (grupoDeEntrada - 1);
						} else {
							indiceSerieDarhal[grupoDeEntrada]++;
							grupo = grupoDeEntrada;
						}
						// grupo=grupoDeEntrada;
						// indiceSerieDarhal[grupo]++;
						break;
					}
				}
				if (!dezenaSelecionada[serie.dezena1 - 1]) {
					if (!dezenaSelecionada[serie.dezena2 - 1]) {
						if (!dezenaSelecionada[serie.dezena3 - 1]) {
							serieDarhal[grupo][0] = serie.dezena1;
							serieDarhal[grupo][1] = serie.dezena2;
							serieDarhal[grupo][2] = serie.dezena3;
							grupoDarhalDaDezena[serie.dezena1 - 1] = grupo;
							grupoDarhalDaDezena[serie.dezena2 - 1] = grupo;
							grupoDarhalDaDezena[serie.dezena3 - 1] = grupo;
							dezenaSelecionada[serie.dezena1 - 1] = true;
							dezenaSelecionada[serie.dezena2 - 1] = true;
							dezenaSelecionada[serie.dezena3 - 1] = true;
							indiceSerieDarhal[grupo] = indice - 1;
							totAcertoNaSerie += serie.acertos;
							grupo++;
							// if (serie.acertos>indiceMaximo) {
							// indiceMaximo=serie.acertos;
							// }
							// System.out.println("O grupo " +(grupo)+" vem com a serie "+ indice+ " com as
							// dezenas "+ Arrays.toString(serieDarhal[grupo-1])+" com "+serie.acertos+"
							// acertos. E tot acertos em: "+totAcertoNaSerie);
							if (grupo == 20) {
								Series.ordem = 0;
								Collections.sort(rankSeries);
								// System.out.println("Retornando serie nova que est� com: "+totAcertoNaSerie+ "
								// acertos na serie.");
								qtdAcertos = totAcertoNaSerie;
								return serieDarhal;
							}
						}
					}
				}
				if ((indice + 19 - grupo) >= rankSeries.size()) {
					if (grupoDeEntrada > 0) {
						grupo = (byte) (grupoDeEntrada - 1);
						// System.out.println("Retocedendo para o grupo " +(grupo+1));
					} else {
						if ((indice + 19) >= rankSeries.size()) {
							Series.ordem = 0;
							Collections.sort(rankSeries);
							// System.out.println("Retornando serie reservada que est� com:
							// "+acertosNaSerieReserva+ " acertos na serie.");
							qtdAcertos = acertosNaSerieReserva;
							return serieDarhalReserva;
						}
						// System.out.println("No grupo inicial e descendo " );
					}
					break;
				}
			}
			if (!temVaga(dezenaSelecionada)) {
				Series.ordem = 0;
				Collections.sort(rankSeries);
				// System.out.println("Retornando serie zerada que est� com: "+totAcertoNaSerie+
				// " acertos na serie.");
				qtdAcertos = totAcertoNaSerie;
				return serieDarhal;
			}

		}
	}

	//// ======================================================================

	private static boolean temVaga(boolean[] dezenaSelecionada) {
		for (int i = 0; i < 60; i++) {
			if (!dezenaSelecionada[i]) {
				return true;
			}
		}
		return false;
	}

	public void add(Sorteio sorteio) {
		soAdd(sorteio);
		recalcule();
	}

	/*
	 * public Darhal clone() { Darhal copia = new Darhal(); copia.rankSeries = new
	 * ArrayList<>(); Series serie; Series serieClone; for (int
	 * i=0;i<this.rankSeries.size();i++) { serie=this.rankSeries.get(i); serieClone
	 * = new Series(); serieClone.acertos=serie.acertos;
	 * serieClone.dezena1=serie.dezena1; serieClone.dezena2=serie.dezena2;
	 * serieClone.dezena3=serie.dezena3; copia.rankSeries.add(serieClone); }
	 * copia.grupoDarhalDaDezena=this.grupoDarhalDaDezena.clone(); for ( int i
	 * =0;i<20;i++) { copia.serieDeDarhal[i][0]=serieDeDarhal[i][0];
	 * copia.serieDeDarhal[i][1]=serieDeDarhal[i][1];
	 * copia.serieDeDarhal[i][2]=serieDeDarhal[i][2]; }
	 * 
	 * 
	 * //copia.indiceDoMaior=this.indiceDoMaior;
	 * //copia.qtdDeGrupos=this.qtdDeGrupos;
	 * //copia.grupoDarhalDaDezena=this.grupoDarhalDaDezena.clone();
	 * 
	 * 
	 * return copia; }
	 */
	public void recalcule() {
		//// ////getSerieDeDarhal();
		if (this.recalcula) {
			getSerieDeDarhalNova();
		}
		/// *//
		/*
		 * int maior=0; int indice=0;
		 * 
		 * for (int contador = 0 ; contador < qtdDeGrupos; contador++ ) { if
		 * (qtd6Grupos[contador]>maior) { maior=qtd6Grupos[contador]; indice=contador; }
		 * 
		 * } if (indiceDoMaior != indice) { indiceDoMaior=indice;
		 * //System.out.println("\nO maior de Darhal passou a ser o numero : "
		 * +indiceDoMaior); }
		 * 
		 * int cursorGrupo,cursorItem;
		 * 
		 * for ( cursorGrupo=0;cursorGrupo<20;cursorGrupo++) { for (
		 * cursorItem=0;cursorItem<3;cursorItem++) {
		 * grupoDarhalDaDezena[matrizComb[indiceDoMaior][cursorGrupo][cursorItem]-1]=
		 * cursorGrupo; } }
		 */
		/// *//

	}

	public void soAdd(Sorteio sorteio) {
		expandeSorteio(sorteio, rankSeries);
		/// *//
		/*
		 * int[] sorteioEmAnalise=sorteio.get(); int contaNaSerie; int contaNoGrupo;
		 * int[][] grupo; Grupo: for ( int cursorGrupos=0
		 * ;cursorGrupos<qtdDeGrupos;cursorGrupos++) { grupo = matrizComb[cursorGrupos];
		 * contaNoGrupo=0; for (int[] serie : grupo) { contaNaSerie=0; for (int
		 * dezenaDaSerie : serie) { for (int dezenaNoSorteio:sorteioEmAnalise ) { if
		 * (dezenaDaSerie==dezenaNoSorteio) { contaNaSerie++; } }
		 * 
		 * } if (contaNaSerie>1) { continue Grupo; } else if (contaNaSerie==1) {
		 * contaNoGrupo++; }
		 * 
		 * } if ( contaNoGrupo==6) { qtd6Grupos[cursorGrupos]++; continue Grupo; } }
		 */
		/// *//
	}

	private static void expandeSorteio(Sorteio sorteio, List<Series> rankSeries) {
		Series serieEmBusca;
		int pos;
		byte[] dezenas = sorteio.dezenas.clone();
		Arrays.sort(dezenas);
		// System.out.println("O sorteio numero " +numeroDoSorteio+" com as dezenas
		// "+Arrays.toString(sorteio.dezenas));
		// int contador=1;
		byte i, j, k;
		for (i = 1; i <= 4; i++) {
			for (j = (byte) (i + 1); j <= 5; j++) {
				for (k = (byte) (j + 1); k <= 6; k++) {
					serieEmBusca = new Series(dezenas[i - 1], dezenas[j - 1], dezenas[k - 1]);
					pos = rankSeries.indexOf(serieEmBusca);
					// System.out.println("A serie " + contador++ +" com as dezenas
					// "+serieEmBusca.dezena1+" "+serieEmBusca.dezena2+" "+serieEmBusca.dezena3+"
					// est� localizada no registro numero "+pos);
					/// *//rankSeries.get(pos).add(numeroDoSorteio);
					rankSeries.get(pos).add();
					/// *//if (pos==-1) {
					/// *// System.out.println("N�o encontrei a serie com as dezenas
					/// "+serieEmBusca.dezena1+" "+serieEmBusca.dezena2+" "+serieEmBusca.dezena3);
					/// *//}
				}
			}
		}
	}

	public void print() {
		byte[][] combParaFiltro = getSerieDeDarhalNova();
		System.out.println("");
		System.out.println(
				"===01===  ===02===  ===03===  ===04===  ===05===  ===06===  ===07===  ===08===  ===09===  ===10===");
		System.out.println(
				"========  ========  ========  ========  ========  ========  ========  ========  ========  ========");
		byte cursorItem, cursorSerie;
		for (cursorItem = 0; cursorItem < 10; cursorItem++) {
			for (cursorSerie = 0; cursorSerie < 3; cursorSerie++) {
				System.out.print(String.format("%02d ", combParaFiltro[cursorItem][cursorSerie]));
			}
			System.out.print(" ");
		}
		System.out.println(
				"\n========  ========  ========  ========  ========  ========  ========  ========  ========  ========");
		System.out.println("");
		System.out.println(
				"===11===  ===12===  ===13===  ===14===  ===15===  ===16===  ===17===  ===18===  ===19===  ===20===");
		System.out.println(
				"========  ========  ========  ========  ========  ========  ========  ========  ========  ========");
		for (cursorItem = 10; cursorItem < 20; cursorItem++) {
			for (cursorSerie = 0; cursorSerie < 3; cursorSerie++) {
				System.out.print(String.format("%02d ", combParaFiltro[cursorItem][cursorSerie]));
			}
			System.out.print(" ");
		}
		System.out.println(
				"\n========  ========  ========  ========  ========  ========  ========  ========  ========  ========");
		System.out.println("A serie esta com " + qtdAcertos + " acertos.");
	}

	// *///
	/*
	 * public static int[][][] geraMatriz() { boolean[] alocadas = new boolean[20];;
	 * int[] semente=new int[60]; int[] serieRegistra=semente.clone();
	 * Arrays.fill(serieRegistra, 77); //System.out.println(); boolean prt=false;
	 * int[][] pares = new int[10][2]; int[][][] matRetorno=new int[1900][20][3];
	 * int delta; for (int
	 * cursorPrimDezena=1;cursorPrimDezena<20;cursorPrimDezena++) { for (int
	 * cursorSegDezena=cursorPrimDezena+1;cursorSegDezena<21;cursorSegDezena++) { if
	 * (prt ) { System.out.print("\n"); } delta=cursorSegDezena-cursorPrimDezena-1;
	 * Arrays.fill(alocadas,false); for (int
	 * cursorPares=0;cursorPares<10;cursorPares++) {
	 * pares[cursorPares][0]=ajustaNum(cursorPrimDezena+cursorPares*(2+delta),
	 * alocadas); pares[cursorPares][1]=ajustaNum(cursorSegDezena
	 * +cursorPares*(2+delta),alocadas); if(prt) {System.out.print(
	 * String.format("%02d %02d ",pares[cursorPares][0],pares[cursorPares][1]));} }
	 * trabalhaPar(pares,matRetorno); } } // setGrupos(1900); return matRetorno; }
	 * 
	 * 
	 * private static void trabalhaPar(int[][] pares,int[][][] matRetorno) { int
	 * contador=0; int[] serieCompleta = new int[60]; int[] serieRotacionada =new
	 * int[60]; for (int[] par : pares) { for (int serie : par) { for( int dezena
	 * :expande(serie)) { serieCompleta[contador++]=dezena; } } }
	 * registra(serieCompleta,matRetorno); for (int
	 * cursorPrimeiraDezena=2;cursorPrimeiraDezena>=0;cursorPrimeiraDezena--) { for
	 * (int cursorSegundaDezena=0;cursorSegundaDezena<3;cursorSegundaDezena++) {
	 * serieRotacionada=serieCompleta.clone(); for (int cursorPares=0 ;
	 * cursorPares<20;cursorPares+=2) {
	 * serieRotacionada[cursorPares*3+cursorPrimeiraDezena]=serieCompleta[(
	 * cursorPares+1)*3+cursorSegundaDezena];
	 * serieRotacionada[(cursorPares+1)*3+cursorSegundaDezena]=serieCompleta[
	 * cursorPares*3+cursorPrimeiraDezena]; } registra(serieRotacionada,matRetorno);
	 * }
	 * 
	 * }
	 * 
	 * }
	 * 
	 * private static int[] expande(int serie) { return new int[]
	 * {(serie-1)*3+1,(serie-1)*3+2,(serie-1)*3+3}; }
	 */
	/// *//

	/*
	 * private static void resetNum() { Arrays.fill(alocadas,false); }
	 */

	/// *//
	/*
	 * private static int ajustaNum(int num, boolean[] alocadas) { int
	 * numRet=((num-1)%20)+1; if (num > 20) { int busca=0; while (alocadas[busca++])
	 * {} numRet = busca; } alocadas[numRet-1]=true; return numRet; }
	 * 
	 * 
	 * public static void registra(int[] serie,int[][][] matRetorno) { for (int
	 * cursorSerie=0;cursorSerie<20;cursorSerie++) { for (int
	 * cursorDezena=0;cursorDezena<3;cursorDezena++) {
	 * ///gravarArq.printf(String.format("%02d",serie[cursorSerie*3+cursorDezena]));
	 * matRetorno[contadorSerie][cursorSerie]
	 * [cursorDezena]=serie[cursorSerie*3+cursorDezena]; } } contadorSerie++; }
	 */
	/// *//

	// ==== fim Gera
	// Matriz========================================================================================================
}
