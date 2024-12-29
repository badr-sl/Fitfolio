import { Component, OnInit } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { CommonModule } from '@angular/common';
import { NavbarComponent } from '../navbar/navbar/navbar.component';
import { UserDataService } from '../services/user-data.service';

interface Activity {
  id: number;
  titre: string;
  description: string;
  categorie: string;
  nbrcalories: number;
  typeactivite: string;
  objectif: string;
  image: string;
  pointcardio: number;
  speed: string;
}

@Component({
  selector: 'app-activities',
  standalone: true,
  imports: [CommonModule, NavbarComponent],
  templateUrl: './activities.component.html',
  styleUrls: ['./activities.component.scss'],
})
export class ActivitiesComponent implements OnInit {
  activities: Activity[] = [];
  pastActivities: any[] = [];
  selectedActivity: Activity | null = null;
  isActivityConfirmed: boolean = false;

  successMessage: string | null = null;
  errorMessage: string | null = null; // Add error message handling

  private activitiesUrl = 'http://localhost:8080/fitfolio/activites';
  private planningUrl = 'http://localhost:8080/fitfolio/planning/addActivitesToPlanning';
  private userGoal: string = '';
  private userId: number = 0; // Will be dynamically assigned
  private jour: string = '';  // Will be dynamically assigned

  constructor(
    private http: HttpClient,
    private userDataService: UserDataService
  ) {}

  ngOnInit(): void {
    this.loadUserData();
    this.fetchActivities();
    this.fetchUserActivities(); // Fetch activities for the user
  }

  /**
   * Load the user's goal, ID, and set today's date dynamically.
   */
  private loadUserData(): void {
    const userData = this.userDataService.getUserData();
    this.userGoal = userData?.objectif || 'Lean & Tone';
    this.userId = userData?.id || 0;

    // Set today's date in 'yyyy-MM-dd' format
    const today = new Date();
    this.jour = today.toISOString().split('T')[0]; // Extract only the date part
  }

  /**
   * Fetch activities based on the user's goal.
   */
  private fetchActivities(): void {
    const url = `${this.activitiesUrl}?objectif=${encodeURIComponent(this.userGoal)}`;
    this.http.get<Activity[]>(url).subscribe(
      (data) => {
        this.activities = data.map((activity) => ({
          ...activity,
          image: `assets/${activity.image}`, // Dynamically prepend "assets/" to image filename
        }));
        if (this.activities.length > 0) {
          this.selectedActivity = this.activities[0];
        }
      },
      (error) => {
        console.error('Error fetching activities:', error);
        this.errorMessage = 'Failed to fetch activities. Please try again.';
      }
      
    );
  }

  /**
   * Handle activity selection.
   */
  selectActivity(activity: Activity): void {
    this.selectedActivity = activity;
    this.isActivityConfirmed = false;
    this.successMessage = null;
    this.errorMessage = null;
  }

  confirmSelection(): void {
    const payload = [this.selectedActivity];
    const url = `${this.planningUrl}?jour=${this.jour}&utilisateurId=${this.userId}`;
  
    this.http.post(url, payload).subscribe(
      (response) => {
        console.log('Activity successfully added to planning:', response);
        this.isActivityConfirmed = true;
        this.successMessage = 'Activity successfully added to your planning!';
        
        // Wait for 3 seconds (3000ms) before refreshing the page
        setTimeout(() => {
          this.successMessage = null;
          window.location.reload(); // Refresh the page
        }, 500); // Adjust the time as needed
  
      },
      (error) => {
        console.error('Error adding activity to planning:', error);
        this.errorMessage = 'Failed to add activity to planning. Please try again.';
        setTimeout(() => {
          this.successMessage = null;
        }, 500);
      }
    );
  }
  

  
  /**
   * Handle fallback image loading.
   */
  onImageError(event: any): void {
    event.target.src = 'assets/placeholder.png'; // Fallback image
  }

  /**
   * Existing selection function (unchanged).
   */
  selection(): void {
    if (!this.selectedActivity) {
        this.errorMessage = 'Please select an activity first.';
        setTimeout(() => (this.successMessage = null), 7000); // Réinitialise après 5 secondes
        return;
    }
    if (this.selectedActivity) {
        this.isActivityConfirmed = true;

        // Délai d'attente avant de définir le message de succès
        setTimeout(() => {
            this.successMessage = 'Activity Selected';
        }, 0); // Attendre 3 secondes avant d'afficher le succès
    }
}


  private fetchUserActivities(): void {
    const url = `http://localhost:8080/fitfolio/AllActivities/${this.userId}`;
    this.http.get<any[]>(url).subscribe(
      (data) => {
        // Ensure the response is an array
        if (Array.isArray(data)) {
          this.pastActivities = data.map((activity) => ({
            id: activity.id,
            titre: activity.titre,
            description: activity.description,
            categorie: activity.categorie,
            nbrcalories: activity.nbrcalories,
            typeactivite: activity.typeactivite,
            objectif: activity.objectif,
            image: `/assets/${activity.image}`, // Construct the image path
            pointcardio: activity.pointcardio,
            speed: activity.speed,
            date: new Date().toLocaleDateString(), // Add a placeholder date
          }));
        } else {
          this.pastActivities = [];

        }
      },
      (error) => {
        console.error('Error fetching user activities:', error);
      }
    );
  }
  
  
  
}
