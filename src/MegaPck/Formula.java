package MegaPck;

public class Formula {

	boolean filtro;
	Componente maiores;
	Componente ultSorteio;
	Componente ultPQtdSB1;
	Componente parImp;
	Componente ciclo;
	Componente ultPQtd;
	Componente ultSorteioSB1;
	Componente ultSorteioSB2;
	Componente monteCarlo;
	Componente drop;
	public long base; // Como a base � armazenada em 5 bits e 2 bits s�o para gravar o tipo (
				// 0-TOPO,1-CENTRO,2-BASE ) restam 3 bits para o fator, que poder� ser de 0 a 7)

	public Formula() {
		this.filtro = false;
		this.maiores = new Componente();
		this.ultSorteio = new Componente();
		this.ultPQtdSB1 = new Componente();
		this.parImp = new Componente();
		this.ciclo = new Componente();
		this.ultPQtd = new Componente();
		this.ultSorteioSB1 = new Componente();
		this.ultSorteioSB2 = new Componente();
		this.monteCarlo = new Componente();
		this.drop = new Componente();
	}

	public Formula(final boolean filtro, final Componente maiores, final Componente ultSorteio,
			final Componente ultPQtdSB1, final Componente parImp, final Componente ciclo, final Componente ultPQtd,
			final Componente ultSorteioSB1, final Componente ultSorteioSB2, final Componente monteCarlo,
			final Componente drop) {
		this.filtro = filtro; // Com filtro de Darhal = true
		this.maiores = maiores;
		this.ultSorteio = ultSorteio;
		this.ultPQtdSB1 = ultPQtdSB1;
		this.parImp = parImp;
		this.ciclo = ciclo;
		this.ultPQtd = ultPQtd;
		this.ultSorteioSB1 = ultSorteioSB1;
		this.ultSorteioSB2 = ultSorteioSB2;
		this.monteCarlo = monteCarlo;
		this.drop = drop;
		this.base = (filtro ? 1L << 50 : 0);
		this.base += (long) drop.base << 45;
		this.base += (long) monteCarlo.base << 40;
		this.base += (long) maiores.base << 35;
		this.base += (long) ultSorteio.base << 30;
		this.base += (long) ultPQtdSB1.base << 25;
		this.base += (long) parImp.base << 20;
		this.base += (long) ciclo.base << 15;
		this.base += (long) ultPQtd.base << 10;
		this.base += (long) ultSorteioSB1.base << 5;
		this.base += ultSorteioSB2.base;
	}

	public Formula(final long base) {
		this.filtro = ((base >> 50) == 1 ? true : false);
		this.drop = new Componente((byte) ((base >> 45) & 0b11111));
		this.monteCarlo = new Componente((byte) ((base >> 40) & 0b11111));
		this.maiores = new Componente((byte) ((base >> 35) & 0b11111));
		this.ultSorteio = new Componente((byte) ((base >> 30) & 0b11111));
		this.ultPQtdSB1 = new Componente((byte) ((base >> 25) & 0b11111));
		this.parImp = new Componente((byte) ((base >> 20) & 0b11111));
		this.ciclo = new Componente((byte) ((base >> 15) & 0b11111));
		this.ultPQtd = new Componente((byte) ((base >> 10) & 0b11111));
		this.ultSorteioSB1 = new Componente((byte) ((base >> 5) & 0b11111));
		this.ultSorteioSB2 = new Componente((byte) ((base) & 0b11111));
		this.base = base;
	}

	public Formula(final long base, final boolean v74) {
		this.filtro = ((base >> 45) == 1 ? true : false);
		this.monteCarlo = new Componente((byte) ((base >> 40) & 0b11111));
		this.maiores = new Componente((byte) ((base >> 35) & 0b11111));
		this.ultSorteio = new Componente((byte) ((base >> 30) & 0b11111));
		this.ultPQtdSB1 = new Componente((byte) ((base >> 25) & 0b11111));
		this.parImp = new Componente((byte) ((base >> 20) & 0b11111));
		this.ciclo = new Componente((byte) ((base >> 15) & 0b11111));
		this.ultPQtd = new Componente((byte) ((base >> 10) & 0b11111));
		this.ultSorteioSB1 = new Componente((byte) ((base >> 5) & 0b11111));
		this.ultSorteioSB2 = new Componente((byte) ((base) & 0b11111));
		this.drop = new Componente();
		this.base = (this.filtro ? 1L << 50 : 0);
		this.base += (long) this.drop.base << 45;
		this.base += (long) this.monteCarlo.base << 40;
		this.base += (long) this.maiores.base << 35;
		this.base += (long) this.ultSorteio.base << 30;
		this.base += (long) this.ultPQtdSB1.base << 25;
		this.base += (long) this.parImp.base << 20;
		this.base += (long) this.ciclo.base << 15;
		this.base += (long) this.ultPQtd.base << 10;
		this.base += (long) this.ultSorteioSB1.base << 5;
		this.base += this.ultSorteioSB2.base;
	}

	public void clear() {
		this.filtro = false;
		this.maiores.clear();
		this.ultSorteio.clear();
		this.ultPQtdSB1.clear();
		this.parImp.clear();
		this.ciclo.clear();
		this.ultPQtd.clear();
		this.ultSorteioSB1.clear();
		this.ultSorteioSB2.clear();
		this.monteCarlo.clear();
		this.drop.clear();
	}

