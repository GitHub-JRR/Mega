package MegaPck;

public class IdxQuad extends AbstractIndice {

	public static byte qualQuadra(final byte numero) {
		if (numero < 31) {
			if ((numero % 10) < 6 & (numero % 10) > 0) {
				return 0;
			} else {
				return 1;
			}
		} else {
			if ((numero % 10) < 6 & (numero % 10) > 0) {
				return 2;
			} else {
				return 3;
			}
		}
	}

	IdxQuad() {
		super();
		this.indiceDeFormula = false;
	}
	/*
	 * public IdxQuad clone() { IdxQuad copia = new IdxQuad(); copia =
	 * super.clone(this, copia) ; return copia; }
	 */

	public void recalcule(final IdxMaiores indiceMaiores) {
		final int[] totais = { 0, 0, 0, 0 };
		byte dezenaCalculada, i;
		for (i = 0; i < 60; i++) {
			dezenaCalculada = indiceMaiores.getDezenaDoRank((byte) (i + 1));
			totais[qualQuadra(dezenaCalculada)] += indiceMaiores.getValorBaseDaDezena(dezenaCalculada);
		}
		for (i = 1; i < 61; i++) {
			this.setValorBaseDaDezena(i, totais[qualQuadra(i)]);
		}
		this.recalcule();
	}
}
