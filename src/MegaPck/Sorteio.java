package MegaPck;

public class Sorteio {
	byte[] dezenas;

	public Sorteio() {
		dezenas = new byte[6];
	}

	public void reg(byte[] sorteio) {
		this.dezenas = sorteio;
	}

	public byte[] get() {
		return this.dezenas;
	}

}