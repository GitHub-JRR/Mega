package MegaPck;

import java.io.BufferedReader;
import MegaPck.GerenciadorMensagens.ModoLimite;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashSet;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Set;
import java.util.TimeZone;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import org.tukaani.xz.*;

import MegaPck.GerenciadorMensagens.DestinoMensagem;

import org.json.JSONArray;
import org.json.JSONObject;

public class Sorteados {

	/**  
	 * Histórico de versões     
	 * Versão 01-0 de 13/08/2018 Levantamento de estatisticas  Par/Impar Colunals, Linhas e quadrantes     
	 * Versão 02-0 de 13/08/2018 Fazendo a leitura direto do HTML     
	 * Versão 03-0 de 14/08/2018 fazendo a leitura do zip file    
	 * Versão 04-0 de 14/08/2018 fazendo o download do zip file     
	 * Versão 05-0 de 14/08/2018 estatistica de qtd de sorteior que o numero nao e sorteado     
	 * Versão 06-0 de 15/08/2018 incluido Rank na  * Versão 05 
	 * Versão 07-0 de 15/08/2018 correção na abertura do HTML do Zip e não do disco + Media Intervalo na  * Versão 05    
	 * Versão 08-0 de 16/08/2018 acrescentei analise das quadras e trincas com sorteio	 * em ordem crescente 
	 * Versão 09-0 de 17/08/2018 foi incluida a analise de ciclos     
	 * Versão 10-0 de 18/08/2018 reformulei a lista dos maiores 
	 * Versão 11-0 de 18/08/2018 reformulei a estrutura de Rank 
	 * Versão 12-0 de 18/08/2018 reformulei a lista dos menores 
	 * Versão 13-0 de 19/08/2018 analise das dezenas no ultimo ciclo 
	 * Versão 14-0 de 19/08/2018 Alterado a formula do indice incluindo media ponderada (inicialmente indices =1)   
	 * Versão 15-0 de 19/08/2018 Incluido % em  relativo na coluna "sorteios p/N acertos e quebra por exemplo "   
	 * Versão 16-0 de 19/08/2018 Incluido pontuação na estatistica das colunas   
	 * Versão 17-0 de 26/08/2018 Montando matriz de combinacoes de 60 dezenas combinadas 3 a 3.  Fazendo pesquisa cruzada.   
	 * Versão 18-0 de 29/08/2018 Eliminando duplicidades.    
	 * Versão 19-0 de 05/09/2018 Com 1706 grupos fechados, vou paralelamente gravar a  matriz.   
	 * Versão 20-0 de 13/09/2018 Com leitura da Matriz_v475.txt. Procedimento  temporario enquanto fecho a Matriz.   
	 * Versão 21-0 de 13/09/2018 Analise dos  grupos / sorteios e definicao da melhor distribuicao.   
	 * Versão 22-0 de 15/09/2018 Separação de sorteios para apuração dos indice e para testes com os indices  determinados   
	 * Versão 23-0 de 24/09/2018 Calibragem dos fatores peso dos indices  para a amostra determinada.   
	 * Versão 24-0 de 24/09/2018 Apuracao dos palpites  para jogos com base nos fatores calibrados.   
	 * Versão 25-0 de 25/09/2018 Revisão  geral dos cálculos.   
	 * Versão 26-0 de 26/09/2018 Nova tecnica de calculo  classificando com os indices de determinação e desempatando com os indices de  alocação.   
	 * Versão 27-0 de 28/09/2018 Incluida nova rotina geradora de 1900  combinaçoes com as 20 series.   
	 * Versão 28-0 de 30/09/2018 Incluida função para  pegar a  * Versão do programa.   
	 * Versão 29-0 de 30/09/2018 Incluida função extarna  bat para registrar  * Versão base para alteraçao. Para testar como posição antes  da modificação.   
	 * Versão 30-0 de 30/09/2018 Alteracão no relatório  "Lista dos ate"   
	 * Versão 30-1 de 30/09/2018 Alteracão no relatório  "Lista dos ate" incluida informacao de qtd de itens na media   
	 * Versão 30-2 de 30/09/2018 Alteracão no relatório "Lista dos ate" incluida media direto na  classe Calibragem   
	 * Versão 31-0 de 30/09/2018 Captura de informaçoes do ultimo  sorteio.   
	 * Versão 32-0 de 01/10/2018 Criacao dos indices com a classificacao de  topo, centro e base   
	 * Versão 32-1 de 01/10/2018 Criacao do indice maiores    
	 * Versão 32-2 de 01/10/2018 Eliminacao estrutura maiores antiga 
	 * Versão 32-3 de 03/10/2018 Criacao da estrutura de sub indices, reformulacao do relatório  "Analise tempo sem sorteio  no formato (Numero - Qtd de sorteios)" e aviso  que nao pode baizar ZIP 
	 * Versão 33-0 de 03/10/2018 Criado o sub indice 1 do  indice para alimentar o indice ptMseq e opcao de executar o programa com zip  antigo. 
	 * Versão 34-0 de 05/10/2018 Criar base de teste para eliminacao de  estrutura antiga. 
	 * Versão 34-0 de 05/10/2018 Criar base de teste para  eliminacao de estrutura antiga. 
	 * Versão 35-0 de 05/10/2018 Criar base de teste  para geral dos indices. 
	 * Versão 36-0 de 05/10/2018 Criar base de teste para  geral dos indices. 
	 * Versão 37-0 de 07/10/2018 Alteração das classes BaseIndice  IndiceAbstract de modo a operarem somente com a base dos indices . 
	 * Versão 38-0 de 07/10/2018 Calibragem com parametros ate o ultimo sorteio .  * Versão  39-0 de 07/10/2018 Paralelizacao. 
	 * Versão 40-0 de 07/10/2018 Renovacao da  rotina de escolha da serie de sucesso. (interrompido por problema  processamento paralelo) 
	 * Versão 41-0 de 07/10/2018 Ajustes no processamento.  Neste ponto processsamento paralelo dentro do sorteio ok. 
	 * Versão 42-0 de 07/10/2018 Inclusao do Indice de darhal na Coleção. 
	 * Versão 43-0 de 21/10/2018 Revisao geral do codigo. Alterar o indice para inibir indice com fator 0  
	 * Versão 52-0 de 31/10/2018 Corrrecao do indice UltimoCiclo .... 
	 * Versão 53-0 de 02/11/2018 Totalização do Rank intermediaraia para evitar falta de memoria.  
	 * Versão 54-0 de 02/11/2018 Criar classe com fatores. 
	 * Versão 55-0 de 03/11/2018 Criar opçaõ para gerar arquivo no formato : java Sorteados >  ..\
	 * Versão 57-0 de 04/11/2018 Gravacao  do Rank intermediario.  * Versão 58-0 de 04/11/2018 Leitura do flag  saidaForcada.flg no diretorio corrente forçando a parada do processamento c/  a gravacao dos resultados atuais  * Versão 59-0 de 09/11/2018 Ajuste na  expressao da formula, gravacao de seguranca para nivel >3 acerto no loop dos  tipos  * Versão 60-0 de 09/11/2018 Com totalização por parametros testados para  melhor performance   
	 * Versão 61-0 de 20/11/2018 em caso de parametro invalido  sai help.   
	 * Versão 62-0 de 21/11/2018 Criação de opção para aproveitar  profundidade anterior   
	 * Versão 63-0 de 28/11/2018 Relatorio com as dezenas  formatadas.   
	 * Versão 64-0 de 11/12/2018 Mudança para fazer calculo com e sem  Drahal na mesma rodada.   
	 * Versão 65-0 de 18/12/2018 Criação do indice de Monte  Carlo.   
	 * Versão 66-0 de 14/02/2019 Criação do relatorio ultimos sorteados.    
	 * Versão 66-1 de 25/03/2019 Aceita nome do html no zip em minuscula. Aceita -d  para verificar data do ultimo sorteio no arquivo da caixa.   
	 * Versão 66-2 de 28/03/2019 Apresenta data do arquivo HTM baixado da caixa na console quando  executado com -d.   
	 * Versão 68-0 de 08/04/2019 Classe Darhal com ajuste na  precisão + PDM review   
	 * Versão 69-0 de 08/04/2019 Quebra do paralelismo para  não clonar Colecao e poupar memoria +m uso do SSD   
	 * Versão 70-0 de 08/04/2019 Processamento com rankCalibragem Final direto.   
	 * Versão 71-0 de 08/04/2019 Processamento com teste de formula.   
	 * Versão 72-0 de 08/04/2019 Processamento  com formulas ajustadas.   
	 * Versão 73-0 de 08/04/2019 Novo hash, Static no  soprocessa e novo busca Acerto   
	 * Versão 74-0 de 08/04/2019 Retorno ao hash  antigo   
	 * Versão 75-0 de 08/04/2019 Criação do indice Drop, reativação do indice  UltPQtdSB1 e ajuste na busca do IndiceAbstrato   
	 * Versão 76-0 de 08/04/2019 Novo  processamento com uma previa analise dos indices   
	 * Versão 77-0 de 15/09/2019 Ajuste na classificação do Rank   
	 * Versão 77-1 de 20/04/2020 Incuida opção para  aguardar divulgacao oficial do sorteio.   
	 * Versão 77-2 de 02/05/2020 Acelerar  processamento calculo das apostas classificadas por quantidade de  fórmulas/acertos computados.   
	 * Versão 77-3 de 09/11/2022 Proteção de erro ao  ler TXT com tab ou espaco.   
	 * Versão 77-4 de 30/01/2025 Obtenção dos resultados  via API.   
	 * Versão 78-0 de 11/05/2025 Novo Rank com registro de acertos  individuais e nova formula de pesquisa.    
	 * Versão 78-1 de 19/05/2025 Foi implementada a correção do Relatorio dos dados de ultimo sorteio, mostrando apenas o total do sorteio e não o total de todos os sorteios.
	 * Versão 78-2 de 17/08/2025 Ajustado função existeSorteio(), retirado warnings e reagrupado comentários
	 * Versão 78-3 de 29/08/2025 Implementado getJsonFromUrlWithoutCache e nova fiunção getJsonFromUrl (evita cache)
	 * Versão 78-4 de 03/09/2025 Processamento paralelo da carga do rank + sistema de logs
	 * Versâo 78-5 de 17/12/2025 Sistea de Log, -w 9999 com apuração já realizada, GitHub
	 */
	// ******************************************************
	/*******************************************************
	 *
	 * Area de ajustes de processamento
	 *
	 */ // ***********************************************
	static GerenciadorMensagens gm = new GerenciadorMensagens(4500,GerenciadorMensagens.ModoLimite.LINHAS);
	static service intService = new service(gm);
	public static MegaSenaResultado resultado =new MegaSenaResultado(gm);
	static double precoAposta = 6;
	static byte numeroDeIteracoes = 2; // variando de 1 a 3.
	static byte profundidade = 2; // =2 são 65.536 procs/sorteio =3 são 6.588.344 porcs/sorteio =4 são 120.000.000
	// =5 são 1.003.976.272 procs / sorteio
	static String ver = "78-" + profundidade + "-" + numeroDeIteracoes;
	static boolean estamosEmTeste = false;
	// static String ver = "99-" + profundidade;
	// static boolean estamosEmTeste = true;
	// static String nomeZipDados="File_Zip_Mega_Base_"+ver+"-1.zip";
	static String nomeZipDados = "";
	static String nomeRankCalibragem = "";
	static String nomeRankCalibragemCarga = "";
	static String nomeTxtFile = "";
	// static List<byte[]> listaApostas = new ArrayList<>();
	static List<byte[]> listaJogos = new ArrayList<>();
	static Set<Long> indiceApostas = new HashSet<>();
	// static long[] baseHash = new long[61];
	public static final long[] baseHash;
	static {
		baseHash = new long[61];
		for (int contador = 1; contador < 61; contador++) {
			baseHash[contador] = (1L << (contador - 1));
		}
	}
	static int[] resultados = new int[3];
	static final byte NUMMAXDEZENAS = 10;
	static List<byte[]> listaOrdemSorteio;
	static Colecao colecao;
	static List<Calibragem> rCF = new ArrayList<>();
	static List<Calibragem> rankCalibragemFinal = Collections.synchronizedList(rCF);
	static Calibragem item;
	static short numSorteio;
	static int qtdGandoresSena;
	static int numeroSorteiosAcumulado;
	static int sorteioParaAguardar = 0;
	static int ultimoSorteioNoRank = 0;
	static int ultimoSorteioNaTabela = 0;
	static String dataSorteio;
	static String dataProximoSorteio;
	static double rateioSena;
	static double rateioQuina;
	static double rateioQuadra;
	static String acumulada;
	static double valorAcumulado;
	static double estimativaPremio;
	static double estimativaMegaVirada;
	static boolean saidaSolicitada;
	static boolean continuidade;
	static long numeroDeProcessamentos = 0;
	static long tempoTotalDeProcessamento = 0;
	final static int qtdItens = (numFormulas(2, 4, 8) * (numFormulas(1, 4, 2) + 1) + numFormulas(1, 4, 2)) * 2;
	public static final int SEGUNDOS_POR_MINUTO = 60;
	public static final int MINUTOS_POR_HORA = 60;
	public static final int HORAS = 6;
	public static final int MINISEGUNDOS_POR_SEGUNDO = 1000;
	public static final double TEMPO_MEDIO_PROCESSO_EM_MINUTOS = 7.5;
	public static final int LIMITE_NUMERO_PARA_GRAVACAO_DE_SEGURANCA = 25;
	public static final long LIMITE_TEMPO_PARA_GRAVACAO_DE_SEGURANCA = TimeUnit.MINUTES
			.toMillis((long) (LIMITE_NUMERO_PARA_GRAVACAO_DE_SEGURANCA * TEMPO_MEDIO_PROCESSO_EM_MINUTOS));
	
	

	private static void adicionaJogo(final byte dez01, final byte dez02, final byte dez03, final byte dez04,
			final byte dez05, final byte dez06, final byte numJogo) {
		final long hash = baseHash[dez01] | baseHash[dez02] | baseHash[dez03] | baseHash[dez04] | baseHash[dez05]
				| baseHash[dez06];
		if (!indiceApostas.contains(hash)) {
			listaJogos.add(new byte[] { numJogo, dez01, dez02, dez03, dez04, dez05, dez06, 1 });
			indiceApostas.add(hash);
		} else {
			/// adiciono contator de repetição
			byte[] jogo = new byte[7];
			long hashBuscador;
			for (int conta = 0; conta < listaJogos.size(); conta++) {
				jogo = listaJogos.get(conta);
				hashBuscador = baseHash[jogo[1]] | baseHash[jogo[2]] | baseHash[jogo[3]] | baseHash[jogo[4]]
						| baseHash[jogo[5]] | baseHash[jogo[6]];
				if (hash == hashBuscador) {
					listaJogos.get(conta)[7]++;
					break;
				}
			}
		}
	}


