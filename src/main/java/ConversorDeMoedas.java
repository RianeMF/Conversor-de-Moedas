import com.google.gson.Gson;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Map;
import java.util.Scanner;

public class ConversorDeMoedas {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        String[] opcoes = {
                "USD -> BRL",
                "BRL -> USD",
                "USD -> EUR",
                "EUR -> USD",
                "USD -> ARS",
                "ARS -> USD"
        };

        System.out.println("=== CONVERSOR DE MOEDAS ===");
        for (int i = 0; i < opcoes.length; i++) System.out.println ((i + 1) + ". " + opcoes[i]);

        System.out.print("Escolha uma opção (1-6): ");
        int escolha = scanner.nextInt();

        System.out.print("Digite o valor a ser convertido: ");
        double valor = scanner.nextDouble();

        String de = opcoes[escolha - 1].split(" -> ")[0];
        String para = opcoes[escolha - 1].split(" -> ")[1];

        try {
            double taxa = obterTaxa(de, para);
            double convertido = valor * taxa;

            System.out.printf("Resultado: %.2f %s = %.2f %s\n", valor, de, convertido, para);
        } catch (Exception e) {
            System.out.println("Erro: " + e.getMessage());
        }

        System.out.println("Programa encerrado.");
    }

    public static double obterTaxa(String de, String para) throws Exception {
        String apiKey = "83cf042fb7175ef748c55a5f";
        String urlStr = "https://v6.exchangerate-api.com/v6/" + apiKey + "/latest/" + de;

        URL url = new URL(urlStr);
        HttpURLConnection conexao = (HttpURLConnection) url.openConnection();
        conexao.setRequestMethod("GET");

        Reader reader = new InputStreamReader(conexao.getInputStream());
        Gson gson = new Gson();
        ExchangeRateResponse resposta = gson.fromJson(reader, ExchangeRateResponse.class);

        return resposta.conversion_rates.get(para);
    }

    // Classe interna para representar o JSON
    static class ExchangeRateResponse {
        Map<String, Double> conversion_rates;
    }
}