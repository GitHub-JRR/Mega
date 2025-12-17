package MegaPck;

import org.apache.commons.math3.distribution.HypergeometricDistribution;

public class Calibragem implements Comparable<Calibragem> {

	static byte ordem;
	
	static final double[] probs = new double[6];
	static final double[] esperado = new double[6];
	static final double[] pesos = new double[6];
	
	private static final double SCORE_NEGATIVO_EXTREMO = -7316; // Um valor abaixo do mínimo observado
	private static double SCORE_POSITIVO_EXTREMO = 200000; // Um valor acima do máximo observado
	
	static int qtdSorteios;
	public short senas;
	public short quinas;
	public short quadras;
	public short ternos;
	public short duplas;
	public short solos;
	// public short senasRel;
	// public short quinasRel;
	// public short quadrasRel;
	// short qtdDezenas;
	short ultimoSorteioComAcerto;
	public Long baseFormula;
	// short numSorteiosComAcerto;

	public int numSorteiosComAcerto() {
		return (this.senas + this.quinas + this.quadras + this.ternos + this.duplas + this.solos);
	}

	public Calibragem() {
		this.baseFormula = 0L;
		ordem = 0;
	}

	public Calibragem(final long baseFormula) {
		this.ternos = 0;
		this.duplas = 0;
		this.solos = 0;
		this.senas = 0;
		this.quinas = 0;
		this.quadras = 0;
		// this.qtdDezenas = 20;
		this.ultimoSorteioComAcerto = 0;
		this.baseFormula = baseFormula;
		// this.numSorteiosComAcerto = 0;
	}

	public Calibragem(final short parTernos, final short parDuplas, final short parSolos, final short senas,
			final short quinas, final short quadras, final short numeroDoTeste, final short numeroSorteioAtual,
			final long baseFormula) {
		this.senas = senas;
		this.quinas = quinas;
		this.quadras = quadras;
		this.ternos = parTernos;
		this.duplas = parDuplas;
		this.solos = parSolos;
		// this.qtdDezenas = numeroDoTeste;
		this.ultimoSorteioComAcerto = numeroSorteioAtual;
		this.baseFormula = baseFormula;
	}

	public Calibragem(final short senasRel, final short quinasRel, final short quadrasRel, final short senas,
			final short quinas, final short quadras, final short numeroDoTeste, final short numeroSorteioAtual,
			final long baseFormula, final short qtdAcertos) {
		this.ternos = senasRel;
		this.duplas = quinasRel;
		this.solos = quadrasRel;
		this.senas = senas;
		this.quinas = quinas;
		this.quadras = quadras;
		// this.qtdDezenas = numeroDoTeste;
		this.ultimoSorteioComAcerto = numeroSorteioAtual;
		this.baseFormula = baseFormula;
		// this.numSorteiosComAcerto = qtdAcertos;
	}

	public long ordem1() {
		/*
		 * return 100000_000_00_00_00_0L * (1000 - this.qtdDezenas) +
		 * 100000_000_00_00_00L * this.senas + 100000_000_00_00L * this.quinas +
		 * 100000_000_00L * this.quadras + 100000_000L * this.numSorteiosComAcerto;//+
		 */
		///return  100000_000_00_00_00L * this.senas
		///		+ 100000_000_00_00L * this.quinas + 100000_000_00L * this.quadras
		///		+ 100000_000L * this.numSorteiosComAcerto();// +
		// calcRankTotais() /// 10_0_0_0_0_0_0_0L
		// );
		return    10000_0000_000_00L * this.senas  +                              
				  10000_0000_000L    * this.quinas  +                               
				  10000_0000L        * this.quadras +                                 
				  10000L             * this.ternos +                                 
				  1L                 * this.numSorteiosComAcerto() ;
	}

	public long calcRankTotais() {
		return this.baseFormula;
	}

	public void clear() {
		this.senas = 0;
		this.quinas = 0;
		this.quadras = 0;
		this.ternos = 0;
		this.duplas = 0;
		this.solos = 0;
		// this.qtdDezenas = 0;
		this.ultimoSorteioComAcerto = 0;
		this.baseFormula = 0L;
		// this.numSorteiosComAcerto = 0;
	}

	@Override
	public int compareTo(final Calibragem o) {
		long a;
		long b;
		if (ordem == 0) {
			a = this.calcRankTotais();
			b = o.calcRankTotais();
		} else if (ordem == 1) {
			a = this.ordem1();
			b = o.ordem1();
		} else if (ordem == 3) {
			a = this.ordem3();
			b = o.ordem3();
		} else if (ordem == 4) {
			double x = this.ordem4();
			double y = o.ordem4();
			if (x > y) {
				return -1;
			} else if (x < y) {
				return 1;
			} else {
				return 0;
			}
		} else {
			a = this.ordem2();
			b = o.ordem2();
		}
		if (a > b) {
			return -1;
		} else if (a < b) {
			return 1;
		} else {
			return 0;
		}
	}

