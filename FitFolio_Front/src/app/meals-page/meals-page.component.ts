import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { Router } from '@angular/router';
import { HttpClient } from '@angular/common/http';
import { UserDataService } from '../services/user-data.service'; // Adjust the path to the actual location of the service


@Component({
  selector: 'app-meals-page',
  standalone: true,
  templateUrl: './meals-page.component.html',
  styleUrls: ['./meals-page.component.css'],
  imports: [CommonModule, FormsModule],
})

export class MealsPageComponent {
  // User data passed dynamically
  userName: string = '';
  lastName: string = '';
  objectif: string = '';
  updatedObjectif: string = ''; // Updated goal to be passed back
  registrationDate: string = ''; // Stocker la date d'inscription
  successMessage: string = '';
  errorMessage: string = '';
  // Dropdown options for filters
  weeks = ['Week 1', 'Week 2', 'Week 3', 'Week 4'];
  days: string[] = ['Monday', 'Tuesday', 'Wednesday', 'Thursday', 'Friday', 'Saturday', 'Sunday'];
  weeknb: number = 1;

  // Selected dropdown values
  selectedWeek = this.weeks[0];
  selectedDay = this.days[0];
  isFavorite = false; // Initial state of the heart button



  modalDays: string[] = [...this.days]; // Independent days for the modal
  modalSelectedDay = ''; // Modal-selected day
  modalWeek = 1; 


  plans = [
    {
      name: 'Toast with Banana Flavor',
      ingredients: 3,
      time: 'Breakfast',
      imageUrl: '/assets/pain-perdu.png',
      week: 'Week 1',
      day: 'Monday',
    },
    {
      name: 'Chickpea Salad',
      ingredients: 5,
      time: 'Lunch',
      imageUrl: '/assets/chickpea-salad.png',
      week: 'Week 1',
      day: 'Monday',
    },
    {
      name: 'Pancake with Honey',
      ingredients: 4,
      time: 'Dinner',
      imageUrl: '/assets/pancake.png',
      week: 'Week 1',
      day: 'Monday',
    },
    {
      name: 'Set of Salad',
      ingredients: 2,
      time: 'Supper',
      imageUrl: '/assets/salad.png',
      week: 'Week 1',
      day: 'Monday',
    },
  ];

  filteredMeals = this.plans;

  ngOnInit(): void {
    this.loadUserData(); // Load user data
  
    // Get the registration day and reorder days
    const registrationDay = this.getRegistrationDay();
    if (registrationDay) {
      const registrationDayIndex = this.days.indexOf(registrationDay);
      if (registrationDayIndex !== -1) {
        // Reorder days to start from the registration day
        this.days = [
          ...this.days.slice(registrationDayIndex),
          ...this.days.slice(0, registrationDayIndex),
        ];
        this.modalDays = [...this.days]; // Align modalDays with reordered days
        console.log('Reordered Days:', this.days); // Debugging log
      } else {
        console.error('Registration day not found in days array.');
      }
    }
  
    // Set default selected week and day
    if (this.registrationDate) {
      const { week, day } = this.calculateWeekAndDay(this.registrationDate);
      this.selectedWeek = `Week ${week}`;
      this.selectedDay = this.days[day - 1]; // Adjust for 0-based index
    }
  
    // Fetch meals
    this.fetchAvailableMeals();
    this.fetchWeeklyPlannedMeals();
  }
  
  loadUserData(): void {
    const userData = this.userDataService.getUserData();
    if (userData) {
      this.userName = userData.userName || '';
      this.lastName = userData.lastName || '';
      this.objectif = userData.objectif || '';
      this.registrationDate = userData.date || ''; // Set registration date
  
      console.log('Loaded User Data:', userData); // Debugging log
      console.log('Registration Date:', this.registrationDate); // Debugging log
    } else {
      console.error('No user data found');
    }
  }
  
  
  constructor(
    private router: Router,
    private http: HttpClient,
    private userDataService: UserDataService // Inject UserDataService
  ) {}
  