	private static void apuraResultado(int sorteioInterno) {
		// processar todas as formulas
		// final byte[][] indice = { { 0, 0, 1 }, { 0, 1, 0 }, { 0, 1, 1 }, { 1, 0, 0 },
		// { 1, 0, 1 }, { 1, 1, 0 },
		// { 0, 0, 2 }, { 0, 2, 0 }, { 0, 2, 2 }, { 2, 0, 0 }, { 2, 0, 2 }, { 2, 2, 0 }
		// };
		final int nReg = rankCalibragemFinal.size();
		byte[][] aposta;
		Formula formulaLocal;
		int fracao = (nReg  / 25)& ~1; // Força que seja par
		if (fracao==0) {
			fracao=2;
		}

		gm.registrarMensagem("Inicio da apuração do ultimo sorteio nas formulas do RankCalibragem.");

		final long t1 = System.currentTimeMillis();
		final byte[] sorteioAtual = listaOrdemSorteio.get(0);
		final long hashSorteio = baseHash[sorteioAtual[0]] + baseHash[sorteioAtual[1]] + baseHash[sorteioAtual[2]]
				+ baseHash[sorteioAtual[3]] + baseHash[sorteioAtual[4]] + baseHash[sorteioAtual[5]];
		long tempHash;
		int senas = 0, quinas = 0, quadras = 0, ternos = 0, duplas = 0, solos = 0;
		ProgressTracker tracker = new ProgressTracker(nReg);
		tracker.print(1);
		// 1. Obtém o Comparator adequado para a ordem especificada
		//Comparator<Calibragem> comparator = Comparator.comparingDouble(Calibragem::ordem4);

		// 2. Cria um min-heap usando o Comparator
		//PriorityQueue<Calibragem> minHeap = new PriorityQueue<>(comparator);
		Calibragem.calcularProbabilidadesTeoricas(1);
		Calibragem itemTeste = new Calibragem();
		Calibragem.ordem=4;
		PriorityQueue<Calibragem> minHeap = new PriorityQueue<>(new Comparator<Calibragem>() {
			@Override
			public int compare(Calibragem c1, Calibragem c2) {
				return Double.compare(c1.ordem4(), c2.ordem4()); // Ordem crescente
			}
		});

		int qtd = 24;
		for (int n = 0; n < nReg; n += 2) {
			if (n % fracao == 0 & n > 0) {
				tracker.print(n);
			}
			item = rankCalibragemFinal.get(n);
			formulaLocal = new Formula(item.baseFormula);
			// aposta = SoProcessa.defineDezenas(formulaLocal, colecao, (byte) 10, indice);
			// 0 entra o resultado com aplicac�o da s�rie de Darhal
			// 1 tem o resultado genuino
			aposta = SoProcessa.defineDezenasUltra(formulaLocal, colecao);// , indice);
			Formula itemFormula= new Formula(item.baseFormula);
			for (byte comOuSemDarhak = 0; comOuSemDarhak < 2; comOuSemDarhak++) {
				tempHash = geraHash(aposta[comOuSemDarhak]);
				long intersecao = hashSorteio & tempHash;
				if (intersecao == 0) {
					continue;
				}
				final int numeroDeAcertos = Long.bitCount(intersecao);

				itemTeste = new Calibragem(item.baseFormula);
				itemTeste.ultimoSorteioComAcerto=numSorteio;

				if (comOuSemDarhak==0) {
					itemFormula.setFiltro();
				} else {
					itemFormula.resetFiltro();
				}
				itemTeste.baseFormula=itemFormula.base;
				// final int numeroDeAcertos = Long.bitCount(hashSorteio & tempHash);
				// if (numeroDeAcertos == 0) {
				// continue;
				// }
				switch (numeroDeAcertos) {
				case 1:
					solos++;
					itemTeste.solos=1;
					break;
				case 2:
					duplas++;
					itemTeste.duplas=1;
					break;
				case 3:
					ternos++;
					itemTeste.ternos=1;
					break;
				case 4:
					quadras++;
					itemTeste.quadras=1;
					break;
				case 5:
					quinas++;
					itemTeste.quinas=1;
					break;
				default:
					senas++; // 6 ou mais (se aplicável)
					itemTeste.senas=1;
				}

				if (minHeap.size() < qtd) {
					minHeap.offer(itemTeste);
				} else {
					if (Double.compare(itemTeste.ordem4(), minHeap.peek().ordem4()) > 0) {
						minHeap.poll();
						minHeap.offer(itemTeste);
					}
				}
				item = rankCalibragemFinal.get(n + 1);
			}
		}
		tracker.print(nReg);
		gm.registrarMensagem("Final da apuração do último sorteio nas fórmulas. Executado em : " + getHMS(t1));

		resultados[0] = senas;
		resultados[1] = quinas;
		resultados[2] = quadras;
		gm.registrarMensagem(
				"Foram computadas no sorteio "+sorteioInterno+"  : " + String.format("%,d senas, ", senas) + String.format("%,d quinas, ", quinas)
				+ String.format("%,d quadras, ", quadras) + String.format("%,d ternos, ", ternos)
				+ String.format("%,d duplas e ", duplas) + String.format("%,d solos.", solos));
		List<Calibragem> result = new ArrayList<>(minHeap);
		result.sort(Comparator.comparingDouble(Calibragem::ordem4).reversed());
		String	nomeRankCalPrint = "RankCalPrint" + ver + "-"+3+"-"+numSorteio+".txt";
		gravaRankCalPrint(nomeRankCalPrint, result,qtd);

	}

	private static void bestNumbers(final PrintStream arquivo, final PrintStream console,
			final boolean semNovoCalculo) {
		// processar todas as formulas
		final SimpleDateFormat format = new SimpleDateFormat();
		format.setTimeZone(TimeZone.getTimeZone("GMT-3:00"));
		List<apostaAcerto> apostasList = new ArrayList<>();
		final List<apostaAcerto> apostasListSave = new ArrayList<>();
		final List<apostaAcerto> apostasListSaveAcertos = new ArrayList<>();
		final List<apostaAcerto> apostasListSaveFormula = new ArrayList<>();
		final int nReg = rankCalibragemFinal.size();
		byte[][] aposta;
		Formula formulaLocal;
		long hash;
		final apostaAcerto itemApostaAcerto = new apostaAcerto();
		int itemEncontrado;
		// final int incremento = (estamosEmTeste ? 1 : 2);
		//int percente = 0;
		final int fracao = (nReg  / 100)&~1;
		System.setOut(console);
		//gm.setDuration(2);
		gm.registrarMensagem("Inicio da apuração das apostas.");
		long t1 = System.currentTimeMillis();
		if (semNovoCalculo == false | !Files.exists(Paths.get("BestNumbers.svd")))  {
			ProgressTracker tracker = new ProgressTracker(nReg) ;
			tracker.print(1);
			for (int n = 0; n < nReg; n += 2) {
				if (n % fracao == 0 & n > 0) {
					tracker.print(n);
				}
				item = rankCalibragemFinal.get(n);
				formulaLocal = new Formula(item.baseFormula);
				// aposta = SoProcessa.defineDezenas(formulaLocal, colecao, (byte) 10, indice);
				aposta = SoProcessa.defineDezenasUltra(formulaLocal, colecao);// , indice);
				for (byte comOuSemDarhak = 0; comOuSemDarhak < 2; comOuSemDarhak++) {
					hash = geraHash(aposta[comOuSemDarhak]);
					itemApostaAcerto.aposta = hash;
					if (apostasList.contains(itemApostaAcerto)) {
						itemEncontrado = apostasList.indexOf(itemApostaAcerto);
						apostasList.get(itemEncontrado).qtdAcertos += item.numSorteiosComAcerto();
						apostasList.get(itemEncontrado).qtdFormulas++;
					} else {
						apostasList.add(new apostaAcerto(hash, item.numSorteiosComAcerto()));
					}
					item = rankCalibragemFinal.get(n + 1);
				}
			}
			tracker.print(nReg);
			apostasList.get(0).totRegSet(apostasList.size());
		} else {
			apostasList = carregaBestNumber();
		}
		gm.registrarMensagem("Final da da apuração das apostas. Executado em : " + getHMS(t1));
		gm.setDuration(4);
		System.setOut(arquivo);
		System.out.println("\nO Ranking esta com " + nReg + " registros");
		System.out.println(
				"\nEm um universo de 238.399 possibilidades {C(10,6)=210	210/50.063.860 = 0,00042% (1 em 238.399)},\n foram geradas: "
						+ String.format("%,d ", apostasList.get(0).totRegGet()) + " combinações ==> ("
						+ String.format("%,d ", apostasList.get(0).totRegGet()) + "* 210/50.063.860 = "
						+ String.format("%5.2f ", ((float) apostasList.get(0).totRegGet() / (float) 238399 * 100))
						+ "% chances de acerto de uma sena.\n");
		itemApostaAcerto.ordemSet((byte) 1); //Acertos
		Collections.sort(apostasList);
		for (byte n = 0; n < (apostasList.size() > 5 ? 5 : apostasList.size()); n++) {
			processa(openApostaNum(apostasList.get(n).aposta), (byte) (n + 1));
			if (!apostasListSaveAcertos.contains(apostasList.get(n))) {
				apostasListSaveAcertos.add(apostasList.get(n));
			}
		}
		itemApostaAcerto.ordemSet((byte) 2); //FormulaAcertos
		Collections.sort(apostasList);
		for (byte n = 0; n < (apostasList.size() > 5 ? 5 : apostasList.size()); n++) {
			processa(openApostaNum(apostasList.get(n).aposta), (byte) (n + 1));
			if (!apostasListSaveFormula.contains(apostasList.get(n))) {
				apostasListSaveFormula.add(apostasList.get(n));
			}
		}
		boolean mesmoGrupo=true;
		for (int i = 0; i < 5; i++) {
			if ( apostasListSaveAcertos.get(i).aposta != apostasListSaveFormula.get(i).aposta || 
					apostasListSaveAcertos.get(i).qtdAcertos != apostasListSaveFormula.get(i).qtdAcertos ||
					apostasListSaveAcertos.get(i).qtdFormulas != apostasListSaveFormula.get(i).qtdFormulas	) {
				mesmoGrupo=false;
				break;
			}
		}


		for (byte tipoRelatorio = 1; tipoRelatorio < 3; tipoRelatorio++) {
			// listaApostas.clear();
			listaJogos.clear();
			indiceApostas.clear();
			System.setOut(console);
			gm.registrarMensagem("Classificando as apostas por quantidade de "
					+ (mesmoGrupo ?  " acertos e fórmulas computadas." : (tipoRelatorio == 1 ? "acertos." : " fórmulas computadas.")));
			t1 = System.currentTimeMillis();
			itemApostaAcerto.ordemSet(tipoRelatorio);
			Collections.sort(apostasList);
			gm.registrarMensagem("Final da Classificação das apostas por quantidade de "
					+ (mesmoGrupo ?  " acertos e fórmulas computadas." :(tipoRelatorio == 1 ? "acertos." : " fórmulas computadas.")) + ". Executado em : " + getHMS(t1));
			System.setOut(arquivo);
			System.out.println("\nRelação das apostas classificadas por quantidade de "
					+ (mesmoGrupo ?  "acertos e fórmulas computadas." : (tipoRelatorio == 1 ? "acertos." : " fórmulas computadas.")) + "\n");
			System.out.println("======   ==================================== ==============  ============");
			System.out.println("Aposta   aposta c/maior número de ocorrencias Núm.acertos     Núm.fórmulas");
			System.out.println("======   ==================================== ==============  ============");
			for (byte n = 0; n < (apostasList.size() > 5 ? 5 : apostasList.size()); n++) {
				System.out.println("  " + (n + 1) + "-         " + openAposta(apostasList.get(n).aposta) + "   "
						+ String.format("%,14d", apostasList.get(n).qtdAcertos) + "  "
						+ String.format("%,12d", apostasList.get(n).qtdFormulas));
				processa(openApostaNum(apostasList.get(n).aposta), (byte) (n + 1));
				if (!apostasListSave.contains(apostasList.get(n))) {
					apostasListSave.add(apostasList.get(n));
				}
			}

			System.out.println("");
			printResumo();
			if (mesmoGrupo) {
				break;
			}
		}
		if (semNovoCalculo == false | !Files.exists(Paths.get("BestNumbers.svd"))) {
			apostasListSave.get(0).totRegSet(apostasList.size());
			salvaBestNumber(apostasListSave);
		}

	}

	private static void calibragemParalelaDosFatores() throws Exception {
		gm.registrarMensagem("             *********************    calibragem dos fatores  de peso    ****************");
		gm.registrarMensagem("Estamos calculando sobre uma base de " + listaOrdemSorteio.size() + " sorteios.");
		short numeroSorteioAtual;
		final long tempoIniCalibragem = System.currentTimeMillis();
		int nTotResult = 1;
		final File flagSaidaForcada = new File("saidaForcada.flg");
		final ProcSorteio processadorDeSorteio = new ProcSorteio();
		processadorDeSorteio.profundidade = profundidade;
		processadorDeSorteio.numMaxDezenas = NUMMAXDEZENAS;
		processadorDeSorteio.rateioSena = rateioSena;
		processadorDeSorteio.rateioQuina = rateioQuina;
		processadorDeSorteio.rateioQuadra = rateioQuadra;
		processadorDeSorteio.rankCalibragemFinal = rankCalibragemFinal;
		long tInicial;
		Date d;
		final SimpleDateFormat format = new SimpleDateFormat();
		format.setTimeZone(TimeZone.getTimeZone("GMT-3:00"));
		long tFinal;
		long tGrupo;
		int sorteiosParaProcessar;
		long tPrevisto;
		Long tSalvaDeSeguranca = System.currentTimeMillis();
		float atual;
		int numeroSorteioBase=ultimoSorteioNoRank+1;
		while (listaOrdemSorteio.size() > 0 & !saidaSolicitada) {
			gm.registrarMensagem("O numeroSorteioBase está com: "+numeroSorteioBase+ " e o calculado está com:"+(numSorteio - listaOrdemSorteio.size() + 1));
			if (listaOrdemSorteio.size() == 1) {

				apuraResultado(numeroSorteioBase);
			}
			numeroSorteioBase++;
			numeroSorteioAtual = (short) (numSorteio - listaOrdemSorteio.size() + 1);
			atual = numeroSorteioAtual;
			gm.registrarMensagem("Disparado o processo do sorteio " + String.format("%1$4s", numeroSorteioAtual) + "/"
					+ numSorteio + " (" + String.format("%6.2f", atual / numSorteio * 100) + "%) com as dezenas:"
					+ String.format("%1$20s", sorteiotoString(listaOrdemSorteio.get(0))));
			processadorDeSorteio.sorteioAtual = listaOrdemSorteio.get(0);
			processadorDeSorteio.numSorteio = numeroSorteioAtual;
			processadorDeSorteio.colecao = colecao;
			tInicial = System.currentTimeMillis();
			d = GregorianCalendar.getInstance().getTime();
			gm.registrarMensagem("Inicio  da coleta : ");// + format.format(d));

			processadorDeSorteio.call();
			tFinal = System.currentTimeMillis();
			tGrupo = (tFinal - tInicial);
			sorteiosParaProcessar = numSorteio - numeroSorteioAtual;
			tPrevisto = tGrupo * sorteiosParaProcessar;
			d = GregorianCalendar.getInstance().getTime();
			gm.registrarMensagem("Termino da coleta. Duração de :"+ getHMS(tInicial) );//: " + format.format(d) + " duracao de : " + getHMS(tInicial));
			d.setTime(d.getTime() + tPrevisto);
			gm.registrarMensagem(
					"================================================>>>>>>>>>>>>  Previsao de termino completo em : "
							+ format.format(d),DestinoMensagem.CONSOLE);
			numeroDeProcessamentos++;
			tempoTotalDeProcessamento += (tFinal - tInicial);
			tGrupo = tempoTotalDeProcessamento / numeroDeProcessamentos;
			tPrevisto = tGrupo * sorteiosParaProcessar;
			d = GregorianCalendar.getInstance().getTime();
			d.setTime(d.getTime() + tPrevisto);
			gm.registrarMensagem("==========Media de (" + String.format("%3d", numeroDeProcessamentos) + ")=== "
					+ getHMS(System.currentTimeMillis() - tGrupo) + " =====>>>>>  Previsao de termino completo em : "
					+ format.format(d),DestinoMensagem.CONSOLE);
			gm.registrarMensagem("Gravacao do Rank em : (" + (LIMITE_NUMERO_PARA_GRAVACAO_DE_SEGURANCA - nTotResult)
					+ " procs) ou " + getHMSPuro(LIMITE_TEMPO_PARA_GRAVACAO_DE_SEGURANCA
							- (System.currentTimeMillis() - tSalvaDeSeguranca)),DestinoMensagem.CONSOLE);
			if (flagSaidaForcada.exists()) {
				saidaSolicitada = true;
				flagSaidaForcada.delete();
				gm.registrarMensagem("Saida forçada pelo flag dentro da coleta detectada no sorteio " + numeroSorteioAtual);
				gravaRankCalibragemTXT(numeroSorteioAtual);
			} else {
				if (profundidade > 2) {
					if ((nTotResult == LIMITE_NUMERO_PARA_GRAVACAO_DE_SEGURANCA | ((System.currentTimeMillis()
							- tSalvaDeSeguranca) > LIMITE_TEMPO_PARA_GRAVACAO_DE_SEGURANCA)) & !continuidade) {
						//gravaRankCalibragem(numeroSorteioAtual);	
						gravaRankCalibragemTXT(numeroSorteioAtual);	
						tSalvaDeSeguranca = System.currentTimeMillis();
						nTotResult = 0;
						gm.registrarMensagem("Salvado registro para profundidade " + profundidade
								+ " de segurança no sorteio " + numeroSorteioAtual);
					}
				}
			}
			nTotResult++;
			final Sorteio sorteio = new Sorteio();
			sorteio.reg(listaOrdemSorteio.get(0));
			colecao.add(sorteio);
			listaOrdemSorteio.remove(0);
			colecao.numeroDoUltimoSorteio = numeroSorteioAtual;
		}
		/// Forçar a gravação em ZIP
		/// gravaRankCalibragem(numSorteio);
		/// gm.registrarMensagem("Registrada a versão ZIP do Rank.");
		if (!saidaSolicitada) {

			///gravaRankCalibragem(numSorteio);
			gm.registrarMensagem("O tempo de iteracao da Calibragem foi de " + getHMS(tempoIniCalibragem));
		}
	}

