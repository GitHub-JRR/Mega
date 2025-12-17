package MegaPck;

public class IdxMonteCarlo extends AbstractIndice {

	private int[][] matrizMonteCarlo;

	IdxMonteCarlo() {
		super();
		matrizMonteCarlo = new int[6][60];
		for (int dezena = 0; dezena < 6; dezena++) {
			for (int dezenas = 0; dezenas < 60; dezenas++) {
				matrizMonteCarlo[dezena][dezenas] = 0;
			}
		}
	}
	/*
	 * public IdxMonteCarlo clone() { IdxMonteCarlo copia = new IdxMonteCarlo();
	 * copia = super.clone(this, copia) ; for (int dezena=0 ;dezena<6;dezena++) {
	 * for (int dezenas=0 ;dezenas<60;dezenas++) {
	 * copia.matrizMonteCarlo[dezena][dezenas]=this.matrizMonteCarlo[dezena][dezenas
	 * ]; } } return copia; }
	 */

	public void recalcule() {
		int[][] matrizMonteCarloClone = new int[6][60];
		for (byte linhaDezena = 0; linhaDezena < 6; linhaDezena++) {
			for (byte colunaDezenas = 0; colunaDezenas < 60; colunaDezenas++) {
				matrizMonteCarloClone[linhaDezena][colunaDezenas] = this.matrizMonteCarlo[linhaDezena][colunaDezenas];
			}
		}
		int maior;
		int indiceDezena;
		int ordenador = 60;
		for (byte serieRota = 0; serieRota < 10; serieRota++) {
			for (byte linhaDezena = 0; linhaDezena < 6; linhaDezena++) {
				indiceDezena = 0;
				maior = 0;
				for (byte colunaDezenas = 0; colunaDezenas < 60; colunaDezenas++) {
					if (matrizMonteCarloClone[linhaDezena][colunaDezenas] > maior) {
						maior = matrizMonteCarloClone[linhaDezena][colunaDezenas];
						indiceDezena = colunaDezenas;
					}
				}
				setValorBaseDaDezena((byte) (indiceDezena + 1), ((ordenador-- * 100) + maior));
				// System.out.println("registrei a dezena "+(indiceDezena+1)+ " com o valor "+
				// (( ordenador * 100 ) + maior ));
				// ordenador--;
				for (byte dezenaB = 0; dezenaB < 6; dezenaB++) {
					matrizMonteCarloClone[dezenaB][indiceDezena] = 0;
				}
			}

		}

	}

	@Override
	public void soAdd(Sorteio sorteio) {

		for (byte indiceDezena = 0; indiceDezena < 6; indiceDezena++) {
			matrizMonteCarlo[indiceDezena][sorteio.get()[indiceDezena] - 1]++;
		}
		recalcule();
	}

	@Override
	public void print(String nomeClasse) {
		super.print(nomeClasse);
		System.out.println("");
		System.out.print("   ");
		for (int dezenas = 1; dezenas <= 60; dezenas++) {
			System.out.print("=" + String.format("%02d", dezenas) + "= ");
		}
		System.out.print("Total\n");
		int tot;
		for (byte dezena = 0; dezena < 6; dezena++) {
			tot = 0;
			System.out.print(String.format("%02d ", dezena + 1));
			for (byte dezenas = 0; dezenas < 60; dezenas++) {
				System.out.print(String.format("%04d ", matrizMonteCarlo[dezena][dezenas]));
				tot += matrizMonteCarlo[dezena][dezenas];
			}
			System.out.print(String.format("%05d ", tot) + "\n");
		}
		System.out.println("");

	}

}
