import { Course } from 'src/app/model/course.model';
import { CourseService } from '../../service/course.service';
import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Student } from 'src/app/model/student.model';
import { HttpErrorResponse } from '@angular/common/http';

@Component({
  selector: 'app-course-info',
  templateUrl: './course-info.component.html',
  styleUrls: ['./course-info.component.scss']
})
export class CourseInfoComponent implements OnInit {

  courseStudents: Student[];
  currentCourse: Course;
  courseId: number;

  constructor(private courseService: CourseService, private route: ActivatedRoute) { }

  ngOnInit(){
    this.courseId = +this.route.snapshot.params.id;
    this.get(this.courseId);
    this.getCourseStudents(this.courseId);

  }

  getCourseStudents(courseId: number): void{
    this.courseService.getCourseStudents(courseId).subscribe((data: Student[]) =>{
      this.courseStudents = data;
    },
    (error: HttpErrorResponse) => {
      alert(error.message);
    })
  }

  get(courseId: number): void{
    this.courseService.get(courseId).subscribe((data: Course) =>{
      this.currentCourse = data;
    },
    (error: HttpErrorResponse) => {
      alert(error.message);
    })
  }

}
