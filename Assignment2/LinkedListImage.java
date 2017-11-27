import java.io.*;
import java.util.*;

public class LinkedListImage implements CompressedImageInterface {
	
	private LinkedList<Integer>[] compressedGrid;
	public int w,h;
	private int[] numBlack;

	public LinkedListImage(String filename)
	{
		try {
			FileInputStream fileinput = new FileInputStream(filename);
			Scanner input = new Scanner(fileinput);
			w = input.nextInt();
			h =input.nextInt();
			compressedGrid = new LinkedList[w]; 
			numBlack = new int[w];
			int i,j,count=0,temp;
			
			for(i=0; i<w; i++) {
				count=0;
				compressedGrid[i] = new LinkedList<Integer>();
				for(j=0; j<h; j++) {
					temp = input.nextInt();					
					if(temp == 1 && count != 0) {
						compressedGrid[i].addLast(j-count);
						compressedGrid[i].addLast(j-1);
						count = 0;
					}
					else if(temp == 0) {
						count++;
						numBlack[i]++;
					}
				}
				if(count != 0){
					compressedGrid[i].addLast(j-count);
					compressedGrid[i].addLast(j-1);
					count = 0;
				}
				compressedGrid[i].addLast(-1);
			}
			
			input.close();
			
		}catch(FileNotFoundException e) {
			System.out.println("File Not Found");
		}
	}

    public LinkedListImage(boolean[][] grid, int width, int height)
    {
    	w=width;
    	h=height;
		int i,j,count=0;
		compressedGrid = new LinkedList[w]; 
		numBlack = new int[w];		
			for(i=0; i<w; i++) {
				compressedGrid[i] = new LinkedList<Integer>();				
				for(j=0; j<h; j++) {
					if(grid[i][j] && count != 0) {
						compressedGrid[i].addLast(j-count);
						compressedGrid[i].addLast(j-1);
						count = 0;
					}
					else if(!grid[i][j]){
						count++;
						numBlack[i]++;
					}
				}
				if(count != 0){
					compressedGrid[i].addLast(j-count);
					compressedGrid[i].addLast(j-1);
					count = 0;
				}
				compressedGrid[i].addLast(-1);
			}
    }

    public boolean getPixelValue(int x, int y) throws PixelOutOfBoundException
    {
    	if(x>=w || y>=h) throw new PixelOutOfBoundException("pixel out of bounds");
    	else {
    		int i;
    		ListNode<Integer> it = compressedGrid[x].getHead();
    		for(i=0; it.getElement( ) != -1; i++) {
    			Integer temp = it.getElement();
    			if(i%2==0) {
    				if(temp>y) return true;
    			}
    			else {
    				if(temp>=y) return false;
				}
				it = it.getNext();
    		}
    	}
    	return true;
    }