	public void copy(final Calibragem item) {
		this.senas = item.senas;
		this.quinas = item.quinas;
		this.quadras = item.quadras;
		this.ternos = item.ternos;
		this.duplas = item.duplas;
		this.solos = item.solos;
		// this.qtdDezenas = item.qtdDezenas;
		this.ultimoSorteioComAcerto = item.ultimoSorteioComAcerto;
		// this.numSorteiosComAcerto = item.numSorteiosComAcerto;
		this.baseFormula = item.baseFormula;
	}

	@Override
	public boolean equals(final Object obj) {
		if (obj == null) {
			return false;
		}
		if (!(obj instanceof Calibragem)) {
			return false;
		}
		if (obj == this) {
			return true;
		}
		return this.calcRankTotais() == ((Calibragem) obj).calcRankTotais();
	}

	@Override
	public int hashCode() {
		return (int) this.calcRankTotais();
	}

	public long ordem2() {
		/*
		 * ver 77 return 1000_000_00000L * this.numSorteiosComAcerto + 1000_000_00L *
		 * (1000 - this.qtdDezenas) + 1000_000L * this.senas + 1000L * this.quinas +
		 * this.quadras;
		 */
		///return 1000_000_00000L * this.numSorteiosComAcerto() + 1000_000_00L * (1000 - 10) + 1000_000L * this.senas
		///		+ 1000L * this.quinas + this.quadras;
		return    10000_0000_000_00_00L * this.numSorteiosComAcerto() +        //4      13
				  10000_0000_000_00L    * this.senas+                          //2  6   18
				  10000_0000_000L       * this.quinas +                        //2  8   10
				  10000_0000L           * this.quadras +                       //3  11  8
				  10000L                * this.ternos +                        //4  15  5
				  1L                    * this.duplas +this.solos;             //4  19
	}

	public long ordem3() {
		/*
		 * ver 77 return 100_000_00000_0000L * this.ultimoSorteioComAcerto + 100_000_00L
		 * * (1000 - this.qtdDezenas) + 100_000L * this.senas + 100L * this.quinas +
		 * this.quadras;
		 */
		return 100_000_00000_0000L * this.ultimoSorteioComAcerto + 100_000_00L * (1000 - 10) + 100_000L * this.senas
				+ 100L * this.quinas + this.quadras;
	}

	public double ordem4() {
		/*
		 * ver 78 return 100_000_00000_0000L * this.ultimoSorteioComAcerto + 100_000_00L
		 * * (1000 - this.qtdDezenas) + 100_000L * this.senas + 100L * this.quinas +
		 * this.quadras;
		 */
		int var[] = { this.solos, this.duplas, this.ternos, this.quadras, this.quinas, this.senas };

		return calcScore(var);
	}

	public void print(final int contador) {
		final Formula formula = new Formula(this.baseFormula);
		System.out.println(String.format("%8d %4d %4d %4d %4d %4d %5d %02d", contador, this.senas, this.quinas,
				this.quadras, this.ternos, this.duplas, this.solos, 10)
				+ String.format("%04d ", this.ultimoSorteioComAcerto) + (formula.filtro ? " S" : " N")
				+ (formula.maiores.getPeso() == 0 ? "   " : String.format(" %2d", formula.maiores.getTipo()))
				+ (formula.ultSorteio.getPeso() == 0 ? "   " : String.format(" %2d", formula.ultSorteio.getTipo()))
				+ (formula.ultPQtdSB1.getPeso() == 0 ? "   " : String.format(" %2d", formula.ultPQtdSB1.getTipo()))
				+ (formula.parImp.getPeso() == 0 ? "   " : String.format(" %2d", formula.parImp.getTipo()))
				+ (formula.ciclo.getPeso() == 0 ? "   " : String.format(" %2d", formula.ciclo.getTipo()))
				+ (formula.ultPQtd.getPeso() == 0 ? "   " : String.format(" %2d", formula.ultPQtd.getTipo()))
				+ (formula.ultSorteioSB1.getPeso() == 0 ? "   "
						: String.format(" %2d", formula.ultSorteioSB1.getTipo()))
				+ (formula.ultSorteioSB2.getPeso() == 0 ? "   "
						: String.format(" %2d", formula.ultSorteioSB2.getTipo()))
				+ (formula.maiores.getPeso() < 2 ? "   " : String.format(" %2.0f", formula.maiores.getPeso()))
				+ (formula.ultSorteio.getPeso() < 2 ? "   " : String.format(" %2.0f", formula.ultSorteio.getPeso()))
				+ (formula.ultPQtdSB1.getPeso() < 2 ? "   " : String.format(" %2.0f", formula.ultPQtdSB1.getPeso()))
				+ (formula.parImp.getPeso() < 2 ? "   " : String.format(" %2.0f", formula.parImp.getPeso()))
				+ (formula.ciclo.getPeso() < 2 ? "   " : String.format(" %2.0f", formula.ciclo.getPeso()))
				+ (formula.ultPQtd.getPeso() < 2 ? "   " : String.format(" %2.0f", formula.ultPQtd.getPeso()))
				+ (formula.ultSorteioSB1.getPeso() < 2 ? "   "
						: String.format(" %2.0f", formula.ultSorteioSB1.getPeso()))
				+ (formula.ultSorteioSB2.getPeso() < 2 ? "   "
						: String.format(" %2.0f", formula.ultSorteioSB2.getPeso()))
				+ String.format(" %7d", this.numSorteiosComAcerto()));
	}

