import { Component, OnInit } from '@angular/core';
import { Router, NavigationEnd } from '@angular/router';
import { RouterModule } from '@angular/router';
import { NavbarComponent } from './navbar/navbar/navbar.component';
import { CommonModule } from '@angular/common'; // Import CommonModule

@Component({
  selector: 'app-root',
  standalone: true,
  imports: [RouterModule, NavbarComponent, CommonModule], // Import CommonModule
  templateUrl: './app.component.html',
})
export class AppComponent implements OnInit {
  isAuthPage: boolean = false;

  constructor(private router: Router) {}

  ngOnInit() {
    this.router.events.subscribe(event => {
      if (event instanceof NavigationEnd) {
        const noStyleRoutes = ['/sign-in', '/register', '/goal'];

        // Determine if the current route is an authentication page
        this.isAuthPage = noStyleRoutes.some(route => event.url.startsWith(route));

        // Dynamically apply or remove the global style class
        const body = document.body;
        if (this.isAuthPage) {
          body.classList.remove('global-style');
        } else {
          body.classList.add('global-style');
        }
      }
    });
  }
}
