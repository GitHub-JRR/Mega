package MegaPck;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URI;

import org.json.JSONObject;

public class service {
    private static GerenciadorMensagens gm;
	public service(GerenciadorMensagens gmPar) {
		gm=gmPar;
	}
	
	public  JSONObject getJsonFromUrl(final String urlBase, final int maxTentativas, final long tempoEsperaMs, boolean semCash) throws Exception {
		int tentativa = 1;
		Exception ultimoErro = null;
		if (maxTentativas < 1) {
			throw new IllegalArgumentException("Número de tentativas deve ser pelo menos 1");
		}
		String url = urlBase;
		while (tentativa <= maxTentativas) {
			HttpURLConnection connection = null;
			BufferedReader in = null;
			try {
                if (semCash) {
                	 url = urlBase + (urlBase.contains("?") ? "&" : "?") + 
            				"timestamp=" + System.currentTimeMillis();
                }
				connection = (HttpURLConnection) new URI(url).toURL().openConnection();
				// Configurações anti-cache
				connection.setRequestMethod("GET");
				connection.setUseCaches(false);
				connection.setRequestProperty("Cache-Control", "no-cache, no-store, must-revalidate");
				connection.setRequestProperty("Pragma", "no-cache");
				connection.setRequestProperty("Expires", "0");
				connection.setConnectTimeout(10000);
				connection.setReadTimeout(10000);
				int responseCode = connection.getResponseCode();
				if (responseCode != HttpURLConnection.HTTP_OK) {
					throw new IOException("HTTP error code: " + responseCode);
				}
				in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
				StringBuilder content = new StringBuilder();
				String inputLine;

				while ((inputLine = in.readLine()) != null) {
					content.append(inputLine);
				}

				return new JSONObject(content.toString());

			} catch (Exception e) {
				ultimoErro = e;
				gm.registrarMensagem("Falha na tentativa " + tentativa + "/"+maxTentativas+" - " + e.getClass().getSimpleName() + ": " +
				e.getMessage()+" URL:"+url);
				if (in != null) try { in.close(); } catch (IOException ioE) { /* Ignorar */ }
				if (connection != null) connection.disconnect();

				// Espera antes da próxima tentativa (exceto na última)
				if (tentativa < maxTentativas) {
					gm.registrarMensagem("Aguardando " + (tempoEsperaMs *tentativa)+ "ms antes da próxima tentativa...");
					try {
						Thread.sleep(tempoEsperaMs*tentativa);
					} catch (InterruptedException ie) {
						Thread.currentThread().interrupt();
						throw new Exception("Interrompido durante espera entre tentativas", ie);
					}

				}

				tentativa++;
			}
		}

		throw new Exception("Não foi possível obter JSON após " + maxTentativas + 
				" tentativas. Último erro: " + 
				(ultimoErro != null ? ultimoErro.getMessage() : "Nenhum detalhe disponível"));
	}
	public JSONObject getJsonFromUrlWithoutCache(String baseUrl) throws Exception {
		// Adiciona um timestamp como parâmetro para evitar cache
		//String urlWithTimestamp = baseUrl + (baseUrl.contains("?") ? "&" : "?") + 
		//		"timestamp=" + System.currentTimeMillis();
		return getJsonFromUrl(baseUrl,20,10000,true);
	}
}
