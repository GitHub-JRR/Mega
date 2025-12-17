package MegaPck;

import java.io.Serializable;

public class apostaAcerto implements Serializable, Comparable<apostaAcerto> {

	private static final long serialVersionUID = -299482035708790408L;
	// transient private int salary;
	public static byte ordem;
	public static int totReg;
	long aposta;
	long qtdAcertos;
	int qtdFormulas;

	public apostaAcerto() {
		this.aposta = 0L;
		ordem = 0;
		this.qtdFormulas = 0;
	}

	public apostaAcerto(final long aposta) {
		this.qtdAcertos = 0;
		this.aposta = aposta;
		this.qtdFormulas = 1;
	}

	public apostaAcerto(final long aposta, final int qtdAcertos) {
		this.aposta = aposta;
		this.qtdAcertos = qtdAcertos;
		this.qtdFormulas = 1;
	}

	public long calcRankTotais() {
		return this.aposta;
	}

	@Override
	public int compareTo(final apostaAcerto o) {
		long a;
		long b;
		if (ordem == 0) {
			a = this.ordem0();
			b = o.ordem0();
		} else if (ordem == 1) {
			a = this.ordem1();
			b = o.ordem1();
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

	@Override
	public boolean equals(final Object obj) {
		if (obj == null) {
			return false;
		}
		if (!(obj instanceof apostaAcerto)) {
			return false;
		}
		if (obj == this) {
			return true;
		}
		return this.calcRankTotais() == ((apostaAcerto) obj).calcRankTotais();
	}

	@Override
	public int hashCode() {
		return (int) this.aposta;
	}

	/*
	 * public long calcRankFinal() {
	 *
	 * return (long) (
	 *
	 * 100000_000_00_00_00_0L*(1000-qtdDezenas)+ 100000_000_00_00_00L*senas+
	 * 100000_000_00_00L*quinas+ 100000_000_00L*quadras+
	 * 100000_000L*numSorteiosComAcerto+
	 *
	 * calcRankTotais() /// 10_0_0_0_0_0_0_0L
	 *
	 * );
	 *
	 *
	 * }
	 */
	private long ordem0() {
		return this.aposta;
	}

	private long ordem1() {
		return this.qtdAcertos;
	}

	private long ordem2() {
		return this.qtdFormulas;
	}

	public void ordemSet(final byte value) {
		apostaAcerto.ordem = value;
	}

	public int totRegGet() {
		return apostaAcerto.totReg;
	}

	public void totRegSet(final int value) {
		apostaAcerto.totReg = value;
	}
}
