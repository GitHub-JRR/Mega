package MegaPck;

/*
1) indices de determina��o - s�o valores estatisticos que ajudam a determinar a dist�ncia do ponto de equilibrio. Utilizados na classifica��o das dezenas.
2) indices de alocacao - s�o valores que determinam a posicao dos valores em relacao ao ponto de equilibrio distributivo. Utilizados na distribui��o das dezenas.


1 ) estrategia distancia mutua entre os maiores e os menores (desvio da media)
2 ) estrat�gia do equilibrio definitivo, onde � medida a distancia para o maior de modo a se obter o equilibrio definitivo.

Os indices podem ser de:
a) influencia direta, quando alteram alem de seu balanciamento o balanciamento de outros indices.
B) indices colaterais s�o os indices que s�o diretamente infunciados por outros indices.
*/
public abstract class AbstractIndice {

	public BaseIndice dados;
	public BaseIndice sub1;
	public BaseIndice sub2;
	public String nomeDados;
	public String nomeSub1;
	public String nomeSub2;
	int qtdSubIndices;
	public boolean indiceDeFormula;

	AbstractIndice() {
		this.nomeDados = "";
		this.nomeSub1 = "";
		this.nomeSub2 = "";
		this.dados = new BaseIndice();
		this.qtdSubIndices = 0;
		this.indiceDeFormula = true;
	}
	/*
	 * public IndiceAbstract clone(IndiceAbstract indiceOrigem, IndiceAbstract
	 * indiceDestino) { indiceDestino.dados=indiceOrigem.dados.clone();
	 * indiceDestino.sub1=indiceOrigem.sub1.clone();
	 * indiceDestino.sub2=indiceOrigem.sub2.clone();
	 * indiceDestino.nomeDados=indiceOrigem.nomeDados;
	 * indiceDestino.nomeSub1=indiceOrigem.nomeSub1;
	 * indiceDestino.nomeSub2=indiceOrigem.nomeSub2;
	 * indiceDestino.qtdSubIndices=indiceOrigem.qtdSubIndices;
	 * 
	 * 
	 * return indiceDestino;
	 * 
	 * }
	 * 
	 * 
	 * public IdxMaiores clone(IdxMaiores indiceOrigem, IdxMaiores copia) {
	 * copia.dados=indiceOrigem.dados.clone();
	 * copia.nomeDados=indiceOrigem.nomeDados; copia.nomeSub1=indiceOrigem.nomeSub1;
	 * copia.nomeSub2=indiceOrigem.nomeSub2;
	 * copia.qtdSubIndices=indiceOrigem.qtdSubIndices;
	 * 
	 * 
	 * return copia; }
	 * 
	 * public IdxCol clone(IdxCol indiceOrigem, IdxCol copia) {
	 * copia.dados=indiceOrigem.dados.clone();
	 * copia.nomeDados=indiceOrigem.nomeDados; copia.nomeSub1=indiceOrigem.nomeSub1;
	 * copia.nomeSub2=indiceOrigem.nomeSub2;
	 * copia.qtdSubIndices=indiceOrigem.qtdSubIndices;
	 * 
	 * 
	 * return copia; }
	 * 
	 * public IdxMonteCarlo clone(IdxMonteCarlo indiceOrigem, IdxMonteCarlo copia) {
	 * copia.dados=indiceOrigem.dados.clone();
	 * copia.nomeDados=indiceOrigem.nomeDados; copia.nomeSub1=indiceOrigem.nomeSub1;
	 * copia.nomeSub2=indiceOrigem.nomeSub2;
	 * copia.qtdSubIndices=indiceOrigem.qtdSubIndices;
	 * 
	 * 
	 * return copia; }
	 * 
	 * public IdxParImp clone(IdxParImp indiceOrigem, IdxParImp copia) {
	 * copia.dados=indiceOrigem.dados.clone();
	 * copia.nomeDados=indiceOrigem.nomeDados; copia.nomeSub1=indiceOrigem.nomeSub1;
	 * copia.nomeSub2=indiceOrigem.nomeSub2;
	 * copia.qtdSubIndices=indiceOrigem.qtdSubIndices;
	 * 
	 * 
	 * return copia; }
	 * 
	 * public IdxLin clone(IdxLin indiceOrigem, IdxLin copia) {
	 * copia.dados=indiceOrigem.dados.clone();
	 * copia.nomeDados=indiceOrigem.nomeDados; copia.nomeSub1=indiceOrigem.nomeSub1;
	 * copia.nomeSub2=indiceOrigem.nomeSub2;
	 * copia.qtdSubIndices=indiceOrigem.qtdSubIndices;
	 * 
	 * 
	 * return copia; }
	 * 
	 * public IdxUltPQtd clone(IdxUltPQtd indiceOrigem, IdxUltPQtd copia) {
	 * copia.dados=indiceOrigem.dados.clone(); copia.sub1=indiceOrigem.sub1.clone();
	 * copia.nomeDados=indiceOrigem.nomeDados; copia.nomeSub1=indiceOrigem.nomeSub1;
	 * copia.nomeSub2=indiceOrigem.nomeSub2;
	 * copia.qtdSubIndices=indiceOrigem.qtdSubIndices;
	 * 
	 * 
	 * return copia; }
	 * 
	 * public IdxQuad clone(IdxQuad indiceOrigem, IdxQuad copia) {
	 * copia.dados=indiceOrigem.dados.clone();
	 * copia.nomeDados=indiceOrigem.nomeDados; copia.nomeSub1=indiceOrigem.nomeSub1;
	 * copia.nomeSub2=indiceOrigem.nomeSub2;
	 * copia.qtdSubIndices=indiceOrigem.qtdSubIndices;
	 * 
	 * 
	 * return copia; }
	 * 
	 * public IdxUltSorteio clone(IdxUltSorteio indiceOrigem, IdxUltSorteio copia) {
	 * copia.dados=indiceOrigem.dados.clone(); copia.sub1=indiceOrigem.sub1.clone();
	 * copia.sub2=indiceOrigem.sub2.clone(); copia.nomeDados=indiceOrigem.nomeDados;
	 * copia.nomeSub1=indiceOrigem.nomeSub1; copia.nomeSub2=indiceOrigem.nomeSub2;
	 * copia.qtdSubIndices=indiceOrigem.qtdSubIndices;
	 * 
	 * 
	 * 
	 * return copia; }
	 * 
	 * public IdxCiclo clone(IdxCiclo indiceOrigem, IdxCiclo copia) {
	 * copia.dados=indiceOrigem.dados.clone();
	 * copia.nomeDados=indiceOrigem.nomeDados; copia.nomeSub1=indiceOrigem.nomeSub1;
	 * copia.nomeSub2=indiceOrigem.nomeSub2;
	 * copia.qtdSubIndices=indiceOrigem.qtdSubIndices;
	 * 
	 * 
	 * return copia; }
	 */

