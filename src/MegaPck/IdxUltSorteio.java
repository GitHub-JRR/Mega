package MegaPck;

import java.util.Arrays;

// numUltimoSorteio guarda a informacao do numero do ultimo sorteio. (a cada sorteio cadastrado incrementa o contador)
// a Base guarda o numero do ultimo sorteio de cada dezena.
// Sub1 Guarda o maior intervalo sem sorteio da dezena
// Sub2 Guarda o menor intervalo sem sorteio da dezena (entre os intervalos maiores)

public class IdxUltSorteio extends AbstractIndice {
	private int numUltimoSorteio;

	IdxUltSorteio() {
		super((byte) 2);
		numUltimoSorteio = 0;
		Arrays.fill(sub2.base, 9999);
	}

	/*
	 * public IdxUltSorteio clone() { IdxUltSorteio copia = new IdxUltSorteio();
	 * copia = super.clone(this, copia) ; copia.setNumSorteios(numUltimoSorteio);
	 * return copia; }
	 */
	@Override
	public void soAdd(Sorteio sorteio) {
		numUltimoSorteio++;
		for (byte dezena : sorteio.get()) {
			setValorBaseDaDezena(dezena, numUltimoSorteio);
		}

	}

	@Override
	public void setValorBaseDaDezena(byte dezena, int valor) {
		int intervalo = valor - dados.base[dezena - 1] - 1;
		if (sub1.base[dezena - 1] < intervalo) {
			sub1.base[dezena - 1] = intervalo;
		}
		if (sub2.base[dezena - 1] > sub1.base[dezena - 1]) {
			sub2.base[dezena - 1] = sub1.base[dezena - 1];
		}
		super.setValorBaseDaDezena(dezena, valor);

	}

	public int getNumSorteios() {
		return numUltimoSorteio;
	}

	public void setNumSorteios(int num) {
		numUltimoSorteio = num;
	}

}
