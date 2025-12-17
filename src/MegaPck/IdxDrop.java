package MegaPck;

public class IdxDrop extends AbstractIndice {

	IdxDrop() {
		super();
	}

	/*
	 * public IdxMaiores clone() { IdxMaiores copia = new IdxMaiores(); copia =
	 * super.clone(this, copia) ; return copia; }
	 */
	@Override
	public void soAdd(final Sorteio sorteio) {
		for (final byte dezena : sorteio.get()) {
			this.incrementaBaseDaDezena(dezena);
			this.dados.base[dezena - 1] += 20;
			this.dados.base[next(dezena, (byte) 1) - 1] += 15;
			this.dados.base[next(dezena, (byte) 2) - 1] += 11;
			this.dados.base[next(dezena, (byte) 3) - 1] += 8;
			this.dados.base[next(dezena, (byte) 4) - 1] += 6;
			this.dados.base[next(dezena, (byte) 5) - 1] += 4;
			this.dados.base[next(dezena, (byte) 6) - 1] += 3;
			this.dados.base[next(dezena, (byte) 7) - 1] += 2;
			this.dados.base[next(dezena, (byte) 8) - 1] += 1;
			this.dados.base[next(dezena, (byte) -1) - 1] += 15;
			this.dados.base[next(dezena, (byte) -2) - 1] += 11;
			this.dados.base[next(dezena, (byte) -3) - 1] += 8;
			this.dados.base[next(dezena, (byte) -4) - 1] += 6;
			this.dados.base[next(dezena, (byte) -5) - 1] += 4;
			this.dados.base[next(dezena, (byte) -6) - 1] += 3;
			this.dados.base[next(dezena, (byte) -7) - 1] += 2;
			this.dados.base[next(dezena, (byte) -8) - 1] += 1;
		}
	}

	public byte next(byte base, byte incremento) {
		return (byte) ((base + incremento + 60 - 1) % 60 + 1);
	}
}
