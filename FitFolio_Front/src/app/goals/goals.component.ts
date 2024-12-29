import { Component } from '@angular/core';
import { CommonModule } from '@angular/common'; // Import CommonModule
import { Router } from '@angular/router';
import { HttpClient } from '@angular/common/http';
import { UserDataService } from '../services/user-data.service'; // Import UserDataService
@Component({
  selector: 'app-choose-goal',
  standalone: true,
  imports: [CommonModule], // Add CommonModule here
  templateUrl: './goals.component.html',
  styleUrls: ['./goals.component.scss'],
})
export class ChooseGoalComponent {
  selectedGoal: string | null = null; // Selected goal
  successMessage: string | null = null; // Success message
  errorMessage: string | null = null;

  private apiUrl = 'http://localhost:8080/fitfolio/addUser'; // Backend endpoint

  constructor(
    private router: Router,
    private http: HttpClient,
    private userDataService: UserDataService // Inject UserDataService
  ) {}

  // Handle goal selection
  selectGoal(goal: string): void {
    this.selectedGoal = goal; // Set selected goal
    this.errorMessage = null; // Clear any previous error messages
    console.log('Selected goal:', goal);
  }

  // Submit user details to the backend and navigate to the Dashboard
  submitUserDetails(): void {
    if (!this.selectedGoal) {
      this.errorMessage = 'Please select a goal before proceeding.';
      console.log('Error Message:', this.errorMessage); // Debug log
      this.successMessage = null;
      return;
    }
  
    const userData = this.userDataService.getUserData();
  
    if (!userData) {
      this.errorMessage = 'User data is missing. Please restart the registration process.';
      this.successMessage = null;
      console.log('Error Message:', this.errorMessage); // Debug log
      this.router.navigate(['/register']);
      return;
    }
  
    // Combine user data with the selected goal
    const requestBody = {
      ...userData,
      objectif: this.selectedGoal,
    };
  
    console.log('Submitting request body:', requestBody);
  
    this.http.post(this.apiUrl, requestBody).subscribe(
      (response: any) => {
        // Use the backend response to update the user data
        const updatedUserData = {
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
          date: response.appUser.date, // Include registration date if returned
        };
  
        // Store the updated user data
        this.userDataService.setUserData(updatedUserData);
        this.successMessage = 'User registered successfully!';
        this.errorMessage = null;
  
        // Wait for 2 seconds (2000ms) before navigating to the dashboard
        setTimeout(() => {
          this.router.navigate(['/dashboard']);
        }, 0.02); // Adjust the delay time as needed
      },
      (error) => {
        console.error('Error registering user:', error);
        this.errorMessage = 'Failed to register user. Please try again.';
        console.log('Error Message:', this.errorMessage); // Debug log
        this.successMessage = null;
      }
    );
  }
  

  // Proceed to the next step
  goToNextStep(): void {
    this.submitUserDetails(); // Submit the user details
  }
}
