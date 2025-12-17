package MegaPck;
import java.io.FileWriter;
import java.io.BufferedWriter;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.Duration;

public class GerenciadorMensagens {

    // Enum para definir os destinos possíveis da mensagem
    public enum DestinoMensagem {
        LOG, CONSOLE, AMBOS
    }

    // Enum para o modo de limite
    public enum ModoLimite {
        LINHAS, TAMANHO_KB, TAMANHO_MB
    }

    private static String arquivoLog;
    private int limiteMaximoNumLinha;
    private int limiteMaximoTamanhoChars;
    private ModoLimite modoLimite;
    static String msgPrefix;
    
    // Variáveis para controle de hibernação
    private static LocalDateTime ultimoRegistroTempo;
    private static Duration limiteHibernacao = Duration.ofMinutes(5); 
    private static Duration tempoTotalHibernacao = Duration.ZERO;
    private static Duration maiorIntervalo = Duration.ofSeconds(60);

    /**
     * Construtor padrão com limite de 1000 linhas
     */
    public GerenciadorMensagens() {
        this(obterNomeArquivoLogPadrao());
    }
    public void setDuration(int minutos) {
    	registrarMensagem("Tempo limite para registro de hibernação estabelecido em "+minutos+" minutos." );
    	GerenciadorMensagens.limiteHibernacao = Duration.ofMinutes(minutos); // 02:30h = 150 minutos
    }
    public GerenciadorMensagens(String arquivoLog) {
        this(arquivoLog, 1000, ModoLimite.LINHAS);
    }

    /**
     * Construtor completo com todos os parâmetros
     */
    public GerenciadorMensagens(String arquivoLog, int limiteMaximo, ModoLimite modoLimite) {
        validarParametros(arquivoLog, limiteMaximo, modoLimite);
        
        GerenciadorMensagens.arquivoLog = arquivoLog;
        this.modoLimite = modoLimite;
        //this.msgPrefix = "";
        GerenciadorMensagens.ultimoRegistroTempo = LocalDateTime.now();
        setLimite(limiteMaximo, modoLimite);
    }

    private void validarParametros(String arquivoLog, int limiteMaximo, ModoLimite modoLimite) {
        if (arquivoLog == null || arquivoLog.trim().isEmpty()) {
            throw new IllegalArgumentException("arquivoLog não pode ser nulo ou vazio");
        }
        if (limiteMaximo <= 0) {
            throw new IllegalArgumentException("limiteMaximo deve ser maior que zero");
        }
        if (modoLimite == null) {
            throw new IllegalArgumentException("modoLimite não pode ser nulo");
        }
    }
    public void setLimite(int limiteMaximo, ModoLimite modoLimite) {
        if (modoLimite == ModoLimite.LINHAS) {
            this.limiteMaximoNumLinha = limiteMaximo;
        } else if (modoLimite == ModoLimite.TAMANHO_KB | modoLimite == ModoLimite.TAMANHO_MB) {
            this.limiteMaximoTamanhoChars = limiteMaximo * 1024 * (modoLimite == ModoLimite.TAMANHO_KB ? 1 : 1024);
        }
    }

    public GerenciadorMensagens(int limiteMaximo, ModoLimite modoLimite) {
        GerenciadorMensagens.arquivoLog = obterNomeArquivoLogPadrao();
        this.limiteMaximoNumLinha = limiteMaximo;
        this.modoLimite = modoLimite;
        msgPrefix = "";
        GerenciadorMensagens.ultimoRegistroTempo = LocalDateTime.now();
    }

    /**
     * Método para verificar e registrar possível retorno de hibernação
     */
    private static void verificarHibernacao() {
    	LocalDateTime agora = LocalDateTime.now();
    	Duration diferenca = Duration.between(ultimoRegistroTempo, agora);

    	if (diferenca.compareTo(limiteHibernacao) > 0) {

    		// Atualiza o tempo total de hibernação
    		tempoTotalHibernacao = tempoTotalHibernacao.plus(diferenca);
    		ultimoRegistroTempo = LocalDateTime.now();
    		// Diferença maior que 02:30h - provável hibernação detectad
    		String mensagemHibernacao = "Provável retorno de hibernação detectado. " +
    				"Tempo decorrido: " + formatarDuracao(diferenca);

    		// Usa o método existente para registrar a mensagem
    		soRegistrarMensagem(mensagemHibernacao, DestinoMensagem.AMBOS, true);

    	} else {
    		if (diferenca.compareTo(maiorIntervalo.plusSeconds(1)) >= 0) {
                Duration oldMaiorIntervalo=maiorIntervalo;
                maiorIntervalo = diferenca;
    			soRegistrarMensagem("Registro de maior intervalo alterado de :"+formatarDuracao(oldMaiorIntervalo)+" para :"+
    					formatarDuracao(diferenca)+".", DestinoMensagem.AMBOS, true);
    			//maiorIntervalo = diferenca;
    		}
    	}
    }

