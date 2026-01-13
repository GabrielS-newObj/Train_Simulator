public class Trem {

    private int passageirosEmbarcados;
    private int passageirosDesembarcados;
    private int totalPassageiros;
    private int estationPosition;
    private boolean isReturning = false;
    private int positionInTrilho;
    private Trem nodePosition;
    private Node<Trem> baseNode;
    private final estacaoTrem acessEstation;
    private ControlaEstacao remote;
    private int tempoDePercursoTotal;
    private int horarioDoTrem;
    private boolean usePercorreToAvoidLoop = false;


    public Trem(estacaoTrem acessEstation, int horarioDoTrem){
        passageirosEmbarcados = 0;
        passageirosDesembarcados = 0;
        totalPassageiros = 0;
        estationPosition = 0;
        positionInTrilho = 1;
        this.acessEstation = acessEstation;
        this.horarioDoTrem = horarioDoTrem;
    }

    public int percorre(){
        if (positionInTrilho == remote.getPosicaoEstacao()[remote.getPosicaoEstacao().length-1] && !isReturning)
            isReturning = true;
        else if (positionInTrilho == 1 && isReturning) {
            remote.addRelatorioBruto(toString());
            remote.removeTrem(nodePosition);
        }


        int resultadoDaEstacao = verificaSeEstaNaEstacao();

        boolean isNecessarySkipPercorre = verificaTremVindo();

        if (isNecessarySkipPercorre && !isReturning) return 0;



        Trem savedElement = null;

        if (!isReturning) {
            if(baseNode.getNext().getElement() != null){
                savedElement = baseNode.getNext().getElement();
                savedElement.setPositionInTrilho(-1);
                savedElement.setBaseNode(baseNode);
            }
            baseNode.getNext().setElement(nodePosition);
            baseNode = baseNode.getNext();
            baseNode.getPrevious().setElement(savedElement);
            positionInTrilho++;
            if (savedElement != null) usePercorreToAvoidLoop = true;
        }
        else {
            if(baseNode.getPrevious().getElement() != null){
                savedElement = baseNode.getPrevious().getElement();
                savedElement.setPositionInTrilho(1);
                savedElement.setBaseNode(baseNode);
            }
            baseNode.getPrevious().setElement(nodePosition);
            baseNode = baseNode.getPrevious();
            baseNode.getNext().setElement(savedElement);
            positionInTrilho--;
            if (savedElement != null) usePercorreToAvoidLoop = true;
        }


        if (usePercorreToAvoidLoop) {
            usePercorreToAvoidLoop = false;
            int novoResultadoDaEstacao = percorre();
            return (resultadoDaEstacao+(3600/ remote.getVelocidadeTrem())) + novoResultadoDaEstacao;
        }
        return resultadoDaEstacao+(3600/ remote.getVelocidadeTrem());
    }

    public void mostrarEventoAtual(){
        int horario = horarioDoTrem/3600;
        String meia = "00";
        if ((horarioDoTrem/1800) % 2 != 0) meia = "30";

        String sentido = (isReturning ? "B para A" : "A para B");

        System.out.println
            ("Trem das "+horario+":"+meia+" horas\n" +
            "Está indo de "+sentido+'\n'+
            "Sua distância no trilho é "+positionInTrilho+" nós de "+remote.getDistanciaTotal()+'\n'+
            "Última estação desembarcada: "+estationPosition+'\n');

        System.out.print("\n\n");
    }

    private int verificaSeEstaNaEstacao(){
        for(int estacao : remote.getPosicaoEstacao())
            if (estacao == positionInTrilho && estationPosition != estacao){
                estationPosition = estacao;
                int resultadoDaEstacao = calculaPassageiros();
                remote.addRelatorioBruto(toString());
                return resultadoDaEstacao;
            }

        return 0;
    }

    private int calculaPassageiros(){
        passageirosDesembarcados = (int) (Math.random()*100);

        passageirosEmbarcados = (int) (Math.random()*100);

        if (passageirosDesembarcados > 10) passageirosDesembarcados = 10;
        if (passageirosEmbarcados > 10) passageirosEmbarcados = 10;

        totalPassageiros = passageirosDesembarcados + passageirosEmbarcados;

        if (totalPassageiros != 0 && totalPassageiros < 10) totalPassageiros = 10;
        else if (totalPassageiros > 50) totalPassageiros = 50;


        if (totalPassageiros != 0){
            if (totalPassageiros % 2 != 0) totalPassageiros++;
            tempoDePercursoTotal += totalPassageiros*30;
            return totalPassageiros*30;
        }

        tempoDePercursoTotal += 60;
        return 60;
    }

    private boolean verificaTremVindo(){
        int estacaoAFrentre = verificaEstacaoAFrente();

        if (estacaoAFrentre != 0) {

            int somador = isReturning ? -1 : 1;
            Node<Trem> current = baseNode;
            for (int i = positionInTrilho; i != estacaoAFrentre; i += somador) {
                if (!isReturning)
                    current = current.getNext();
                else
                    current = current.getPrevious();

                if (current == null) return false;
                else if (current.getElement() != null && (current.getElement().isReturning() != this.isReturning)) {
                    if (verificaSeEstaNoDesvio()) return true;
                    verificaDesvios(i);
                    return true;
                }
            }
        }

        return false;
    }

    private int verificaEstacaoAFrente(){
        int posicaoAtual = positionInTrilho;
        posicaoAtual++;

        int somador = isReturning ? -1 : 1;
        int condicao = remote.getDistaciaEntreEstacao()*somador;

        for(int i = 0; i != condicao; i += somador){
            for (int i2 =0; i2 < remote.getPosicaoEstacao().length; i2++)
                if (posicaoAtual == remote.getPosicaoEstacao()[i2]) return i2;

            posicaoAtual++;
        }

        return 0;
    }

    private boolean verificaSeEstaNoDesvio(){
        for (int desvio : remote.getPosicaoDesvios())
            if (desvio == positionInTrilho) return true;


        return false;
    }

    private void verificaDesvios(int posicaoAteTrem){
        int somador = isReturning ? -1 : 1;


        int quantDesvioIgnorado = 0;

        if (isReturning) {
            int cont = remote.getPosicaoDesvios().length-1;
            for(int i = remote.getPosicaoDesvios()[cont]; i > positionInTrilho; i=remote.getPosicaoDesvios()[cont--])
                quantDesvioIgnorado = cont;
        }
        else {
            int cont = 0;
            for (int i = remote.getPosicaoDesvios()[cont]; i < positionInTrilho; i=remote.getPosicaoDesvios()[cont++])
                quantDesvioIgnorado = cont;
        }

        int posicaoDesvio1 = remote.getPosicaoDesvios()[quantDesvioIgnorado];

        Node<Trem> current = baseNode;
        for (int i = positionInTrilho; i != posicaoAteTrem; i += somador) {
            if (i == posicaoDesvio1) {
                current.setElement(nodePosition);
                baseNode.setElement(null);
                baseNode = current;
                positionInTrilho = i;
                break;
            }
            if (isReturning) current = current.getPrevious();
            else current = current.getNext();
        }
    }

    @Override
    public String toString() {
        String sentido = (isReturning ? "de B para A" : "de A para B");

        return "Trem das "+horarioDoTrem+" (em segundos) {" +
                "Passageiros Embarcados=" + passageirosEmbarcados +
                ", Passageiros Desembarcados=" + passageirosDesembarcados +
                ", Total de Passageiros=" + totalPassageiros +
                ", Número da Estação=" + estationPosition +
                ", Sentido do Trajeto=" + sentido +
                ", Posição no Trilho da Ferrovia=" + positionInTrilho +
                ", Tempo de Percurso até o momento=" + tempoDePercursoTotal +
                '}';
    }

    public void setNodePosition(Trem nodePosition){
        this.nodePosition = nodePosition;
        baseNode = acessEstation.getBegin();
    }

    public void setRemote(ControlaEstacao remote) {
        this.remote = remote;
        tempoDePercursoTotal = (int) (3600 * (remote.getDistanciaTotal() / remote.getVelocidadeTrem()));
    }

    public boolean isReturning() {
        return isReturning;
    }

    public void setBaseNode(Node<Trem> baseNode) {
        this.baseNode = baseNode;
    }

    public Node<Trem> getBaseNode() {
        return baseNode;
    }

    public void setPositionInTrilho(int positionInTrilho) {
        this.positionInTrilho += positionInTrilho;
    }


}
