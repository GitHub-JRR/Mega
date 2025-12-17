package MegaPck;

public class IdxLin extends AbstractIndice {

	public static byte qualLinha(final byte numero) {
		if (numero < 11) {
			return 0;
		} else if (numero < 21) {
			return 1;
		} else if (numero < 31) {
			return 2;
		} else if (numero < 41) {
			return 3;
		} else if (numero < 51) {
			return 4;
		} else {
			return 5;
		}
	}

	IdxLin() {
		super();
		this.indiceDeFormula = false;
	}

	/*
	 * public IdxLin clone() { IdxLin copia = new IdxLin(); copia =
	 * super.clone(this, copia) ; return copia; }
	 */
	public void recalcule(final IdxMaiores indiceMaiores) {
		final int[] totais = { 0, 0, 0, 0, 0, 0 };
		byte i;
		for (i = 0; i < 60; i++) {
			totais[qualLinha(indiceMaiores.getDezenaDoRank((byte) (i + 1)))] += indiceMaiores
					.getValorBaseDaDezena(indiceMaiores.getDezenaDoRank((byte) (i + 1)));
		}
		for (i = 1; i < 61; i++) {
			this.setValorBaseDaDezena(i, totais[qualLinha(i)]);
		}
		this.recalcule();
	}
}
