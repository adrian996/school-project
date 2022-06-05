import { StudentService } from 'src/app/service/student.service';
import { UtilityService } from './../../service/utility.service';
import { MatDialog, MatDialogModule } from '@angular/material/dialog';
import { EventService } from './../../service/event.service';
import { TeacherService } from './../../service/teacher.service';
import { CourseService } from '../../service/course.service';
import { Component, OnInit } from '@angular/core';
import { HttpErrorResponse } from '@angular/common/http';
import { Course } from 'src/app/model/course.model';
import { Teacher } from 'src/app/model/teacher.model';
import { TokenStorageService } from 'src/app/service/token-storage.service';



@Component({
  selector: 'app-courses-list',
  templateUrl: './courses-list.component.html',
  styleUrls: ['./courses-list.component.scss']
})
export class CoursesListComponent implements OnInit {
  p: number = 1;
  courses: Course[];
  events: Event[];
  courseName = '';
  toggle = true;

  currentUser: any;

  studentId: number;

  course: Course = {
    name: '',
    description: '',
    points: null
  }

  constructor(private token: TokenStorageService,
              private courseService: CourseService,
              private eventService: EventService,
              private matDialog: MatDialog,
              private utilityService: UtilityService,
              private studentService: StudentService) { }

  ngOnInit(): void {
    this.currentUser = this.token.getUser();
    this.getAllCourses();
    this.studentId = +localStorage.getItem('id');
    console.log("this is id" + this.studentId);
  }

  saveCourse(): void{
    const data = {
      name: this.course.name,
      description: this.course.description,
      points: +this.course.points
    };

    this.courseService.create(data).subscribe({
      next: (res) => {
        console.log(res);
      },
      error: (e) => console.error(e),
    });
    this.matDialog.closeAll();
    alert("New course added");
    this.getAllCourses();
  }

  addCourseToStudent(studentId: number, courseId: number){
    this.studentService.addCourseToStudent(studentId, courseId).subscribe({
      next: (res) => {
        alert("Course added");
      },
      error: (e) => console.error(e),
    });
  }

  openDialog(templateRef, course) {
    this.course = course;
    let dialogRef = this.matDialog.open(templateRef, {
     width: '300px'
   });
  }

  editCourse(){
    const data = {
      name: this.course.name,
      description: this.course.description,
      points: +this.course.points
    };
    console.log(this.course);
    this.courseService.update(this.course.id, data)
      .subscribe(
        {
          next: (res) => {
            console.log(res);
            alert("Course edited");
            this.getAllCourses();
            this.matDialog.closeAll();
          } ,
          error: e => console.error(e)
        }
      );
  }

  getAllCourses(): void{
    {
      this.courseService.getAll().subscribe((data: Course[]) =>{
        this.courses = data;
        this.sortByName();
      },
      (error: HttpErrorResponse) => {
        alert(error.message);
      })
    }
  }

  getByName(): void{
    {
      this.courseService.getByName(this.courseName).subscribe((data: Course[]) =>{
        this.courses = data;
        this.sortByName();
        console.log(data);
      },
      (error: HttpErrorResponse) => {
        alert(error.message);
      })
    }
  }

  getByCourseId(courseId: number): void{
    {
      this.eventService.getByCourseId(courseId).subscribe((data: Event[]) =>{
        this.events = data;
      },
      (error: HttpErrorResponse) => {
        alert(error.message);
      })
    }
  }

  sortByName(){
    this.courses.sort((a, b) =>
      this.toggle ?
        a.name.toLowerCase().localeCompare(b.name.toLowerCase()) :
        b.name.toLowerCase().localeCompare(a.name.toLowerCase()));
    this.toggle = !this.toggle;
  }

  sortByPoints(){
      this.courses.sort((a, b) =>
        this.toggle ?
          b.points - a.points :
          a.points - b.points);
    this.toggle = !this.toggle;
  }

  sortByDescription(){
    this.courses.sort((a, b) =>
      this.toggle ?
        a.description.toLowerCase().localeCompare(b.description.toLowerCase()) :
        b.description.toLowerCase().localeCompare(a.description.toLowerCase()));
    this.toggle = !this.toggle;
  }

  deleteCourse(id: any): void{
    if(confirm("Do you want to delete course " + id)){
    this.courseService.delete(id).subscribe(
      (r) => {
        console.log(r);
      },
      err => {
        console.log(err);
      }
      );
      alert("Course deleted");
      this.getAllCourses();
    }
  }

  deleteAllCourses(): void{
    if(confirm("Do you want to delete all courses?")){
    this.courseService.deleteAll().subscribe(
      (r) => {
        console.log(r);
      },
      err => {
        console.log(err);
      }
      );
      alert("All courses deleted");
    this.getAllCourses();
    }
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
