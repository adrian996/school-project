import { MyProfileComponent } from './component/my-profile/my-profile.component';
import { UtilityService } from './service/utility.service';
import { authInterceptorProviders } from './service/basicauthinterceptor.service';
import { MatDialogModule } from '@angular/material/dialog';
import { BrowserModule } from '@angular/platform-browser';
import { NgModule } from '@angular/core';

import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { CoursesListComponent } from './component/courses-list/courses-list.component';
import { StudentsListComponent } from './component/students-list/students-list.component';
import { HttpClientModule } from '@angular/common/http';
import { TeachersListComponent } from './component/teachers-list/teachers-list.component';
import { CourseInfoComponent } from './component/course-info/course-info.component';
import { StudentInfoComponent } from './component/student-info/student-info.component';
import { EventsListComponent } from './component/events-list/events-list.component';
import { FormsModule } from '@angular/forms';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { LoginComponent } from './component/login/login.component';
import { NgxPaginationModule } from 'ngx-pagination';

@NgModule({
  declarations: [
    AppComponent,
    CoursesListComponent,
    StudentsListComponent,
    TeachersListComponent,
    CourseInfoComponent,
    StudentInfoComponent,
    EventsListComponent,
    MyProfileComponent,
    LoginComponent
  ],
  imports: [
    NgxPaginationModule,
    BrowserModule,
    AppRoutingModule,
    HttpClientModule,
    FormsModule,
    MatDialogModule,
    BrowserAnimationsModule
  ],
  providers: [authInterceptorProviders, UtilityService],
  bootstrap: [AppComponent]
})
export class AppModule { }


// {
//   provide:HTTP_INTERCEPTORS, useClass:BasicauthinterceptorService, multi:true
// }
