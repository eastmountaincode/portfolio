package wordHashmap;

import java.util.List;

/**
 * The interface Word hashmap.
 */
public interface IWordHashmap {
    /**
     * Insert a new key into the hashmap. When we do this, the value should be set to
     * an initial value of 1. Later, if we try to insert this key again and find
     * that it already exists, we'll use the increase function instead.
     *
     * @param key       the key
     */
    void insert(String key);

    /**
     * Delete a key from the hashmap.
     *
     * @param key the key
     */
    void delete(String key);

    /**
     * Find if a key is in the hashmap. This is especially useful as a helper
     * function for the delete operation.
     *
     * @param key the key
     */
    WordOccurrence find(String key);

    /**
     * Increase the word count (the value) of a key by 1.
     *
     * @param key the key whose word count we want to increase
     */
    void increase(String key);

    /**
     * Return a list of all the unique keys.
     *
     * @return the list of all unique keys
     */
    List<String> listAllKeys();


    /**
     * Hash function. Takes in the string to hash and the number of buckets,
     * the number of possible outcomes of the hash.
     *
     * @param stringToHash the string to hash
     * @param M            the number of buckets
     * @return the int
     */
    int hashFunction(String stringToHash, int M);

}