	AbstractIndice(final byte qtdSub) {
		this.nomeDados = "";
		this.nomeSub1 = "";
		this.nomeSub2 = "";
		this.dados = new BaseIndice();
		this.qtdSubIndices = 0;
		this.indiceDeFormula = true;
		if (qtdSub >= 1) {
			this.sub1 = new BaseIndice();
			this.qtdSubIndices = 1;
		}
		if (qtdSub == 2) {
			this.sub2 = new BaseIndice();
			this.qtdSubIndices = 2;
		}
	}

	public void add(final Sorteio sorteio) {
		this.soAdd(sorteio);
		this.dados.recalcule();
	}

	public void addValorBaseDaDezena(final byte dezena, final int valor) {
		this.dados.base[dezena - 1] += valor;
	}

	public byte getDezenaDoRank(final byte i) {
		return this.getDezenaDoRank(i, TipoIndice.TOPO);
	}

	public byte getDezenaDoRank(final byte classificacao, final TipoIndice tipo) {
		if (tipo == TipoIndice.TOPO) {
			return (byte) (this.dados.rank[classificacao - 1] + 1);
		} else if (tipo == TipoIndice.BASE) {
			return (byte) (this.dados.rank[61 - classificacao - 1] + 1);
		} else {
			return (byte) (this.dados.rank[Math
					.abs((61 - (classificacao) * 2) + (((61 - (classificacao) * 2)) <= 0 ? 1 : 0))] + 1);
		}
	}

