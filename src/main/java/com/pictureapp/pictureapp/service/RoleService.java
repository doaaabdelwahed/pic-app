package com.pictureapp.pictureapp.service;

import java.util.Optional;

import org.springframework.stereotype.Service;

import com.pictureapp.pictureapp.models.ERole;
import com.pictureapp.pictureapp.models.Role;
import com.pictureapp.pictureapp.repository.RoleRepository;
@Service
public class RoleService {
	
	RoleRepository roleRepository;
	
	public Optional<Role> findByName(ERole name){
		return roleRepository.findByName(name);
	}

}
