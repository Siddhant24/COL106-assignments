
public class SinglyLinkedList<E> {

	private static class ListNode<E> {
	
		private E element;
		private ListNode<E> next;
		
		public ListNode(){
			this(null, null);
		}
		
		public ListNode(E elem, ListNode<E> nextNode) {
			element = elem;
			next = nextNode;
		}
		
		public E getElement() {
			return element;
		}
		
		public void setElement(E elem) {
			element = elem;
		}
		
		public void setNext(ListNode<E> newNext) {
			next = newNext;
		}
		
		public ListNode<E> getNext(){
			return next;
		}
	}


	private static class EmptyListException  extends RuntimeException {
		public EmptyListException (String message) {
			super (message);
		}
	}




	protected ListNode<E> head = null;
	protected int numNodes = 0;
	
	public E getFirst() throws EmptyListException{
		if(isEmpty()) throw new EmptyListException("The list is empty");
		return head.getElement();
	}
	
	public void addFirst(E elem) {
		head = new ListNode<E>(elem, head);
		numNodes++;
	}
	
	public E removeFirst() throws EmptyListException{
		if(isEmpty()) throw new EmptyListException("The list is empty");
		E removed = head.getElement();
		head = head.getNext();
		numNodes--;
		return removed;
	}
	
	
	public boolean contains(E elem) throws EmptyListException {
		if(isEmpty()) throw new EmptyListException("The list is empty");
		ListNode<E> temp = head;
		while(temp!=null) {
			if(temp.getElement().equals(elem)) return true;
			temp = temp.getNext();
		}
		return false;
	}
	
	public void print() {
		for(ListNode<E> temp = head; temp != null; temp = temp.getNext()) {
			System.out.println(temp.getElement());
		}
	}
	
	public boolean isEmpty() {
		return head == null ? true : false;
	}
}
