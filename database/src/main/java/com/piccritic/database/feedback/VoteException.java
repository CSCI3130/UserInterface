/**
 * VoteException.java
 * Created Mar 13, 2017
 */
package com.piccritic.database.feedback;

/**
 * Error thrown when a vote cannot be validated.
 * @author Jonathan Ignacio <br> Frank Bosse
 *
 */
@SuppressWarnings("serial")
public class VoteException extends Exception {
	
	public VoteException(String message) {
		super(message);
	}
}
