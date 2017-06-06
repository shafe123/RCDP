package DFA;
/**
 * 
 * @author Ajinkya
 * This class defines the static utility methods that other classes of DFA package will use
 * to perform some basic tasks.
 */
public class Utility {
	/**
	 * 
	 * @param field
	 * @return {@link Boolean}
	 * Returns true if field String is null or empty. Otherwise, returns false
	 */
	static boolean isEmpty(String field){
		return (field == null || field.length() == 0);
	}
}
