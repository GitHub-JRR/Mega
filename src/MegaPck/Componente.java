package MegaPck;

public class Componente {
	public byte base;

	public Componente(byte tipoInterno, byte pesoInterno) {
		this.base = (byte) (tipoInterno + (pesoInterno << 2));
	}

	public Componente(byte base) {
		this.base = base;
	}

	public byte getTipo() {
		return (byte) (this.base & 0b11);
	}

	public byte getPeso() {
		return (byte) (this.base >> 2);
	}

	public Componente() {
		this.base = 1;
	}

	public void clear() {
		this.base = 1;
	}

	public void copy(Componente componente) {
		this.base = componente.base;
	}

	public String codIndice() {
		if (getPeso() == 0) {
			return "           ";
		} else if (getPeso() == 1) {
			return String.format("%3d        ", getTipo());

		} else {
			return String.format("%3d %3d    ", getTipo(), getPeso());
		}
	}

	public String toStringDividendo(String nome) {
		//// ---- top,centro,base;
		//// ---- 0 1 2
		if (getPeso() == 0) {
			return " ";
		}
		return (getPeso() == 1 ? "" : getPeso() + "*") + nome + "("
				+ (getTipo() == 0 ? "Topo" : (getTipo() == 1 ? "Centro" : (getTipo() == 2 ? "Base" : ""))) + ")";

	}

	public String toStringDivisor() {
		//// ---- top,centro,base;
		//// ---- 0 1 2
		if (getPeso() == 0) {
			return " ";
		}
		return "" + getPeso();

	}
}
