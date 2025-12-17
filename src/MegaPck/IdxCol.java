package MegaPck;

public class IdxCol extends AbstractIndice {

	public static byte qualColuna(final byte numero) {
		return (byte) (numero % 10);
	}

	IdxCol() {
		super();
		this.indiceDeFormula = false;
	}

	/*
	 * public IdxCol clone() { IdxCol copia = new IdxCol(); copia =
	 * super.clone(this, copia) ; return copia; }
	 */
	public void recalcule(final IdxMaiores indiceMaiores) {
		final int[] totais = { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 };
		byte i;
		for (i = 1; i < 61; i++) {
			totais[qualColuna(indiceMaiores.getDezenaDoRank(i))] += indiceMaiores
					.getValorBaseDaDezena(indiceMaiores.getDezenaDoRank(i));
		}
		for (i = 1; i < 61; i++) {
			this.setValorBaseDaDezena(i, totais[qualColuna(i)]);
		}
		this.recalcule();
	}
}
