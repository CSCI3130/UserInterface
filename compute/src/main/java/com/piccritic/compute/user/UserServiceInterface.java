package com.piccritic.compute.user;

import com.piccritic.database.user.Critic;
import com.piccritic.database.user.UserException;

public interface UserServiceInterface {

	String create(Critic critic, String password) throws UserException;

	String update(Critic critic, String password) throws UserException;

	Critic select(String handle);

}