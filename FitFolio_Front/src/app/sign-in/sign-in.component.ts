import { Component } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { Router } from '@angular/router';
import { HttpClient, HttpClientModule, HttpErrorResponse } from '@angular/common/http';
import { catchError } from 'rxjs/operators';
import { throwError } from 'rxjs';
import { UserDataService } from '../services/user-data.service';
import { CommonModule } from '@angular/common'; // Import CommonModule

@Component({
  selector: 'app-sign-in',
  standalone: true,
  imports: [FormsModule, HttpClientModule, CommonModule], // Include CommonModule here
  templateUrl: './sign-in.component.html',
  styleUrls: ['./sign-in.component.scss'],
})
export class SignInComponent {
  email: string = ''; // User's email
  password: string = ''; // User's password
  isPasswordVisible: boolean = false;
  successMessage: string | null = null; // Success message
  errorMessage: string | null = null;
  private apiUrl = 'http://localhost:8080/fitfolio/login'; // Backend API URL

  constructor(
    private router: Router,
    private http: HttpClient,
    private userDataService: UserDataService
  ) {}

  // Toggle visibility of the password field
  togglePasswordVisibility(): void {
    this.isPasswordVisible = !this.isPasswordVisible;
  }

  // Handle the login process
  SignIn(): void {
    if (!this.email.trim() || !this.password.trim()) {
      this.errorMessage = 'All fields are required. Please fill out all the fields.';
      this.successMessage = null;
      return;
    }
  
    const params = new URLSearchParams();
    params.set('email', this.email);
    params.set('password', this.password);
  
    this.http
      .post(`${this.apiUrl}?${params.toString()}`, null)
      .pipe(
        catchError((error: HttpErrorResponse) => {
          console.error('Login failed:', error);
  
          switch (error.status) {
            case 400:
            case 401: // Unauthorized error
              this.errorMessage = 'Invalid email or password. Please try again.';
              break;
            case 0: // Network or server unreachable
              this.errorMessage = 'Unable to connect to the server. Please check your network.';
              break;
            default:
              this.errorMessage = 'An unexpected error occurred. Please try again.';
          }
  
          this.successMessage = null;
          return throwError(() => new Error(this.errorMessage ?? 'An unknown error occurred.'));
        })
      )
      .subscribe(
        (response: any) => {
          console.log('Login successful:', response);
  
          const userData = {
            id: response.appUser.id,
            firstName: response.appUser.prenom,
            lastName: response.appUser.nom,
            phoneNumber: response.appUser.telephone,
            email: response.appUser.email,
            age: response.appUser.age,
            gender: response.appUser.sexe,
            height: response.appUser.taille,
            weight: response.appUser.poids,
            objectif: response.appUser.objectif,
            date: response.appUser.date,
          };
  
          this.userDataService.setUserData(userData);
          this.successMessage = 'Login successful!';
          this.errorMessage = null;
  
          // Wait for 2 seconds (2000ms) before navigating to the dashboard
          setTimeout(() => {
            this.router.navigate(['/dashboard']);
          }, 500); // Adjust the delay time as needed
        }
      );
  }
  

  // Navigate to the registration page
  navigateToRegister(): void {
    this.router.navigate(['/register']);
  }
}
