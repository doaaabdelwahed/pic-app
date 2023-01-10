package com.pictureapp.pictureapp.controllers;



import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.pictureapp.pictureapp.models.ERole;
import com.pictureapp.pictureapp.models.Role;
import com.pictureapp.pictureapp.models.User;
import com.pictureapp.pictureapp.payload.request.LoginRequest;
import com.pictureapp.pictureapp.payload.request.SignupRequest;
import com.pictureapp.pictureapp.payload.response.JwtResponse;
import com.pictureapp.pictureapp.payload.response.MessageResponse;
import com.pictureapp.pictureapp.security.jwt.JwtUtils;
import com.pictureapp.pictureapp.security.services.UserDetailsImpl;
import com.pictureapp.pictureapp.service.RoleService;
import com.pictureapp.pictureapp.service.UserService;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/auth")
public class AuthController {
  @Autowired
  AuthenticationManager authenticationManager;

  @Autowired
  UserService userService;

  @Autowired
  RoleService roleService;

  @Autowired
  PasswordEncoder encoder;

  @Autowired
  JwtUtils jwtUtils;

  @PostMapping("/signin")
  public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {

    Authentication authentication = authenticationManager.authenticate(
        new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));

    SecurityContextHolder.getContext().setAuthentication(authentication);
    String jwt = jwtUtils.generateJwtToken(authentication);
    
    UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();    
    List<String> roles = userDetails.getAuthorities().stream()
        .map(item -> item.getAuthority())
        .collect(Collectors.toList());

    return ResponseEntity.ok(new JwtResponse(jwt, 
                         userDetails.getId(), 
                         userDetails.getUsername(), 
                
                         roles));
  }

  @PostMapping("/signup")
  public ResponseEntity<?> registerUser(@Valid @RequestBody SignupRequest signUpRequest) {
   
	  if (!signUpRequest.getPassword().equals(signUpRequest.getConfirmPassword())){
	    	return ResponseEntity
	    	          .badRequest()
	    	          .body(new MessageResponse("Error:password and confrim password not match!"));
	    	    
	    }
	  if (userService.existsByUsername(signUpRequest.getUsername())) {
      return ResponseEntity
          .badRequest()
          .body(new MessageResponse("Error: Username is already taken!"));
    }

   
    // Create new user's account
    User user = new User(signUpRequest.getUsername(),
               encoder.encode(signUpRequest.getPassword()));

   
    Set<Role> roles = new HashSet<>();

   
      Role userRole = roleService.findByName(ERole.ROLE_USER)
          .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
      roles.add(userRole);
      user.setRoles(roles);
      
    userService.save(user);

    return ResponseEntity.ok(new MessageResponse("User registered successfully!"));
  }
  
  @PostMapping("/adminlogin")
  public ResponseEntity<?>adminLogin(@Valid @RequestBody LoginRequest loginRequest) {
	  if(!loginRequest.getUsername().equals("admin@gmail.com")||!loginRequest.getPassword().equals("admin123")) {
		  return ResponseEntity
		          .badRequest()
		          .body(new MessageResponse("Error: admin login failed,wrong username or password!"));
		    
	  }
	  if (!userService.existsByUsername("admin@gmail.com")) {
		  User user = new User("admin@gmail.com",
	               encoder.encode("admin123"));
	    Set<Role> roles = new HashSet<>();
		  Role adminRole = roleService.findByName(ERole.ROLE_ADMIN)
		          .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
		      roles.add(adminRole);
		      user.setRoles(roles);
		      
		    userService.save(user); 
	  }
    Authentication authentication = authenticationManager.authenticate(
        new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));

    SecurityContextHolder.getContext().setAuthentication(authentication);
    String jwt = jwtUtils.generateJwtToken(authentication);
    
    UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();    
    List<String> roles = userDetails.getAuthorities().stream()
        .map(item -> item.getAuthority())
        .collect(Collectors.toList());

    return ResponseEntity.ok(new JwtResponse(jwt, 
    		userDetails.getId(), 
                         userDetails.getUsername(), 
                
                         roles));
  }
}
