package MegaPck;

public class Series implements Comparable<Series> {
	byte dezena1;
	byte dezena2;
	byte dezena3;
	int acertos;
	int ultimoSorteio;

	static byte ordem;

	public Series() {
		ordem = 0;
		acertos = 0;
	}

	public Series(byte dez1, byte dez2, byte dez3) {
		this.dezena1 = dez1;
		this.dezena2 = dez2;
		this.dezena3 = dez3;
		ordem = 0;
		acertos = 0;

	}

	public void add(int ultSort) {
		this.ultimoSorteio = ultSort;
		this.acertos++;
	}

	public void add() {
		/// *//this.ultimoSorteio=ultSort;
		this.acertos++;
	}

	public String toString() {
		return String.format("%2d %2d %2d %4d %6d", dezena1, dezena2, dezena3, ultimoSorteio, acertos);
	}

	@Override
	public int compareTo(Series o) {
		long a;
		long b;
		if (ordem == 0) {
			a = this.ordem0();
			b = o.ordem0();
		} else if (ordem == 1) {
			a = this.ordem1();
			b = o.ordem1();
		} else if (ordem == 3) {
			a = this.ordem0();
			b = o.ordem0();
		} else {
			a = this.ordem0();
			b = o.ordem0();
		}
		if (a > b) {
			return -1;
		} else if (a < b) {
			return 1;
		} else {
			return 0;
		}
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == null)
			return false;
		if (!(obj instanceof Series))
			return false;

		if (obj == this)
			return true;

		return this.ordem0() == ((Series) obj).ordem0();

	}

	@Override

	public int hashCode() {

		return (int) ordem0();

	}

	private long ordem0() {

		long retorno = (long) (

		1_00_00L * dezena1 + 1_00L * dezena2 + 1L * dezena3

		);

		return retorno;
	}

	private long ordem1() {

		long retorno = (long) (

		1_00_00_00L * (10000 - acertos) +
		// 1_00_00_00L*(acertos)+

				1_00_00L * dezena1 + 1_00L * dezena2 + 1L * dezena3

		);

		return retorno;
	}

	/*
	 * private long ordem2() {
	 * 
	 * long retorno= (long) (
	 * 
	 * 1000_000_00000L*numSorteiosComAcerto+ 1000_000_00L*(1000-qtdDezenas)+
	 * 1000_000L*senas+ 1000L*quinas+ quadras);
	 * 
	 * 
	 * return retorno; } private long ordem3() {
	 * 
	 * long retorno= (long) (
	 * 
	 * 1000_000_00000_0000L*sorteio+ //1000_000_00000L*numSorteiosComAcerto+
	 * 1000_000_00L*(1000-qtdDezenas)+ 1000_000L*senas+ 1000L*quinas+ quadras);
	 * 
	 * 
	 * 
	 * return retorno; }
	 * 
	 * public long calcRankTotais() {
	 * 
	 * return baseFormula;
	 * 
	 * }
	 */
}
