import java.io.*;
import java.util.Scanner;
public class FabricBreakup {

	private static class Shirt {
		
		private int count;
		private int score;
		
		public void setScore(int s) {
			score = s;
		}
		
		public int getScore() {
			return score;
		}
		
		public int getCount() {
			return count;
		}
		
		public void setCount(int i) {
			count = i;
		}
	}

	public static void main(String args[]) {
		int n, i, index, score, id, maxScore = 0;
		Stack<Shirt> stack = new Stack<Shirt>();
		try {
			FileInputStream fileInput = new FileInputStream(args[0]);
			Scanner input = new Scanner(fileInput);
			n = input.nextInt();
			for(i=0; i<n; i++) {
				Shirt shirt = new Shirt();
				index = input.nextInt();
				id = input.nextInt();
				if(id == 1) {
					score = input.nextInt();
					if(score > maxScore) {
						maxScore = score;
						shirt.setScore(score);
						shirt.setCount(0);
						stack.push(shirt);
					}
					else {
						shirt = stack.pop();
						shirt.setCount(shirt.getCount()+1);
						stack.push(shirt);
					}
				}
				else if(id == 2){
					if(!stack.isEmpty()) {
						shirt = stack.pop();
						if(!stack.isEmpty()) maxScore = stack.top().getScore();
						else maxScore = 0;
						System.out.println(index + " " + shirt.getCount());
					}
					else {
						System.out.println(index + " " + -1);
					}
				}
			}
			input.close();
		}catch(FileNotFoundException e) {
			System.out.println (" File not found ");
		}
	}
}
