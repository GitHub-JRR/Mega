package MegaPck;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class ProcSorteio {

	byte profundidade;
	byte numMaxDezenas;
	byte[] sorteioAtual;
	short numSorteio;
	Colecao colecao;
	Set<Long> iC = new HashSet<>();
	Set<Long> indexCalibragem = Collections.synchronizedSet(this.iC);
	double rateioSena;
	double rateioQuadra;
	double rateioQuina;
	List<Calibragem> rankCalibragemFinal;
	final byte[][] indice = { { 0, 0, 1 }, { 0, 1, 0 }, { 0, 1, 1 }, { 1, 0, 0 }, { 1, 0, 1 }, { 1, 1, 0 }, { 0, 0, 2 },
			{ 0, 2, 0 }, { 0, 2, 2 }, { 2, 0, 0 }, { 2, 0, 2 }, { 2, 2, 0 } };
	// final byte[][] multiplicadorQuadra = { { 6, 1 }, { 7, 3 }, { 8, 6 }, { 9, 10
	// }, { 10, 15 }, { 11, 21 }, { 12, 28 },
	// { 13, 36 }, { 14, 45 }, { 15, 55 } };
	// final short[][] multiplicadorQuina = { { 6, 1, 0 }, { 7, 2, 5 }, { 8, 3, 15
	// }, { 9, 4, 30 }, { 10, 5, 50 },
	// { 11, 6, 75 }, { 12, 7, 105 }, { 13, 8, 140 }, { 14, 9, 180 }, { 15, 10, 225
	// } };
	// final short[][] multiplicadorSena = { { 6, 0, 0 }, { 7, 6, 0 }, { 8, 12, 15
	// }, { 9, 18, 45 }, { 10, 24, 90 },
	// { 11, 30, 150 }, { 12, 36, 225 }, { 13, 42, 315 }, { 14, 48, 420 }, { 15, 54,
	// 540 } };

	public ProcSorteio() {
		super();
	}

	public ProcSorteio(final byte profundidade1, final byte numMaxDezenas1, final byte[] sorteioAtual,
			final short numSorteio1, final Colecao colecao1, final double rateioSena1, final double rateioQuina1,
			final double rateioQuadra1) {
		super();
		this.profundidade = profundidade1;
		this.numMaxDezenas = numMaxDezenas1;
		this.sorteioAtual = sorteioAtual;
		this.numSorteio = numSorteio1;
		this.colecao = colecao1;
		this.rateioSena = rateioSena1;
		this.rateioQuina = rateioQuina1;
		this.rateioQuadra = rateioQuadra1;
	}

	public void call() throws InterruptedException, ExecutionException {
		try {
			final int qtdProcPar = 4;
			NumberFormat defaultFormat;
			defaultFormat = NumberFormat.getCurrencyInstance();
			final DecimalFormatSymbols decimalFormatSymbols = ((DecimalFormat) defaultFormat).getDecimalFormatSymbols();
			decimalFormatSymbols.setCurrencySymbol("");
			((DecimalFormat) defaultFormat).setDecimalFormatSymbols(decimalFormatSymbols);
			List<Future<Void>> futures;
			final ExecutorService eProcSorteio = Executors.newFixedThreadPool(qtdProcPar);
			this.indexCalibragem.clear();
			futures = new ArrayList<>();
			final Future<Void> resultadoProc = null;
			@SuppressWarnings("unused")
			final SoProcessa baseProcessamento = new SoProcessa(this.colecao, this.rankCalibragemFinal,
					this.sorteioAtual, this.numSorteio, this.indexCalibragem, this.indice);// ,
																							// this.multiplicadorQuadra,
			// this.multiplicadorQuina, this.multiplicadorSena);
			// final SoProcessa processador;
			for (int cursorFormulas = 0; cursorFormulas < this.rankCalibragemFinal.size(); cursorFormulas += 2) {
				final Future<Void> resultadoExec = eProcSorteio.submit(new SoProcessa(cursorFormulas));
				futures.add(resultadoExec);
				if (cursorFormulas % 50 == 0) {
					this.coleteParcial(futures, resultadoProc);
					GerenciadorMensagens.verificaHibernacaoSetRegTempo();
				}
			}
			eProcSorteio.shutdown();
			this.colete(futures);
			eProcSorteio.shutdownNow();
			return;
		} finally {
			// System.out.println("Thread "+Thread.currentThread().getId()+" - Executado o
			// processamento de todos sorteios....");
		}
	}

	private void colete(final List<Future<Void>> futures) throws InterruptedException, ExecutionException {
		Future<Void> resultadoProc;
		while (!futures.isEmpty()) {
			for (int n = 0; n < futures.size(); n++) {
				resultadoProc = futures.get(n);
				if (resultadoProc.isDone()) {
					futures.remove(n);
				} else if (resultadoProc.isCancelled()) {
					System.out.println("Tem resultado cancelado !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
					futures.remove(n);
				}
			}
		}
	}

	private void coleteParcial(final List<Future<Void>> futures, Future<Void> resultadoProc)
			throws InterruptedException, ExecutionException {
		while (!futures.isEmpty()) {
			if (futures.size() <= 16) {
				break;
			}
			for (int n = 0; n < futures.size(); n++) {
				resultadoProc = futures.get(n);
				if (resultadoProc.isDone()) {
					futures.remove(n);
				} else if (resultadoProc.isCancelled()) {
					System.out.println("Tem resultado cancelado !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
					futures.remove(n);
				}
			}
		}
	}
}