	public void copy(final Formula formula) {
		this.filtro = formula.filtro;
		this.maiores.copy(formula.maiores);
		this.ultSorteio.copy(formula.ultSorteio);
		this.ultPQtdSB1.copy(formula.ultPQtdSB1);
		this.parImp.copy(formula.parImp);
		this.ciclo.copy(formula.ciclo);
		this.ultPQtd.copy(formula.ultPQtd);
		this.ultSorteioSB1.copy(formula.ultSorteioSB1);
		this.ultSorteioSB2.copy(formula.ultSorteioSB2);
		this.monteCarlo.copy(formula.monteCarlo);
		this.drop.copy(formula.drop);
	}

	public void inverteFiltro() {
		this.base ^= 1L << 50;
		this.filtro = !this.filtro;
	}

	public boolean isFiltro() {
		return this.filtro;
	}
	public int indiceFiltro() {
		return this.isFiltro()?SoProcessa.Tipo.ComDarhal.indice():SoProcessa.Tipo.SemDarhal.indice();
	}

	public void resetFiltro() {
		this.base &= ~(1L << 50);
		this.filtro = false; // Sem filtro de Darhal
	}

	public void setFiltro() {
		this.base |= 1L << 50;
		this.filtro = true; // Com filtro de Darhal
	}

	@Override
	public String toString() {
		final String retorno = (this.filtro ? "Darhal( " : "        ") + " ("
				+ (this.drop.getPeso() > 0 ? this.drop.toStringDividendo("drop") + "+" : "")
				+ (this.monteCarlo.getPeso() > 0 ? this.monteCarlo.toStringDividendo("monteCarlo") + "+" : "")
				+ (this.maiores.getPeso() > 0 ? this.maiores.toStringDividendo("maiores") + "+" : "")
				+ (this.ultSorteio.getPeso() > 0 ? this.ultSorteio.toStringDividendo("ultSorteio") + "+" : "")
				+ (this.ultPQtdSB1.getPeso() > 0 ? this.ultPQtdSB1.toStringDividendo("ultPQtdSB1") + "+" : "")
				+ (this.parImp.getPeso() > 0 ? this.parImp.toStringDividendo("parImp") + "+" : "")
				+ (this.ciclo.getPeso() > 0 ? this.ciclo.toStringDividendo("ciclo") + "+" : "")
				+ (this.ultPQtd.getPeso() > 0 ? this.ultPQtd.toStringDividendo("ultPQtd") + "+" : "")
				+ (this.ultSorteioSB1.getPeso() > 0 ? this.ultSorteioSB1.toStringDividendo("ultSorteioSB1") + "+" : "")
				+ (this.ultSorteioSB2.getPeso() > 0 ? this.ultSorteioSB2.toStringDividendo("ultSorteioSB2") : "")
				+ " / (" + (this.drop.getPeso() > 0 ? this.drop.toStringDivisor() + "+" : "")
				+ (this.monteCarlo.getPeso() > 0 ? this.monteCarlo.toStringDivisor() + "+" : "")
				+ (this.maiores.getPeso() > 0 ? this.maiores.toStringDivisor() + "+" : "")
				+ (this.ultSorteio.getPeso() > 0 ? this.ultSorteio.toStringDivisor() + "+" : "")
				+ (this.ultPQtdSB1.getPeso() > 0 ? this.ultPQtdSB1.toStringDivisor() + "+" : "")
				+ (this.parImp.getPeso() > 0 ? this.parImp.toStringDivisor() + "+" : "")
				+ (this.ciclo.getPeso() > 0 ? this.ciclo.toStringDivisor() + "+" : "")
				+ (this.ultPQtd.getPeso() > 0 ? this.ultPQtd.toStringDivisor() + "+" : "")
				+ (this.ultSorteioSB1.getPeso() > 0 ? this.ultSorteioSB1.toStringDivisor() + "+" : "")
				+ (this.ultSorteioSB2.getPeso() > 0 ? this.ultSorteioSB2.toStringDivisor() : "") + " ) "
				+ (this.filtro ? " )" : "");
		final String fora = ((this.drop.getPeso() == 0 | this.monteCarlo.getPeso() == 0 | this.maiores.getPeso() == 0
				| this.ultSorteio.getPeso() == 0 | this.ultPQtdSB1.getPeso() == 0 | this.parImp.getPeso() == 0
				| +this.ciclo.getPeso() == 0 | this.ultPQtd.getPeso() == 0 | this.ultSorteioSB1.getPeso() == 0
				| this.ultSorteioSB2.getPeso() == 0)
						? (" Indices fora : " + (this.drop.getPeso() == 0 ? "drop - " : "")
								+ (this.monteCarlo.getPeso() == 0 ? "monteCarlo - " : "")
								+ (this.maiores.getPeso() == 0 ? "maiores - " : "")
								+ (this.ultSorteio.getPeso() == 0 ? "ultSorteio - " : "")
								+ (this.ultPQtdSB1.getPeso() == 0 ? "ultPQtdSB1 - " : "")
								+ (this.parImp.getPeso() == 0 ? "parImp - " : "")
								+ (this.ciclo.getPeso() == 0 ? "ciclo - " : "")
								+ (this.ultPQtd.getPeso() == 0 ? "ultPQtd - " : "")
								+ (this.ultSorteioSB1.getPeso() == 0 ? "ultSorteioSB1 - " : "")
								+ (this.ultSorteioSB2.getPeso() == 0 ? "ultSorteioSB2 - " : ""))
						: "");
		return (retorno + fora);
	}
}