  // Filter meals based on selected week and day
  filterMeals() {
    this.filteredMeals = this.plans.filter(
      (plan) => plan.week === this.selectedWeek && plan.day === this.selectedDay
    );
  }

  selectedInfo: any = null;

  openInfo(plan: any): void {
    this.selectedInfo = plan; // Set the selected meal
  }

  closeInfo(): void {
    this.selectedInfo = null; // Clear the selected meal to close the modal
  }

  isModalOpen: boolean = false;

  // Meal Plan Management
  mealPlan: { [day: string]: { Breakfast?: any; Lunch?: any; Dinner?: any } } = {};
  selectedMeal: any = null;


  showAdditionalDetails = false;

  openModal() {
    this.isModalOpen = true;


    const registrationDay = this.getRegistrationDay();
    if (registrationDay) {
      const registrationDayIndex = this.days.indexOf(registrationDay);
      if (registrationDayIndex !== -1) {
        this.modalDays = [
          ...this.days.slice(registrationDayIndex),
          ...this.days.slice(0, registrationDayIndex),
        ];
        console.log('Reorganized modal days:', this.modalDays);
      } else {
        console.error('Registration day not found in modal days array.');
      }
    }

    this.modalSelectedDay = registrationDay || this.days[0]; // Default to the first day if registration day is unavailable
    this.modalWeek = this.getCurrentWeekNumber();
  }

  closeModal() {
    this.isModalOpen = false;
    this.selectedMeal = null;
  }


  navigateModalWeek(direction: 'previous' | 'next'): void {
    if (direction === 'previous' && this.modalWeek > 1) {
      this.modalWeek--;
    } else if (direction === 'next') {
      this.modalWeek++;
    }
  }

  selectDayInModal(day: string): void {
    this.modalSelectedDay = day; // Change only the modal's day
    this.selectedMeal = null;
  }


  getCurrentWeekNumber(): number {
    if (!this.registrationDate) return 1;

    const { week } = this.calculateWeekAndDay(this.registrationDate);
    return week;
  }

  getDateForModalSelectedDay(): string {
    if (!this.registrationDate) {
      console.error('Registration date is not available.');
      return '';
    }
  
    const registrationDate = new Date(this.registrationDate);
  
    // Calculate the start of the modal's week
    const modalWeekStartDate = new Date(registrationDate);
    modalWeekStartDate.setDate(registrationDate.getDate() + (this.modalWeek - 1) * 7);
  
    // Find the index of the selected day in reordered modalDays
    const modalDayIndex = this.modalDays.indexOf(this.modalSelectedDay);
    if (modalDayIndex === -1) {
      console.error('Invalid modal selected day:', this.modalSelectedDay);
      return '';
    }
  
    // Calculate the date for the selected day
    const selectedDate = new Date(modalWeekStartDate);
    selectedDate.setDate(modalWeekStartDate.getDate() + modalDayIndex);
  
    // Format to YYYY-MM-DD
    const formattedDate = selectedDate.toISOString().split('T')[0];
    console.log('Calculated date for modal selected day:', formattedDate); // Debugging log
    return formattedDate;
  }
  

  selectDay(day: string) {
    this.selectedDay = day;
    this.selectedMeal = null;
  }

