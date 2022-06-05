import { UtilityService } from './service/utility.service';
import { Component } from '@angular/core';
import { Router } from '@angular/router';
import { TokenStorageService } from './service/token-storage.service';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.scss']
})


export class AppComponent {
  title = 'School database';

  private roles: string[] = [];
  isLoggedIn = false;
  username?: string;

  constructor(private router: Router,
              private tokenStorageService: TokenStorageService,
              private utilityService: UtilityService){}

  isLoginPage() {
    return this.router.url === '/';
  }

  ngOnInit(): void {
    this.isLoggedIn = !!this.tokenStorageService.getToken();
    if (this.isLoggedIn) {
      const user = this.tokenStorageService.getUser();
      //alert("logged in as " + user.username);
      this.roles = user.roles;
      this.username = user.username;
    }
  }

  logout(): void {
    this.tokenStorageService.signOut();
    //window.location.reload();
    //this.router.url = '/';
  }

  isStudent(): boolean{
    return this.utilityService.isStudent();
  }

  isTeacher(): boolean{
    return this.utilityService.isTeacher();
  }

  isAdmin(): boolean{
    return this.utilityService.isAdmin();
  }

}