	public byte getDezenaDoRank(final byte classificacao, final TipoIndice tipo, final byte numeroSub) {
		if (numeroSub == 1) {
			if (tipo == TipoIndice.TOPO) {
				return (byte) (this.sub1.rank[classificacao - 1] + 1);
			} else if (tipo == TipoIndice.BASE) {
				return (byte) (this.sub1.rank[61 - classificacao - 1] + 1);
			} else {
				return (byte) (this.sub1.rank[Math
						.abs((61 - (classificacao) * 2) + (((61 - (classificacao) * 2)) <= 0 ? 1 : 0))] + 1);
			}
		}
		if (numeroSub == 2) {
			if (tipo == TipoIndice.TOPO) {
				return (byte) (this.sub2.rank[classificacao - 1] + 1);
			} else if (tipo == TipoIndice.BASE) {
				return (byte) (this.sub2.rank[61 - classificacao - 1] + 1);
			} else {
				return (byte) (this.sub2.rank[Math
						.abs((61 - (classificacao) * 2) + (((61 - (classificacao) * 2)) <= 0 ? 1 : 0))] + 1);
			}
		}
		if (numeroSub == 0) {
			return this.getDezenaDoRank(classificacao, tipo);
		}
		return 0;
	}

	public float getIndice(final byte dezena) {
		return this.getIndice(dezena, TipoIndice.TOPO);
	}

	//// ---- top,centro,base;
	//// ---- 1 2 3
	public float getIndice(final byte dezena, final byte tipo) {
		if (tipo == 1) {
			return this.dados.indice[dezena - 1];
		} else if (tipo == 2) {
			return 1 - this.dados.indice[dezena - 1];
			// } else if (tipo==3 ) {
		} else {
			return Math.abs(2 * this.dados.indice[dezena - 1] - 1);
		}
	};

	public float getIndice(final byte dezena, final byte tipo, final int numeroSub) {
		if (numeroSub == 1) {
			if (tipo == 1) {
				return this.sub1.indice[dezena - 1];
			} else if (tipo == 2) {
				return 1 - this.sub1.indice[dezena - 1];
				// } else if (tipo==3 ) {
			} else {
				return Math.abs(2 * this.sub1.indice[dezena - 1] - 1);
			}
		}
		if (numeroSub == 2) {
			if (tipo == 1) {
				return this.sub2.indice[dezena - 1];
			} else if (tipo == 2) {
				return 1 - this.sub2.indice[dezena - 1];
				// } else if (tipo==3 ) {
			} else {
				return Math.abs(2 * this.sub2.indice[dezena - 1] - 1);
			}
		} else {
			return 0;
		}
	}

	public float getIndice(final byte dezena, final TipoIndice tipo) {
		if (tipo == TipoIndice.TOPO) {
			return this.dados.indice[dezena - 1];
		} else if (tipo == TipoIndice.BASE) {
			return 1 - this.dados.indice[dezena - 1];
		} else if (tipo == TipoIndice.CENTRO) {
			return Math.abs(2 * this.dados.indice[dezena - 1] - 1);
		}
		return 0;
	}

	public float getIndice(final byte dezena, final TipoIndice tipo, final byte numeroSub) {
		if (numeroSub == 1) {
			if (tipo == TipoIndice.TOPO) {
				return this.sub1.indice[dezena - 1];
			} else if (tipo == TipoIndice.BASE) {
				return 1 - this.sub1.indice[dezena - 1];
			} else if (tipo == TipoIndice.CENTRO) {
				return Math.abs(2 * this.sub1.indice[dezena - 1] - 1);
			}
		}
		if (numeroSub == 2) {
			if (tipo == TipoIndice.TOPO) {
				return this.sub2.indice[dezena - 1];
			} else if (tipo == TipoIndice.BASE) {
				return 1 - this.sub2.indice[dezena - 1];
			} else if (tipo == TipoIndice.CENTRO) {
				return Math.abs(2 * this.sub2.indice[dezena - 1] - 1);
			}
		}
		return 0;
	}

	public int getRankDaDezena(final byte dezena) {
		return this.getRankDaDezena(dezena, TipoIndice.TOPO);
	}

