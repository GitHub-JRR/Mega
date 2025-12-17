package MegaPck;

import java.io.BufferedReader;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class Resultado {

	static String dataUltimoSorteio;
	static int numUltimoSorteio;
	static double previsao;
	static List<byte[]> numSorteados;
	static double rateioSena;
	static double rateioQuina;
	static double rateioQuadra;
	static int qtdGandoresSena;

	private static void carregaSorteios(final String xFile) {
		BufferedReader reader;
		final byte anExample[] = new byte[6];
		try {
			reader = new BufferedReader(new FileReader(xFile));
			String line;
			int fase = 0;
			int n;
			while ((line = reader.readLine()) != null) {
				if (fase == 1) {
					if (line.contains("<tr")) {
						fase = 2;
						// System.out.println(line);
					}
				} else if (fase == 2) {
					if (line.contains("<td")) {
						fase = 3;
						n = line.indexOf("<td align=") + 19;
						// System.out.print("Sorteio numero: " + line.substring(n,
						// line.indexOf("</td>")));
						numUltimoSorteio = Integer.parseInt(line.substring(n, line.indexOf("</td>")));
						line = reader.readLine();
						line = reader.readLine();
						n = line.indexOf("<td align=") + 19;
						// System.out.println(" Data do sorteio: " + line.substring(n,
						// line.indexOf("</td>")));
						dataUltimoSorteio = line.substring(n, line.indexOf("</td>"));
						// dataUltimoSorteio = new SimpleDateFormat("dd/MM/yyyy").parse(sDate1);
						line = reader.readLine();
						for (int x = 0; x < 6; x++) {
							line = reader.readLine();
							n = line.indexOf("<td align=") + 19;
							// System.out.println(x + " - " + line.substring(n, n + 3));
							// System.out.print(line.substring(n, n + 3) + " ");
							anExample[x] = Byte.parseByte(line.substring(n, n + 3));
						}
						numSorteados.add(anExample.clone());
						System.out.println();
						line = reader.readLine();
						line = reader.readLine();
						n = line.indexOf("<td align=") + 19;
						qtdGandoresSena = Integer.parseInt(line.substring(n, line.indexOf("</td>")));
						// System.out.println("Numero de ganhadores da sena:"
						// + Integer.parseInt(line.substring(n, line.indexOf("</td>"))));
						line = reader.readLine();
						line = reader.readLine();
						line = reader.readLine();
						line = reader.readLine();
						n = line.indexOf("<td align=") + 19;
						if (Double.parseDouble(
								line.substring(n, line.indexOf("</td>")).replace(".", "").replace(",", ".")) > 0) {
							rateioSena = Double.parseDouble(
									line.substring(n, line.indexOf("</td>")).replace(".", "").replace(",", "."));
						}
						line = reader.readLine();
						n = line.indexOf("<td align=") + 19;
						rateioQuina = Double.parseDouble(
								line.substring(n, line.indexOf("</td>")).replace(".", "").replace(",", "."));
						line = reader.readLine();
						n = line.indexOf("<td align=") + 19;
						rateioQuadra = Double.parseDouble(
								line.substring(n, line.indexOf("</td>")).replace(".", "").replace(",", "."));
					}
				} else if (fase == 3) {
					if (line.contains("<!-- FIM LINHA CIDADE-->")) {
						fase = 4;
						line = reader.readLine();
						line = reader.readLine();
						line = reader.readLine();
						n = line.indexOf("<td align=") + 19;
						// System.out.println("Estimativa proximo sorteio :" + Double.parseDouble(
						// line.substring(n, line.indexOf("</td>")).replace(".", "").replace(",",
						// ".")));
						previsao = Double.parseDouble(
								line.substring(n, line.indexOf("</td>")).replace(".", "").replace(",", "."));
					}
				} else if (fase == 4) {
					if (line.contains("</tr>")) {
						fase = 1;
					}
				} else if (fase == 0) {
					if (line.contains("<title>Resultados Mega Sena</title>")) {
						fase = 1;
						System.out.println(line);
					}
				}
			}
			reader.close();
		} catch (final IOException e) {
			e.printStackTrace();
		}
		return;
	}

	private static String getURL(final String xFile) {
		BufferedReader reader;
		String saida = "";
		try {
			reader = new BufferedReader(new FileReader(xFile));
			String line = reader.readLine();
			while (line != null & saida.length() == 0) {
				if (line.contains("Download de Resultados")) {
					line = reader.readLine();
					while (line != null & saida.length() == 0) {
						if (line.contains("href")) {
							saida = line;
						}
						line = reader.readLine();
					}
				}
				// System.out.println(line);
				// read next line
				line = reader.readLine();
			}
			reader.close();
		} catch (final IOException e) {
			e.printStackTrace();
		}
		return saida.substring(saida.indexOf("href=") + 6, saida.length() - 1);
	}

	public static void saveURL(final String filename, final String urlString) {
		try {
			// final URL obj = new URL(urlString);
			final URL obj = new URI(urlString).toURL();
			HttpURLConnection conn = (HttpURLConnection) obj.openConnection();
			conn.setReadTimeout(5000);
			// System.out.println("Request URL ... " + urlString);
			boolean redirect = false;
			// normally, 3xx is redirect
			final int status = conn.getResponseCode();
			if (status != HttpURLConnection.HTTP_OK) {
				if (status == HttpURLConnection.HTTP_MOVED_TEMP || status == HttpURLConnection.HTTP_MOVED_PERM
						|| status == HttpURLConnection.HTTP_SEE_OTHER) {
					redirect = true;
				}
			}
			// System.out.println("Response Code ... " + status);
			if (redirect) {
				final String newUrl = conn.getHeaderField("Location");
				final String cookies = conn.getHeaderField("Set-Cookie");
				conn = (HttpURLConnection) new URI(newUrl).toURL().openConnection();
				conn.setRequestProperty("Cookie", cookies);
				// System.out.println("Redirect to URL : " + newUrl);
			}
			final BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
			String inputLine;
			final FileOutputStream fos = new FileOutputStream(filename);
			while ((inputLine = in.readLine()) != null) {
				inputLine += "\n";
				fos.write(inputLine.getBytes());
			}
			in.close();
			fos.close();
			// System.out.println("URL Content... \n" + html.toString());
			// System.out.println("Done");
		} catch (final Exception e) {
			e.printStackTrace();
		}
	}

	@SuppressWarnings("static-access")
	Resultado() {
		this.numUltimoSorteio = 0;
		this.previsao = 0;
		this.numSorteados = new ArrayList<>();
		this.dataUltimoSorteio = "";
		this.rateioSena = 0;
		this.rateioQuina = 0;
		this.rateioQuadra = 0;
		this.Reset();
	}

	public void Reset() {
		HttpURLConnection.setFollowRedirects(false);
		final String xURL = "http://loterias.caixa.gov.br/wps/portal/loterias/landing/megasena";
		String xFile = "C:\\temp\\teste0.html";
		saveURL(xFile, xURL);
		final String urlDados = getURL(xFile);
		xFile = "C:\\temp\\teste0F.html";
		final String urlFinal = "http://loterias.caixa.gov.br/wps/portal/loterias/landing/megasena/!ut/p/a1/04_Sj9CPykssy0xPLMnMz0vMAfGjzOLNDH0MPAzcDbwMPI0sDBxNXAOMwrzCjA0sjIEKIoEKnN0dPUzMfQwMDEwsjAw8XZw8XMwtfQ0MPM2I02-AAzgaENIfrh-FqsQ9wNnUwNHfxcnSwBgIDUyhCvA5EawAjxsKckMjDDI9FQE-F4ca/"
				+ urlDados;
		saveURL(xFile, urlFinal);
		carregaSorteios(xFile);
	}
}
