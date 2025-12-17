package MegaPck;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class MegaSenaResultado {
    
    // Atributos principais
    private short numero;
    private boolean acumulado;
    private String dataApuracao;
    private String dataProximoConcurso;
    private List<String> dezenasSorteadasOrdemSorteio;
    private List<String> listaDezenas;
    private boolean exibirDetalhamentoPorCidade;
    private Object id;
    private int indicadorConcursoEspecial;
    private List<String> listaDezenasSegundoSorteio;
    private List<MunicipioGanhador> listaMunicipioUFGanhadores;
    private List<RateioPremio> listaRateioPremio;
    private List<Object> listaResultadoEquipeEsportiva;
    private String localSorteio;
    private String nomeMunicipioUFSorteio;
    private String nomeTimeCoracaoMesSorte;
    private int numeroConcursoAnterior;
    private int numeroConcursoFinal_0_5;
    private int numeroConcursoProximo;
    private int numeroJogo;
    private String observacao;
    private Object premiacaoContingencia;
    private String tipoJogo;
    private int tipoPublicacao;
    private boolean ultimoConcurso;
    private double valorArrecadado;
    private double valorAcumuladoConcurso_0_5;
    private double valorAcumuladoConcursoEspecial;
    private double valorAcumuladoProximoConcurso;
    private double valorEstimadoProximoConcurso;
    private double valorSaldoReservaGarantidora;
    private double valorTotalPremioFaixaUm;
    private static GerenciadorMensagens gm;
    private static service intService ;

    // Classe interna para representar municípios ganhadores
    public static class MunicipioGanhador {
        private int ganhadores;
        private String municipio;
        private String nomeFantasiaUL;
        private int posicao;
        private String serie;
        private String uf;

        public MunicipioGanhador() {}

        // Getters e Setters
        public int getGanhadores() { return ganhadores; }
        public void setGanhadores(int ganhadores) { this.ganhadores = ganhadores; }
        
        public String getMunicipio() { return municipio; }
        public void setMunicipio(String municipio) { this.municipio = municipio; }
        
        public String getNomeFantasiaUL() { return nomeFantasiaUL; }
        public void setNomeFantasiaUL(String nomeFantasiaUL) { this.nomeFantasiaUL = nomeFantasiaUL; }
        
        public int getPosicao() { return posicao; }
        public void setPosicao(int posicao) { this.posicao = posicao; }
        
        public String getSerie() { return serie; }
        public void setSerie(String serie) { this.serie = serie; }
        
        public String getUf() { return uf; }
        public void setUf(String uf) { this.uf = uf; }
    }

    // Classe interna para representar rateio de prêmios
    public static class RateioPremio {
        private String descricaoFaixa;
        private int faixa;
        private int numeroDeGanhadores;
        private double valorPremio;

        public RateioPremio() {}

        // Getters e Setters
        public String getDescricaoFaixa() { return descricaoFaixa; }
        public void setDescricaoFaixa(String descricaoFaixa) { this.descricaoFaixa = descricaoFaixa; }
        
        public int getFaixa() { return faixa; }
        public void setFaixa(int faixa) { this.faixa = faixa; }
        
        public int getNumeroDeGanhadores() { return numeroDeGanhadores; }
        public void setNumeroDeGanhadores(int numeroDeGanhadores) { this.numeroDeGanhadores = numeroDeGanhadores; }
        
        public double getValorPremio() { return valorPremio; }
        public void setValorPremio(double valorPremio) { this.valorPremio = valorPremio; }
    }

    // Construtor padrão
    public MegaSenaResultado() {
        this.dezenasSorteadasOrdemSorteio = new ArrayList<>();
        this.listaDezenas = new ArrayList<>();
        this.listaMunicipioUFGanhadores = new ArrayList<>();
        this.listaRateioPremio = new ArrayList<>();
        this.listaResultadoEquipeEsportiva = new ArrayList<>();
    }
    public MegaSenaResultado(GerenciadorMensagens gmPar) {
    	this(); // Chama o construtor padrão
        gm = gmPar;
    }
    // Método para carregar dados da URL
    public static MegaSenaResultado carregarDados(GerenciadorMensagens gmPar) {
    	gm = gmPar;
    	intService = new service(gm);
        final String url = "https://servicebus2.caixa.gov.br/portaldeloterias/api/megasena";
        try {
            final JSONObject jsonResponse = intService.getJsonFromUrlWithoutCache(url);
            return fromJson(jsonResponse);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    // Método para converter JSON para objeto
    public static MegaSenaResultado fromJson(JSONObject json) {
        MegaSenaResultado resultado = new MegaSenaResultado();
        
        // Atributos simples
        resultado.setNumero((short) json.getInt("numero"));
        resultado.setAcumulado(json.getBoolean("acumulado"));
        resultado.setDataApuracao(json.getString("dataApuracao"));
        resultado.setDataProximoConcurso(json.getString("dataProximoConcurso"));
        resultado.setExibirDetalhamentoPorCidade(json.getBoolean("exibirDetalhamentoPorCidade"));
        resultado.setId(json.isNull("id") ? null : json.get("id"));
        resultado.setIndicadorConcursoEspecial(json.getInt("indicadorConcursoEspecial"));
        resultado.setLocalSorteio(json.getString("localSorteio"));
        resultado.setNomeMunicipioUFSorteio(json.getString("nomeMunicipioUFSorteio"));
        resultado.setNomeTimeCoracaoMesSorte(json.getString("nomeTimeCoracaoMesSorte"));
        resultado.setNumeroConcursoAnterior(json.getInt("numeroConcursoAnterior"));
        resultado.setNumeroConcursoFinal_0_5(json.getInt("numeroConcursoFinal_0_5"));
        resultado.setNumeroConcursoProximo(json.getInt("numeroConcursoProximo"));
        resultado.setNumeroJogo(json.getInt("numeroJogo"));
        resultado.setObservacao(json.getString("observacao"));
        resultado.setPremiacaoContingencia(json.isNull("premiacaoContingencia") ? null : json.get("premiacaoContingencia"));
        resultado.setTipoJogo(json.getString("tipoJogo"));
        resultado.setTipoPublicacao(json.getInt("tipoPublicacao"));
        resultado.setUltimoConcurso(json.getBoolean("ultimoConcurso"));
        resultado.setValorArrecadado(json.getDouble("valorArrecadado"));
        resultado.setValorAcumuladoConcurso_0_5(json.getDouble("valorAcumuladoConcurso_0_5"));
        resultado.setValorAcumuladoConcursoEspecial(json.getDouble("valorAcumuladoConcursoEspecial"));
        resultado.setValorAcumuladoProximoConcurso(json.getDouble("valorAcumuladoProximoConcurso"));
        resultado.setValorEstimadoProximoConcurso(json.getDouble("valorEstimadoProximoConcurso"));
        resultado.setValorSaldoReservaGarantidora(json.getDouble("valorSaldoReservaGarantidora"));
        resultado.setValorTotalPremioFaixaUm(json.getDouble("valorTotalPremioFaixaUm"));

        // Arrays de strings
        JSONArray dezenasOrdemSorteio = json.getJSONArray("dezenasSorteadasOrdemSorteio");
        for (int i = 0; i < dezenasOrdemSorteio.length(); i++) {
            resultado.getDezenasSorteadasOrdemSorteio().add(dezenasOrdemSorteio.getString(i));
        }

        JSONArray listaDezenasArray = json.getJSONArray("listaDezenas");
        for (int i = 0; i < listaDezenasArray.length(); i++) {
            resultado.getListaDezenas().add(listaDezenasArray.getString(i));
        }

        // Lista de municípios ganhadores
        if (!json.isNull("listaMunicipioUFGanhadores")) {
            JSONArray municipiosArray = json.getJSONArray("listaMunicipioUFGanhadores");
            for (int i = 0; i < municipiosArray.length(); i++) {
                JSONObject municipioJson = municipiosArray.getJSONObject(i);
                MunicipioGanhador municipio = new MunicipioGanhador();
                municipio.setGanhadores(municipioJson.getInt("ganhadores"));
                municipio.setMunicipio(municipioJson.getString("municipio"));
                municipio.setNomeFantasiaUL(municipioJson.getString("nomeFatansiaUL"));
                municipio.setPosicao(municipioJson.getInt("posicao"));
                municipio.setSerie(municipioJson.getString("serie"));
                municipio.setUf(municipioJson.getString("uf"));
                resultado.getListaMunicipioUFGanhadores().add(municipio);
            }
        }

        // Lista de rateio de prêmios
        if (!json.isNull("listaRateioPremio")) {
            JSONArray rateioArray = json.getJSONArray("listaRateioPremio");
            for (int i = 0; i < rateioArray.length(); i++) {
                JSONObject rateioJson = rateioArray.getJSONObject(i);
                RateioPremio rateio = new RateioPremio();
                rateio.setDescricaoFaixa(rateioJson.getString("descricaoFaixa"));
                rateio.setFaixa(rateioJson.getInt("faixa"));
                rateio.setNumeroDeGanhadores(rateioJson.getInt("numeroDeGanhadores"));
                rateio.setValorPremio(rateioJson.getDouble("valorPremio"));
                resultado.getListaRateioPremio().add(rateio);
            }
        }

        return resultado;
    }
    /*
    // Método para obter JSON da URL (igual ao seu exemplo)
    private static JSONObject getJsonFromUrlWithoutCache(String urlString) throws Exception {
    	URL url = new URI(urlString).toURL();
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");
        connection.setRequestProperty("Accept", "application/json");
        connection.setRequestProperty("Cache-Control", "no-cache");

        StringBuilder response = new StringBuilder();
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()))) {
            String line;
            while ((line = reader.readLine()) != null) {
                response.append(line);
            }
        }

        return new JSONObject(response.toString());
    }
    */
    

    // Getters e Setters para todos os atributos
    public short getNumero() { return numero; }
    public void setNumero(short numero) { this.numero = numero; }

    public boolean isAcumulado() { return acumulado; }
    public void setAcumulado(boolean acumulado) { this.acumulado = acumulado; }

    public String getDataApuracao() { return dataApuracao; }
    public void setDataApuracao(String dataApuracao) { this.dataApuracao = dataApuracao; }

    public String getDataProximoConcurso() { return dataProximoConcurso; }
    public void setDataProximoConcurso(String dataProximoConcurso) { this.dataProximoConcurso = dataProximoConcurso; }

    public List<String> getDezenasSorteadasOrdemSorteio() { return dezenasSorteadasOrdemSorteio; }
    public void setDezenasSorteadasOrdemSorteio(List<String> dezenasSorteadasOrdemSorteio) { this.dezenasSorteadasOrdemSorteio = dezenasSorteadasOrdemSorteio; }

    public List<String> getListaDezenas() { return listaDezenas; }
    public void setListaDezenas(List<String> listaDezenas) { this.listaDezenas = listaDezenas; }

    public boolean isExibirDetalhamentoPorCidade() { return exibirDetalhamentoPorCidade; }
    public void setExibirDetalhamentoPorCidade(boolean exibirDetalhamentoPorCidade) { this.exibirDetalhamentoPorCidade = exibirDetalhamentoPorCidade; }

    public Object getId() { return id; }
    public void setId(Object id) { this.id = id; }

    public int getIndicadorConcursoEspecial() { return indicadorConcursoEspecial; }
    public void setIndicadorConcursoEspecial(int indicadorConcursoEspecial) { this.indicadorConcursoEspecial = indicadorConcursoEspecial; }

    public List<String> getListaDezenasSegundoSorteio() { return listaDezenasSegundoSorteio; }
    public void setListaDezenasSegundoSorteio(List<String> listaDezenasSegundoSorteio) { this.listaDezenasSegundoSorteio = listaDezenasSegundoSorteio; }

    public List<MunicipioGanhador> getListaMunicipioUFGanhadores() { return listaMunicipioUFGanhadores; }
    public void setListaMunicipioUFGanhadores(List<MunicipioGanhador> listaMunicipioUFGanhadores) { this.listaMunicipioUFGanhadores = listaMunicipioUFGanhadores; }

    public List<RateioPremio> getListaRateioPremio() { return listaRateioPremio; }
    public void setListaRateioPremio(List<RateioPremio> listaRateioPremio) { this.listaRateioPremio = listaRateioPremio; }

    public List<Object> getListaResultadoEquipeEsportiva() { return listaResultadoEquipeEsportiva; }
    public void setListaResultadoEquipeEsportiva(List<Object> listaResultadoEquipeEsportiva) { this.listaResultadoEquipeEsportiva = listaResultadoEquipeEsportiva; }

    public String getLocalSorteio() { return localSorteio; }
    public void setLocalSorteio(String localSorteio) { this.localSorteio = localSorteio; }

    public String getNomeMunicipioUFSorteio() { return nomeMunicipioUFSorteio; }
    public void setNomeMunicipioUFSorteio(String nomeMunicipioUFSorteio) { this.nomeMunicipioUFSorteio = nomeMunicipioUFSorteio; }

    public String getNomeTimeCoracaoMesSorte() { return nomeTimeCoracaoMesSorte; }
    public void setNomeTimeCoracaoMesSorte(String nomeTimeCoracaoMesSorte) { this.nomeTimeCoracaoMesSorte = nomeTimeCoracaoMesSorte; }

    public int getNumeroConcursoAnterior() { return numeroConcursoAnterior; }
    public void setNumeroConcursoAnterior(int numeroConcursoAnterior) { this.numeroConcursoAnterior = numeroConcursoAnterior; }

    public int getNumeroConcursoFinal_0_5() { return numeroConcursoFinal_0_5; }
    public void setNumeroConcursoFinal_0_5(int numeroConcursoFinal_0_5) { this.numeroConcursoFinal_0_5 = numeroConcursoFinal_0_5; }

    public int getNumeroConcursoProximo() { return numeroConcursoProximo; }
    public void setNumeroConcursoProximo(int numeroConcursoProximo) { this.numeroConcursoProximo = numeroConcursoProximo; }

    public int getNumeroJogo() { return numeroJogo; }
    public void setNumeroJogo(int numeroJogo) { this.numeroJogo = numeroJogo; }

    public String getObservacao() { return observacao; }
    public void setObservacao(String observacao) { this.observacao = observacao; }

    public Object getPremiacaoContingencia() { return premiacaoContingencia; }
    public void setPremiacaoContingencia(Object premiacaoContingencia) { this.premiacaoContingencia = premiacaoContingencia; }

    public String getTipoJogo() { return tipoJogo; }
    public void setTipoJogo(String tipoJogo) { this.tipoJogo = tipoJogo; }

    public int getTipoPublicacao() { return tipoPublicacao; }
    public void setTipoPublicacao(int tipoPublicacao) { this.tipoPublicacao = tipoPublicacao; }

    public boolean isUltimoConcurso() { return ultimoConcurso; }
    public void setUltimoConcurso(boolean ultimoConcurso) { this.ultimoConcurso = ultimoConcurso; }

    public double getValorArrecadado() { return valorArrecadado; }
    public void setValorArrecadado(double valorArrecadado) { this.valorArrecadado = valorArrecadado; }

    public double getValorAcumuladoConcurso_0_5() { return valorAcumuladoConcurso_0_5; }
    public void setValorAcumuladoConcurso_0_5(double valorAcumuladoConcurso_0_5) { this.valorAcumuladoConcurso_0_5 = valorAcumuladoConcurso_0_5; }

    public double getValorAcumuladoConcursoEspecial() { return valorAcumuladoConcursoEspecial; }
    public void setValorAcumuladoConcursoEspecial(double valorAcumuladoConcursoEspecial) { this.valorAcumuladoConcursoEspecial = valorAcumuladoConcursoEspecial; }

    public double getValorAcumuladoProximoConcurso() { return valorAcumuladoProximoConcurso; }
    public void setValorAcumuladoProximoConcurso(double valorAcumuladoProximoConcurso) { this.valorAcumuladoProximoConcurso = valorAcumuladoProximoConcurso; }

    public double getValorEstimadoProximoConcurso() { return valorEstimadoProximoConcurso; }
    public void setValorEstimadoProximoConcurso(double valorEstimadoProximoConcurso) { this.valorEstimadoProximoConcurso = valorEstimadoProximoConcurso; }

    public double getValorSaldoReservaGarantidora() { return valorSaldoReservaGarantidora; }
    public void setValorSaldoReservaGarantidora(double valorSaldoReservaGarantidora) { this.valorSaldoReservaGarantidora = valorSaldoReservaGarantidora; }

    public double getValorTotalPremioFaixaUm() { return valorTotalPremioFaixaUm; }
    public void setValorTotalPremioFaixaUm(double valorTotalPremioFaixaUm) { this.valorTotalPremioFaixaUm = valorTotalPremioFaixaUm; }

    // Método toString para facilitar a visualização
    @Override
    public String toString() {
        return "MegaSenaResultado{" +
                "numero=" + numero +
                ", dataApuracao='" + dataApuracao + '\'' +
                ", listaDezenas=" + listaDezenas +
                ", valorArrecadado=" + valorArrecadado +
                '}';
    }
    public MegaSenaResultado lastSorteio() throws Exception {
		//try {
			//final String url = "https://servicebus2.caixa.gov.br/portaldeloterias/api/megasena";
			//final JSONObject jsonResponse = getJsonFromUrlWithoutCache(url);
			//final short sorteio = (short) jsonResponse.getInt("numero");
			MegaSenaResultado resultado = MegaSenaResultado.carregarDados(gm);
			return resultado;
		//} catch (IOException | InterruptedException e) {
			//e.printStackTrace();
		//}
		//return null;
	}
	public short nextSorteio() throws Exception {
		try {
			final String url = "https://servicebus2.caixa.gov.br/portaldeloterias/api/megasena";
			final JSONObject jsonResponse = intService.getJsonFromUrlWithoutCache(url);
			final short sorteio = (short) jsonResponse.getInt("numero");
			final String dataApuracao = jsonResponse.getString("dataApuracao");
			if (dataApuracao ==  LocalDate.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")) ) {
				return sorteio;
			}
			return (short) (sorteio+1);
		} catch (IOException | InterruptedException e) {
			//e.printStackTrace();
		}
		return 0;
	}
	public boolean existeSorteio(int numeroSorteio) throws Exception {
		try {
			// URL correta da API
			final String url = "https://servicebus2.caixa.gov.br/portaldeloterias/api/megasena/"+numeroSorteio;
			final JSONObject jsonResponse = intService.getJsonFromUrlWithoutCache(url);
			final short sorteio = (short) jsonResponse.getInt("numero");
			return (sorteio==numeroSorteio);
		} catch (IOException | InterruptedException e) {
			//e.printStackTrace();
		}
		return false;
	}

    /* 
    // Exemplo de uso
    public static void main(String[] args) {
        MegaSenaResultado resultado = MegaSenaResultado.carregarDados();
        if (resultado != null) {
            System.out.println("Concurso: " + resultado.getNumero());
            System.out.println("Data: " + resultado.getDataApuracao());
            System.out.println("Dezenas: " + resultado.getListaDezenas());
            System.out.println("Valor Arrecadado: R$ " + resultado.getValorArrecadado());
            
            // Exibir rateio de prêmios
            for (RateioPremio rateio : resultado.getListaRateioPremio()) {
                System.out.println(rateio.getDescricaoFaixa() + ": " + 
                                 rateio.getNumeroDeGanhadores() + " ganhadores - R$ " + 
                                 rateio.getValorPremio());
            }
        }
    }
    */
}
