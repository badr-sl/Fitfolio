import { Component } from '@angular/core';
import { Router } from '@angular/router';
import { MatIconModule } from '@angular/material/icon';  // Import MatIconModule
import { RouterModule } from '@angular/router';  // Import RouterModule

@Component({
  selector: 'app-navbar',
  standalone: true,
  imports: [MatIconModule, RouterModule],  // Add RouterModule here
  templateUrl: './navbar.component.html',
  styleUrls: ['./navbar.component.css']
})
export class NavbarComponent {
  constructor(public router: Router) {}
}