    public void setPixelValue(int x, int y, boolean val) throws PixelOutOfBoundException
    {
		if(x>=w || y>=h) throw new PixelOutOfBoundException("pixel out of bounds");
		else {
			int i,j;
			j = val ? 1 : 0; // j = 1 if val = true
			ListNode<Integer> cur = compressedGrid[x].getHead();
			ListNode<Integer> prev=null;
    		for(i=0; cur != null; i++) {
    			if(i%2==j) { 
					if(j==0){ // value to be set is black
						if(cur.getElement()>y && cur.getElement() != -1){
							if(prev != null && cur.getElement() == y+1 && prev.getElement() == y-1){
								compressedGrid[x].remove(cur);
								compressedGrid[x].remove(prev);					
							}
							else if(prev != null && cur.getElement() == y+1){
								compressedGrid[x].addAfter(prev, y);
								compressedGrid[x].remove(cur);
							}
							else if(prev!= null && prev.getElement() == y-1){
								compressedGrid[x].addAfter(prev, y);	
								compressedGrid[x].remove(prev);
							}
							else if(cur.getElement() == y+1){
								compressedGrid[x].addAfter(prev, y);
								compressedGrid[x].remove(cur);
							}
							else{
								compressedGrid[x].addAfter(prev, y);
								compressedGrid[x].addAfter(prev, y);	
							}
							numBlack[x]++;							
							return;
						}
						else if(cur.getElement() == -1){
							// compressedGrid[x].print();
							
							if(prev!= null && prev.getElement() == y-1){
								compressedGrid[x].addAfter(prev, y);							
								compressedGrid[x].remove(prev);
							}
							else{
								compressedGrid[x].addAfter(prev, y);
								compressedGrid[x].addAfter(prev, y);	
							}
							numBlack[x]++;
							return;
						}
					}
					else{ // value to be set is white
						if(cur.getElement()>=y){
							if(prev.getElement() == y && cur.getElement() == y){
								compressedGrid[x].remove(cur);
								compressedGrid[x].remove(prev);
							}
							else if(prev.getElement() == y){
								compressedGrid[x].addAfter(prev, y+1);
								compressedGrid[x].remove(prev);
							}
							else if(cur.getElement() == y){
								compressedGrid[x].addAfter(prev, y-1);
								compressedGrid[x].remove(cur);
							}
							else{
								compressedGrid[x].addAfter(prev, y+1);
								compressedGrid[x].addAfter(prev, y-1);	
							}
							numBlack[x]--;
							return;
						}
					}
				}
				else{
					if(j == 0){
						if(cur.getElement() >= y ) return;
					}
					else{
						if(cur.getElement() == -1) return;
						else if(cur.getElement() > y) return;
					}
				}
				prev = cur;
				cur = cur.getNext();
    		}
    	}	
    }

    public int[] numberOfBlackPixels()
    {
    	return numBlack;
    }
    
    public void invert()
    {
		int i,j;
		for(i=0; i<w; i++){
			LinkedList<Integer> inverted = new LinkedList<Integer>();
			ListNode<Integer> temp = compressedGrid[i].getHead();
			for(j=0; temp.getElement() != -1; j++){
				if(j == 0 && temp.getElement()!=0){
					inverted.addLast(0);
				}
				if(j%2 == 0){
					if(temp.getElement()-1 >= 0)
						inverted.addLast(temp.getElement()-1);
				}
				else{
					if(temp.getElement()+1 < h)
						inverted.addLast(temp.getElement()+1);
				}
				temp = temp.getNext();
			}
			if(inverted.size()%2 != 0){
				inverted.addLast(h-1);
			}
			inverted.addLast(-1);
			compressedGrid[i] = inverted;
			numBlack[i] = w - numBlack[i];
		}
    }
    
    public void performAnd(CompressedImageInterface img) throws BoundsMismatchException
    {
		int i,j,count;
		LinkedListImage img1 = (LinkedListImage) img;
		Boolean newVal;
		if(img1.w != w) throw new BoundsMismatchException("Size of images is not same");
		for(i=0; i<w; i++){
			count = 0;
			for(j=0; j<h; j++){
				try{
					newVal = getPixelValue(i, j) && img.getPixelValue(i, j);
					setPixelValue(i, j, newVal);
					if(!newVal) count++;
				}
				catch(PixelOutOfBoundException e){
					System.out.println("Errorrrrrrrr");					
				}
			}
			numBlack[i] = count;
		}
    }
    
    public void performOr(CompressedImageInterface img) throws BoundsMismatchException
    {
		int i,j,count;
		LinkedListImage img1 = (LinkedListImage) img;	
		Boolean newVal;	
		if(img1.w != w) throw new BoundsMismatchException("Size of images is not same");		
		for(i=0; i<w; i++){
			count = 0;
			for(j=0; j<h; j++){
				try{
					newVal = getPixelValue(i, j) || img.getPixelValue(i, j);
					setPixelValue(i, j, newVal);
					if(!newVal) count++;
				}
				catch(PixelOutOfBoundException e){
					System.out.println("Errorrrrrrrr");					
				}
			}
			numBlack[i] = count;
		}
    }
    