    /**
     * Formata uma duração para string legível
     */
    private static String formatarDuracao(Duration duracao) {
        long horas = duracao.toHours();
        long minutos = duracao.toMinutesPart();
        long segundos = duracao.toSecondsPart();
        return String.format("%02d:%02d:%02d", horas, minutos, segundos);
    }

    /**
     * Método para registrar uma mensagem no sistema com controle de tamanho
     */
    public static void registrarMensagem(String mensagem, DestinoMensagem destino, boolean incluirDataHora) {
        // Verifica se houve possível hibernação antes de registrar
        verificarHibernacao();
        soRegistrarMensagem( mensagem,  destino,  incluirDataHora);
        // Atualiza a variável de controle de tempo antes de retornar
        setRegTempo();
        
    }
    public static void soRegistrarMensagem(String mensagem, DestinoMensagem destino, boolean incluirDataHora) {
        // Verifica se houve possível hibernação antes de registrar
        //verificarHibernacao();
        
        String mensagemFormatada = formatarMensagem(mensagem, incluirDataHora);

        try {
            switch (destino) {
                case LOG:
                    escreverNoArquivoLog(mensagemFormatada);
                    break;
                case CONSOLE:
                    escreverNoConsole(msgPrefix + mensagemFormatada);
                    break;
                case AMBOS:
                    escreverNoArquivoLog(mensagemFormatada);
                    escreverNoConsole(msgPrefix + mensagemFormatada);
                    break;
                default:
                    System.err.println("Destino inválido: " + destino);
            }
            soResetMsgPrefix();
        } catch (IOException e) {
            System.err.println("Erro ao escrever no log '" + arquivoLog + "': " + e.getMessage());
        } finally {
            // Atualiza a variável de controle de tempo antes de retornar
        	//setRegTempo();
        }
    }
    public static void setRegTempo() {
    	LocalDateTime agora = LocalDateTime.now();
        Duration diferenca = Duration.between(ultimoRegistroTempo, agora);
        maiorIntervalo = maiorIntervalo.compareTo(diferenca) > 0 ? maiorIntervalo : diferenca;
    	ultimoRegistroTempo = LocalDateTime.now();
        return ;
    }
    /**
     * Retorna o tempo total de hibernação detectado
     */
    public Duration getTempoHibernacao() {
        return tempoTotalHibernacao;
    }
    public Duration getMaiorIntervalo() {
        return maiorIntervalo;
    }
    public void setTempoHibernacao(long minutos) {
    	GerenciadorMensagens.tempoTotalHibernacao = Duration.ofMinutes(minutos);
    }

    /**
     * Retorna o tempo total de hibernação em formato legível
     */
    public String getTempoHibernacaoFormatado() {
        return formatarDuracao(tempoTotalHibernacao);
    }
    public String getMaiorIntervaloFormatado() {
        return formatarDuracao(maiorIntervalo);
    }

    /**
     * Reinicia o contador de tempo de hibernação
     */
    public void reiniciarTempoHibernacao() {
        GerenciadorMensagens.tempoTotalHibernacao = Duration.ZERO;
        setRegTempo();
    }
    public static void verificaHibernacaoSetRegTempo() {
    	verificarHibernacao();
    	setRegTempo();
    }
    public void registrarMensagem(String mensagem) {
        registrarMensagem(mensagem, DestinoMensagem.AMBOS, true);
    }

    public static void setMsgPrefix() {
    	verificaHibernacaoSetRegTempo();
        msgPrefix = "\n";
    }
    public static void soSetMsgPrefix() {
    	//verificaHibernacaoSetRegTempo();
        msgPrefix = "\n";
    }
    public static void resetMsgPrefix() {
    	verificaHibernacaoSetRegTempo();;
        msgPrefix = "";
    }
    public static void soResetMsgPrefix() {
    	//verificaHibernacaoSetRegTempo();;
        msgPrefix = "";
    }
    /**
     * Obtém o nome do arquivo de log padrão baseado no nome da classe principal
     */
    private static String obterNomeArquivoLogPadrao() {
        try {
            String javaCommand = System.getProperty("sun.java.command");
            if (javaCommand != null && !javaCommand.trim().isEmpty()) {
                String firstPart = javaCommand.split(" ")[0];

                // Remove caminho de diretórios se existir
                String fileName = firstPart.substring(firstPart.lastIndexOf('/') + 1);
                fileName = fileName.substring(fileName.lastIndexOf('\\') + 1);

                // Remove extensão .jar ou .class se existir
                if (fileName.endsWith(".jar") || fileName.endsWith(".class")) {
                    fileName = fileName.substring(0, fileName.lastIndexOf('.'));
                }

                // Para classes Java, pega apenas o nome simples (sem pacote)
                if (firstPart.contains(".") && !firstPart.endsWith(".jar")) {
                    fileName = firstPart.substring(firstPart.lastIndexOf('.') + 1);
                }

                return fileName + ".log";
            }
        } catch (Exception e) {
            // Ignora exceções
        }
        return "Sistema.log";
    }

