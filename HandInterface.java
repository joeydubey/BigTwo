/**
 * An abstract interface which contains the declarations of the abstract functions relating to Hand objects.
 * 
 * @author Aryaman Dubey
 *
 */
public interface HandInterface
{
	/**
	 * An abstract function that checks whether a set of cards is valid.
	 * @return
	 * 			Returns a boolean value indicating validity. 
	 * 			True if hand is valid, else false.
	 */
	public abstract boolean isValid();
	
	/**
	 * An abstract function that returns the type of a set of cards.
	 * @return
	 * 			Returns a string containing the type of hand.
	 */
	public abstract String getType();
}
