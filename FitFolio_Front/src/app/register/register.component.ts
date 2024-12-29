import { Component } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { CommonModule } from '@angular/common';
import { Router } from '@angular/router';
import { HttpClient, HttpClientModule } from '@angular/common/http';
import { UserDataService } from '../services/user-data.service';

@Component({
  selector: 'app-register',
  standalone: true,
  imports: [CommonModule, FormsModule, HttpClientModule],
  templateUrl: './register.component.html',
  styleUrls: ['./register.component.scss'],
})
export class RegisterComponent {
  firstName: string = '';
  lastName: string = '';
  phoneNumber: string = '';
  email: string = '';
  password: string = '';
  age: number | null = null;
  gender: string = '';
  height: number | null = null;
  weight: number | null = null;
  isPasswordVisible: boolean = false;

  successMessage: string = '';
  errorMessage: string = '';

  private apiUrl = 'http://localhost:8080/fitfolio/addUser';

  constructor(
    private router: Router,
    private http: HttpClient,
    private userDataService: UserDataService
  ) {}

  togglePasswordVisibility(): void {
    this.isPasswordVisible = !this.isPasswordVisible;
  }

  Register(): void {
    if (
      !this.firstName ||
      !this.lastName ||
      !this.phoneNumber ||
      !this.email ||
      !this.password ||
      this.age === null ||
      !this.gender ||
      this.height === null ||
      this.weight === null
    ) {
      this.errorMessage = 'All fields are required.';
      this.successMessage = ''; // Clear any previous success messages
      return;
    }
  
    const userData = {
      username: this.email,
      password: this.password,
      prenom: this.firstName,
      nom: this.lastName,
      email: this.email,
      telephone: this.phoneNumber,
      age: this.age,
      poids: this.weight,
      taille: this.height,
      sexe: this.gender,
      objectif: 'Lean & Tone',
      date: new Date().toISOString().split('T')[0], // Ajouter la date actuelle (format YYYY-MM-DD)
    };
  
    this.http.post(this.apiUrl, userData).subscribe(
      (response) => {
        console.log('Data Submission Successful!', response); // Log the response to check
        this.successMessage = 'Data Submission Successful!';
        this.errorMessage = ''; // Clear any previous error messages
  
        // Wait for 2 seconds (2000ms) before navigating to the goal page
        setTimeout(() => {
          this.userDataService.setUserData(userData);
          this.router.navigate(['/goal']);
        }, 500); // Adjust the time (in ms) as needed
      },
      (error) => {
        console.error('Data Submission failed:', error);
        this.errorMessage = 'Data Submission failed. Please try again.';
        this.successMessage = ''; // Clear any previous success messages
      }
    );
  }
  

  navigateToSignIn(): void {
    this.router.navigate(['/sign-in']);
  }
}
