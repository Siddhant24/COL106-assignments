import java.util.*;
import java.io.*;

public class Anagram {
	static LinkedList<String>[] hashTable = (LinkedList<String>[])new LinkedList[49157];

	private static Integer hashCode(String s) {		
		int size = s.length();
		Integer hash = 0;
		for(int i=0; i<size; i++) {
			hash = (s.charAt(i) + 31*hash)%49157;
		}
		return hash;
	}
	
	private static Integer hashCode(char[] s, int l, int r) {
		Integer hash = 0;
		for(int i=l; i<r; i++) {
			hash = (s[i] + 31*hash)%49157;
		}
		return hash;
	}
	
	private static Boolean findInTable(char[] str, int l, int r) {
		int hash = hashCode(str, l, r);
		String s = new String(Arrays.copyOfRange(str, l, r));
		if(hashTable[hash] != null) {		
			if(hashTable[hash].contains(s)) {
				return true;
			}
			else return false;
		}
		else return false;
	}
	
	private static void findAnagramsWithDoubleSpace(char[] str) {
		// inserting double space
		int size = str.length;
		if(size < 9) return;
		int i,j;
		for(i=3;i<size;i++) {
			if(findInTable(str, 0, i)) {
				for(j=i+3;j<size && j<10;j++) {
					if(findInTable(str, i, j) && findInTable(str, j, size)) {
						int k;
						for(k=0;k<size;k++) {
							if(k == i || k == j) {
								System.out.print(' ');
							}
							System.out.print(str[k]);
						}
						System.out.println();
					}
				}
			}
		}
	}
	
	private static void findAnagramsWithSingleSpace(char[] str) {
		int size = str.length;
		if(size < 6) return;
		int i;
		for(i=3; i<size && i<10; i++) {
			if(findInTable(str, 0, i) && findInTable(str, i, size)) {
				int k;
				for(k=0;k<size;k++) {
					if(k == i) {
						System.out.print(' ');
					}
					System.out.print(str[k]);
				}
				System.out.println();
			}
		}
	}
	

	
	public static void main(String args[]) {
		try {
			FileInputStream vocab = new FileInputStream(args[0]);
			Scanner v = new Scanner(vocab);
			FileInputStream input = new FileInputStream(args[1]);
			Scanner in = new Scanner(input);
			int vocabSize = v.nextInt();
			int K = in.nextInt();
			while(v.hasNext()) {
				String s = v.next();
				int hash = hashCode(s);
				if(hashTable[hash] == null) {
					hashTable[hash] = new LinkedList<String>();
				}
				hashTable[hash].addLast(s);
			}
			
			while(in.hasNext()) {
					String s = in.next();
					int strlen = s.length();
					if(strlen > 12 && strlen < 3) continue;
					char str[] = s.toCharArray();
			    Arrays.sort(str);
			    Boolean completed = false;
			    while(!completed) {
				   	findAnagramsWithDoubleSpace(str);
				   	findAnagramsWithSingleSpace(str);
				   	if(findInTable(str, 0, str.length)) {
							System.out.println(str);
						}
			    	int i;
			    	for (i = strlen - 2; i >= 0; i--) {
	            if (str[i] < str[i+1]) break;
			    	}
			    	if (i == -1) completed = true;
			    	else {
			    		char a = str[i];
			    		int j;
			    		int k = i+1;
			    		for(j=i+2; j<strlen; j++) {
			    			if(str[j] > a && str[j] <= str[k]) {
			    				k = j;
			    			}
			    		}
			    		char temp = str[k];
			    		str[k] = a;
			    		str[i] = temp;
			    		//Arrays.sort(str, i+1, strlen);
			    		for(j=i+1; j<(strlen+i+1)/2; j++) {
			    			temp = str[j];
			    			str[j] = str[strlen-j+i];
			    			str[strlen-j+i] = temp;
			    		}
			    	}
			    }
				System.out.println(-1);
			}
			v.close();
			in.close();
		}catch(FileNotFoundException e) {
			System.out.println("File not found");
		}
	}
}
