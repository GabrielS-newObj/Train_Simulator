import java.util.Scanner;

public class ControlaEstacao {

    /*
    unidade de medida para o tempo: em segundos
     */
    private final int horarioInicial = 28800;
    private final int horarioFinal = 63000;
    private int horario = 0;
    private int horarioDoTrem = horarioInicial - 1800;
    private String[] relatorioBruto;
    private int linha = 0;
    private final Trem[] arrayTrem = new Trem[((horarioFinal-horarioInicial)/1800)+1];
    private final int distaciaEntreEstacao = 20;
    private int quantEstacoes;
    private int distanciaTotal;
    private final estacaoTrem mainEstacao;
    private int[] posicaoEstacao;
    private int[] posicaoDesvios;
    /*
    velocidadeTrem deve ser em km/h
     */
    private final int velocidadeTrem = 60;
    private ControlaEstacao mainControlaEstacao;
    private final Scanner input = new Scanner(System.in);


    public ControlaEstacao() {
        int quantEstacoes = (int) (Math.random() * 100);
        if (quantEstacoes < 11) quantEstacoes=11;
        if (quantEstacoes > 29) quantEstacoes=29;

        this.quantEstacoes = quantEstacoes;

        distanciaTotal = (((quantEstacoes+1)*distaciaEntreEstacao)+quantEstacoes)+2;

        mainEstacao = new estacaoTrem(distanciaTotal);
        setarPosicaoEstacao();
        setarPosicaoDesvios();

        relatorioBruto = new String[arrayTrem.length*(((quantEstacoes+2)*2)-1)];
    }


    public void begin(int exitCode){
        if (exitCode == -10) validaHorario(exitCode);

        input.close();
        DoMain.printFileTrem(relatorioBruto);
    }

    public void validaHorario(int tempoAcrescido){

        if (horarioDoTrem == horarioFinal) return;

        int quantTrem = 0;

        if (tempoAcrescido > 0) {
            horario += tempoAcrescido;
            quantTrem = horario / 1800;
        }
        else if (tempoAcrescido == -10) quantTrem = 1;

        if (quantTrem != 0){

            Trem[] newTrem = new Trem[quantTrem];

            for (int i = 0; i < quantTrem; i++) {
                horarioDoTrem += 1800;

                newTrem[i] = new Trem(mainEstacao, horarioDoTrem);
                newTrem[i].setNodePosition(newTrem[i]);
                newTrem[i].setRemote(mainControlaEstacao);
            }

            if (tempoAcrescido != -10) horario = horario - (quantTrem * 1800);

            setArrayTrem(newTrem);
        }
    }

    public void setArrayTrem(Trem[] newTrem){

        for (int i = 0, i2 = 0; i < arrayTrem.length && i2 < newTrem.length; i++)
            if (arrayTrem[i] == null ){
                arrayTrem[i] = newTrem[i2];
                i2++;
            }

        startMoveTrem();
    }

    public void startMoveTrem(){
        if(linha % 2 == 0) {
            input.nextLine();
            mostrarEventoAtual();
        }

        mostrarEventoAtual();

        int maxSize = 0;
        while(true){
            for (int i = 0; i < arrayTrem.length; i++) {
                if (arrayTrem[i] != null) {
                    validaHorario(arrayTrem[i].percorre());
                    maxSize++;
                }
            }
            if (maxSize == 0) begin(0);
        }
    }

    private void mostrarEventoAtual(){
        boolean flag = false;
        for(Trem t: arrayTrem)
            if (t != null){
                t.mostrarEventoAtual();
                flag = true;
            }


        if (flag) System.out.println("_______________________________________________________");
    }

    public void removeTrem(Trem forRemove){
        for (int i = 0; i < arrayTrem.length; i++)
            if (arrayTrem[i] != null && arrayTrem[i].equals(forRemove)){
                forRemove.getBaseNode().setElement(null);
                arrayTrem[i] = null;
                startMoveTrem();
            }
    }

    public void addRelatorioBruto(String toString) {
        relatorioBruto[linha] = toString;
        linha++;
    }

    public void setMainControlaEstacao(ControlaEstacao mainControlaEstacao) {
        this.mainControlaEstacao = mainControlaEstacao;
    }

    private void setarPosicaoEstacao(){
        int[] posicaoEstacaoGerada = new int[quantEstacoes+2];
        int cont = 1;

        posicaoEstacaoGerada[0] = 1;

        for (int posicao = distaciaEntreEstacao+1; posicao <= distanciaTotal; posicao+=distaciaEntreEstacao+1){
            posicaoEstacaoGerada[cont] = posicao+1;
            cont++;
        }

        posicaoEstacao = posicaoEstacaoGerada;
    }

    private void setarPosicaoDesvios(){
        int desvios = (posicaoEstacao.length*2)-2;
        int[] posicaoDesviosGerada = new int[desvios];

        posicaoDesviosGerada[0] = 2;
        posicaoDesviosGerada[desvios-1] = (posicaoEstacao[posicaoEstacao.length-1])-1;

        int cont = 1;
        int posicaoEstacaoEmAnalise;
        for (int i = 1; i < posicaoEstacao.length-1; i++) {

            posicaoEstacaoEmAnalise = posicaoEstacao[i];
            posicaoDesviosGerada[cont] = posicaoEstacaoEmAnalise-1;
            cont++;
            posicaoDesviosGerada[cont] = posicaoEstacaoEmAnalise+1;
            cont++;
        }

        posicaoDesvios = posicaoDesviosGerada;
    }

    public int getDistanciaTotal() {
        return distanciaTotal;
    }

    public int[] getPosicaoEstacao() {
        return posicaoEstacao;
    }

    public int getVelocidadeTrem() {
        return velocidadeTrem;
    }

    public int[] getPosicaoDesvios() {
        return posicaoDesvios;
    }

    public int getDistaciaEntreEstacao() {
        return distaciaEntreEstacao;
    }

}
