package MegaPck;

public class IdxParImp extends AbstractIndice {

	IdxParImp() {
		super();
	}

	/*
	 * public IdxParImp clone() { IdxParImp copia = new IdxParImp(); copia =
	 * super.clone(this, copia) ; return copia; }
	 */
	public void recalcule(IdxMaiores indiceMaiores) {
		int[] totais = { 0, 0 };
		byte i;
		for (i = 0; i < 60; i++) {
			totais[qualParimp(indiceMaiores.getDezenaDoRank((byte) (i + 1)))] += indiceMaiores
					.getValorBaseDaDezena(indiceMaiores.getDezenaDoRank((byte) (i + 1)));
		}
		for (i = 1; i < 61; i++) {
			setValorBaseDaDezena(i, totais[qualParimp(i)]);
		}

		recalcule();
	}

	public static byte qualParimp(byte numero) {
		return (byte) (numero % 2);
	}

}