package col106.a3;

import java.util.*;

public class BTree<Key extends Comparable<Key>,Value> implements DuplicateBTree<Key,Value> {
	
		public static class Node<Key extends Comparable<Key>,Value>{
			public Node<Key, Value> parent;
			public Pair<Key, Value>[] keys;
			public Node<Key, Value>[] children;
			public int numKeys = 0;
			
			public Node(Node<Key, Value> p, int b) {
				parent = p;
				keys = (Pair<Key, Value>[])new Pair[b];
				children = (Node<Key, Value>[])new Node[b+1];
			}
			
			public int findPos(Key k) {
				int i;
				for(i=0; i<numKeys; i++) {
					if(keys[i].first().compareTo(k) >= 0) {
						return i;
					}
				}
				return i;
			}
						
			public int addKey(Key k, Value v) {
				if(numKeys == 0) {
					keys[0] = new Pair<Key, Value>(k, v);
					numKeys++;
					return 0;
				}
				int pos = findPos(k);
				int i;
				children[numKeys+1] = children[numKeys];
				for(i=numKeys-1; i >= pos; i--) {
					keys[i+1] = keys[i];
					children[i+1] = children[i];
				}

				keys[pos] = new Pair<Key, Value>(k, v); 
				numKeys++;
				return pos;
			}			
		}
		
		private int size = 0;
		private int t;
		public Node<Key, Value> root = null;

    public BTree(int b) throws bNotEvenException {  /* Initializes an empty b-tree. Assume b is even. */
    	if(b%2 != 0) throw new bNotEvenException();
    	root = new Node<Key, Value>(null, b);
    	t = b/2;
    }

    @Override
    public boolean isEmpty() {
    	return (size == 0);
    }

    @Override
    public int size() {
      return size;
    }

    @Override
    public int height() {
    	if(isEmpty()) return -1;
      int h = 0;
    	Node<Key, Value> temp = root;
    	while(temp.children[0] != null) {
    		h++;
    		temp = temp.children[0];
    	}
    	return h;
    }

    @Override
    public List<Value> search(Key key) throws IllegalKeyException {
			List<Value> list = new ArrayList<Value>(size);
    	LinkedList<Node<Key,Value>> q = new LinkedList<Node<Key,Value>>();
    	q.addLast(root);
    	Node<Key, Value> temp = root;
    	int i;
    	while(!q.isEmpty()) {
    		temp = q.removeFirst();
    		int token = 0;
    		for(i=0;i<temp.numKeys;i++) {
    			int cmp = temp.keys[i].first().compareTo(key);
    			if(cmp == 0) {
      			list.add(temp.keys[i].second());
      		}
      		if(temp.children[i] != null && cmp >= 0) {
      			q.addLast(temp.children[i]);
      		}
      		if(cmp > 0) {
      			token = 1;
      			break;
      		}
    		}
    		if(temp.children[i] != null && token == 0) {
    			q.addLast(temp.children[i]);
    		}
    	}
//			if(list.isEmpty()) throw new IllegalKeyException();
    	return list;
    }
    
    public Pair<Node<Key, Value>, Integer> searchKey(Key k, Node<Key, Value> root){
    	if(root == null) return null;
    	int i = root.findPos(k);
    	Boolean b = false;
    	if(i == root.numKeys || (root.keys[i].first().compareTo(k) != 0)) {
    		if(i == root.numKeys) b = true;
    		if(root.children[i].numKeys == t-1) {
    			checkUnderflow(root.children[i], i);
    		}
    		if(b && i > root.numKeys) {
    			return searchKey(k, root.children[i-1]);
    		}
    		else {
    			return searchKey(k, root.children[i]);
    		}
    	}
    	else {
    		return new Pair<Node<Key, Value>, Integer>(root, i);
    	}
    }

    @Override
    public void insert(Key key, Value val) {
    	Node<Key, Value> temp = root;
    	int pos;
    	while(temp.children[0] != null) {
    		if(temp.numKeys == 2*t-1) {
    			temp = split(temp);
    		}
    		pos = temp.findPos(key);
    		temp = temp.children[pos];
    	}
    	if(temp.numKeys == 2*t-1) {
  			temp = split(temp);
  		}
    	while(temp.children[0] != null) {
    		pos = temp.findPos(key);
    		temp = temp.children[pos];
    	}
    	temp.addKey(key, val);
    	size++;
    }