	public void printTitulo() {
		System.out.println("01 senas  - 02 quinas  - 03 quadras    -  11 ternos   - 12 duplas    - 13 solos     ");
		System.out.println(
				"20 qtdDezenas - 21 Valor Obtido - 22 Filtro (s/n) - 23 dezenas utilizadas no processamento - 24 Numero do sorteio");
		System.out.println(
				"30 MaioresTipo - 31 SemSortTipo - 32 MedSort1Tipo - 33 ParImTipo - 34 MedSortUltimoCicloTipo - 35 MedSortTipo - 36 SemSort1Tipo - 37 SemSort2Tipo");
		System.out.println(
				"40 Maiores - 41 SemSort - 42 MediaSort1 - 43 ParImp - 44 MediaSortUltimoCiclo - 45 MedSort - 46 SemSort1 - 47 SemSort2 - 50 num processo");
		System.out.println("");
		System.out.println(
				"    num.   01   02   03   11   12    13 20  Sort 22 30 31 32 33 34 35 36 37 40 41 42 43 44 45 46 47  Acertos");
		System.out.println(
				"======== ==== ==== ==== ==== ==== ===== ==  ==== == == == == == == == == == == == == == == == == ==  =======");
	}

	@Override
	public String toString() {
		return ("" + this.senas + "," + this.quinas + "," + this.quadras + "," + this.ternos + "," + this.duplas + ","
				+ this.solos + "," + this.ultimoSorteioComAcerto + "," + this.baseFormula);

	}

	static void calcularProbabilidadesTeoricas(int i) {
		//probs = new double[6];
		setQtdSorteios(i);
		HypergeometricDistribution dist = new HypergeometricDistribution(60, 6, 10);
				// TOTAL_NUMEROS, NUMEROS_SORTEADOS, NUMEROS_APOSTADOS);
				
		for (int k = 0; k < 6; k++) {
			probs[k] = dist.probability(k + 1);
			esperado[k]=probs[k]*qtdSorteios;
		}
		
		pesos[0]=1; // Sena (6 acertos) >> Solo (1 acerto)
		for (int k = 1; k < 6; k++) {
			pesos[k] = pesos[k-1]*probs[k-1]/probs[k];
		}
		return;
	}
	/*
	static void calcProbabilidadesTeoricas(double[] probability) {
		//probs = new double[6];
		
		//HypergeometricDistribution dist = new HypergeometricDistribution(60, 6, 10);
				// TOTAL_NUMEROS, NUMEROS_SORTEADOS, NUMEROS_APOSTADOS);
				
		for (int k = 0; k < 6; k++) {
			probs[k] = probability[k ];
		}
		return;
	}
	*/
	static void setQtdSorteios(int numeroSorteios) {
		qtdSorteios = numeroSorteios;
	}
    static void ajustaSCORE_POSITIVO_EXTREMO(double valor) {
    	if (valor > SCORE_POSITIVO_EXTREMO ) {
    		//System.out.println("SCORE_POSITIVO_EXTREMO passando de "+SCORE_POSITIVO_EXTREMO+" para :"+ (valor*1.05) );
    		SCORE_POSITIVO_EXTREMO=valor*1.05;
    	}
    }
    public static double calcScore(int[] acertos) {
		double calcScore=0;
		for (int k = 0; k < 6; k++) {
			calcScore+= (acertos[k]-esperado[k])*pesos[k];
	    }
		ajustaSCORE_POSITIVO_EXTREMO(calcScore);
        
	    return calcScore;
	}
    
    public double scoreItem() {
		/*
		 * ver 78 return 100_000_00000_0000L * this.ultimoSorteioComAcerto + 100_000_00L
		 * * (1000 - this.qtdDezenas) + 100_000L * this.senas + 100L * this.quinas +
		 * this.quadras;
		 */
		int var[] = { this.solos, this.duplas, this.ternos, this.quadras, this.quinas, this.senas };

		return avaliarPerformance(var);
	}
	public static double avaliarPerformance(int[] acertos) {
		
		return normalizarScore(calcScore(acertos));
	    
	}
	private static double normalizarScore(double scoreOriginal) {
	    // Se for o score ideal, retorna 100 (máxima pontuação)
	    
	    
	    // Determina em qual direção estamos do ideal
	    if (scoreOriginal > 0) {
	        // Normaliza scores positivos entre 50 e 100
	        double rangeOriginal = SCORE_POSITIVO_EXTREMO ;
	        double rangeNormalizado = 100 - 50;
	        return 50 + ((scoreOriginal ) / rangeOriginal) * rangeNormalizado;
	    } else {
	        // Normaliza scores negativos entre 0 e 50
	        double rangeOriginal =  - SCORE_NEGATIVO_EXTREMO;
	        double rangeNormalizado = 50 ;
	        return ((scoreOriginal - SCORE_NEGATIVO_EXTREMO) / rangeOriginal) * rangeNormalizado;
	    }
	}
}