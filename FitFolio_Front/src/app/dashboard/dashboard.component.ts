import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Router } from '@angular/router';
import jsPDF from 'jspdf';
import html2canvas from 'html2canvas';
import { UserDataService } from '../services/user-data.service';

@Component({
  selector: 'app-dashboard',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './dashboard.component.html',
  styleUrls: ['./dashboard.component.css'],
})
export class DashboardComponent implements OnInit {
  todayDate: string = new Date().toLocaleDateString();

  // User and health data properties
  userName: string = '';
  lastName: string = '';
  weight: number = 0;
  height: number = 0;
  gender: string = '';
  age: number = 0;
  bmiValue: number = 0;
  bmiStatus: string = '';
  objectif: string = '';
  sleepTime: string = '7h 30m'; // Default sleep time

  // Calorie data
  caloriesConsumed: number = 800; // Default consumed calories
  totalCalories: number = 2000; // Daily calorie goal
  caloriesBurned: number = 700; // Default burned calories
  get caloriesLeft(): number {
    return this.totalCalories - this.caloriesConsumed; // Calculate remaining calories
  }

  get progressPercentage(): number {
    return ((this.caloriesConsumed / this.totalCalories) * 100).toFixed(2) as any; // Circular progress
  }

  // Water intake tracking
  waterTasks = [
    { time: '6am', amount: 500, completed: false },
    { time: '9am', amount: 500, completed: false },
    { time: '12pm', amount: 500, completed: false },
    { time: '3pm', amount: 500, completed: false },
    { time: '6pm', amount: 500, completed: false },
    { time: '9pm', amount: 500, completed: false },
  ];
  totalWaterIntakeGoal: number = 3000; // Goal in mL
  completedWaterIntake: number = 0;

  get waterIntakePercentage(): number {
    return (this.completedWaterIntake / this.totalWaterIntakeGoal) * 100;
  }

  // Workout progress
  isWeekly: boolean = true; // Toggle between weekly and monthly views
  workoutProgressWeekly: number[] = [40, 60, 50, 70, 80, 60, 40];
  workoutProgressMonthly: number[] = [
    50, 55, 60, 65, 70, 75, 80, 85, 90, 85, 80, 75, 70, 65, 60, 55, 50,
  ];

  constructor(private router: Router, private userDataService: UserDataService) {}

  ngOnInit(): void {
    const userData = this.userDataService.getUserData();

    if (userData) {
      this.populateUserData(userData);
    } else {
      console.error('No user data found in UserDataService.');
    }
  }

  private populateUserData(userData: any): void {
    console.log('Populating user data:', userData);
  
    // Basic user information
    this.userName = userData.firstName || 'User';
    this.lastName = userData.lastName || '';
    this.weight = userData.weight || 0;
    this.height = userData.height ? userData.height / 100 : 0; // Convert cm to meters
    this.gender = userData.gender || '';
    this.age = userData.age || 0;
    this.objectif = userData.objectif || 'Not Set';
  
    // Initialize calorie-related data
    this.caloriesConsumed = userData.caloriesConsumed || 0;
    this.caloriesBurned = userData.caloriesBurned || 0;
    this.totalCalories = userData.totalCalories || 2000; // Default daily calorie goal
  
    // Calculate derived values
    this.bmiValue = this.calculateBMI(this.weight, this.height);
    this.bmiStatus = this.getBMIStatus(this.bmiValue);
  }
  
  

  // Toggle water task completion
  toggleTaskCompletion(task: { time: string; amount: number; completed: boolean }): void {
    if (!task.completed) {
      this.completedWaterIntake += task.amount;
    } else {
      this.completedWaterIntake -= task.amount;
    }
    task.completed = !task.completed;
  }

  // Calculate BMI
  private calculateBMI(weight: number, height: number): number {
    if (weight > 0 && height > 0) {
      return +(weight / (height * height)).toFixed(1);
    }
    return 0;
  }

  // Determine BMI status
  private getBMIStatus(bmi: number): string {
    if (bmi < 18.5) return 'Underweight';
    if (bmi >= 18.5 && bmi < 24.9) return 'Normal weight';
    if (bmi >= 25 && bmi < 29.9) return 'Overweight';
    return 'Obesity';
  }

  // Switch between weekly and monthly views
  showWeekly(): void {
    this.isWeekly = true;
  }

  showMonthly(): void {
    this.isWeekly = false;
  }

  // Generate SVG points for graphs
  getSvgPoints(data: number[]): string {
    const numPoints = data.length;
    const graphWidth = 700; // Matches the SVG viewBox width
    const graphHeight = 180; // SVG height minus padding for labels

    return data
      .map((value, index) => {
        const x = (index + 0.5) * (graphWidth / numPoints);
        const y = graphHeight - (value / 100) * graphHeight;
        return `${x},${y}`;
      })
      .join(' ');
  }

  getXAxisY(): number {
    return 190; // Static Y-axis label position
  }

  // Export to PDF
  downloadPDF(): void {
    const pdfContent = document.getElementById('pdf-content');

    if (pdfContent) {
      pdfContent.style.display = 'block';

      html2canvas(pdfContent, { scale: 2 }).then((canvas) => {
        pdfContent.style.display = 'none';
        const imgData = canvas.toDataURL('image/png');
        const pdf = new jsPDF('p', 'mm', 'a4');

        const pdfWidth = pdf.internal.pageSize.getWidth();
        const pdfHeight = (canvas.height * pdfWidth) / canvas.width;

        pdf.addImage(imgData, 'PNG', 0, 0, pdfWidth, pdfHeight);
        pdf.save('Dashboard-Report.pdf');
      });
    }
  }
}