    /**
     * Conta o número de linhas no arquivo de log
     */
    private int contarLinhas() throws IOException {
        int linhas = 0;
        try (BufferedReader reader = new BufferedReader(new FileReader(arquivoLog))) {
            while (reader.readLine() != null) {
                linhas++;
            }
        }
        return linhas;
    }

    /**
     * Reduz o arquivo de log mantendo apenas as mensagens mais recentes
     */
    private void reduzirArquivoLog() throws IOException {
        Path arquivoOriginal = Paths.get(arquivoLog);
        Path arquivoTemp = Paths.get(arquivoLog + ".tmp");
        String primeiraLinha = "";
        String ultimaLinha = "";

        // Calcula quantas linhas pular (as mais antigas)
        int totalLinhas = contarLinhas();
        int linhasParaManter = (int) Math.max(limiteMaximoNumLinha , 100);
        int linhasParaPular = totalLinhas - linhasParaManter;

        if (linhasParaPular > 0) {
            // Cria arquivo temporário com as linhas mais recentes
            try (BufferedReader reader = new BufferedReader(new FileReader(arquivoOriginal.toFile()));
                    BufferedWriter writer = new BufferedWriter(new FileWriter(arquivoTemp.toFile()))) {

                String linha;
                int linhaAtual = 0;

                // Pula as linhas mais antigas
                while (linhaAtual < linhasParaPular && (linha = reader.readLine()) != null) {
                    linhaAtual++;
                    if (linhaAtual == 1) {
                        primeiraLinha = linha;
                    }
                    if (linhaAtual == (linhasParaPular - 1)) {
                        ultimaLinha = linha;
                    }
                }

                // Copia as linhas mais recentes para o arquivo temporário
                while ((linha = reader.readLine()) != null) {
                    writer.write(linha);
                    writer.newLine();
                }
            }

            // Substitui o arquivo original pelo temporário
            Files.deleteIfExists(arquivoOriginal);
            Files.move(arquivoTemp, arquivoOriginal, StandardCopyOption.REPLACE_EXISTING);

            registrarMensagem("Arquivo de log reduzido em " + String.format("%,d ",linhasParaPular )+ " linhas. Mantidas as " + String.format("%,d ",linhasParaManter ) + 
                    " mensagens mais recentes.");
            registrarMensagem("Removidas linhas de : " + primeiraLinha.substring(0, Math.min(25, primeiraLinha.length())) + 
                    " até : " + ultimaLinha.substring(0, Math.min(25, ultimaLinha.length())));
        }
        // Verifica se após reduzir as linhas, ainda é necessário ajustar por tamanho
        if (limiteMaximoTamanhoChars != 0) {
            ajustarPorTamanho();
        }
    }

    /**
     * Ajusta o arquivo de log por tamanho se necessário
     */
    private void ajustarPorTamanho() throws IOException {
        Path arquivoOriginal = Paths.get(arquivoLog);

        if (!Files.exists(arquivoOriginal)) {
            return;
        }

        long tamanhoAtual = Files.size(arquivoOriginal);

        if (tamanhoAtual > limiteMaximoTamanhoChars) {
            reduzirArquivoLogPorTamanho(tamanhoAtual, limiteMaximoTamanhoChars);
        }
    }

