package com.pictureapp.pictureapp.payload.request;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
 
public class SignupRequest {
       @NotBlank
	    @Size(max = 50)
	    @Email
    private String username;
 
   
    
    @NotBlank
    @Size(min = 6, max = 40)
    private String password;
  
    
    @NotBlank
    @Size(min = 6, max = 40)
    private String confirmPassword;
  
    public String getUsername() {
        return username;
    }
 
    public void setUsername(String username) {
        this.username = username;
    }
 

 
    public String getPassword() {
        return password;
    }
 
    public void setPassword(String password) {
        this.password = password;
    }

	public String getConfirmPassword() {
		return confirmPassword;
	}

	public void setConfirmPassword(String confirmPassword) {
		this.confirmPassword = confirmPassword;
	}
    

}
