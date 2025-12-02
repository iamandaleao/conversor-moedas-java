import com.google.gson.Gson;
import com.google.gson.JsonObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;

public class Main {

    // Coloque sua chave da ExchangeRate API aqui:
    private static final String API_KEY = "c409302c1a6bfcc1feb89d41";

    public static void main(String[] args) {

        Scanner scanner = new Scanner(System.in);

        System.out.println("=== CONVERSOR DE MOEDAS ===");
        System.out.println("1 - USD -> BRL");
        System.out.println("2 - BRL -> USD");
        System.out.println("3 - EUR -> BRL");
        System.out.println("4 - BRL -> EUR");
        System.out.println("5 - USD -> EUR");
        System.out.println("6 - EUR -> USD");
        System.out.print("Escolha uma opção (1 a 6): ");

        int opcao = scanner.nextInt();

        System.out.print("Digite o valor a converter: ");
        double valor = scanner.nextDouble();

        String moedaOrigem = "";
        String moedaDestino = "";

        switch (opcao) {
            case 1 -> { moedaOrigem = "USD"; moedaDestino = "BRL"; }
            case 2 -> { moedaOrigem = "BRL"; moedaDestino = "USD"; }
            case 3 -> { moedaOrigem = "EUR"; moedaDestino = "BRL"; }
            case 4 -> { moedaOrigem = "BRL"; moedaDestino = "EUR"; }
            case 5 -> { moedaOrigem = "USD"; moedaDestino = "EUR"; }
            case 6 -> { moedaOrigem = "EUR"; moedaDestino = "USD"; }
            default -> {
                System.out.println("Opção inválida, encerrando.");
                return;
            }
        }

        try {
            double taxa = obterCotacao(moedaOrigem, moedaDestino);
            double convertido = valor * taxa;

            System.out.printf("\n%.2f %s equivalem a %.2f %s\n",
                    valor, moedaOrigem, convertido, moedaDestino);

        } catch (Exception e) {
            System.out.println("Erro ao consultar API: " + e.getMessage());
        }

        System.out.println("\nPrograma encerrado.");
    }

    // Método que chama a API e retorna a taxa de conversão
    public static double obterCotacao(String base, String destino) throws Exception {

        String urlString =
                "https://v6.exchangerate-api.com/v6/" +
                        API_KEY +
                        "/latest/" +
                        base;

        URL url = new URL(urlString);
        HttpURLConnection conexao = (HttpURLConnection) url.openConnection();
        conexao.setRequestMethod("GET");

        BufferedReader reader =
                new BufferedReader(new InputStreamReader(conexao.getInputStream()));

        StringBuilder resposta = new StringBuilder();
        String linha;

        while ((linha = reader.readLine()) != null) {
            resposta.append(linha);
        }

        reader.close();

        // Usando Gson para desserializar o JSON
        Gson gson = new Gson();
        JsonObject json = gson.fromJson(resposta.toString(), JsonObject.class);

        // resultado → conversion_rates → moedaDestino
        double taxa = json.getAsJsonObject("conversion_rates")
                .get(destino)
                .getAsDouble();

        return taxa;
    }
}
