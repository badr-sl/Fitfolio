package com.example.fitfolio.dto;

import com.example.fitfolio.entities.Utilisateur;

public class UserDto {
private String message ;
private Utilisateur appUser;
public String getMessage() {
	return message;
}
public void setMessage(String message) {
	this.message = message;
}
public Utilisateur getAppUser() {
	return appUser;
}
public void setAppUser(Utilisateur appUser) {
	this.appUser = appUser;
}

}
