package MegaPck;

import java.util.ArrayList;
import java.util.List;

public class Colecao implements Cloneable {

	IdxMaiores idxMaiores;
	IdxUltSorteio idxUltSorteio;
	IdxCiclo idxCiclo;
	IdxUltPQtd idxUltPQtd;
	IdxQuad idxQuad;
	IdxLin idxLin;
	IdxParImp idxParImp;
	IdxCol idxCol;
	IdxMonteCarlo idxMonteCarlo;
	IdxDrop idxDrop;
	List<AbstractIndice> todos;
	int numeroDoUltimoSorteio;
	// Darhal darhal;

	// teste Git
	public Colecao() {
		this.idxMaiores = new IdxMaiores();
		this.idxUltSorteio = new IdxUltSorteio();
		this.idxCiclo = new IdxCiclo();
		this.idxUltPQtd = new IdxUltPQtd(1);
		this.idxQuad = new IdxQuad();
		this.idxLin = new IdxLin();
		this.idxParImp = new IdxParImp();
		this.idxCol = new IdxCol();
		this.idxDrop = new IdxDrop();
		this.idxMonteCarlo = new IdxMonteCarlo();
		this.idxMaiores.setName("indices das dezenas mais sorteadas");
		this.idxCiclo.setName("Ultimo Ciclo");
		this.idxUltSorteio.setName("indices das dezena sem sorteio");
		this.idxUltSorteio.setNameSub1("indices dos maiores intervalos sem sorteio");
		this.idxUltSorteio.setNameSub2("indices dos menores intervalos (entre os intervalos maiores) sem sorteio");
		this.idxUltPQtd.setName("indices da media dos intervalos nos ciclos de sorteios");
		this.idxUltPQtd.setNameSub1("Indice para comparacao com o indice ptMseq do Rank Geral");
		this.idxQuad.setName("Indice de aloca��o nos quadrantes");
		this.idxLin.setName("Indice de aloca��o nas linhas");
		this.idxParImp.setName("Indice de distribuicao entre pares e impares");
		this.idxCol.setName("Indice de aloca��o nas colunas");
		this.idxDrop.setName("Indice de registro da pegada da onda");
		this.idxMonteCarlo.setName("Indice de Monte Carlo");
		this.todos = new ArrayList<AbstractIndice>();
		this.todos.add(this.idxMaiores);
		this.todos.add(this.idxCiclo);
		this.todos.add(this.idxUltSorteio);
		this.todos.add(this.idxUltPQtd);
		this.todos.add(this.idxQuad);
		this.todos.add(this.idxLin);
		this.todos.add(this.idxParImp);
		this.todos.add(this.idxCol);
		this.todos.add(this.idxDrop);
		this.todos.add(this.idxMonteCarlo);
		this.numeroDoUltimoSorteio = 0;
		// this.darhal = new Darhal();
	}

	/*
	 * public Colecao clone() { Colecao copia = new Colecao(); copia.idxMaiores =
	 * this.idxMaiores.clone(); copia.idxCiclo = this.idxCiclo.clone();
	 * copia.idxUltSorteio = this.idxUltSorteio.clone(); copia.idxUltPQtd =
	 * this.idxUltPQtd.clone(); copia.idxQuad = this.idxQuad.clone(); copia.idxLin =
	 * this.idxLin.clone(); copia.idxParImp = this.idxParImp.clone(); copia.idxCol =
	 * this.idxCol.clone(); copia.idxMonteCarlo = this.idxMonteCarlo.clone();
	 * copia.todos = new ArrayList<IndiceAbstract>(); copia.todos.add(idxMaiores);
	 * copia.todos.add(idxCiclo); copia.todos.add(idxUltSorteio);
	 * copia.todos.add(idxUltPQtd); copia.todos.add(idxQuad);
	 * copia.todos.add(idxLin); copia.todos.add(idxParImp); copia.todos.add(idxCol);
	 * copia.todos.add(idxMonteCarlo);
	 * copia.numeroDoUltimoSorteio=this.numeroDoUltimoSorteio;
	 * copia.darhal=darhal.clone();
	 *
	 * return copia ; }
	 */
	public void add(final Sorteio sorteio) {
		this.idxMaiores.add(sorteio);
		this.idxUltSorteio.add(sorteio);
		this.idxCiclo.add(sorteio);
		this.idxUltSorteio.recalcule();
		this.idxCol.recalcule(this.idxMaiores);
		this.idxMonteCarlo.add(sorteio);
		this.idxLin.recalcule(this.idxMaiores);
		this.idxQuad.recalcule(this.idxMaiores);
		this.idxParImp.recalcule(this.idxMaiores);
		this.idxUltPQtd.recalcule(this.idxUltSorteio, this.idxMaiores);
		this.idxDrop.add(sorteio);
		// this.darhal.add(sorteio);
	}

	public void print() {
		for (final AbstractIndice indice : this.todos) {
			indice.print(indice.getClass().getName());
			if (indice.qtdSubIndices >= 1) {
				indice.printSub1("Sub1:" + indice.getClass().getName());
			}
			if (indice.qtdSubIndices >= 2) {
				indice.printSub2("Sub2:" + indice.getClass().getName());
			}
		}
		// this.darhal.print();
	}
}
