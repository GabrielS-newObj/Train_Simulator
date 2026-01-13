public class estacaoTrem{

    private Node<Trem> begin;

    public estacaoTrem(int percursoTotal){
        begin = new Node<>(null);

        Node<Trem> current = begin;
        Node<Trem> next;

        for(int i = 1; i < percursoTotal; i++){
            next = current.getNext();
            next = new Node<>(null);
            next.setPrevious(current);
            current.setNext(next);
            current = next;
        }
    }


    public Node<Trem> getBegin() {
        return begin;
    }
}