    @Override
    public void delete(Key key) throws IllegalKeyException{
    	List<Value> l = search(key);
    	int i = l.size();
    	if(i == 0) throw new IllegalKeyException();
    	while(i != 0) {
    		remove(key, root);
    		i--;
    		size--;

    	}
    }
    
    
    public void remove(Key k, Node<Key, Value> root) {
    	Pair<Node<Key, Value>, Integer> n = searchKey(k, root);
    	int i;
    	int pos = n.second();
    	if(n.first().children[0] == null) {
    		for(i=pos+1;i<n.first().numKeys;i++) {
    			n.first().keys[i-1] = n.first().keys[i];
    		}
    		n.first().numKeys--;
    	}
    	else {
    		if(n.first().children[pos].numKeys >= t) {
    			Pair<Key, Value> pre = swapPre(n.first(), pos);
    			n.first().keys[pos] = pre;
    			remove(pre.first(), n.first().children[pos]);
    		}
    		else if(n.first().children[pos+1].numKeys >=t) {
    			Pair<Key, Value> suc = swapSuc(n.first(), pos);
    			n.first().keys[pos] = suc;
    			remove(suc.first(), n.first().children[pos+1]);
    		}
    		else {
						n.first(merge(n.first().children[pos], pos, false));
						remove(k, n.first());
    		}
    	}
    }
    
    public String toString() {
    	return stringify(root);
    }
    public String stringify(Node<Key, Value> root) {
    	StringBuilder s = new StringBuilder();
    	int i;
  		s.append('[');
  		for(i=0; i<root.numKeys; i++) {
  			if(root.children[i] != null)
  			{s.append(stringify(root.children[i]));
  			s.append(", ");}
  			s.append(root.keys[i].first());
  			s.append('=');
  			s.append(root.keys[i].second());
  			if(root.children[i+1] != null || i != root.numKeys-1)
  			s.append(", ");
  		}
			if(root.children[i] != null)
			s.append(stringify(root.children[i]));
  		s.append(']');
    	return s.toString();

    }
    
    private Node<Key, Value> split(Node<Key, Value> n){
    	int splitPos = t-1;
  		int i;
  		Pair<Key, Value> movedKey = n.keys[splitPos];
  		Node<Key, Value> u;
    	if(n.parent != null) {
    		u = n.parent;
    	}
    	else {
    		u = new Node<Key, Value>(null, 2*t);
    		root = u;
    	}
    	int pos = u.addKey(movedKey.first(), movedKey.second());
  		Node<Key, Value> left = new Node<Key, Value>(u,2*t);
  		Node<Key, Value> right = new Node<Key, Value>(u,2*t);
  		for(i=0; i<splitPos; i++) {
  			left.keys[i] = n.keys[i];
  			left.children[i] = n.children[i];
    		if(n.children[i] != null)
  			n.children[i].parent = left;
  		}

  		left.children[i] = n.children[i];
  		if(n.children[i] != null)
			n.children[i].parent = left;
			left.numKeys += splitPos;
  		for(i=0; i+splitPos+1<n.numKeys; i++) {
  			right.keys[i] = n.keys[i+splitPos+1];
  			right.children[i] = n.children[i+splitPos+1];
    		if(n.children[i+splitPos+1] != null)
  			n.children[i+splitPos+1].parent = right;
  		}
  		right.children[i] = n.children[i+splitPos+1];
  		if(n.children[i+splitPos+1] != null)
			n.children[i+splitPos+1].parent = right;
			right.numKeys += n.numKeys-splitPos-1;
			u.children[pos] = left;
			u.children[pos+1] = right;
			return u;
    }
    
    private Node<Key, Value> redistribute(Node<Key, Value> n, int pos, Boolean isLeft){
    	if(isLeft) {
    		Node<Key, Value> leftSibling = n.parent.children[pos-1];
    		int i;
    		for(i=n.numKeys-1;i>=0;i--) {
    			n.keys[i+1] = n.keys[i];
    		}
    		for(i=n.numKeys;i>=0;i--) {
    			n.children[i+1] = n.children[i];
    		}
    		n.keys[0] = n.parent.keys[pos-1];
    		n.children[0] = leftSibling.children[leftSibling.numKeys];
    		if(leftSibling.children[0] != null)
    			leftSibling.children[leftSibling.numKeys].parent = n;
    		n.parent.keys[pos-1] = leftSibling.keys[leftSibling.numKeys-1];
    		leftSibling.numKeys--;
    		n.numKeys++;
    	}
    	else {
    		Node<Key, Value> rightSibling = n.parent.children[pos+1];
    		n.keys[n.numKeys] = n.parent.keys[pos];
    		n.children[n.numKeys+1] = rightSibling.children[0];
    		n.numKeys++;
    		if(rightSibling.children[0] != null)
    			rightSibling.children[0].parent = n;
    		n.parent.keys[pos] = rightSibling.keys[0];
    		int i;
    		for(i=0;i<rightSibling.numKeys-1;i++) {
    			rightSibling.keys[i] = rightSibling.keys[i+1];
    			rightSibling.children[i] = rightSibling.children[i+1];
    		}
  			rightSibling.children[i] = rightSibling.children[i+1];
    		rightSibling.numKeys--;
    	}
    	return n;
    }
    
