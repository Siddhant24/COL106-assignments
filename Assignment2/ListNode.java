public class ListNode<T>{
    private T element;
    private ListNode<T> next;
    private ListNode<T> prev;
    
    public ListNode(T elem, ListNode<T> n, ListNode<T> p) {
        element = elem;
        next = n;
        prev = p;
        
    }
    
    public T getElement() {
        return element;
    }
    
    public ListNode<T> getNext() {
        return next;
    }
    
    public ListNode<T> getPrev(){
        return prev;
    }
    
    public void setNext(ListNode<T> newNext) {
        next = newNext;
    }
    
    public void setPrev(ListNode<T> newPrev) {
        prev = newPrev;
    }
}
