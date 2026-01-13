import java.io.*;

public class DoMain {
    public static void main(String[] args) {

        ControlaEstacao acess = new ControlaEstacao();

        acess.setMainControlaEstacao(acess);

        System.out.println("\n\t Aperte enter a qualquer momento para ver o que está acontecendo na Simulação da Estação!\n\n\n");
        acess.begin(-10);
    }

    public static void printFileTrem(String[] relatorioBruto) {

        File relatorioDeViagem = new File("RelatorioDeViagem");

        try (PrintWriter writer = new PrintWriter(new FileWriter(relatorioDeViagem))) {

            for (String linha : relatorioBruto) {
                if (linha == null) {break;}
                writer.println(linha);
            }

            writer.close();
        } catch (IOException io) {
            System.out.println("Erro ao ESCREVER no arquivo");
            io.printStackTrace();
        }

        try (BufferedReader buffer = new BufferedReader(new FileReader(relatorioDeViagem))) {
            String linha = buffer.readLine();

            while (linha != null) {
                System.out.println(linha);
                System.out.println("\n\n");
                linha = buffer.readLine();
            }


            buffer.close();
        } catch (IOException io) {
            System.out.println("Erro ao LER o arquivo");
            io.printStackTrace();
        }

        System.out.println("\n\n\t\t\t\t OBRIGADO POR USAR MEU SIMULADOR :)");

        System.exit(0);
    }
}