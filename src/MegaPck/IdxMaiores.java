package MegaPck;

public class IdxMaiores extends AbstractIndice {

	IdxMaiores() {
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
		}
	}
}
