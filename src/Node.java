public class Node<Trem>{

    private Trem element;
    private Node<Trem> next;
    private Node<Trem> previous;

    public Node(Trem e) {
        element = e;
        next = null;
        previous = null;
    }

    public Trem getElement() {
        return element;
    }

    public Node<Trem> getNext(){
        return next;
    }

    public Node<Trem> getPrevious(){return previous;}

    public void setElement( Trem e ) {
        element = e;
    }

    public void setNext( Node<Trem> n ) {
        next = n;
    }

    public void setPrevious( Node<Trem> n ) {previous = n;}

}
