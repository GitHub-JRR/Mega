package MegaPck;

public class IdxUltPQtd extends AbstractIndice {

	IdxUltPQtd() {
		super();
	}

	IdxUltPQtd(int numSub) {
		super((byte) numSub);
	}
	/*
	 * public IdxUltPQtd clone() { IdxUltPQtd copia = new IdxUltPQtd(); copia =
	 * super.clone(this, copia) ; return copia; }
	 * 
	 */

	// O calculo � realizado em duas fases.
	// Fase 1 :
	// pego as dezenas em ordem de maior numero de sorteios
	// registro a quantidade de sorteios dela (val)
	// registro o ultimo sorteio da dezena no (ultimo)
	// registro na base o resultado do calculo ( Ultimo sorteio * 100.000) /
	// (quantidade de sorteior)
	//
	// Fase 2 :
	// percorro o indice na ordem definida acima
	// Calculo o valor medio sendo : ultimo sorteio da dezena / qtd de vezes que a
	// dezena foi sorteada;
	// Calculo o delta sendo : a quantidade total de sorteios - indice definido na
	// fase 1
	// calculo a base sendo : valor medio - delta *100.000
	// armazeno o resultado da Base no Sub1

	public void recalcule(IdxUltSorteio indiceSemSorteio, IdxMaiores indiceMaiores) {
		byte dezenaCalculada;
		for (byte i = 0; i < 60; i++) {
			dezenaCalculada = indiceMaiores.getDezenaDoRank((byte) (i + 1));
			if ((indiceMaiores.getValorBaseDaDezena(dezenaCalculada)) > 0) {
				setValorBaseDaDezena(dezenaCalculada, indiceSemSorteio.getValorBaseDaDezena(dezenaCalculada) * 100000
						/ indiceMaiores.getValorBaseDaDezena(dezenaCalculada));
			}
		}
		recalcule();
		float valorMedio;
		float delta;
		int base;

		for (byte i = 1; i < 61; i++) {
			dezenaCalculada = getDezenaDoRank(i);
			valorMedio = (float) indiceSemSorteio.getValorBaseDaDezena(dezenaCalculada)
					/ indiceMaiores.getValorBaseDaDezena(dezenaCalculada);

			delta = indiceSemSorteio.getNumSorteios() - indiceSemSorteio.getValorBaseDaDezena(dezenaCalculada);

			base = (int) (valorMedio - delta * 100000);

			setValorBaseDaDezena(dezenaCalculada, base, (byte) 1);
		}
		recalcule();
	}

}
