package col106.a3;

/**
 * 
 * @author Sid
 *
 * @param <E> Type of first element of pair
 * @param <F> Type of second element of pair
 */
public class Pair<E,F> {
	
	/**
	 * The first element of the pair
	 */
	private E first;
	
	/**
	 * The second element of the pair
	 */
	private F second;
	
	/**
	 * Default constructor for the pair. Initialises the pair to <null, null>
	 */
	public Pair() {
		this(null, null);
	}
	/**
	 * Initialises a pair <a, b>
	 * @param a The first element of the pair
	 * @param b The second element of the pair
	 */
	public Pair(E a, F b) {
		first = a;
		second = b;
	}
	
	/**
	 *
	 * @return The first element of the pair
	 */
	public E first() {
		return first;
	}
	
	/**
	 * 
	 * @param a The value of the first element to be set.
	 * @return The new value of the first element.
	 */
	public E first(E a) {
		first = a;
		return first;
	}
	
	/**
	 * 
	 * @return The second element of the pair
	 */
	public F second() {
		return second;
	}
	
	/**
	 * 
	 * @param The value of the second element to be set.
	 * @return The new value of the second element.
	 */
	public F second(F a) {
		second = a;
		return second;
	}
}
