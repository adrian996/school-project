import { MyProfileComponent } from './component/my-profile/my-profile.component';
import { EventsListComponent } from './component/events-list/events-list.component';
import { CoursesListComponent } from './component/courses-list/courses-list.component';
import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';
import { StudentsListComponent } from './component/students-list/students-list.component';
import { TeachersListComponent } from './component/teachers-list/teachers-list.component';
import { CourseInfoComponent } from './component/course-info/course-info.component';
import { StudentInfoComponent } from './component/student-info/student-info.component';
import { LoginComponent } from './component/login/login.component';

const routes: Routes = [
  {path: '', component: LoginComponent},
  { path: 'courses', component: CoursesListComponent},
  { path: 'students', component: StudentsListComponent},
  { path: 'teachers', component: TeachersListComponent},
  { path: 'courses/course-info/:id', component: CourseInfoComponent},
  { path: 'courses/events-list/:id', component: EventsListComponent},
  { path: 'students/student-info/:id', component: StudentInfoComponent},
  { path: 'my-profile', component: MyProfileComponent}
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }


