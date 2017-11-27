
public class ArrayDequeue implements DequeInterface {
	
	private Object[] data = new Object[1];
	private int head = -1;
	private int tail = -1;
	private int size = 0;
	
  public void insertFirst(Object o){
  	if(head == -1) {
  		data[0] = o;
  		head = 0;
  		tail = 0;
  	}
  	else {
	  	if(size == data.length) data = extend(data, head);
	  	head = (head-1+data.length)%data.length;
	  	data[head] = o;
  	}
  	size++;
  }
  
  public void insertLast(Object o){
  	if(tail == -1) {
  		data[0] = o;
  		head = 0;
  		tail = 0;
  	}
  	else {
  		if(size == data.length) data = extend(data, head);
    	tail = (tail+1)%data.length;
    	data[tail] = o;
  	}	
  	size++;
  }
  
  public Object removeFirst() throws EmptyDequeException{
    if(!isEmpty()) {
    	Object temp = data[head];
    	head = (head+1)%data.length;
    	size--;
    	return temp;
    }
    else throw new EmptyDequeException("The deque is empty");
  }
  
  public Object removeLast() throws EmptyDequeException{
  	if(!isEmpty()) {
    	Object temp = data[tail];
  		tail = (tail-1+data.length)%data.length;
  		size--;
    	return temp;
    }
    else throw new EmptyDequeException("The deque is empty");
  }
  
  public Object first() throws EmptyDequeException{
    if(!isEmpty()) {
    	return data[head];
    }
    else throw new EmptyDequeException("The deque is empty");
  }
  
  public Object last() throws EmptyDequeException{
  	if(!isEmpty()) {
    	return data[tail];
    }
    else throw new EmptyDequeException("The deque is empty");
  }
  
  public int size(){
    return size;
  }
  
  public boolean isEmpty(){
    return (size == 0);
  }
  
  public String toString(){
    StringBuilder sb = new StringBuilder();
    sb.append('[');
    int i;
    for(i=0; i<size; i++) {
    	sb.append(data[(head+i)%data.length]);
  		if(i != size-1) sb.append(", ");
    }
    sb.append(']');
    return sb.toString();
  }
  
  private Object[] extend(Object[] data, int h) {
  	Object[] newData = new Object[data.length*2];
  	int i;
  	for(i=0; i<data.length ; i++) {
  		newData[i] = data[(h+i)%data.length];
  	}
  	head = 0;
  	tail = data.length-1;
  	return newData;
  }
  
  public static void main(String[] args){
    int  N = 10;
    DequeInterface myDeque = new ArrayDeque1();
    for(int i = 0; i < N; i++) {
      myDeque.insertFirst(i);
      System.out.println(myDeque.toString());
      myDeque.insertLast(-1*i);
    	System.out.println(myDeque.toString());
    }
   
    int size1 = myDeque.size();
    System.out.println("Size: " + size1);
    System.out.println(myDeque.toString());
    
    if(size1 != 2*N){
      System.err.println("Incorrect size of the queue.");
    }
    
    //Test first() operation
    try{
      int first = (int)myDeque.first();
      int size2 = myDeque.size(); //Should be same as size1
      if(size1 != size2) {
        System.err.println("Error. Size modified after first()");
      }
    }
    catch (EmptyDequeException e){
      System.out.println("Empty queue");
    }
    
    //Remove first N elements
    for(int i = 0; i < N; i++) {
      try{
        int first = (Integer)myDeque.removeFirst();
      }
      catch (EmptyDequeException e) {
        System.out.println("Cant remove from empty queue");
      }
      
    }

    int size3 = myDeque.size();
    System.out.println("Size: " + myDeque.size());
    System.out.println(myDeque.toString());
    
    if(size3 != N){
      System.err.println("Incorrect size of the queue.");
    }
    
    try{
      int last = (int)myDeque.last();
      int size4 = myDeque.size(); //Should be same as size3
      if(size3 != size4) {
        System.err.println("Error. Size modified after last()");
      }
    }
    catch (EmptyDequeException e){
      System.out.println("Empty queue");
    }
    
    //empty the queue  - test removeLast() operation as well
    while(!myDeque.isEmpty()){
        try{
          int last = (int)myDeque.removeLast();
        }
        catch (EmptyDequeException e) {
          System.out.println("Cant remove from empty queue");
        }
    }
    
    int size5 = myDeque.size();
    if(size5 != 0){
      System.err.println("Incorrect size of the queue.");
    }
  }
  
}