    /**
     * Reduz o arquivo de log baseado no tamanho máximo
     */
    private void reduzirArquivoLogPorTamanho(long tamanhoAtual, long tamanhoLimiteBytes) throws IOException {
        Path arquivoOriginal = Paths.get(arquivoLog);
        Path arquivoTemp = Paths.get(arquivoLog + ".tmp");
        long tamanhoTemp = 0;
        long tamanhoLinha =0;
        String primeiraLinha = "";
        String ultimaLinha = "";
        int linhaAtual = 1;
        try (BufferedReader reader = new BufferedReader(new FileReader(arquivoOriginal.toFile()));
                BufferedWriter writer = new BufferedWriter(new FileWriter(arquivoTemp.toFile()))) {

            String linha;
            boolean comecarEscrever = false;

            // Lê o arquivo e começa a escrever apenas quando o tamanho acumulado
            // atingir a parte que deve ser mantida (últimos X bytes)
            
            while ((linha = reader.readLine()) != null) {
                if (!comecarEscrever) {
                    // Tamanho da linha + quebra de linha
                    tamanhoLinha = linha.getBytes().length + System.lineSeparator().getBytes().length;
                    // Se adicionando esta linha ultrapassaria o limite, começamos a escrever daqui
                    if (linhaAtual == 1) {
                        primeiraLinha = linha;
                        
                    }
                    if (tamanhoTemp + tamanhoLinha > (tamanhoAtual - tamanhoLimiteBytes)) {
                        comecarEscrever = true;
                    } else {
                        tamanhoTemp += tamanhoLinha;
                        ultimaLinha=linha;
                        linhaAtual++;
                        continue; // Pula esta linha (mais antiga)
                    }
                }
                // Escreve a linha no arquivo temporário
                if (comecarEscrever) {
                    writer.write(linha);
                    writer.newLine();
                }
            }
        }

        // Substitui o arquivo original pelo temporário
        Files.deleteIfExists(arquivoOriginal);
        Files.move(arquivoTemp, arquivoOriginal, StandardCopyOption.REPLACE_EXISTING);

        registrarMensagem("Arquivo de log reduzido por tamanho (limite: " +String.format("%,d ", limiteMaximoTamanhoChars) +
                ") De : "+String.format("%,d ", tamanhoAtual) + 
                " bytes para aprox.: " + String.format("%,d ",(tamanhoAtual-tamanhoTemp )) + " caracters.");
        registrarMensagem("Removidas aprox. "+linhaAtual+" linhas de : " + primeiraLinha.substring(0, Math.min(25, primeiraLinha.length())) + 
                " até : " + ultimaLinha.substring(0, Math.min(25, ultimaLinha.length())));
    }

    /**
     * Método para forçar a redução do arquivo de log imediatamente
     */
    public void forcarReducaoLog() {
        try {
            reduzirArquivoLog();
            registrarMensagem("Redução do arquivo de log forçada com sucesso.");
        } catch (IOException e) {
            System.err.println("Erro ao forçar redução do log: " + e.getMessage());
        }
    }

    /**
     * Formata a mensagem incluindo data e hora se necessário
     */
    private static String formatarMensagem(String mensagem, boolean incluirDataHora) {
        if (incluirDataHora) {
            LocalDateTime agora = LocalDateTime.now();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss.SSS");
            String dataHora = agora.format(formatter);
            return "[" + dataHora + "] " + mensagem;
        }
        return mensagem;
    }

    /**
     * Escreve a mensagem no arquivo de log
     */
    private static void escreverNoArquivoLog(String mensagem) throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(arquivoLog, true))) {
            writer.write(mensagem);
            writer.newLine();
        }
    }

    /**
     * Escreve a mensagem no console
     */
    private static void escreverNoConsole(String mensagem) {
        System.out.println(mensagem);
    }

    /**
     * Método para limpar completamente o arquivo de log
     */
    public void limparLog() throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(arquivoLog, false))) {
            writer.write("");
        }
    }

    // Getters e Setters
    public String getArquivoLog() {
        return arquivoLog;
    }

    public void setArquivoLog(String arquivoLog) {
        GerenciadorMensagens.arquivoLog = arquivoLog;
    }

    public int getLimiteMaximo() {
        return limiteMaximoNumLinha;
    }

    public void setLimiteMaximo(int limiteMaximo) {
        this.limiteMaximoNumLinha = limiteMaximo;
    }

    public ModoLimite getModoLimite() {
        return modoLimite;
    }

    public void setModoLimite(ModoLimite modoLimite) {
        this.modoLimite = modoLimite;
    }

    /**
     * Método para mostrar informações sobre o arquivo de log
     */
    public void mostrarInfoLog() {
        registrarMensagem("Arquivo de log: " + arquivoLog);
        registrarMensagem("Modo de limite: " + modoLimite);
        registrarMensagem("Limite máximo: " + String.format("%,6d ",limiteMaximoNumLinha ) + 
                (modoLimite == ModoLimite.LINHAS ? "linhas e " : 
                    modoLimite == ModoLimite.TAMANHO_KB ? " KB" : " MB") +
                (limiteMaximoTamanhoChars>0?String.format("%,6d ",limiteMaximoTamanhoChars)+"bytes.":""));

        try {
            Path path = Paths.get(arquivoLog);
            if (Files.exists(path)) {
                String Mensagem ="";
                if (modoLimite == ModoLimite.LINHAS) {
                    int linhas = contarLinhas();
                    Mensagem="Tamanho atual: " + String.format("%,6d ",linhas);
                }
                long tamanho = Files.size(path);
                registrarMensagem(Mensagem+"linhas e " + String.format("%,6d ",tamanho )+ "bytes.");
            }
        } catch (IOException e) {
            registrarMensagem("Arquivo não existe ou erro ao ler informações.");
        }
    }

    public void registrarMensagem(String string, DestinoMensagem log) {
        registrarMensagem(string, log, true);
    }

    public void registrarMensagem(String string, boolean b) {
        registrarMensagem(string, DestinoMensagem.AMBOS, b);
    }
}