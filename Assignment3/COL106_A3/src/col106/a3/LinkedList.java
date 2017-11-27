package col106.a3;

import java.util.Iterator;
import java.util.NoSuchElementException;

public class LinkedList<E> implements Iterable<E>{
	
	private class ListNode<T>{
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
	
	public E search(E elem) throws EmptyListException{
		if(isEmpty()) throw new EmptyListException("List is empty");
		else {
			ListNode<E> temp = getHead();
			while(temp!=null) {
				if(temp.getElement().equals(elem)) return elem;
				temp = temp.getNext();
			}
			return null;
		}
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
	
	private class ElementIterator implements Iterator<E>{
		private ListNode<E> cursor = getHead();
		private ListNode<E> recent = null;
		public boolean hasNext() {return (cursor!=null);}
		public E next() throws NoSuchElementException{
			if(cursor == null) throw new NoSuchElementException();
			recent = cursor;
			cursor = cursor.getNext();
			return recent.getElement();
		}
	}
	
	public Iterator<E> iterator(){
		return new ElementIterator();
	}
	
}