	@SuppressWarnings("unchecked")
	private static List<apostaAcerto> carregaBestNumber() {
		List<apostaAcerto> apostasList = new ArrayList<>();
		int total;
		try {
			final FileInputStream fi = new FileInputStream(new File("BestNumbers.svd"));
			final ObjectInputStream oi = new ObjectInputStream(fi);
			// Read objects
			apostasList = (List<apostaAcerto>) oi.readObject();
			total = (int) oi.readObject();
			oi.close();
			fi.close();
			apostasList.get(0).totRegSet(total);
		} catch (final FileNotFoundException e) {
			System.out.println("File not found");
		} catch (final IOException e) {
			System.out.println("Error initializing stream");
		} catch (final ClassNotFoundException e) {
			e.printStackTrace();
		}
		return apostasList;
	}

	public static void carregaRankCalibragem() throws IOException {
		try {
			carregaRankCalibragem(getName());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	public static String getName() {
		String caminho = "RankCalibragem" + ver + ".ZIP";
		File arquivo = new File(caminho);
		if (arquivo.exists() && !arquivo.isDirectory()) {
			return caminho;
		}
		caminho = "RankCalibragem" + ver + ".txt";
		arquivo = new File(caminho);
		if (arquivo.exists() && !arquivo.isDirectory()) {
			return caminho;
		}

		return caminho;
	}
	public static void carregaRankCalibragem(final String nomeRankCalibragem) throws IOException {
		if (nomeRankCalibragem.substring(nomeRankCalibragem.length()-4).toUpperCase().compareTo(".ZIP")==0) {
			carregaRankCalibragemZIP(nomeRankCalibragem);
		} else {
			carregaRankCalibragemTXT( nomeRankCalibragem);
		}

	}
	public static void carregaRankCalibragemTXT(final String nomeRankCalibragem) {
		gm.registrarMensagem("Inicio da carga do arquivo " + nomeRankCalibragem + " ");
		int qtdRegistros = 0;
		final long limite = (qtdItens / 25)&~1; // 13_999_999; 10% do total
		BufferedReader br = null;
		ProgressTracker tracker = new ProgressTracker(qtdItens);
		tracker.print(1);
		try {
			br = Files.newBufferedReader(Paths.get(nomeRankCalibragem), StandardCharsets.UTF_8);
			String line = null;
			while ((line = br.readLine()) != null) {
				if ((++qtdRegistros % limite) == 0L) {
					tracker.print(qtdRegistros);
					GerenciadorMensagens.verificaHibernacaoSetRegTempo();
				}
				if (line.substring(0, 1).equals("#")) {
					ultimoSorteioNoRank = Integer.parseInt(line.substring(1).replaceAll(" ", ""));
				} else {
					line = line.trim().replaceAll("\\s{2,}", " ");
					line = line.replaceAll(" ", ",");
					final String[] values = line.split(",");
					final Calibragem item = new Calibragem();
					item.senas = Short.parseShort(values[0]);
					item.quinas = Short.parseShort(values[1]);
					item.quadras = Short.parseShort(values[2]);
					item.ternos = Short.parseShort(values[3]);
					item.duplas = Short.parseShort(values[4]);
					item.solos = Short.parseShort(values[5]);
					// item.qtdDezenas = Short.parseShort(values[6]);
					item.ultimoSorteioComAcerto = Short.parseShort(values[6]);
					item.baseFormula = Long.parseLong(values[7]);
					// item.numSorteiosComAcerto = Short.parseShort(values[8]);
					rankCalibragemFinal.add(item);
				}
			}
			tracker.print(qtdItens);
			if (br != null) {
				br.close();
			}
			gm.registrarMensagem("Final da carga do arquivo " + nomeRankCalibragem + " ");
			if (!continuidade | nomeRankCalibragem.equals("Continuidade.ZIP")) {
				if (listaOrdemSorteio.size() < ultimoSorteioNoRank) {
					gm.registrarMensagem(
							" ******************  URGENTE   ARQUIVO de memoria do Rank mais atual que o arquivo com dados dos Sorteios **************");
				} 
			}
		} catch (final FileNotFoundException ex) {
			ex.printStackTrace();
		} catch (final IOException ex) {
			ex.printStackTrace();
		}
	}

	public static void carregaRankCalibragemZIP(final String nomeRankCalibragem)  throws IOException { 
		gm.registrarMensagem("Inicio da carga do arquivo " + nomeRankCalibragem );
		int qtdRegistros = 0;
		final long limite = (qtdItens / 25)&~1; // 13_999_999; 10% do total
		ProgressTracker tracker = new ProgressTracker(qtdItens);
		tracker.print(1);
		try (FileInputStream fis = new FileInputStream(nomeRankCalibragem);
				XZInputStream xzIn = new XZInputStream(fis);
				InputStreamReader isr = new InputStreamReader(xzIn);
				BufferedReader reader = new BufferedReader(isr)) {

			String line;
			while ((line = reader.readLine()) != null ) {
				
				if ((++qtdRegistros % limite) == 0L) {
					tracker.print(qtdRegistros);
				}
				if (line.substring(0, 1).equals("#")) {
					ultimoSorteioNoRank = Integer.parseInt(line.substring(1).replaceAll(" ", ""));
				} else {
					line = line.trim().replaceAll("\\s{2,}", " ");
					line = line.replaceAll(" ", ",");
					final String[] values = line.split(",");
					final Calibragem item = new Calibragem();
					item.senas = Short.parseShort(values[0]);
					item.quinas = Short.parseShort(values[1]);
					item.quadras = Short.parseShort(values[2]);
					item.ternos = Short.parseShort(values[3]);
					item.duplas = Short.parseShort(values[4]);
					item.solos = Short.parseShort(values[5]);
					item.ultimoSorteioComAcerto = Short.parseShort(values[6]);
					item.baseFormula = Long.parseLong(values[7]);
					rankCalibragemFinal.add(item);
				}
			}
		}


		tracker.print(qtdItens);

		gm.registrarMensagem("Final da carga do arquivo " + nomeRankCalibragem );


	}

	public static void calculoDosFatores()  throws IOException {
		gm.registrarMensagem("Calculo dos fatores no Rank " + ultimoSorteioNoRank + " sorteios carregados na tabela com "+ ultimoSorteioNaTabela + " registros." ); //+ " "
		gm.registrarMensagem("A lista com os sorteios entrou com "+listaOrdemSorteio.size()+" registros");
		int contadorSorteio = 1;
		final Sorteio sorteio = new Sorteio();
		final int fracao = (ultimoSorteioNoRank > 10 ? (ultimoSorteioNoRank / 10)&~1 : 1);
		ProgressTracker tracker2 = new ProgressTracker(ultimoSorteioNoRank);
		tracker2.print(1);
		while (contadorSorteio <= ultimoSorteioNoRank) {
			if (contadorSorteio >= ultimoSorteioNoRank) {
				tracker2.print(contadorSorteio);
				gm.registrarMensagem("Saindo para apurar o resultado antes de calcular o ultimo sorteio da lista de sorteios");
				apuraResultado(ultimoSorteioNoRank);
				gm.registrarMensagem("Voltando de  apurar o resultado antes de calcular o ultimo sorteio da lista de sorteios");
			}
			if ((contadorSorteio % fracao == 0 & listaOrdemSorteio.size() > 1)) {
				tracker2.print(contadorSorteio);
			}
			sorteio.reg(listaOrdemSorteio.get(0));
			colecao.add(sorteio);
			listaOrdemSorteio.remove(0);
			contadorSorteio++;
		}
		gm.registrarMensagem("A lista com os sorteios saio com "+listaOrdemSorteio.size()+" registros");
		tracker2.print(ultimoSorteioNoRank);
		gm.registrarMensagem("Final do calculo dos fatores dos sorteios carregados." );//+ " "
		//+ format.format(GregorianCalendar.getInstance().getTime()));
	}




	public static void carregaRankCalPrint(final String nomeRankCalPrint, List<Calibragem> rankCalibragemTemp, int qtd) {
		gm.registrarMensagem("Inicio da carga do arquivo " + nomeRankCalPrint );
		BufferedReader br = null;
		try {
			br = Files.newBufferedReader(Paths.get(nomeRankCalPrint), StandardCharsets.UTF_8);
			String line = null;
			int contador = 0;
			while ((line = br.readLine()) != null & contador++<qtd) {
				line = line.trim().replaceAll("\\s{2,}", " ");
				line = line.replaceAll(" ", ",");
				final String[] values = line.split(",");
				final Calibragem item = new Calibragem();
				item.senas = Short.parseShort(values[0]);
				item.quinas = Short.parseShort(values[1]);
				item.quadras = Short.parseShort(values[2]);
				item.ternos = Short.parseShort(values[3]);
				item.duplas = Short.parseShort(values[4]);
				item.solos = Short.parseShort(values[5]);
				item.ultimoSorteioComAcerto = Short.parseShort(values[6]);
				item.baseFormula = Long.parseLong(values[7]);
				rankCalibragemTemp.add(item);
			}
			if (br != null) {
				br.close();
			}
			gm.registrarMensagem("Fim da carga do arquivo " + nomeRankCalPrint );
		} catch (final FileNotFoundException ex) {
			ex.printStackTrace();
		} catch (final IOException ex) {
			ex.printStackTrace();
		}
	}

	public static int comb(final int tamanho, final int serie) {
		return (int) (fatorial(tamanho) / (fatorial(serie) * fatorial(tamanho - serie)));
	}

	public static int contarLinhas(final String nomeArquivo) {
		try {
			// Usa Files.lines() para criar um Stream de linhas e conta o número de linhas
			return (int) Files.lines(Paths.get(nomeArquivo)).count();
		} catch (final IOException e) {
			// Se o arquivo não existir ou ocorrer um erro de leitura, retorna 0
			return 0;
		}
	}

	public static void copy(final InputStream input, final OutputStream output, final int bufferSize)
			throws IOException {
		final byte[] buf = new byte[bufferSize];
		int n = input.read(buf);
		while (n >= 0) {
			output.write(buf, 0, n);
			n = input.read(buf);
		}
		output.flush();
	}

	private static void criaItemVer74(final long baseFormula) {
		final Formula formula = new Formula(baseFormula, true);
		formula.setFiltro();
		boolean existe = false;
		for (int testaExistencia = 0; testaExistencia < rankCalibragemFinal.size(); testaExistencia += 2) {
			if (rankCalibragemFinal.get(testaExistencia).baseFormula == formula.base) {
				existe = true;
				System.out.println("A formula " + baseFormula + " já existe na posição " + testaExistencia + " .");
				break;
			}
		}
		if (!existe) {
			rankCalibragemFinal.add(new Calibragem(formula.base));
			formula.resetFiltro();
			rankCalibragemFinal.add(new Calibragem(formula.base));
		}
	}

	public static double fatorial(final double num) {
		if (num <= 1) {
			return 1;
		} else {
			return num * fatorial(num - 1);
		}
	}

	public static long geraHash(final byte[] serie) {
		return (baseHash[serie[0]] + baseHash[serie[1]] + baseHash[serie[2]] + baseHash[serie[3]] + baseHash[serie[4]]
				+ baseHash[serie[5]] + baseHash[serie[6]] + baseHash[serie[7]] + baseHash[serie[8]]
						+ baseHash[serie[9]]);
	}

	private static void geraZerezima() {
		System.out.println("Inicio da geração da Zerezima ");
		Formula formula;
		if (estamosEmTeste) {
			/*
			 * criaItem(39582419648512L); criaItem(35400204882086L);
			 * criaItem(35404495655078L); criaItem(35400204888230L);
			 * criaItem(41169422989614L); criaItem(35467853902248L);
			 * criaItem(44330515636352L); criaItem(48663054463424L);
			 * criaItem(42234574746021L); criaItem(35493619185957L);
			 * criaItem(35640721801381L); criaItem(35645016768677L);
			 * criaItem(35467853902248L); criaItem(493930938698L); criaItem(493930938506L);
			 */
			criaItemVer74(10078155717032L);
			/// final Formula formulaTeste = new Formula(10078155717032L, false);
			/// final String verificaFormula = formulaTeste.toString();
			criaItemVer74(10071713266088L);
			criaItemVer74(10081363311016L);
			criaItemVer74(10218807497128L);
			criaItemVer74(10218807503272L);
			criaItemVer74(10214512535976L);
			criaItemVer74(10214512529832L);
			criaItemVer74(10115736674728L);
			criaItemVer74(10077068343720L);
			criaItemVer74(10081368553896L);
			criaItemVer74(9909564613032L);
			criaItemVer74(9905269645736L);
			/*
			 * criaItemVer74(10357328583080L); criaItemVer74(10348738648488L);
			 * criaItemVer74(10353029421480L); criaItemVer74(10348734454184L);
			 * criaItemVer74(10346586970536L); criaItemVer74(10342296197544L);
			 * criaItemVer74(10342292003240L); criaItemVer74(10347660712360L);
			 * criaItemVer74(10347664906664L); criaItemVer74(10356254835112L);
			 * criaItemVer74(10244585697704L); criaItemVer74(10248880665000L);
			 * criaItemVer74(493930938698L); criaItemVer74(493930938506L);
			 * criaItemVer74(493935133002L); criaItemVer74(489641214094L);
			 * criaItemVer74(493926744394L); criaItemVer74(493926750538L);
			 * criaItemVer74(283478595726L); criaItemVer74(489631776906L);
			 * criaItemVer74(496074166470L); criaItemVer74(493935132810L);
			 * criaItemVer74(493930944842L); criaItemVer74(493926750346L);
			 * criaItemVer74(489637019790L); criaItemVer74(489632825486L);
			 * criaItemVer74(283482790030L); criaItemVer74(493931987086L);
			 * criaItemVer74(425211603086L); criaItemVer74(425207408782L);
			 * criaItemVer74(287769368718L); criaItemVer74(493926744202L);
			 * criaItemVer74(420917557390L); criaItemVer74(420913363086L);
			 * criaItemVer74(287768320330L); criaItemVer74(420920703118L);
			 * criaItemVer74(7057719703686L); criaItemVer74(6915977388166L);
			 * criaItemVer74(6911682420870L); criaItemVer74(6915980533898L);
			 * criaItemVer74(6915980533892L); criaItemVer74(6915972145286L);
			 * criaItemVer74(6768883317062L); criaItemVer74(6775325768006L);
			 * criaItemVer74(6597076230470L); criaItemVer74(7054497425540L);
			 * criaItemVer74(7054497419396L); criaItemVer74(7050202458244L);
			 * criaItemVer74(7050202452100L); criaItemVer74(6906321057924L);
			 * criaItemVer74(6906321051780L); criaItemVer74(7049128716426L);
			 * criaItemVer74(7049128716420L); criaItemVer74(7049128710282L);
			 * criaItemVer74(7049128710276L); criaItemVer74(6912763502724L);
			 * criaItemVer74(7057718651018L); criaItemVer74(7057718651012L);
			 * criaItemVer74(7057718644874L); criaItemVer74(7057718644868L);
			 */
			return;
		}
		System.out.println("Apurando potencial dos indices ");
		final byte[][] indiceOrdem = testaFormulas(listaOrdemSorteio, colecao);
		System.out.println("Apurado potencial dos indices ");
		colecao = new Colecao();
		byte cursorParImpPeso, cursorParImpTipo, cursorUltPQtdPeso, cursorUltPQtdTipo, cursorUltSorteioSB1Peso,
		cursorUltSorteioSB1Tipo, cursorUltSorteioSB2Peso, cursorUltSorteioSB2Tipo, cursorUltSorteioPeso,
		cursorUltSorteioTipo, cursorDropTipo;// , numeroDeIteracoes;
		byte cursorUltPQtdSB1Peso, cursorUltPQtdSB1Tipo;
		byte cursorCicloPeso, cursorCicloTipo, cursorMaioresPeso, cursorMaioresTipo, cursormonteCarloPeso,
		cursormonteCarloTipo, cursorDropPeso;
		boolean inicial = true;
		double numeroItens = 0;
		/*
		 * Indice Nome 
		 * =============================================================================
		 * 0 indices das dezenas mais sorteadas 
		 * 1 Ultimo Ciclo 
		 * 2 indices das dezena sem sorteio 
		 * 3 indices dos maiores intervalos sem sorteio 
		 * 4 indices dos menores intervalos (entre os intervalos maiores) sem sorteio 
		 * 5 indices da media dos intervalos nos ciclos de sorteios 
		 * 6 Indice para comparacao com o indice ptMseq do Rank Geral 
		 * 7 Indice de distribuicao entre pares e impares 
		 * 8 Indice de registro da pegada da onda 
		 * 9 Indice de Monte Carlo
		 *
		 */
		// numeroDeIteracoes = 3; // variando de 1 a 3.
		System.out.println("Inicio da apuraçao das formulas; cada ponto 10.000.000 formulas.\n");
		for (cursorParImpPeso = 0; cursorParImpPeso < profundidade; cursorParImpPeso++) {
			for (cursorParImpTipo = 0; cursorParImpTipo < (cursorParImpPeso == 0 ? 1
					: numeroDeIteracoes); cursorParImpTipo++) {
				for (cursorUltPQtdPeso = 0; cursorUltPQtdPeso < profundidade; cursorUltPQtdPeso++) {
					for (cursorUltPQtdTipo = 0; cursorUltPQtdTipo < (cursorUltPQtdPeso == 0 ? 1
							: 1); cursorUltPQtdTipo++) { /// Alterado de ? 1: numeroDeIteracoes para ? 1: 1
						for (cursorUltSorteioSB1Peso = 0; cursorUltSorteioSB1Peso < profundidade; cursorUltSorteioSB1Peso++) {
							for (cursorUltSorteioSB1Tipo = 0; cursorUltSorteioSB1Tipo < (cursorUltSorteioSB1Peso == 0
									? 1
											: numeroDeIteracoes); cursorUltSorteioSB1Tipo++) {
								for (cursorUltSorteioSB2Peso = 0; cursorUltSorteioSB2Peso < profundidade; cursorUltSorteioSB2Peso++) {
									for (cursorUltSorteioSB2Tipo = 0; cursorUltSorteioSB2Tipo < (cursorUltSorteioSB2Peso == 0
											? 1
													: numeroDeIteracoes); cursorUltSorteioSB2Tipo++) {
										for (cursorUltSorteioPeso = 0; cursorUltSorteioPeso < profundidade; cursorUltSorteioPeso++) {
											for (cursorUltSorteioTipo = 0; cursorUltSorteioTipo < (cursorUltSorteioPeso == 0
													? 1
															: numeroDeIteracoes); cursorUltSorteioTipo++) {
												for (cursorUltPQtdSB1Peso = 0; cursorUltPQtdSB1Peso < profundidade; cursorUltPQtdSB1Peso++) {
													for (cursorUltPQtdSB1Tipo = 0; cursorUltPQtdSB1Tipo < (cursorUltPQtdSB1Peso == 0
															? 1
																	: 1); cursorUltPQtdSB1Tipo++) { /// Alterado de ? 1:
														/// numeroDeIteracoes para ?
														/// 1: 1
														for (cursorCicloPeso = 0; cursorCicloPeso < profundidade; cursorCicloPeso++) {
															for (cursorCicloTipo = 0; cursorCicloTipo < (cursorCicloPeso == 0
																	? 1
																			: numeroDeIteracoes); cursorCicloTipo++) {
																for (cursorMaioresPeso = 0; cursorMaioresPeso < profundidade; cursorMaioresPeso++) {
																	for (cursorMaioresTipo = 0; cursorMaioresTipo < (cursorMaioresPeso == 0
																			? 1
																					: numeroDeIteracoes); cursorMaioresTipo++) {
																		for (cursormonteCarloPeso = 0; cursormonteCarloPeso < profundidade; cursormonteCarloPeso++) {
																			for (cursormonteCarloTipo = 0; cursormonteCarloTipo < (cursormonteCarloPeso == 0
																					? 1
																							: numeroDeIteracoes); cursormonteCarloTipo++) {
																				for (cursorDropPeso = 0; cursorDropPeso < profundidade; cursorDropPeso++) {
																					for (cursorDropTipo = 0; cursorDropTipo < (cursorDropPeso == 0
																							? 1
																									: numeroDeIteracoes); cursorDropTipo++) {
																						if (inicial) {
																							inicial = false;
																							continue;
																						}
																						formula = new Formula(true, // filtro,
																								// Com
																								// filtro de Darhal
																								new Componente(
																										indiceOrdem[0][cursorMaioresTipo],
																										cursorMaioresPeso),
																								new Componente(
																										indiceOrdem[2][cursorUltSorteioTipo],
																										cursorUltSorteioPeso),
																								new Componente(
																										indiceOrdem[6][cursorUltPQtdSB1Tipo],
																										cursorUltPQtdSB1Peso),
																								new Componente(
																										indiceOrdem[7][cursorParImpTipo],
																										cursorParImpPeso),
																								new Componente(
																										indiceOrdem[1][cursorCicloTipo],
																										cursorCicloPeso),
																								new Componente(
																										indiceOrdem[5][cursorUltPQtdTipo],
																										cursorUltPQtdPeso),
																								new Componente(
																										indiceOrdem[3][cursorUltSorteioSB1Tipo],
																										cursorUltSorteioSB1Peso),
																								new Componente(
																										indiceOrdem[4][cursorUltSorteioSB2Tipo],
																										cursorUltSorteioSB2Peso),
																								new Componente(
																										indiceOrdem[9][cursormonteCarloTipo],
																										cursormonteCarloPeso),
																								new Componente(
																										indiceOrdem[8][cursorDropTipo],
																										cursorDropPeso));
																						rankCalibragemFinal
																						.add(new Calibragem(
																								formula.base));
																						formula.resetFiltro(); // Sem o
																						// filtro
																						// de
																						// Darhal
																						rankCalibragemFinal
																						.add(new Calibragem(
																								formula.base));
																						numeroItens += 2;
																						if (((numeroItens
																								% 10_000_000) == 0)) {
																							System.out.print(".");
																						}
																					}
																				} //// ---- for
																				//// cursorPesoPontuacaoSemSorteio
																			}
																		} //// ---- for cursornPesoPontuacaoMaiores
																	}
																}
															}
														}
													}
												}
											}
										}
									}
								}
							}
						}
					}
				}
			}
		}
		gm.registrarMensagem(
				"Final da geração da Zerezima com " + textNum(rankCalibragemFinal.size()) + " formulas geradas.");
	}
	public static String getHMS(long t1) {
		t1 = System.currentTimeMillis() - t1;
		return getHMSPuro(t1) + " (" + t1 + ")";
	}

	public static String getHMSPuro(long t1) {
		long tempo = Math.max(0, t1);

		long horas = TimeUnit.MILLISECONDS.toHours(tempo);
		long minutos = TimeUnit.MILLISECONDS.toMinutes(tempo) - TimeUnit.HOURS.toMinutes(horas);
		long segundos = TimeUnit.MILLISECONDS.toSeconds(tempo) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(tempo));
		long milissegundos = tempo % 1000;

		return String.format("%02d:%02d:%02d.%03d", horas, minutos, segundos, milissegundos);
	}
	
	private static String getSequencia(final String local, final String string) throws IOException {
		final FileFilter filter = file -> file.getName().startsWith(string) & file.getName().endsWith(".txt");
		final File dir = new File(local);
		final File[] files = dir.listFiles(filter);
		final int proximo = files.length + 1;
		final SimpleDateFormat formatoData = new SimpleDateFormat("_ddMMyyyy_HHmmss");
		formatoData.setTimeZone(TimeZone.getTimeZone("GMT-3:00"));
		final Calendar data = Calendar.getInstance();
		return local + "\\" + string + proximo + formatoData.format(data.getTime()) + ".txt";
	}

	private static void gravaRankCalibragem(final int numSorteioInterno) throws IOException {
		gravaRankCalibragemZIP(nomeRankCalibragem, numSorteioInterno);
	}
	private static void gravaRankCalibragemTXT(final int numSorteioInterno) throws IOException {
		gravaRankCalibragemTXT(nomeRankCalibragem, numSorteioInterno);
	}

	//@SuppressWarnings("unused")
	private static void gravaRankCalibragemTXT(String nomeRankCalibragem, final int numSorteioInterno) {
		if (continuidade) {
			nomeRankCalibragem = "Continuidade.TXT";
		}
		final SimpleDateFormat format = new SimpleDateFormat();
		format.setTimeZone(TimeZone.getTimeZone("GMT-3:00"));
		try {
			// if (!estamosEmTeste) {
			//System.out.print("\nIniciando a gravação da calibragem dos fatores. "
			//		+ format.format(GregorianCalendar.getInstance().getTime()) + "\n");
			gm.registrarMensagem("Iniciando a gravação da calibragem dos fatores.");
			// }
			// Vou verificar se ja existe o arquivo tmp, e se existir vou remover
			final String nomeTmp = nomeRankCalibragem + ".tmp";
			final File fileTmp = new File(nomeTmp);
			if (fileTmp.exists()) {
				fileTmp.delete();
			}
			final BufferedWriter arq = Files.newBufferedWriter(Paths.get(nomeTmp), StandardCharsets.UTF_8);
			final PrintWriter gravarArq = new PrintWriter(arq);
			Calibragem item = new Calibragem();
			final int fim = rankCalibragemFinal.size();
			final int divisor = (fim/ 20 )&~1 ;
			//int counter = 0;
			ProgressTracker tracker = new ProgressTracker(fim);
			tracker.print(1);
			//Double percentGravado;
			for (int i = 0; i < fim; i++) {
				item = rankCalibragemFinal.get(i);
				if (item.baseFormula != 0) {
					gravarArq.printf(item.toString() + "%n");
				}

				// if (!estamosEmTeste) {
				if (i % divisor == 0 & i > 0) {
					tracker.print(i);
				}
			}
			gravarArq.printf("#" + numSorteioInterno);
			gravarArq.close();
			arq.close();
			// Vou verificar se ja existe o arquivo nomeRankCalibragem, e se existir vou
			// remover
			final File fileNomeRankCalibragem = new File(nomeRankCalibragem);
			if (fileNomeRankCalibragem.exists()) {
				fileNomeRankCalibragem.delete();
			}
			new File(nomeTmp).renameTo(new File(nomeRankCalibragem));
			// if (!estamosEmTeste) {
			tracker.print(fim);
			gm.registrarMensagem("Rank da calibragem dos fatores salvo com sucesso como " + nomeRankCalibragem + " .\n");
			// }
		} catch (final IOException e1) {
			e1.printStackTrace();
		}
	}

	private static void gravaRankCalibragemZIP(String nomeRankCalibragem, final int numSorteioInterno) throws  IOException {
		if (continuidade) {
			nomeRankCalibragem = "Continuidade.ZIP";
		}
		//final SimpleDateFormat format = new SimpleDateFormat();
		//format.setTimeZone(TimeZone.getTimeZone("GMT-3:00"));
		gm.registrarMensagem("Iniciando a gravação da calibragem dos fatores.");
		System.out.flush();
		// Vou verificar se ja existe o arquivo tmp, e se existir vou remover
		final String nomeTmp = nomeRankCalibragem + ".tmp";
		final File fileTmp = new File(nomeTmp);
		if (fileTmp.exists()) {
			fileTmp.delete();
		}
		Calibragem item = new Calibragem();
		final int fim = rankCalibragemFinal.size();
		final int divisor = (fim/ 100 )&~1 ;
		ProgressTracker tracker = new ProgressTracker(fim);
		tracker.print(1);
		try (FileOutputStream fos = new FileOutputStream(fileTmp);
				XZOutputStream xzOut = new XZOutputStream(fos, new LZMA2Options());
				PrintWriter pw = new PrintWriter(new OutputStreamWriter(xzOut, StandardCharsets.UTF_8))) {


			for (int i = 0; i < fim; i++) {
				item = rankCalibragemFinal.get(i);
				if (item.baseFormula != 0) {

					pw.printf("%s%n",item.toString()); // Converte o item para String
				}

				if (i % divisor == 0 & i > 0) {
					tracker.print(i);
					GerenciadorMensagens.verificaHibernacaoSetRegTempo();
				}
			}
			pw.printf("%s","#" + numSorteioInterno); // Converte o item para String
		}
		// Vou verificar se ja existe o arquivo nomeRankCalibragem, e se existir vou
		// remover
		final File fileNomeRankCalibragem = new File(nomeRankCalibragem);
		if (fileNomeRankCalibragem.exists()) {
			fileNomeRankCalibragem.delete();
		}
		tracker.print(fim);
		new File(nomeTmp).renameTo(new File(nomeRankCalibragem));
		gm.registrarMensagem("Rank da calibragem dos fatores salvo com sucesso como " + nomeRankCalibragem + " .");

	}
	private static void gravaRankCalPrint(String nomeRankCalPrint, List<Calibragem> rankCalibragemTemp, int fim) {
		try {
			gm.registrarMensagem("Iniciando a gravação dos " +fim+" itens do Rank Impressão "+nomeRankCalPrint+" que chegou com "+rankCalibragemTemp.size()+" Regs.");
			final BufferedWriter arq = Files.newBufferedWriter(Paths.get(nomeRankCalPrint), StandardCharsets.UTF_8);
			final PrintWriter gravarArq = new PrintWriter(arq);
			Calibragem item = new Calibragem();
			for (int i = 0; i < fim; i++) {
				item = rankCalibragemTemp.get(i);
				if (item.baseFormula != 0) {
					gravarArq.printf(item.toString() + "%n");
				}
			}
			gravarArq.close();
			arq.close();
			// Vou verificar se ja existe o arquivo nomeRankCalibragem, e se existir vou
			// remover

			gm.registrarMensagem("RankCalPrint da calibragem dos fatores salvo com sucesso como " + nomeRankCalPrint + " .");
			cleanOldFiles(nomeRankCalPrint);
		} catch (final IOException e1) {
			e1.printStackTrace();
		}
	}
	public static void cleanOldFiles(String currentFileName) {
		File currentFile = new File(currentFileName);
		if (!currentFile.exists()) {
			return;
		}

		// Extrai o prefixo e o número atual do nome do arquivo
		String[] parts = extractPrefixAndNumber(currentFileName);
		if (parts == null) {
			//System.out.println("Formato do arquivo inválido: " + currentFileName);
			return;
		}

		String prefix = parts[0];
		int currentNumber;
		try {
			currentNumber = Integer.parseInt(parts[1]);
		} catch (NumberFormatException e) {
			//System.out.println("Número no arquivo não é válido: " + parts[1]);
			return;
		}

		int threshold = currentNumber - 2;
		File directory = currentFile.getParentFile();
		if (directory == null) {
			directory = new File("."); // diretório atual se não houver pai
		}

		// Lista todos os arquivos no diretório com o mesmo prefixo
		File[] files = directory.listFiles((_, name) -> name.startsWith(prefix) && name.endsWith(".txt"));
		if (files == null) {
			//System.out.println("Nenhum arquivo encontrado com o prefixo: " + prefix);
			return;
		}

		// Processa cada arquivo encontrado
		for (File file : files) {
			String fileName = file.getName();
			String[] fileParts = extractPrefixAndNumber(fileName);
			if (fileParts == null) {
				continue;
			}

			try {
				int fileNumber = Integer.parseInt(fileParts[1]);
				if (fileNumber < threshold) {
					if (file.delete()) {
						gm.registrarMensagem("Removendo arquivo antigo (anteriores ao sorteio "+threshold+") de Rank Cal Print: " + fileName,DestinoMensagem.LOG );
					} else {
						//System.out.println("Falha ao deletar arquivo: " + fileName);
					}
				}
			} catch (NumberFormatException e) {
				// Ignora arquivos com números inválidos
				continue;
			}
		}
	}

	private static String[] extractPrefixAndNumber(String fileName) {
		// Remove a extensão .txt
		String baseName = fileName.replace(".txt", "");

		// Verifica se há pelo menos 4 caracteres antes do número
		if (baseName.length() < 4) {
			return null;
		}

		// Pega os últimos 4 caracteres antes da extensão
		String numberStr = baseName.substring(baseName.length() - 4);

		// Verifica se são dígitos
		if (!numberStr.matches("\\d{4}")) {
			return null;
		}

		// O prefixo é tudo antes dos 4 dígitos
		String prefix = baseName.substring(0, baseName.length() - 4);

		return new String[]{prefix, numberStr};
	}
	public static boolean isMegaSemana(LocalDate data) {
		while (data.getDayOfWeek().getValue() < 2 | data.getDayOfWeek().getValue() > 5) {
			data = data.plusDays(1); // from 1 (Monday) to 7 (Sunday)
		}
		;
		final LocalDate[] megaSemanas = setMegaSemana();// new LocalDate[8];
		LocalDate dataFinal;
		for (int i = 0; i < 8; i++) {
			dataFinal = megaSemanas[i].plusDays(4);
			if ((data.isEqual(megaSemanas[i]) | data.isAfter(megaSemanas[i]))
					& (data.isEqual(dataFinal) | data.isBefore(dataFinal))) {
				return true;
			}
		}
		return false;
	}

	
	public static void testaScore(final String[] args) throws Exception {

		Calibragem.calcularProbabilidadesTeoricas(2881);
		System.out.println( Arrays.toString(Calibragem.probs));
		System.out.println( Arrays.toString(Calibragem.esperado));
		System.out.println( Arrays.toString(Calibragem.pesos));
		aval(new int[] {1  ,    1    , 12  , 129  ,  569  ,  1202});
		aval(new int[] {2  ,    3    , 17  , 140  ,  574  ,  1243});
		aval(new int[] {0  ,    2    , 10  , 154  ,  607  ,  1305});
		aval(new int[] {0  ,    1    , 14  , 135  ,  596  ,  1219});
		aval(new int[] {0  ,    0    , 0  , 541  ,  600  ,  1216});
		return;
	}
	public static void aval(int[] array) {
		int[] var = {array[5],array[4],array[3],array[2],array[1],array[0]};
		double score=Calibragem.avaliarPerformance(var);
		System.out.println( Arrays.toString(array) +" :"+ score+" - "+determinarClassificacao(score));
	}
	public static void main(final String[] args) throws Exception {
		boolean aquivoZipInformado = false;
		boolean aquivoRnkInformado = false;
		boolean aquivoTxtInformado = false;
		boolean profundidadeInformada = false;
		boolean inclusaoInformada = false;
		boolean waitSorteio = false;
		boolean verificaData = false;
		boolean soVerificaData = false;
		boolean lerTxtInformado = false;
		continuidade = false;
		boolean semNovoCalculo = false;
		final byte[] sorteioParaIncluir = new byte[6];
		final File flagSaidaForcada = new File("saidaForcada.flg");
		Duration duracaoWaitSorteio = Duration.ZERO ;
		
		gm.registrarMensagem("=====================================   *********    ===================================================");
		gm.registrarMensagem("Inicio do processamento do sistema."+ Arrays.toString(args).replace(",", ""));
		gm.setLimite(500, ModoLimite.TAMANHO_KB);
		gm.mostrarInfoLog();
		
		gm.forcarReducaoLog();
		gm.mostrarInfoLog();
		resultado = resultado.lastSorteio();
		if (flagSaidaForcada.exists()) {
			flagSaidaForcada.delete();
			gm.registrarMensagem("Saida forçada pelo flag detectada no inicio do processamento. Arquivo apagado!");
		}
		if (args.length > 0) {
			for (final String argument : args) {
				if (aquivoZipInformado) {
					nomeZipDados = argument;
					aquivoZipInformado = false;
					continue;
				}
				if (aquivoRnkInformado) {
					if (continuidade) {
						gm.registrarMensagem("Os parametro ( -R nomeRank e -C ) não podem ser utilizados conjuntamente");
						new java.io.File("Flag_saida_forcada.flg").createNewFile();
						return;
					}
					nomeRankCalibragem = argument;
					nomeRankCalibragemCarga = nomeRankCalibragem;
					aquivoRnkInformado = false;
					continue;
				}
				if (aquivoTxtInformado) {
					nomeTxtFile = argument;
					aquivoTxtInformado = false;
					continue;
				}
				if (waitSorteio) {
					sorteioParaAguardar = Integer.parseInt(argument);
					resultado = resultado.lastSorteio();
					if (sorteioParaAguardar < resultado.getNumero() ) {
						gm.registrarMensagem(
								"Solicitado para aguardar o sorteio "+sorteioParaAguardar+" sendo que já estamos no sorteio número "+resultado.getNumero());
						new java.io.File("Flag_saida_forcada.flg").createNewFile();
						return;

					}
					if (sorteioParaAguardar == 9999) {
						/// getSorteio();
						/// sorteioParaAguardar = numSorteio + 1;
						// //sorteioParaAguardar = nextSorteio();
						LocalDate dataAtual = LocalDate.now();
						DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
						String dataFormatada = dataAtual.format(formatter);
						if (resultado.getDataApuracao().contentEquals(dataFormatada) ){
							sorteioParaAguardar = resultado.getNumero();
						} else {
							sorteioParaAguardar = resultado.getNumeroConcursoProximo();
						}
					}
					waitSorteio = false;
					continue;
				}
				if (profundidadeInformada) {
					profundidade = Byte.parseByte(argument);
					if (profundidade > 8 | profundidade < 2) {
						gm.registrarMensagem(
								"A profundidade permitida deve estar entre os níveis de 2 até 8 e a profundidade informada foi de "
										+ profundidade);
						new java.io.File("Flag_saida_forcada.flg").createNewFile();
						return;
					}
					ver = ver.substring(0, 5) + profundidade;
					profundidadeInformada = false;
					continue;
				}
				if (inclusaoInformada) {
					String[] tempArray;
					tempArray = argument.replace("(", "").replace(")", "").split("\\.");
					if (tempArray.length != 6) {
						gm.registrarMensagem(
								"É necessário informar as 6 dezenas do sorteio a ser incluído. Foram informadas "
										+ tempArray.length + " dezenas.");
						new java.io.File("Flag_saida_forcada.flg").createNewFile();
						return;
					}
					for (byte n = 0; n < 6; n++) {
						sorteioParaIncluir[n] = Byte.parseByte(tempArray[n]);
						if (sorteioParaIncluir[n] < 1 | sorteioParaIncluir[n] > 60) {
							gm.registrarMensagem("Dezena informada fora do intervalo entre 1 e 60. Dezena informada foi : "
									+ sorteioParaIncluir[n] + " .");
							new java.io.File("Flag_saida_forcada.flg").createNewFile();
							return;
						}
					}
					for (byte n = 0; n < 5; n++) {
						for (byte k = (byte) (n + 1); k < 6; k++) {
							if (sorteioParaIncluir[n] == sorteioParaIncluir[k]) {
								gm.registrarMensagem("As dezenas informadas : " + (n + 1) + " (" + sorteioParaIncluir[n]
										+ ") e " + (k + 1) + " (" + sorteioParaIncluir[k] + ") são iguais. ");
								new java.io.File("Flag_saida_forcada.flg").createNewFile();
								return;
							}
						}
					}
					inclusaoInformada = false;
					continue;
				}
				if (argument.toUpperCase().equals("-V")) {
					System.out.print(ver);
					return;
				}
				if (argument.toUpperCase().equals("-D")) {
					verificaData = true;
					continue;
				}
				if (argument.toUpperCase().equals("-Q")) {
					soVerificaData = true;
					continue;
				}
				if (argument.toUpperCase().equals("-Z")) {
					aquivoZipInformado = true;
					continue;
				}
				if (argument.toUpperCase().equals("-R")) {
					aquivoRnkInformado = true;
					continue;
				}
				if (argument.toUpperCase().equals("-T")) {
					aquivoTxtInformado = true;
					continue;
				}
				if (argument.toUpperCase().equals("-P")) {
					profundidadeInformada = true;
					continue;
				}
				if (argument.toUpperCase().equals("-I")) {
					inclusaoInformada = true;
					continue;
				}
				if (argument.toUpperCase().equals("-X")) {
					lerTxtInformado = true;
					continue;
				}
				if (argument.toUpperCase().equals("-W")) {
					waitSorteio = true;
					continue;
				}
				if (argument.toUpperCase().equals("-C")) {
					if (nomeRankCalibragem.length() != 0) {
						System.out.println("Os parametro ( -R nomeRank e -C ) não podem ser utilizados conjuntamente");
						new java.io.File("Flag_saida_forcada.flg").createNewFile();
						return;
					}
					continuidade = true;
					continue;
				}
				if (argument.toUpperCase().equals("-H") | argument.toUpperCase().equals("-?")) {
					System.out.println(
							"Sorteados [-v] [-x] [ [-z nomeArquiviZip] ou  [-c] ] [-r nomeArquivoRank] [-p numeroDaProfundidade {2-8}] [-t nomeArquivodeSaida] [-d] [-q] "
									+ "[-i IncluirSorteio {(Dez01.Dez02.Dez03.Dez04.Dez05.Dez06)}] -w [numeroSorteio|9999]");
					new java.io.File("Flag_saida_forcada.flg").createNewFile();
					return;
				} else {
					System.out.println("Parametro (" + argument
							+ ") não reconhecido como parametro válido. Os parametros válidos são:");
					System.out.println(
							"Sorteados [-v]  [-x] [ [-z nomeArquiviZip] ou  [-c] ] [-r nomeArquivoRank] [-p numeroDaProfundidade {2-8}] [-t nomeArquivodeSaida] [-d] [-q] "
									+ "[-i IncluirSorteio {(Dez01.Dez02.Dez03.Dez04.Dez05.Dez06)}] -w [numeroSorteio|9999]");
					new java.io.File("Flag_saida_forcada.flg").createNewFile();
					return;
				}
			}
		}
		if (inclusaoInformada) {
			System.out.println(
					"Quando utilizado o parâmetro -i, é necessário informar as 6 dezenas do sorteio a serem incluídas. Não foram informadas nenhuma dezena.");
			new java.io.File("Flag_saida_forcada.flg").createNewFile();
			return;
		}
		// nomeZipDados="c:\\$JRR\\Java\\Mega\\src\\File_Zip_Mega.zip";
		// nomeRankCalibragem="c:\\$JRR\\Java\\Mega\\teste08\\RankCalibragem58-3.txt";
		final long tInicialDoPrograma = System.currentTimeMillis();
		gm.registrarMensagem("Processados os parametros de entrada.");
		if (nomeRankCalibragem.length() == 0) {
			// nomeRankCalibragem = nomeRankCalibragem;
			// } else {
			nomeRankCalibragem = "RankCalibragem" + ver + ".ZIP";
			if (continuidade) {
				final File fileCont = new File("Continuidade.ZIP");
				if (fileCont.exists()) {
					nomeRankCalibragemCarga = "Continuidade.ZIP";
				} else {
					nomeRankCalibragemCarga = "RankCalibragem" + (ver.substring(0, 3) + (profundidade - 1)) + ".ZIP";
				}
			} else {
				nomeRankCalibragemCarga = nomeRankCalibragem;
			}
		}
		if (nomeTxtFile.length() == 0) {
			nomeTxtFile = getSequencia("F:\\WrkPlc\\Relatorios", "Lixo_v_" + ver + "-");
			gm.registrarMensagem("Definido nome temporario "+nomeTxtFile,DestinoMensagem.LOG );
		}
		saidaSolicitada = false;
		final PrintStream arquivo = new PrintStream(new File(nomeTxtFile));
		final PrintStream console = System.out;
		System.setOut(arquivo);
		final File f = new File(".");
		final Date d = GregorianCalendar.getInstance().getTime();
		final SimpleDateFormat format = new SimpleDateFormat();
		format.setTimeZone(TimeZone.getTimeZone("GMT-3:00"));
		// System.out.println("\nO programa esta executando no "+ f.getAbsolutePath());
		gm.registrarMensagem("Parâmetros do processamento : " + Arrays.toString(args).replace(",", "")
				+ "\nO programa esta executando no " + f.getAbsolutePath() + "\nVersão do programa : " + ver
				+ "\nVersão do java : " + System.getProperty("java.version") + "\nVersão do JRE  : "
				+ System.getProperty("java.runtime.version") + "\nProcessado com "
				+ Runtime.getRuntime().availableProcessors()
				+ " processadores, e com "
				+ (Runtime.getRuntime().maxMemory() / (double) (1000 * 1000 * 1000)) + " GB de memória disponível para o processamento."
				+ "\nProfundidade da calibragem em nível :" + profundidade
				+ "\nO relatório irá calcular para jogos de " + NUMMAXDEZENAS + " dezenas."
				+ "\nArquivo base de dados do rank :"
				+ ((nomeRankCalibragem.length() == 0)
						? " não fornecido (sera utilizado o arquivo " + "RankCalibragem" + ver + ".ZIP" + " )."
						: nomeRankCalibragem)
				+ "\nArquivo base de dados dos sorteios:"
				+ ((nomeZipDados.length() == 0) ? " não fornecido (será baixado novo arquivo da Caixa)." : nomeZipDados)
				+ "\nProcessado em :" + format.format(d),false);
		colecao = new Colecao();
		System.out.println();
		// Debug remover
		//listaOrdemSorteio = readAPI(verificaData, console, arquivo,2928);
		if (sorteioParaAguardar > 0 & sorteioParaIncluir[0] == 0) {
			System.setOut(console);

			ExecutorService executor = Executors.newFixedThreadPool(1);
			Future<?> tarefa1 = executor.submit(() -> {
				try {
					gm.registrarMensagem("Inicio carga paralela do Rank "+nomeRankCalibragemCarga,DestinoMensagem.LOG );
					carregaRankCalibragem(nomeRankCalibragemCarga);
				} catch (IOException e) {
					System.err.println("Erro: " + e.getMessage());
				}
			});

			duracaoWaitSorteio= waitSorteio(sorteioParaAguardar);

			try {
				tarefa1.get(); // Aguarda a conclusão
				gm.registrarMensagem("Chegada do sorteio e fim da carga paralela do Rank "+nomeRankCalibragemCarga);
				//System.out.println("Tarefa finalizada");
			} catch (InterruptedException | ExecutionException e) {
				e.printStackTrace();
			} finally {
				executor.shutdown();
			}

			System.setOut(arquivo);
		}

		if (lerTxtInformado) {
			/// incluir leitura txt
			listaOrdemSorteio = readFileTXT(verificaData, console, arquivo);
		} else {
			if (nomeZipDados.length() > 0) {
				System.out.println("Usando arquivo ZIP " + nomeZipDados);
			} else {
				if (sorteioParaAguardar > 0 & sorteioParaIncluir[0] == 0) {
					listaOrdemSorteio = readAPI(verificaData, console, arquivo,sorteioParaAguardar);
				}else {
					listaOrdemSorteio = readAPI(verificaData, console, arquivo);
				}
				if (listaOrdemSorteio.size()==0) {
					return;
				}
			}
		}
		if (sorteioParaIncluir[0] != 0) {
			// Testa se sorteio para ser incluido já não consta na lista
			final int lstReg = listaOrdemSorteio.size() - 1;
			final byte[] sorteioParaComparar = listaOrdemSorteio.get(lstReg);
			if (Arrays.equals(sorteioParaComparar, sorteioParaIncluir)) {
				System.setOut(console);
				System.out.println("Não foi adicionado manualmente o sorteio de numero " + numSorteio
						+ " com as seguintes dezenas: " + Arrays.toString(sorteioParaIncluir) + " .");
				System.setOut(arquivo);
				System.out.println("Não foi adicionado manualmente o sorteio de numero " + numSorteio
						+ " com as seguintes dezenas: " + Arrays.toString(sorteioParaIncluir) + " .");
			} else {
				listaOrdemSorteio.add(sorteioParaIncluir);
				numSorteio++;
				System.setOut(console);
				System.out.println("Estamos adicionando manualmente o sorteio de numero " + numSorteio
						+ " com as seguintes dezenas: " + Arrays.toString(sorteioParaIncluir) + " .");
				System.setOut(arquivo);
				System.out.println("Estamos adicionando manualmente o sorteio de numero " + numSorteio
						+ " com as seguintes dezenas: " + Arrays.toString(sorteioParaIncluir) + " .");
			}
		}
		if (verificaData) {
			System.setOut(console);
			gm.registrarMensagem("Processamento com base no sorteio número " + numSorteio + " "
					+ Arrays.toString(listaOrdemSorteio.get(listaOrdemSorteio.size() - 1)) + " de " + dataSorteio);
			System.setOut(arquivo);
			if (soVerificaData) {
				return;
			}
		}
		LocalDate ld = LocalDate.of(Integer.parseInt(dataSorteio.substring(6, 10)),
				Integer.parseInt(dataSorteio.substring(3, 5)), Integer.parseInt(dataSorteio.substring(0, 2)));
		ld = ld.plusDays(1);
		String nomeMegaSemana = "";
		if (isMegaSemana(ld)) {
			nomeMegaSemana = qualMegaSemana(ld);
		}
		final DecimalFormat df = new DecimalFormat("###,###,###,###,###.00");
		System.setOut(arquivo);
		System.out.println(
				"=======================================================================================================");
		System.out.println("\nProcessamento com base no sorteio número " + numSorteio + " de " + dataSorteio);
		if (sorteioParaIncluir[0] == 0) {
			System.out.println("Neste sorteio " + (qtdGandoresSena == 0 ? "não " : "") + "houve "
					+ (qtdGandoresSena != 0 ? qtdGandoresSena : "") + " ganhador" + (qtdGandoresSena > 1 ? "es " : "")
					+ " da sena." +
					(qtdGandoresSena == 0 ? (" O prêmio está acumulado ha "+numeroSorteiosAcumulado+" sorteios.") : ""));
			if (qtdGandoresSena != 0) {
				System.out.println(
						"O rateio da sena foi de " + String.format("%1$12s", df.format(rateioSena)).replace(" ", ""));
			}
			System.out.println(
					"O rateio da quina foi de " + String.format("%1$12s", df.format(rateioQuina)).replace(" ", ""));
			System.out.println(
					"O rateio da quadra foi de " + String.format("%1$12s", df.format(rateioQuadra)).replace(" ", ""));
			if (qtdGandoresSena == 0) {
				System.out.println("O valor acumulado é de " + df.format(valorAcumulado));
			}
			System.out.println("A estimativa de prêmio para o próximo sorteio é de " + df.format(estimativaPremio));
			System.out.println("A estimativa de prêmio para a Mega da Virada é de " + df.format(estimativaMegaVirada));
		} else {
			System.out.println("Sem estimativas dos rateios do ultimo sorteio incluido manualmente.");
		}
		if (numSorteio % 10 == 4 | numSorteio % 10 == 9) {
			System.out.println("O próximo sorteio, o de número " + (numSorteio + 1) + ", ocorrerá em "
					+ dataProximoSorteio + " às 21:00h, vai ratear o valor acumulado nos últimos 4 sorteios.");
		} else { 
			System.out.println("O próximo sorteio, o de número " + (numSorteio + 1) + ", ocorrerá em "
					+ dataProximoSorteio + " às 21:00h.");
		}
		if (!nomeMegaSemana.isEmpty()) {
			System.out.println("\n* * *  *    *   Mega-semana " + nomeMegaSemana
					+ " com sorteios na terça-feira, quinta-feira e no Sábado. * * *  *    *\n");
		}
		System.out.println("\nApostas eletrônicas via site da caixa até as 20:00 de "+dataProximoSorteio+".");
		System.out.println(
				"\n=======================================================================================================");
		final File file = new File(nomeRankCalibragemCarga);
		System.setOut(console);
		Future<?> tarefa2 = null;
		ExecutorService executor2 = null;
		if (file.exists()) {
			if (rankCalibragemFinal.size()==0) {
				carregaRankCalibragem(nomeRankCalibragemCarga);
			}
			if (!continuidade | nomeRankCalibragem.equals("Continuidade.ZIP")) {
				if (listaOrdemSorteio.size() < ultimoSorteioNoRank) {
					System.out.println(
							" ******************  URGENTE   ARQUIVO de memoria do Rank mais atual que o arquivo com dados dos Sorteios **************");
				} 
			}
			calculoDosFatores() ; // Popula a Coleção com a Tabela de Sorteios e 
			System.setOut(console);
			if (listaOrdemSorteio.size() > 0) {
				calibragemParalelaDosFatores();
				executor2 = Executors.newFixedThreadPool(1);
				tarefa2 = executor2.submit(() -> {
					System.setOut(console);
					gm.registrarMensagem("🚀 INICIANDO thread background");
					System.out.flush();
					try {
						gm.registrarMensagem("Inicio gravação paralela do Rank "+nomeRankCalibragemCarga,DestinoMensagem.LOG );
						gravaRankCalibragem(numSorteio);
						gm.registrarMensagem("✅ FINALIZANDO thread background");
					} catch (IOException e) {
						System.err.println("Erro: " + e.getMessage());
						System.err.flush();
					}
				});
			} else {
				if (verificaData) {
					gm.registrarMensagem("Sem sorteio para novo cálculo das fórmulas.");
					semNovoCalculo = true;
				}
			}
		} else {
			geraZerezima();
			calibragemParalelaDosFatores();
			executor2 = Executors.newFixedThreadPool(1);
			tarefa2 = executor2.submit(() -> {
				gm.registrarMensagem("🚀 INICIANDO thread background");
				System.out.flush();
				try {
					gm.registrarMensagem("Inicio gravação paralela do Rank "+nomeRankCalibragemCarga,DestinoMensagem.LOG );
					gravaRankCalibragem(numSorteio);
					gm.registrarMensagem("✅ FINALIZANDO thread background");
				} catch (IOException e) {
					System.err.println("Erro: " + e.getMessage());
					System.err.flush();
				}
			});
		}
		if (!saidaSolicitada) {
			gm.registrarMensagem("                 *********************   Determina indices atuais    ****************");
			System.setOut(arquivo);
			System.out.println("\nNo sorteio anterior (" + numSorteio + ") foram computadas : "
					+ String.format("%,d senas, ", resultados[0]) + String.format("%,d quinas e ", resultados[1])
					+ String.format("%,d quadras.\n", resultados[2]));
			// colecao.print();
			gm.setDuration(3);
			relatorioApostas(numSorteio + 1, arquivo, console, semNovoCalculo);
		}
		
		if (tarefa2 != null ) {
			System.setOut(console);
			gm.registrarMensagem("Termino do processamento. Verificando o termino da gravação paralela do Rank "+nomeRankCalibragem);
			try {
				tarefa2.get(); // Aguarda a conclusão
				gm.registrarMensagem("Termino do processamento e fim da gravação paralela do Rank "+nomeRankCalibragem);
			} catch (InterruptedException | ExecutionException e) {
				e.printStackTrace();
			} finally {
				executor2.shutdown();
			}
		}
		System.setOut(arquivo);
		final SimpleDateFormat formatoData = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
		formatoData.setTimeZone(TimeZone.getTimeZone("GMT-3:00"));
		final Calendar data = Calendar.getInstance();
		System.out.println("");
		System.out.println(
				"\n\rProcessamento encerrado, " + "em " + formatoData.format(data.getTime()) + " com duração de "
						+ getHMS(tInicialDoPrograma) + "." + "\n\rVerifique resultados no arquivo :" + nomeTxtFile);
		arquivo.close();
		System.setOut(console);
		Duration duracaoTotal = Duration.ofMillis(System.currentTimeMillis()- tInicialDoPrograma );
		gm.registrarMensagem("Processado com o maior intervalo de : "+gm.getMaiorIntervaloFormatado());
		gm.registrarMensagem(
				"Processamento encerrado : "+ formatarDuracao(duracaoTotal) + " de duração" +
		       (gm.getTempoHibernacao().isPositive()?" - hibernação de aproximadamente "+gm.getTempoHibernacaoFormatado():"")+
		       (duracaoWaitSorteio.isPositive()?"  - espera do sorteio "+
		       String.format("%02d:%02d:%02d", duracaoWaitSorteio.toHours(), duracaoWaitSorteio.toMinutesPart(), duracaoWaitSorteio.toSecondsPart()):"")+
			   (gm.getTempoHibernacao().isPositive()|duracaoWaitSorteio.isPositive()?
								" = "+formatarDuracao(duracaoTotal.minus(gm.getTempoHibernacao()).minus(duracaoWaitSorteio))+" tempo de processamento.":"."));
		gm.registrarMensagem( "Verifique resultados no arquivo :" + nomeTxtFile);

		console.close();
		gm.registrarMensagem("Final do processamento do sistema.");
	}
	private static String formatarDuracao(Duration duration) {
        long horas = duration.toHours();
        long minutos = duration.toMinutesPart();
        long segundos = duration.toSecondsPart();
        
        return String.format("%02d:%02d:%02d", horas, minutos, segundos);
        }

	// Recebe uma matriz com n elementos e a definição da quantidade de itens
	// classificados na resposta.
	// Retorna um matriz indice dos maiores elementos. Exemplo retorno[3]=11 ==> O
	// terceiro maior numero é o contido no original[11]
	public static int[] maiores(final int[] original, int qtd) {
		if (qtd > original.length) {
			qtd = original.length;
		}
		final int[] myOriginal = original.clone();
		final int[] numeros = new int[qtd];
		int maior = -1;
		int indiceMaior = 0;
		for (int i = 0; i < qtd; i++) {
			maior = -1;
			indiceMaior = 0;
			for (int j = 0; j < myOriginal.length; j++) {
				if (myOriginal[j] > maior) {
					maior = myOriginal[j];
					indiceMaior = j;
				}
			}
			numeros[i] = indiceMaior;
			myOriginal[indiceMaior] = -1;
		}
		return numeros;
	}

	public static int numFormulas(final int tipo, final int peso, final int quantidade) {
		return (int) (Math.pow(tipo * (peso - 1) + 1, quantidade) - 1);
	}

	private static String openAposta(final long aposta) {
		String retorno = "";
		for (byte n = 1; n < 61; n++) {
			if ((aposta & baseHash[n]) == baseHash[n]) {
				retorno += String.format("%02d ", n);
			}
		}
		return retorno;
	}

	private static byte[] openApostaNum(final long aposta) {
		final byte[] retorno = new byte[10];
		byte contador = 0;
		for (byte n = 1; n < 61; n++) {
			if ((aposta & baseHash[n]) == baseHash[n]) {
				retorno[contador++] = n;
				if (contador == 10) {
					break;
				}
			}
		}
		return retorno;
	}

	private static short pontos(final byte[] sorteioAtual, final AbstractIndice abstractIndice, final byte subNumber,
			final TipoIndice tipo) {
		int pontos = 0;
		for (byte testSorteio = 0; testSorteio < 6; testSorteio++) {
			for (byte testIndice = 1; testIndice <= 10; testIndice++) {
				if (sorteioAtual[testSorteio] == abstractIndice.getDezenaDoRank(testIndice, tipo, subNumber)) {
					pontos++;
				}
			}
		}
		/*
		 * if (pontos >= 6) { String teste = ""; for (byte testIndice = 1; testIndice <=
		 * 10; testIndice++) { teste += abstractIndice.getDezenaDoRank(testIndice, tipo,
		 * subNumber) + " "; } System.out.println( "Total de " + pontos +
		 * " acertos. Sorteio: " + Arrays.toString(sorteioAtual) + " ==> " + teste);
		 * System.out.println("Mega sena !!!"); }
		 */
		return (short) (Math.pow(3, pontos) - 1);
	}

	private static void printResumo() {
		byte[] jogo;
		byte[] dezenas;
		System.out.println("");
		System.out.println(" Jg Formula       Dezenas       Qtd Valor acumulado");
		System.out.println("=== =======   ================= === ===============");
		for (int conta = 0; conta < listaJogos.size(); conta++) {
			jogo = listaJogos.get(conta);
			dezenas = new byte[] { jogo[1], jogo[2], jogo[3], jogo[4], jogo[5], jogo[6] };
			System.out.println(String.format("%3d", (conta + 1)) + "-" + String.format("%7d", jogo[0]) + "   "
					+ String.format("%02d", dezenas[0]) + " " + String.format("%02d", dezenas[1]) + " "
					+ String.format("%02d", dezenas[2]) + " " + String.format("%02d", dezenas[3]) + " "
					+ String.format("%02d", dezenas[4]) + " " + String.format("%02d", dezenas[5]) + " "
					+ String.format("%03d", jogo[7]) + String.format("%16.2f ", ((conta + 1) * precoAposta)));
		}
	}

	private static void processa(final byte[] aposta, final byte numJogo) {
		for (int x = 4; x < 9; x++) {
			for (int y = x + 1; y < 10; y++) {
				adicionaJogo(aposta[0], aposta[1], aposta[2], aposta[3], aposta[x], aposta[y], numJogo);
			}
		}
	}

	public static String prtDezenas(final byte[] dezenas) {
		String retorno = "";
		for (final int dezena : dezenas) {
			retorno += String.format(" %02d", dezena);
		}
		return retorno;
	}

	public static String qualMegaSemana(LocalDate data) {
		while (data.getDayOfWeek().getValue() < 2 | data.getDayOfWeek().getValue() > 5) {
			data = data.plusDays(1);
		}
		;
		final LocalDate[] megaSemanas = setMegaSemana();// new LocalDate[8];
		// Registrar a terca feira da semana
		final String[] megaSemanaNome = new String[8];
		megaSemanaNome[0] = "Verão";
		megaSemanaNome[1] = "Pascoa";// "Carnaval";
		megaSemanaNome[2] = "Mães";
		megaSemanaNome[3] = "Férias";
		megaSemanaNome[4] = "Pais";
		megaSemanaNome[5] = "Primavera";
		megaSemanaNome[6] = "Sorte";
		megaSemanaNome[7] = "Natal";
		LocalDate dataFinal;
		for (int i = 0; i < 8; i++) {
			dataFinal = megaSemanas[i].plusDays(4);
			if ((data.isEqual(megaSemanas[i]) | data.isAfter(megaSemanas[i]))
					& (data.isEqual(dataFinal) | data.isBefore(dataFinal))) {
				return megaSemanaNome[i];
			}
		}
		return "";
	}
	public static List<byte[]> readAPI(final boolean verificaData, final PrintStream console, final PrintStream arquivo) throws Exception{
		return readAPI( verificaData, console,  arquivo,0);
	}
	public static List<byte[]> readAPI(final boolean verificaData, final PrintStream console, final PrintStream arquivo, int sorteioParaAguardar)
			throws Exception {
		final List<byte[]> numSorteados = new ArrayList<>();
		final byte anExample[] = new byte[6];
		System.setOut(console);
		gm.registrarMensagem("Entrada no ReadAPI com sorteio :"+sorteioParaAguardar+".");
		try {
			final String nomeTXT = "F:\\WrkPlc\\rede\\SorteiosV4.txt";
			// DEBUG Remover
			final String baseUrl = "https://servicebus2.caixa.gov.br/portaldeloterias/api/megasena";
			//final String baseUrl = "https://httpstat.us/403";
			final int qtdLinhasNoTxt = contarLinhas(nomeTXT);
			String linha;
			String url;
			// //JSONObject jsonResponse;
			JSONArray dezenasSorteadas;
			JSONArray listaRateioPremio;
			numeroSorteiosAcumulado=0;
			if (qtdLinhasNoTxt > 0) {
				final File arquivoTXT = new File(nomeTXT);
				String[] partes;
				try (BufferedReader leitor = new BufferedReader(new FileReader(arquivoTXT))) {
					for (int n = 1; n <= qtdLinhasNoTxt; n++) {
						linha = leitor.readLine();
						partes = linha.split(",");
						for (int k = 0; k < 6; k++) {
							anExample[k] = Byte.parseByte(partes[k].trim());
						}
						numSorteados.add(anExample.clone());
						numeroSorteiosAcumulado=Integer.parseInt(partes[6].trim());
					}
				}
			}
			// int confirmado=0;
			int concursoFinal = 0;
			// //while (confirmado==0) {
				while (concursoFinal==0){
					//gm.registrarMensagem("Na leitura do TXT erro na obtenção do numero do ultimo sorteio.");
					resultado = resultado.lastSorteio();
					concursoFinal = resultado.getNumero();
					if (sorteioParaAguardar>0 & sorteioParaAguardar != concursoFinal) {
						gm.registrarMensagem("O concurso disponível ("+concursoFinal+") está diferente do concurso alvo do processsamento ("+
								sorteioParaAguardar+"). A data prevista para o proximo sorteio "+resultado.getDataProximoConcurso()+" Vou tentar novamente.");
						concursoFinal=0;
					} else if (concursoFinal != 0 ) {
						gm.registrarMensagem("Obtido "+concursoFinal+" como numero do ultimo sorteio.");
						break;
					}
					
				}
				/*// //
				gm.registrarMensagem("Confirmando "+concursoFinal+" como numero do ultimo sorteio.");
				confirmado = lastSorteio();
				if (confirmado == concursoFinal ) {
					gm.registrarMensagem("Confirmado "+confirmado+" como numero do ultimo sorteio.");
					break;
				} else {
					gm.registrarMensagem("Falha na confirmacao do "+concursoFinal+" como numero do ultimo sorteio.");
					concursoFinal=0;
					confirmado=0;
				}
			}
			*/ // //
			if (qtdLinhasNoTxt < concursoFinal) {
				for (int n = (qtdLinhasNoTxt + 1); n <= concursoFinal; n++) {
					url = baseUrl + "/" + n;
					dezenasSorteadas = getDezenas(url);
					for (int i = 0; i < 6; i++) {
						anExample[i] = Byte.parseByte(dezenasSorteadas.getString(i).trim());
					}
					numSorteados.add(anExample.clone());
					listaRateioPremio = getPremio(url);
					if ( listaRateioPremio.getJSONObject(0).getInt("numeroDeGanhadores") > 0) {
						numeroSorteiosAcumulado = 0;
					} else {
						numeroSorteiosAcumulado +=  1 ;
					}
				}
			}
			ultimoSorteioNaTabela = numSorteados.size();
			/*// //
			while (true) {

				jsonResponse = getJsonFromUrlWithoutCache(baseUrl);

				// Obtém os campos solicitados
				listaRateioPremio = jsonResponse.getJSONArray("listaRateioPremio");
				rateioSena = listaRateioPremio.getJSONObject(0).getDouble("valorPremio");
				qtdGandoresSena = listaRateioPremio.getJSONObject(0).getInt("numeroDeGanhadores");
				rateioQuina = listaRateioPremio.getJSONObject(1).getDouble("valorPremio");
				rateioQuadra = listaRateioPremio.getJSONObject(2).getDouble("valorPremio");
				dataSorteio = jsonResponse.getString("dataApuracao");
				dataProximoSorteio = jsonResponse.getString("dataProximoConcurso");
				numSorteio = (short) jsonResponse.getInt("numero");
				valorAcumulado = jsonResponse.getDouble("valorAcumuladoProximoConcurso");
				estimativaPremio = jsonResponse.getDouble("valorEstimadoProximoConcurso");
				if (numSorteio == (int) concursoFinal) {
					break;
				} else {
					System.setOut(console);
					gm.registrarMensagem("# # # # # #   =======>>>  Estamos como  numSorteio: " + numSorteio
							+ " Com o concursoFinal: " + concursoFinal+" tentando ajustar.("+baseUrl+").");
					System.setOut(arquivo);
				}
			}
			*/ // //
			//listaRateioPremio = jsonResponse.getJSONArray("listaRateioPremio");
			rateioSena = resultado.getListaRateioPremio().get(0).getValorPremio();// // listaRateioPremio.getJSONObject(0).getDouble("valorPremio");
			qtdGandoresSena = resultado.getListaRateioPremio().get(0).getNumeroDeGanhadores() ; // //listaRateioPremio.getJSONObject(0).getInt("numeroDeGanhadores");
			rateioQuina = resultado.getListaRateioPremio().get(1).getValorPremio(); // //listaRateioPremio.getJSONObject(1).getDouble("valorPremio");
			rateioQuadra = resultado.getListaRateioPremio().get(2).getValorPremio();// // listaRateioPremio.getJSONObject(2).getDouble("valorPremio");
			dataSorteio = resultado.getDataApuracao(); // //.getString("dataApuracao");
			dataProximoSorteio = resultado.getDataProximoConcurso();// // jsonResponse.getString("dataProximoConcurso");
			numSorteio = resultado.getNumero() ;// //(short) jsonResponse.getInt("numero");
			valorAcumulado = resultado.getValorAcumuladoProximoConcurso(); // //jsonResponse.getDouble("valorAcumuladoProximoConcurso");
			estimativaPremio = resultado.getValorEstimadoProximoConcurso(); // //jsonResponse.getDouble("valorEstimadoProximoConcurso");
			estimativaMegaVirada = resultado.getValorAcumuladoConcursoEspecial(); // //jsonResponse.getDouble("valorEstimadoProximoConcurso");
			if (numSorteio != (int) concursoFinal | numSorteados.size() != numSorteio ) {
				System.setOut(console);
				gm.registrarMensagem("###########   =======>>>");
				gm.registrarMensagem("###########   =======>>>");
				gm.registrarMensagem("###########   =======>>>  Estamos como  numSorteio: " + numSorteio
						+ " Com o concursoFinal: " + concursoFinal + " E o array numSorteados, com: "+numSorteados.size()+ " elementos.");
				gm.registrarMensagem("###########   =======>>>");
				gm.registrarMensagem("###########   =======>>>");
				System.setOut(arquivo);
				numSorteados.clear();
				return numSorteados;
			} else {
				return numSorteados;
			}	
		} catch (final IOException e) {
			e.printStackTrace();
			numSorteados.clear();
			return numSorteados; // Retorna -1 em caso de erro na leitura do arquivo
		}
	}

	private static JSONArray getPremio(String url) throws Exception {
		while (true) {
			try {
				JSONObject jsonResponse = intService.getJsonFromUrlWithoutCache(url);
				JSONArray listaRateioPremio = jsonResponse.getJSONArray("listaRateioPremio");
				return listaRateioPremio;
			} 
			catch (final IOException e) {
				e.printStackTrace();
			}
		}
	}

	private static JSONArray getDezenas(String url) throws Exception {
		while (true) {
			try {
				JSONObject jsonResponse = intService.getJsonFromUrlWithoutCache(url);
				JSONArray dezenasSorteadas = jsonResponse.getJSONArray("dezenasSorteadasOrdemSorteio");
				return dezenasSorteadas;
			} 
			catch (final IOException e) {
				e.printStackTrace();
			}
		}
	}


	public static List<byte[]> readFileTXT(final boolean verificaData, final PrintStream console,
			final PrintStream arquivo) {
		final String nomereadFileTXT = "F:\\WrkPlc\\rede\\SorteiosV4.txt";
		gm.registrarMensagem("Inicio da carga do arquivo " + nomereadFileTXT );
		BufferedReader br = null;
		final List<byte[]> numSorteados = new ArrayList<>();
		final byte anExample[] = new byte[6];
		try {
			br = Files.newBufferedReader(Paths.get(nomereadFileTXT), StandardCharsets.UTF_8);
			String line = null;

			while ((line = br.readLine()) != null) {

				line = line.replaceAll("\t", ",");
				line = line.replaceAll(" ", ",");
				line = line.replaceAll(",,", ",");
				line = line.replaceAll(",,", ",");
				final String[] values = line.split(",");

				for (int k = 0; k < 6; k++) {
					anExample[k] = Byte.parseByte(values[k]);
				}
				numSorteados.add(anExample.clone());
				// }
			}
			if (br != null) {
				br.close();
			}
			gm.registrarMensagem("Final da carga do arquivo " + nomereadFileTXT );

		} catch (final FileNotFoundException ex) {
			ex.printStackTrace();
		} catch (final IOException ex) {
			ex.printStackTrace();
		}
		numSorteio = (short) (numSorteados.size());
		dataSorteio = "01/01/2021";
		return numSorteados;
	}

	private static void relatorioApostas(final int sorteioProposto, final PrintStream arquivo,
			final PrintStream console, final boolean semNovoCalculo) throws InterruptedException, ExecutionException {
		// final byte[][] indice = { { 0, 0, 1 }, { 0, 1, 0 }, { 0, 1, 1 }, { 1, 0, 0 },
		// { 1, 0, 1 }, { 1, 1, 0 },
		// { 0, 0, 2 }, { 0, 2, 0 }, { 0, 2, 2 }, { 2, 0, 0 }, { 2, 0, 2 }, { 2, 2, 0 }
		// };
		final DecimalFormat df = new DecimalFormat("###,###,###,###,###.00");
		if (rankCalibragemFinal.size() == 0) {
			System.out.println(
					"\n ************************  Não encontrado registro nas condicoes estabelecidas.  ************************  ");
			return;
		} else {
			final SimpleDateFormat format = new SimpleDateFormat();
			format.setTimeZone(TimeZone.getTimeZone("GMT-3:00"));
			System.setOut(console);
			System.out.println("\n");
			gm.registrarMensagem("Calculando a aposta mais bem classificada para este sorteio.");

			System.setOut(arquivo);

			int qtd = 24;

			//// ---- Impressão do cabeçalho
			System.out.println("\n" + "*".repeat(103));
			System.out.println("           ********** Calculo das dezenas apontadas para apostas no próximo sorteio "
					+  sorteioProposto + " **********");
			System.out.println("*".repeat(103) + "\n");

			bestNumbers(arquivo, console, semNovoCalculo);
			System.out.println("\n" + "*".repeat(103));

			System.out.println("                  Lista dos até " + qtd + " melhores avaliados.  ");
			System.out.println("\n" + "*".repeat(103));

			System.out.println("\n" + "*".repeat(103));
			int[] dezenasMaisApontadas;
			int[] dezenasMaisApontadasGeral;
			dezenasMaisApontadasGeral = new int[60];
			Arrays.fill(dezenasMaisApontadasGeral, 0);
			//Calibragem.setQtdSorteios(numSorteio);
			//Calibragem.calcularProbabilidadesTeoricas(numSorteio);
			List<Calibragem> rCFPrint = new ArrayList<>();
			List<Calibragem> rankCalibragemPrint = Collections.synchronizedList(rCFPrint);
			ProgressPrinter progress = new ProgressPrinter(rankCalibragemFinal.size());
			for (int tipo = 1; tipo < 5; tipo++) {
				Calibragem.calcularProbabilidadesTeoricas(numSorteio);

				listaJogos.clear();
				indiceApostas.clear();

				dezenasMaisApontadas = new int[60];
				Arrays.fill(dezenasMaisApontadas, 0);
				System.setOut(console);
				rankCalibragemPrint = getItensToPrint(rankCalibragemFinal,tipo,qtd , numSorteio ,arquivo,console,progress);
				System.setOut(arquivo);
				if (tipo == 1) {
					System.out.println("");
					System.out.println(
							"\n\n*******    Resultados classificados por ordem de acertos em Sena=> Quina => Quadra ==> terno e num Acertos *****");
				}
				if (tipo == 2) {
					System.out.println(
							"\n\n*******    Resultados classificados por ordem de acertos em Num Acertos => Sena => Quina => Quadra => Ternos => Duplas+Solos  *****");
				}
				if (tipo == 3) {
					Calibragem.calcularProbabilidadesTeoricas(1);
					System.out.println(
							"\n\n*******    Resultados classificados por ordem de acertos em Num Sorteio ==> Score  *****");
				}
				if (tipo == 4) {
					System.out.println("\n\n*******    Resultados classificados por ordem do Score  *****");
				}
				prtCabecalho();
				int cursorMaiores = 0;
				int contador = 0;
				Formula formulaLocal;
				byte[][] dezenasLocalBase = new byte[2][10];
				byte[] dezenasLocal = new byte[10];
				double obtido;
				double ScoreItem;
				while (cursorMaiores < qtd & cursorMaiores < rankCalibragemPrint.size()) {
					item = rankCalibragemPrint.get(contador++);
					formulaLocal = new Formula(item.baseFormula);
					dezenasLocalBase = SoProcessa.defineDezenasUltra(formulaLocal, colecao);// , indice);
					if (dezenasLocalBase == null) {
						Arrays.fill(dezenasLocal, (byte) 0);
					}
					dezenasLocal = dezenasLocalBase[(formulaLocal.filtro ? 0 : 1)];
					for (final int dezenas : dezenasLocal) {
						dezenasMaisApontadas[dezenas - 1]++;
					}
					final int combinacoes = comb(10, 6);
					obtido = item.quadras * rateioQuadra + item.quinas * rateioQuina + item.senas * rateioSena;
					System.out.println("");
					System.out.print(String.format("%4d-", (cursorMaiores + 1)));
					System.out.print(String.format("%1$14s", df.format(obtido / item.numSorteiosComAcerto())));
					System.out.print(String.format("%1$13s", df.format(((combinacoes * precoAposta)))));
					System.out.print(String.format("%1$15s",
							df.format((obtido / item.numSorteiosComAcerto()) - (1 * (combinacoes * precoAposta)))));
					System.out.print(String.format(" %5d %6d %7d", item.senas, item.quinas, item.quadras));
					System.out.print(String.format(" %5d %6d %7d", item.ternos, item.duplas, item.solos));
					System.out.print((formulaLocal.filtro ? "  sim  " : "  não  "));
					System.out.print(String.format(" %6d ", item.ultimoSorteioComAcerto));
					System.out.print(String.format(" %5d ", item.numSorteiosComAcerto()));
					ScoreItem = item.scoreItem();
					System.out.print(String.format("%5.2f", ScoreItem));
					System.out.print(" " + determinarClassificacao(ScoreItem) + " ");
					System.out.print(String.format("%1$" + ((10 * 3)-2) + "s ", prtDezenas(dezenasLocal)));


					System.out.print(
							"" + String.format("%1$16s", formulaLocal.base) + " => " + formulaLocal.toString());
					processa(dezenasLocal, (byte) ((cursorMaiores + 1)));
					cursorMaiores++;
				}
				for (int nEscolhidas = 0; nEscolhidas < 60; nEscolhidas++) {
					dezenasMaisApontadasGeral[nEscolhidas] += dezenasMaisApontadas[nEscolhidas];
				}
				final int[] maisEscolhidasOrdem = maiores(dezenasMaisApontadas, 10);
				System.out.println("");
				System.out.print("\n\nRelacao das 10 dezenas mais apontadas nos resultados acima :");
				for (int nEscolhidas = 0; nEscolhidas < 10; nEscolhidas++) {
					System.out.print(" " + (maisEscolhidasOrdem[nEscolhidas] + 1));
				}
				System.out.println("");
				printResumo();
			}
			final int[] maisEscolhidasOrdem = maiores(dezenasMaisApontadasGeral, 10);
			System.out.println("");
			System.out.print("\n\nRelacao GERAL das 10 dezenas mais apontadas em todos os resultados acima :");
			for (int nEscolhidas = 0; nEscolhidas < 10; nEscolhidas++) {
				System.out.print(" " + (maisEscolhidasOrdem[nEscolhidas] + 1));
			}
			System.out.println("");
		}
	}

	private static List<Calibragem> getItensToPrint(List<Calibragem> rankCalibragemFinal, int tipo, int qtd, short numSorteio2, PrintStream arquivo, PrintStream console, ProgressPrinter progress) {
		List<Calibragem> rCFgetItens = new ArrayList<>();
		List<Calibragem> rankCalibragemTemp = Collections.synchronizedList(rCFgetItens);

		String	nomeRankCalPrint = "RankCalPrint" + ver + "-"+tipo+"-"+numSorteio+".txt";
		final File file = new File(nomeRankCalPrint);
		System.setOut(console);

		if (file.exists() ) {
			gm.registrarMensagem("Carregando a base de dados ordem "+tipo);
			carregaRankCalPrint(nomeRankCalPrint,rankCalibragemTemp, qtd);
		}	else	{
			long t1 = System.currentTimeMillis();
			gm.registrarMensagem("Classificando a base de dados ordem "+tipo);

			Calibragem.ordem = (byte) tipo;

			//Collections.sort(rankCalibragemFinal);
			//CalibragemSorter.sortWithProgress(rankCalibragemFinal);
			//MassSorter.sortWithProgress(rankCalibragemFinal);
			System.setOut(console);
			rankCalibragemTemp=TopNElements.getTopNWithProgress(rankCalibragemFinal, qtd, tipo, progress);
			gm.registrarMensagem("Final da Classificacao da base de dados ordem "+tipo+". Executado em : " + getHMS(t1));
			console.flush();

			gravaRankCalPrint(nomeRankCalPrint, rankCalibragemTemp,qtd);
		}
		System.setOut(arquivo);
		return rankCalibragemTemp;
	}

	private static void salvaBestNumber(final List<apostaAcerto> apostasListSave) {
		final int total = apostasListSave.get(0).totRegGet();
		try {
			final FileOutputStream f = new FileOutputStream(new File("BestNumbers.svd"));
			final ObjectOutputStream o = new ObjectOutputStream(f);
			// Write objects to file
			o.writeObject(apostasListSave);
			o.writeObject(total);
			o.close();
			f.close();
		} catch (final FileNotFoundException e) {
			System.out.println("File not found");
		} catch (final IOException e) {
			System.out.println("Error initializing stream");
		}
	}

	public static LocalDate[] setMegaSemana() {
		// Mães 04 terça, 06 quinta e 08 sabado de mai. 2368, 2369 e 2370
		final LocalDate[] megaSemanas = new LocalDate[8];
		// Registrar a terca feira da semana
		megaSemanas[0] = LocalDate.of(2025, 02, 4);
		megaSemanas[1] = LocalDate.of(2025, 2, 11);
		megaSemanas[2] = LocalDate.of(2025, 2, 18);
		megaSemanas[3] = LocalDate.of(2025, 6, 29);
		megaSemanas[4] = LocalDate.of(2025, 8, 10);
		megaSemanas[5] = LocalDate.of(2025, 10, 29);
		megaSemanas[6] = LocalDate.of(2025, 10, 19);
		megaSemanas[7] = LocalDate.of(2025, 12, 7);
		return megaSemanas;
	}

	private static String sorteiotoString(final byte[] is) {
		final String retorno = "["
				+ String.format("%02d %02d %02d %02d %02d %02d", is[0], is[1], is[2], is[3], is[4], is[5]) + "]";
		return retorno;
	}

	private static byte[][] testaFormulas(final List<byte[]> listaOrdemSorteio2, final Colecao colecao2) {

		short numeroSorteioAtual = 0;
		byte[] sorteioAtual;

		short contaSorteio = 0;
		short contaIndices;
		AbstractIndice indice;
		final byte numIndices = (byte) colecao.todos.size();
		final int[][][] totais = new int[numIndices][3][3];
		final byte[][] ordemIndices = new byte[numIndices][3];

		while (contaSorteio < listaOrdemSorteio.size()) {
			numeroSorteioAtual = (short) (contaSorteio + 1);
			sorteioAtual = listaOrdemSorteio.get(contaSorteio);

			contaIndices = 0;
			byte contInterno;
			while (contaIndices < numIndices) {
				indice = colecao.todos.get(contaIndices);
				contInterno = 0;
				while ((indice.qtdSubIndices >= contInterno)) {
					totais[contaIndices][contInterno][0] += pontos(sorteioAtual, indice, contInterno, TipoIndice.TOPO);
					totais[contaIndices][contInterno][1] += pontos(sorteioAtual, indice, contInterno,
							TipoIndice.CENTRO);
					totais[contaIndices][contInterno][2] += pontos(sorteioAtual, indice, contInterno, TipoIndice.BASE);
					/*
					 * System.out.println(String.format("%2d-%1d  %72s     %6d  %6d  %6d  %6d ",
					 * (contaIndices + 1), contInterno, (contInterno == 0 ? indice.nomeDados :
					 * (contInterno == 1 ? indice.nomeSub1 : indice.nomeSub2)), numeroSorteioAtual,
					 * totais[contaIndices][contInterno][0], totais[contaIndices][contInterno][1],
					 * totais[contaIndices][contInterno][2]));
					 */
					contInterno++;
				}
				contaIndices++;
			}
			final Sorteio sorteio = new Sorteio();
			sorteio.reg(listaOrdemSorteio.get(contaSorteio++));
			colecao.add(sorteio);
			// listaOrdemSorteio.remove(0);
			colecao.numeroDoUltimoSorteio = numeroSorteioAtual;
		}

		byte contaOrdem = 0;
		contaIndices = 0;
		byte contInterno;
		int a, b, c;
		while (contaIndices < numIndices) {
			indice = colecao.todos.get(contaIndices);
			if (indice.indiceDeFormula == true) {
				contInterno = 0;
				while ((indice.qtdSubIndices >= contInterno)) {
					a = totais[contaIndices][contInterno][0];
					b = totais[contaIndices][contInterno][1];
					c = totais[contaIndices][contInterno][2];
					if (a > b) {
						if (a > c) {
							ordemIndices[contaOrdem][0] = 0;
							if (b > c) {
								ordemIndices[contaOrdem][1] = 1;
								ordemIndices[contaOrdem][2] = 2;
							} else {
								ordemIndices[contaOrdem][1] = 2;
								ordemIndices[contaOrdem][2] = 1;
							}
						} else {
							ordemIndices[contaOrdem][0] = 2;
							ordemIndices[contaOrdem][1] = 0;
							ordemIndices[contaOrdem][2] = 1;
						}
					} else if (b > c) {
						ordemIndices[contaOrdem][0] = 1;
						if (a > c) {
							ordemIndices[contaOrdem][1] = 0;
							ordemIndices[contaOrdem][2] = 2;
						} else {
							ordemIndices[contaOrdem][1] = 2;
							ordemIndices[contaOrdem][2] = 0;
						}
					} else {
						ordemIndices[contaOrdem][0] = 2;
						ordemIndices[contaOrdem][1] = 1;
						ordemIndices[contaOrdem][2] = 0;
					}

					contaOrdem++;
					contInterno++;
				}
			}
			contaIndices++;
		}
		return ordemIndices;
	}

	private static String textNum(final int valor) {

		final DecimalFormat df = new DecimalFormat("###,###,###,###,###.00");
		return df.format(valor);
	}


	public static void wait(final int s) {
		try {
			Thread.sleep(s * 1000);
		} catch (final InterruptedException ex) {
			Thread.currentThread().interrupt();
		}
	}

	private static Duration waitSorteio(final int sorteioParaAguardar) throws Exception {
		gm.registrarMensagem("Aguardando chegada do sorteio " + sorteioParaAguardar + " .");
		//int intervalo = 600;
		//Calendar agora;
		//int hora;
		 SmartWait waitManager = new SmartWait(gm);
         
         // Configura os intervalos com diferentes unidades
         waitManager.addInterval("01:00", 10);  
         waitManager.addInterval("21:00", 1);   
         waitManager.addInterval("21:30", 30, SmartWait.TimeUnitMeasure.SECONDS);  
         waitManager.addInterval("22:00", 1); 
		 waitManager.addInterval("23:00", 10); 
         waitManager.addInterval("23:30", 1 ,SmartWait.TimeUnitMeasure.HOURS); 
         waitManager.listIntervals();
         
         // Lista os intervalos configurados
		while (true) {
			/// getSorteio();
			/// if (sorteioParaAguardar == numSorteio) {
			resultado = resultado.lastSorteio();
			numSorteio = resultado.getNumero();
			// //numSorteio = lastSorteio();
			//System.out.println("\n");
			if (sorteioParaAguardar == numSorteio) {
				//System.out.println("\nChegou o sorteio " + sorteioParaAguardar +" na pagina principal. Vou verificar a página individual");
				gm.registrarMensagem("Chegou o sorteio " + sorteioParaAguardar +" na pagina principal. Vou verificar a página individual");
				if (resultado.existeSorteio(numSorteio)) {
					// Incluida mais uma camada de confirmação					//System.out.println("\nTambem chegou o sorteio " + sorteioParaAguardar +" na pagina individual. Vou confirmar as verificações.");
					gm.registrarMensagem("Tambem chegou o sorteio " + sorteioParaAguardar +" na pagina individual. Vou confirmar as verificações.");
					// //numSorteio = lastSorteio();
					resultado = resultado.lastSorteio();
					numSorteio = resultado.getNumero();
					if (sorteioParaAguardar == numSorteio) {
						if (resultado.existeSorteio(numSorteio)) {
							gm.registrarMensagem("Foi confirmada a chegada do sorteio " + sorteioParaAguardar +" na pagina principal e individual.");
							break;
						} else	{
							gm.registrarMensagem("Falhou a confirmação da chegada do sorteio " + sorteioParaAguardar + ".");
						}
					} else {
						gm.registrarMensagem("Chegou o sorteio " + sorteioParaAguardar + " na pagina do principal o sorteio, mas não está pronta na pagina individual do sorteio.");
					}
				}
				if (numSorteio >  sorteioParaAguardar) {
					gm.registrarMensagem("Aguardando chegada do sorteio " + sorteioParaAguardar + ", mas o sorteio atual na Caixa é o "+numSorteio+".");
				}
			}
			
			if (numSorteio == 0) {
				gm.registrarMensagem(" Erro ao acessar o serviço da Caixa.");
			} else {
				gm.registrarMensagem("Aguardando para nova verificacao da chegada do sorteio " + sorteioParaAguardar
						+ ". A previsão do próximo sorteio é para "+
				(resultado.getDataProximoConcurso().equals(java.time.LocalDate.now().format(java.time.format.DateTimeFormatter.ofPattern("dd/MM/yyyy")))?
						"hoje":
							resultado.getDataProximoConcurso()	)+". O sorteio atual e o " + numSorteio);
			}
			//wait(intervalo);
			waitManager.smartWait();
		}
		gm.registrarMensagem("Retornando do WaitSorteio. Sorteio solicitado:" + sorteioParaAguardar+" Sorteio fornecido:"+ numSorteio,DestinoMensagem.LOG);
		return waitManager.getDuracao();
	}
	private static void prtCabecalho() {
		final int maiorQtdDezenas = 10;
		final String preencheIguais = "=".repeat((maiorQtdDezenas * 3));
		final String preencheEspacos = " ".repeat((maiorQtdDezenas * 3));
		System.out.println(
				"\n                                                                                                 Número   Qtd      Score"
						+ preencheEspacos + "");
		System.out.println(
				"Rank         Obtido    Investido      Resultado Senas Quinas Quadras Terno Duplas Solos   Filtro Sorteio  Ace      % Descrição       "
						+ (preencheEspacos + "  Números escolhidos").substring(18)
						+ " Código Fórmula => Fórmula utilizada para obtenção das dezenas indicadas");
		System.out.print(
				"==== ============== ============ ============== ===== ====== ======= ===== ====== ======= ====== ======= ===== ===== ================= "
						+ preencheIguais + " " +"================ "+ "=".repeat(226));
		return;
	}

	private static String determinarClassificacao(double score) {
		if (score >= 95)
			return "EXCEPCIONAL *****";//★★★★★";
		if (score >= 85)
			return "EXCELENTE ****   ";//★★★★   ";
		if (score >= 75)
			return "MUITO BOA ***    ";//★★★    ";
		if (score >= 65)
			return "BOA **           ";//★★            ";
		if (score >= 50)
			return "REGULAR *        ";//★         ";
		return     "FRACA            ";
	}
	
}
