import { TokenStorageService } from 'src/app/service/token-storage.service';
import { Injectable } from '@angular/core';

@Injectable({
  providedIn: 'root'
})
export class UtilityService {

  constructor(private tokenStorage: TokenStorageService) { }

  isStudent(): boolean{
    if (this.tokenStorage.getUser().roles.some(v => "ROLE_STUDENT" === v)) {
      return true;
    }
    return false;
  }

  isTeacher(): boolean{
    if (this.tokenStorage.getUser().roles.some(v => "ROLE_TEACHER" === v)) {
      return true;
    }
    return false;
  }

  isAdmin(): boolean{
    if (this.tokenStorage.getUser().roles.some(v => "ROLE_ADMIN" === v)) {
      return true;
    }
    return false;
  }
}
