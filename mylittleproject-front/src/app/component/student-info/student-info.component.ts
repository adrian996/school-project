import { StudentService } from '../../service/student.service';
import { Student } from 'src/app/model/student.model';
import { Course } from 'src/app/model/course.model';
import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpErrorResponse } from '@angular/common/http';

@Component({
  selector: 'app-student-info',
  templateUrl: './student-info.component.html',
  styleUrls: ['./student-info.component.scss']
})
export class StudentInfoComponent implements OnInit {

  studentCourses: Course[];
  currentStudent: Student = {
    firstName: "first",
    lastName: "last",
    email: "email",
    phoneNumber: "phone"
  }
  studentId: number;

  constructor(private studentService: StudentService, private route: ActivatedRoute) { }

  ngOnInit(){
    this.studentId = +this.route.snapshot.params.id;
    //alert(this.studentId);
    this.get(this.studentId);
    this.getStudentCourses(this.studentId);
  }

  getStudentCourses(studentId: number): void{
    this.studentService.getStudentCourses(studentId).subscribe((data: Course[]) =>{
      this.studentCourses = data;
    },
    (error: HttpErrorResponse) => {
      alert(error.message);
    })
  }

  get(studentId: number): void{
    this.studentService.get(studentId).subscribe((data: Student) =>{
      this.currentStudent = data;
    },
    (error: HttpErrorResponse) => {
      alert(error.message);
    })
  }

}