    private Node<Key, Value> merge(Node<Key, Value> n, int pos, Boolean isLeft){
    	Node<Key, Value> mergedNode = new Node<Key, Value>(n.parent, 2*t);
    	if(n.parent.numKeys == 1) {
    		root = mergedNode;
    		mergedNode.parent = null;
    	}
    	if(isLeft) {
    		Node<Key, Value> leftSibling = n.parent.children[pos-1];
    		int i;
    		for(i=0;i<leftSibling.numKeys;i++) {
    			mergedNode.keys[i] = leftSibling.keys[i];
    			mergedNode.children[i] = leftSibling.children[i];
      		if(leftSibling.children[i] != null)
    			leftSibling.children[i].parent = mergedNode;
    		}
  			mergedNode.children[i] = leftSibling.children[i];
    		if(leftSibling.children[i] != null)
  			leftSibling.children[i].parent = mergedNode;
  			mergedNode.numKeys += leftSibling.numKeys;
  			mergedNode.keys[i] = n.parent.keys[pos-1];
  			mergedNode.numKeys++;
  			for(i=0;i<n.numKeys;i++) {
  				mergedNode.keys[i+mergedNode.numKeys] = n.keys[i];
    			mergedNode.children[i+mergedNode.numKeys] = n.children[i];
      		if(n.children[i] != null)
    			n.children[i].parent = mergedNode;
  			}
  			mergedNode.children[i+mergedNode.numKeys] = n.children[i];
    		if(n.children[i] != null)
  			n.children[i].parent = mergedNode;
  			mergedNode.numKeys += n.numKeys;
  			for(i=pos-1;i<n.parent.numKeys-1;i++) {
  				n.parent.keys[i] = n.parent.keys[i+1];
  				n.parent.children[i] = n.parent.children[i+1];
  			}
				n.parent.children[i] = n.parent.children[i+1];
  			n.parent.children[pos-1] = mergedNode;
  			n.parent.numKeys--;
    	}
    	else {
    			Node<Key, Value> rightSibling = n.parent.children[pos+1];
    			int i;
    			for(i=0; i<n.numKeys; i++) {
    				mergedNode.keys[i] = n.keys[i];
    				mergedNode.children[i] = n.children[i];
        		if(n.children[i] != null)
    				n.children[i].parent = mergedNode;
    			}
    			mergedNode.children[i] = n.children[i];
      		if(n.children[i] != null)
    			n.children[i].parent = mergedNode;
    			mergedNode.numKeys += n.numKeys;
    			mergedNode.keys[i] = n.parent.keys[pos];
    			mergedNode.numKeys++;
    			for(i=0; i<rightSibling.numKeys; i++) {
    				mergedNode.keys[i+mergedNode.numKeys] = rightSibling.keys[i];
    				mergedNode.children[i+mergedNode.numKeys] = rightSibling.children[i];
        		if(rightSibling.children[i] != null)
    				rightSibling.children[i].parent = mergedNode;
    			}
    			mergedNode.children[i+mergedNode.numKeys] = rightSibling.children[i];
      		if(rightSibling.children[i] != null)
  				rightSibling.children[i].parent = mergedNode;
  				mergedNode.numKeys += rightSibling.numKeys;

  				for(i=pos;i<n.parent.numKeys-1;i++) {
  					n.parent.keys[i] = n.parent.keys[i+1];
    				n.parent.children[i] = n.parent.children[i+1];
  				}
  				n.parent.children[i] = n.parent.children[i+1];
  				n.parent.children[pos] = mergedNode;
  				n.parent.numKeys--;

    	}

    	return mergedNode;
    }
    
    private Node<Key, Value> checkUnderflow(Node<Key, Value> temp, Integer i) {
    	if(i == null) return temp;
    	if(temp.numKeys == t-1) {
				if(i-1>=0 && temp.parent.children[i-1].numKeys > t-1) {
					redistribute(temp, i, true);
				}
				else if(i+1 <= temp.parent.numKeys && (temp.parent.children[i+1].numKeys > t-1)) {
					redistribute(temp, i, false);
				}
				else {
					if(i+1<=temp.parent.numKeys) {
						temp = merge(temp, i, false);
					}
					else if(i-1>=0) {
						temp = merge(temp, i, true);
					}
					
				}
			}
    	return temp;
    }
    
    private Pair<Key, Value> swapPre(Node<Key, Value> n, int pos) {
    	Node<Key, Value> temp = n.children[pos];
    	while(temp.children[0] != null) {
    		temp = temp.children[temp.numKeys];
    	}
    	return temp.keys[temp.numKeys-1];
    }
    
    private Pair<Key, Value> swapSuc(Node<Key, Value> n, int pos) {
    	Node<Key, Value> temp = n.children[pos+1];
    	while(temp.children[0] != null) {
    		temp = temp.children[0];
    	}
    	return temp.keys[0];
    }
}