    public void performXor(CompressedImageInterface img) throws BoundsMismatchException
    {
		int i,j,count;
		LinkedListImage img1 = (LinkedListImage) img;	
		Boolean newVal;	
		if(img1.w != w) throw new BoundsMismatchException("Size of images is not same");		
		for(i=0; i<w; i++){
			count = 0;
			for(j=0; j<h; j++){
				try{
					newVal = getPixelValue(i, j) ^ img.getPixelValue(i, j);
					setPixelValue(i, j, newVal);
					if(!newVal) count++;
				}
				catch(PixelOutOfBoundException e){
					System.out.println("Errorrrrrrrr");					
				}
			}
			numBlack[i] = count;
		}
    }
    
    public String toStringUnCompressed()
    {
		int i,j;
		StringBuilder str = new StringBuilder();
		str.append(w + " " + h + ", ");		
		for(i=0; i<w; i++){
			for(j=0; j<h; j++){
				try{
					str.append(getPixelValue(i, j) ? 1 : 0);
				}catch(PixelOutOfBoundException e){
					System.out.println("Errorrrrrrrr");					
				}
				if(j != h-1) str.append(' ');
			}
			if(i != w-1)
				str.append(", ");
		}
		return str.toString();
    }
    
    public String toStringCompressed()
    {
    	int i;
		StringBuilder str = new StringBuilder();
		str.append(w + " " + h + ", ");
    	for(i=0; i<w; i++) {
    		ListNode<Integer> it = compressedGrid[i].getHead();
    		while(it != null) {
    			str.append(it.getElement());
				if(it.getNext() != null) str.append(' ');
				it = it.getNext();
			}
			if(i != w-1){
				str.append(", ");
			}
    	}
    	return str.toString();
    }

