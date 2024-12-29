package com.example.fitfolio.entities;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;

@Entity
public class Utilisateur {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String username;
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String password;
    @ManyToMany(fetch = FetchType.EAGER)
    private Collection<Role> roles = new ArrayList<>();
    private String prenom;

    private String email;

	private String date;

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public String getNom() {
		return nom;
	}

	public void setNom(String nom) {
		this.nom = nom;
	}

	private String nom;

    private String telephone;

    private int age;

	private Double poids;

	private Double taille;

	private String sexe;

	private String objectif;


	public String getPrenom() {
		return prenom;
	}
	public void setPrenom(String prenom) {
		this.prenom = prenom;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getTelephone() {
		return telephone;
	}
	public void setTelephone(String telephone) {
		this.telephone = telephone;
	}
	public int getAge() {
		return age;
	}
	public void setAge(int age) {
		this.age = age;
	}
	public Double getPoids() {
		return poids;
	}
	public void setPoids(Double poids) {
		this.poids = poids;
	}
	public Double getTaille() {
		return taille;
	}
	public void setTaille(Double taille) {
		this.taille = taille;
	}
	public String getSexe() {
		return sexe;
	}
	public void setSexe(String sexe) {
		this.sexe = sexe;
	}
	public String getObjectif() {
		return objectif;
	}
	public void setObjectif(String objectif) {
		this.objectif = objectif;
	}
	public Collection<Planning> getPlannings() {
		return plannings;
	}
	public void setPlannings(Collection<Planning> plannings) {
		this.plannings = plannings;
	}


@ManyToMany(fetch = FetchType.EAGER)
private Collection<Planning> plannings = new ArrayList<>();

	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
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
	public Collection<Role> getRoles() {
		return roles;
	}
	public void setRoles(Collection<Role> roles) {
		this.roles = roles;
	}


	public Utilisateur(String date, Long id, String username, String password, Collection<Role> roles, String prenom, String email, String nom, String telephone, int age, Double poids, Double taille, String sexe, String objectif, Collection<Planning> plannings) {
		this.date = date;
		this.id = id;
		this.username = username;
		this.password = password;
		this.roles = roles;
		this.prenom = prenom;
		this.email = email;
		this.nom = nom;
		this.telephone = telephone;
		this.age = age;
		this.poids = poids;
		this.taille = taille;
		this.sexe = sexe;
		this.objectif = objectif;
		this.plannings = plannings;
	}

	public Utilisateur(Long id, String username, String password, Collection<Role> roles, int age) {
		super();
		this.id = id;
		this.username = username;
		this.password = password;
		this.roles = roles;
		this.age = age;
	}
	public Utilisateur() {
		
	}
	@Override
	public String toString() {
		return "Utilisateur{" +
				"id=" + id +
				", username='" + username + '\'' +
				", prenom='" + prenom + '\'' +
				", email='" + email + '\'' +
				", date='" + date + '\'' +
				", nom='" + nom + '\'' +
				", telephone='" + telephone + '\'' +
				", age=" + age +
				", poids=" + poids +
				", taille=" + taille +
				", sexe='" + sexe + '\'' +
				", objectif='" + objectif + '\'' +
				", roles=" + roles +
				", plannings=" + plannings +
				'}';
	}



	@Override
	public int hashCode() {
		return Objects.hash(id, username, password, roles, prenom, email, date, nom, telephone, age, poids, taille, sexe, objectif, plannings);
	}
	
}