  selectMeal(meal: any, mealType: 'Breakfast' | 'Lunch' | 'Dinner') {
    if (!this.mealPlan[this.modalSelectedDay]) {
      this.mealPlan[this.modalSelectedDay] = {}; // Initialize the day if it doesn't exist
    }
    this.mealPlan[this.modalSelectedDay][mealType] = meal; // Add the meal for the specific type
    this.selectedMeal = meal; // Set selected meal
    console.log('Updated Meal Plan:', this.mealPlan); // Debugging log
  }
  
  
  savePlan(): void {
    this.clearMessages();
  
    if (!Object.keys(this.mealPlan).length) {
      this.errorMessage = 'No meals selected! Please add meals before saving.';
      return;
    }
  
    const selectedMeals = this.mealPlan[this.modalSelectedDay];
    if (!selectedMeals || Object.keys(selectedMeals).length === 0) {
      this.errorMessage = 'No meals selected for this day.';
      return;
    }
  
    const repasIds = Object.values(selectedMeals)
      .filter((meal) => meal?.id)
      .map((meal) => ({ id: meal.id }));
  
    if (!repasIds.length) {
      this.errorMessage = 'No valid meals to save.';
      return;
    }
  
    const userData = this.userDataService.getUserData();
    const utilisateurId = userData?.id;
  
    if (!utilisateurId) {
      this.errorMessage = 'User ID not found. Please log in again.';
      return;
    }
  
    const jour = this.getDateForModalSelectedDay();
    if (!jour) {
      this.errorMessage = 'Failed to calculate the date for the selected day.';
      return;
    }
  
    const url = `http://localhost:8080/fitfolio/planning/addRepasToPlanning?jour=${jour}&utilisateurId=${utilisateurId}`;
  
    this.http.post(url, repasIds).subscribe(
      () => {
        this.successMessage = 'Meal plan saved successfully!';
        
        // Wait for 2 seconds (2000ms) before closing the modal
        setTimeout(() => {
          delete this.mealPlan[this.modalSelectedDay];
          this.selectedMeal = null;
          this.closeModal();
        }, 0.2); // Adjust the delay time as needed (currently 2 seconds)
      },
      (error) => {
        console.error('Failed to save meal plan:', error);
        this.errorMessage = 'Failed to save meal plan. Please try again.';
      }
    );
    
  }
  
  clearMessages(): void {
    this.successMessage = '';
    this.errorMessage = '';
  }
  

  onScroll(event: Event): void {
    const target = event.target as HTMLElement;
    const scrollThreshold = 150;

    this.showAdditionalDetails = target.scrollTop > scrollThreshold;
  }

  availableMeals: any[] = [];
  breakfastMeals: any[] = [];
  lunchMeals: any[] = [];
  dinnerMeals: any[] = [];


  fetchAvailableMeals(): void {
    if (!this.objectif) {
      console.error('Objectif is not set. Aborting API call.');
      return;
    }

    const url = `http://localhost:8080/fitfolio/repas/objectif?objectif=${encodeURIComponent(this.objectif)}`;
    this.http.get<any[]>(url).subscribe(
      (meals) => {
        if (!Array.isArray(meals)) {
          console.error('Invalid API response:', meals);
          this.availableMeals = [];
          return;
        }

        this.availableMeals = meals.map((meal) => ({
          id: meal.id,
          name: meal.titre,
          description: meal.description,
          category: meal.categorie,
          calories: meal.nbrcalories,
          type: meal.typerepas,
          objectif: meal.objectif,
          imageUrl: `/assets/${meal.image}`,
        }));

        this.breakfastMeals = this.availableMeals.filter((meal) => meal.type === 'Breakfast');
        this.lunchMeals = this.availableMeals.filter((meal) => meal.type === 'Lunch');
        this.dinnerMeals = this.availableMeals.filter((meal) => meal.type === 'Dinner');

        console.log('Available Meals:', this.availableMeals);
      },
      (error) => {
        console.error('Error fetching available meals:', error);
      }
    );
  }

  getRegistrationDay(): string {
    if (!this.registrationDate) {
      console.error('Registration date is not available.');
      return '';
    }
  
    try {
      const date = new Date(this.registrationDate); // Parse the registration date
      const daysOfWeek = ['Sunday', 'Monday', 'Tuesday', 'Wednesday', 'Thursday', 'Friday', 'Saturday'];
  
      const registrationDay = daysOfWeek[date.getDay()]; // Get the day name
      console.log('Calculated registration day:', registrationDay); // Debugging log
      return registrationDay;
    } catch (error) {
      console.error('Error parsing registration date:', error);
      return '';
    }
  }
  

