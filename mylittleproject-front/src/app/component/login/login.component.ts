import { UtilityService } from './../../service/utility.service';
import { Router } from '@angular/router';
import { TeacherService } from './../../service/teacher.service';
import { StudentService } from 'src/app/service/student.service';
import { LoginService } from 'src/app/service/login.service';
import { Component, OnInit } from '@angular/core';
import { TokenStorageService } from 'src/app/service/token-storage.service';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.scss']
})
export class LoginComponent implements OnInit {
  form: any = {
    username: null,
    password: null
  };
  isLoggedIn = false;
  isLoginFailed = false;
  errorMessage = '';
  roles: string[] = [];
  constructor(private loginService: LoginService,
              private tokenStorage: TokenStorageService,
              private router: Router,
              private utilityService: UtilityService) { }
  ngOnInit(): void {
    if (this.tokenStorage.getToken()) {
      this.isLoggedIn = true;
      this.roles = this.tokenStorage.getUser().roles;
    }
  }
  onSubmit(): void {
    let username = this.form.username;
    let password = this.form.password;
    console.log(username + "> " + password);
    this.loginService.login(username, password).subscribe(
      data => {
        this.tokenStorage.saveToken(data.token);
        this.tokenStorage.saveUser(data);
        this.isLoginFailed = false;
        this.isLoggedIn = true;
        this.roles = this.tokenStorage.getUser().roles;
        if(this.isAdmin())
          this.router.navigate(['courses']);
        else
          this.router.navigate(['my-profile']);
      },
      err => {
        this.errorMessage = err.error.message;
        this.isLoginFailed = true;
      }
    );
  }
  reloadPage(): void {
    window.location.reload();
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
