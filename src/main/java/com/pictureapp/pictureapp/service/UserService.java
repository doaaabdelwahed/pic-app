package com.pictureapp.pictureapp.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.pictureapp.pictureapp.models.User;
import com.pictureapp.pictureapp.repository.UserRepository;
@Service
public class UserService {
	@Autowired
	UserRepository userRepository;
	 public Optional<User> findByUsername(String username){
		 return userRepository.findByUsername(username);
	 }

	 public  Boolean existsByUsername(String username) {
		  return userRepository.existsByUsername(username);
	  };
	  public User save(User user) {
	        return userRepository.save(user);
	    }
}
