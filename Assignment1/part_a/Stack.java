
import java.util.EmptyStackException; 

public class Stack<E> {
	private SinglyLinkedList<E> data = new SinglyLinkedList<E>();
	private long size = 0;
	
	public void push(E element) {
		data.addFirst(element);
		size++;
	}
	
	public E pop() {
		if(isEmpty()) throw new EmptyStackException();
		size--;
		E removed = data.removeFirst();
		return removed;
	}
	
	public boolean isEmpty() {
		return data.isEmpty();
	}
	
	public E top() {
		return data.getFirst();
	}
	
	public long size() {
		return size;
	}

}