	public int getRankDaDezena(final byte dezena, final TipoIndice tipo) {
		if (tipo == TipoIndice.TOPO | tipo == TipoIndice.BASE) {
			return this.dados.classificacao[dezena - 1];
		} else {
			return Math.abs((61 - (this.dados.classificacao[dezena - 1]) * 2))
					+ (((61 - (this.dados.classificacao[dezena - 1]) * 2)) <= 0 ? 1 : 0);
		}
	}

	public int getRankDaDezena(final byte dezena, final TipoIndice tipo, final byte numeroSub) {
		if (numeroSub == 1) {
			if (tipo == TipoIndice.TOPO) {
				return this.sub1.classificacao[dezena - 1];
			} else if (tipo == TipoIndice.BASE) {
				return this.sub1.classificacao[60 - dezena - 1];
			} else {
				return Math.abs((61 - (this.sub1.classificacao[dezena - 1]) * 2))
						+ (((61 - (this.sub1.classificacao[dezena - 1]) * 2)) <= 0 ? 1 : 0);
			}
		}
		if (numeroSub == 2) {
			if (tipo == TipoIndice.TOPO) {
				return this.sub2.classificacao[dezena - 1];
			} else if (tipo == TipoIndice.BASE) {
				return this.sub2.classificacao[60 - dezena - 1];
			} else {
				return Math.abs((61 - (this.sub2.classificacao[dezena - 1]) * 2))
						+ (((61 - (this.sub2.classificacao[dezena - 1]) * 2)) <= 0 ? 1 : 0);
			}
		}
		return 0;
	}

	public int getValorBaseDaDezena(final byte dezena) {
		return this.getValorBaseDaDezena(dezena, (byte) 0);
	}

	public int getValorBaseDaDezena(final byte dezena, final byte subIndice) {
		if (subIndice == 0) {
			return this.dados.base[dezena - 1];
		}
		if (subIndice == 1) {
			return this.sub1.base[dezena - 1];
		}
		if (subIndice == 2) {
			return this.sub2.base[dezena - 1];
		}
		return 0;
	}

	public void incrementaBaseDaDezena(final byte dezena) {
		this.dados.base[dezena - 1]++;
	}

	public int maior() {
		return this.dados.maiorValor();
	}

	public int menor() {
		return this.dados.menorValor();
	}

	public void print() {
		this.dados.print(this.nomeDados);
	}

	public void print(final String nomeClasse) {
		this.dados.print(this.nomeDados, nomeClasse);
	}

	public void print(final String nome, final String nomeVar) {
		this.dados.print(nome, nomeVar);
	}

	public void printSub1() {
		this.sub1.print(this.nomeSub1);
	}

	public void printSub1(final String nomeClasse) {
		this.sub1.print(this.nomeSub1, nomeClasse);
	}

	public void printSub1(final String nome, final String nomeVar) {
		this.sub1.print(nome, nomeVar);
	}

	public void printSub2() {
		this.sub2.print(this.nomeSub2);
	}

	public void printSub2(final String nomeClasse) {
		this.sub2.print(this.nomeSub2, nomeClasse);
	}

	public void printSub2(final String nome, final String nomeVar) {
		this.sub2.print(nome, nomeVar);
	}

	public void recalcule() {
		this.dados.recalcule();
		if (this.qtdSubIndices >= 1) {
			this.sub1.recalcule();
		}
		if (this.qtdSubIndices == 2) {
			this.sub2.recalcule();
		}
	}

	public void reset() {
		this.dados = new BaseIndice();
	}

	public void setName(final String nome) {
		this.nomeDados = nome;
	}

	public void setNameSub1(final String nome) {
		this.nomeSub1 = nome;
	}

	public void setNameSub2(final String nome) {
		this.nomeSub2 = nome;
	}

	public void setValorBaseDaDezena(final byte dezena, final int valor) {
		this.setValorBaseDaDezena(dezena, valor, (byte) 0);
	}

	public void setValorBaseDaDezena(final byte dezena, final int valor, final byte subIndice) {
		if (subIndice == 0) {
			this.dados.base[dezena - 1] = valor;
		}
		if (subIndice == 1) {
			this.sub1.base[dezena - 1] = valor;
		}
		if (subIndice == 2) {
			this.sub2.base[dezena - 1] = valor;
		}
	}

	public void soAdd(final Sorteio sorteio) {
	}
}
