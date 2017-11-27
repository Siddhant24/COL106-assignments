import java.util.*;

public class LinkedList<E>{
	
	private ListNode<E> head = null;
	private ListNode<E> tail = null;
	private int numNodes = 0;
	
	public E first() throws EmptyListException {
		if(isEmpty()) throw new EmptyListException("The list is empty");
		return head.getElement();
	}
	
	public E last() throws EmptyListException {
		if(isEmpty()) throw new EmptyListException("the list is empty");
		return tail.getElement();
	}
	
	public ListNode<E> getHead() {
		return head;
	}
	
	public ListNode<E> getTail(){
		return tail;
	}
	
	public void addAfter(ListNode<E> curr, E elem) {
		if(curr != null) {
			ListNode<E> newNode = new ListNode<E>(elem, curr.getNext(), curr);
			if(curr.getNext() != null) {
				curr.getNext().setPrev(newNode);			}
			else {
				tail = newNode;
			}
			curr.setNext(newNode);
		}
		else {
			ListNode<E> newNode = new ListNode<E>(elem, head, null);
			if(head != null)
			head.setPrev(newNode);
			head = newNode;
			if(numNodes == 0) tail = newNode;
		}
		numNodes++;
	}
	
	public E remove(ListNode<E> curr) throws EmptyListException{
		if(!isEmpty() && curr != null) {
			E elem = curr.getElement();
			if(curr.getNext() == null && curr.getPrev() == null) {
				head = null;
				tail = null;
			}
			else if(curr.getNext() == null) {
				curr.getPrev().setNext(null);
				tail = curr.getPrev();
			}
			else if(curr.getPrev() == null) {		
				curr.getNext().setPrev(null);
				head = curr.getNext();
			}
			else {	
				curr.getNext().setPrev(curr.getPrev());
				curr.getPrev().setNext(curr.getNext());
			}
			numNodes--;
			return elem;
		}
		else throw new EmptyListException("No node available to remove");
	}
	
	public void addFirst(E elem) {
		addAfter(null, elem);
	}
	
	public void addLast(E elem) {
		addAfter(tail, elem);
	}
	
	public E removeFirst() {
		return remove(head);
	}
	
	public E removeLast() {
		return remove(tail);
	}
	
	public int size() {
		return numNodes;
	}
	
	public boolean isEmpty() {
		return (numNodes == 0);
	}
	
	public void print() throws EmptyListException {
		if(!isEmpty()) {
			ListNode<E> temp = head;
			while(temp != null) {
				System.out.println(temp.getElement());
				temp = temp.getNext();
			}
		}
		else throw new EmptyListException("The list is empty");
	}
	
/* 	private class PositionIterator implements Iterator<ListNode<E>>{
		private ListNode<E> cursor = getHead();
		private ListNode<E> recent = null;
		public boolean hasNext() {return (cursor!=null);}
		public ListNode<E> next() throws NoSuchElementException{
			if(cursor == null) throw new NoSuchElementException();
			recent = cursor;
			cursor = cursor.getNext();
			return recent;
		}
	}
	
	public Iterator<E> iterator(){
		return new PositionIterator();
	} */
	
}