  getDateForSelectedDay(): string {
    if (!this.registrationDate) {
      console.error('Registration date is not available.');
      return '';
    }
  
    const registrationDate = new Date(this.registrationDate);
  
    // Calculate the start of the selected week
    const selectedWeekNumber = parseInt(this.selectedWeek.replace('Week ', ''), 10); // Extract the week number
    const weekStartDate = new Date(registrationDate);
    weekStartDate.setDate(registrationDate.getDate() + (selectedWeekNumber - 1) * 7);
  
    // Find the selected day's offset
    const selectedDayIndex = this.days.indexOf(this.selectedDay);
    if (selectedDayIndex === -1) {
      console.error('Invalid selected day:', this.selectedDay);
      return '';
    }
  
    const selectedDate = new Date(weekStartDate);
    selectedDate.setDate(weekStartDate.getDate() + selectedDayIndex);
  
    // Format to YYYY-MM-DD
    const formattedDate = selectedDate.toISOString().split('T')[0];
    console.log('Calculated date for selected day:', formattedDate); // Debugging log
    return formattedDate;
  }
  

  
  calculateWeekAndDay(startDate: string): { week: number; day: number } {
    const registrationDate = new Date(startDate);
    const currentDate = new Date();
  
    // Total days since registration
    const diffInDays = Math.floor((currentDate.getTime() - registrationDate.getTime()) / (1000 * 60 * 60 * 24));
  
    // Offset for start day to align with the registration day (e.g., Tuesday as start)
    const startDayOffset = this.days.indexOf(this.getRegistrationDay()); // Offset based on registration day
    const adjustedDiff = diffInDays - startDayOffset; // Subtract the offset here to fix alignment
  
    // Calculate week and day
    const week = Math.floor(adjustedDiff / 7) + 1; // Week number (1-based)
    const day = (adjustedDiff % 7 + 7) % 7; // Day number (0-based, wraps correctly)
  
    // Adjust for 1-based indexing for days
    return { week, day: day + 1 }; // Return day as 1-based
  }
  
  

  



fetchWeeklyPlannedMeals(): void {
  const userData = this.userDataService.getUserData();
  const utilisateurId = userData?.id;

  if (!utilisateurId || !this.registrationDate) {
    console.error('User ID or registration date not available.');
    return;
  }

  // Correctly calculate the week number and day name
  const weekNumber = parseInt(this.selectedWeek.replace('Week ', ''), 10); // Ensure week is parsed correctly
  const dayName = this.selectedDay.toUpperCase(); // Day name is aligned with reordered days

  // API URL
  const url = `http://localhost:8080/fitfolio/weekly-repas?utilisateurId=${utilisateurId}&startDate=${this.registrationDate}&weekNumber=${weekNumber}&day=${dayName}`;
  console.log('Fetching meals from URL:', url); // Debugging log

  this.http.get<any[]>(url).subscribe(
    (meals) => {
      if (Array.isArray(meals)) {
        this.filteredMeals = meals.map((meal) => ({
          id: meal.id,
          name: meal.titre,
          description: meal.description,
          category: meal.categorie,
          calories: meal.nbrcalories,
          type: meal.typerepas,
          objectif: meal.objectif,
          imageUrl: `/assets/${meal.image}`,
          ingredients: 0, // Default value
          time: meal.typerepas, // Approximation
          week: `Week ${weekNumber}`, // Ensure proper week is shown
          day: dayName, // Correct day
        }));
        console.log('Planned Meals:', this.filteredMeals);
      } else {
        console.error('Invalid response for weekly planned meals:', meals);
      }
    },
    (error) => {
      console.error('Error fetching weekly planned meals:', error);
    }
  );
}











  navigateToDashboard(): void {
    const updatedData = {
      userName: this.userName,
      lastName: this.lastName,
      objectif: this.updatedObjectif || this.objectif,
    };
    this.userDataService.setUserData(updatedData); // Update user data in the service
    this.router.navigate(['/dashboard'], { state: { userData: updatedData } });
  }

}