import { Injectable } from '@angular/core';

@Injectable({
  providedIn: 'root',
})
export class UserDataService {
  private localStorageKey = 'userData';

  // Set user data and store it in localStorage
  setUserData(data: any): void {
    this.saveToLocalStorage(data);
  }

  // Get user data from localStorage
  getUserData(): any {
    const data = this.loadFromLocalStorage();
    return data ? JSON.parse(data) : null;
  }

  // Clear user data from localStorage
  clearUserData(): void {
    localStorage.removeItem(this.localStorageKey);
  }

  // Helper method to save data to localStorage
  private saveToLocalStorage(data: any): void {
    localStorage.setItem(this.localStorageKey, JSON.stringify(data));
  }

  // Helper method to load data from localStorage
  private loadFromLocalStorage(): string | null {
    return localStorage.getItem(this.localStorageKey);
  }
}