    public static void main(String[] args) {
    	// testing all methods here :
    	boolean success = true;

    	// check constructor from file
    	CompressedImageInterface img1 = new LinkedListImage("sampleInputFile.txt");

    	// check toStringCompressed
    	String img1_compressed = img1.toStringCompressed();
    	String img_ans = "16 16, -1, 5 7 -1, 3 7 -1, 2 7 -1, 2 2 6 7 -1, 6 7 -1, 6 7 -1, 4 6 -1, 2 4 -1, 2 3 14 15 -1, 2 2 13 15 -1, 11 13 -1, 11 12 -1, 10 11 -1, 9 10 -1, 7 9 -1";
		
		
    	success = success && (img_ans.equals(img1_compressed));

    	if (!success)
    	{
    		System.out.println("Constructor (file) or toStringCompressed ERROR");
    		return;
    	}

    	// check getPixelValue
    	boolean[][] grid = new boolean[16][16];
    	for (int i = 0; i < 16; i++)
    		for (int j = 0; j < 16; j++)
    		{
                try
                {
        			grid[i][j] = img1.getPixelValue(i, j);                
                }
                catch (PixelOutOfBoundException e)
                {
                    System.out.println("Errorrrrrrrr");
                }
    		}

    	// check constructor from grid
    	CompressedImageInterface img2 = new LinkedListImage(grid, 16, 16);
		String img2_compressed = img2.toStringCompressed();
		// System.out.println(img_ans);
    	success = success && (img2_compressed.equals(img_ans));

    	if (!success)
    	{
    		System.out.println("Constructor (array) or toStringCompressed ERROR");
    		return;
    	}

    	// check Xor
        try
        {
        	img1.performXor(img2);       
        }
        catch (BoundsMismatchException e)
        {
            System.out.println("Errorrrrrrrr");
        }
    	for (int i = 0; i < 16; i++)
    		for (int j = 0; j < 16; j++)
    		{
                try
                {
        			success = success && (!img1.getPixelValue(i,j));                
                }
                catch (PixelOutOfBoundException e)
                {
                    System.out.println("Errorrrrrrrr");
                }
    		}

    	if (!success)
    	{
    		System.out.println("performXor or getPixelValue ERROR");
    		return;
		}
	
    	// check setPixelValue
    	for (int i = 0; i < 16; i++)
        {
            try
            {
    	    	img1.setPixelValue(i, 0, true);            
            }
            catch (PixelOutOfBoundException e)
            {
                System.out.println("Errorrrrrrrr");
            }
        }
    	// check numberOfBlackPixels
    	int[] img1_black = img1.numberOfBlackPixels();
		success = success && (img1_black.length == 16);		
    	for (int i = 0; i < 16 && success; i++)
			success = success && (img1_black[i] == 15);
    	if (!success)
    	{
    		System.out.println("setPixelValue or numberOfBlackPixels ERROR");
    		return;
    	}

    	// check invert
        img1.invert();
        for (int i = 0; i < 16; i++)
        {
            try
            {
                success = success && !(img1.getPixelValue(i, 0));            
            }
            catch (PixelOutOfBoundException e)
            {
                System.out.println("Errorrrrrrrr");
            }
        }
        if (!success)
        {
            System.out.println("invert or getPixelValue ERROR");
            return;
        }

    	// check Or
        try
        {
            img1.performOr(img2);        
        }
        catch (BoundsMismatchException e)
        {
            System.out.println("Errorrrrrrrr");
        }
        for (int i = 0; i < 16; i++)
            for (int j = 0; j < 16; j++)
            {
                try
                {
                    success = success && img1.getPixelValue(i,j);
                }
                catch (PixelOutOfBoundException e)
                {
                    System.out.println("Errorrrrrrrr");
                }
            }
        if (!success)
        {
            System.out.println("performOr or getPixelValue ERROR");
            return;
        }

        // check And
        try
        {
            img1.performAnd(img2);    
        }
        catch (BoundsMismatchException e)
        {
            System.out.println("Errorrrrrrrr");
        }
        for (int i = 0; i < 16; i++)
            for (int j = 0; j < 16; j++)
            {
                try
                {
                    success = success && (img1.getPixelValue(i,j) == img2.getPixelValue(i,j));             
                }
                catch (PixelOutOfBoundException e)
                {
                    System.out.println("Errorrrrrrrr");
                }
            }
        if (!success)
        {
            System.out.println("performAnd or getPixelValue ERROR");
            return;
        }

    	// check toStringUnCompressed
        String img_ans_uncomp = "16 16, 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1, 1 1 1 1 1 0 0 0 1 1 1 1 1 1 1 1, 1 1 1 0 0 0 0 0 1 1 1 1 1 1 1 1, 1 1 0 0 0 0 0 0 1 1 1 1 1 1 1 1, 1 1 0 1 1 1 0 0 1 1 1 1 1 1 1 1, 1 1 1 1 1 1 0 0 1 1 1 1 1 1 1 1, 1 1 1 1 1 1 0 0 1 1 1 1 1 1 1 1, 1 1 1 1 0 0 0 1 1 1 1 1 1 1 1 1, 1 1 0 0 0 1 1 1 1 1 1 1 1 1 1 1, 1 1 0 0 1 1 1 1 1 1 1 1 1 1 0 0, 1 1 0 1 1 1 1 1 1 1 1 1 1 0 0 0, 1 1 1 1 1 1 1 1 1 1 1 0 0 0 1 1, 1 1 1 1 1 1 1 1 1 1 1 0 0 1 1 1, 1 1 1 1 1 1 1 1 1 1 0 0 1 1 1 1, 1 1 1 1 1 1 1 1 1 0 0 1 1 1 1 1, 1 1 1 1 1 1 1 0 0 0 1 1 1 1 1 1";
		success = success && (img1.toStringUnCompressed().equals(img_ans_uncomp)) && (img2.toStringUnCompressed().equals(img_ans_uncomp));

        if (!success)
        {
            System.out.println("toStringUnCompressed ERROR");
            return;
        }
        else
            System.out.println("ALL TESTS SUCCESSFUL! YAYY!");
    }
}