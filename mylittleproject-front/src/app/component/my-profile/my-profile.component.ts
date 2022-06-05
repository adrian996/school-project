import { TeacherService } from './../../service/teacher.service';
import { Event } from '../../model/event.model';
import { UtilityService } from '../../service/utility.service';
import { TokenStorageService } from '../../service/token-storage.service';
import { HttpErrorResponse } from '@angular/common/http';
import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Course } from 'src/app/model/course.model';
import { Student } from 'src/app/model/student.model';
import { EventService } from 'src/app/service/event.service';
import { StudentService } from 'src/app/service/student.service';
import * as html2pdf from 'html2pdf.js'
import { Teacher } from 'src/app/model/teacher.model';
import { MatDialog } from '@angular/material/dialog';
import { DateService } from 'src/app/service/date.service';
import { Date } from 'src/app/model/date.model';

@Component({
  selector: 'app-my-profile',
  templateUrl: './my-profile.component.html',
  styleUrls: ['./my-profile.component.scss']
})
export class MyProfileComponent implements OnInit {

  studentCourses: Course[];
  events: Event[];
  studentId: number;
  selectedDay: string = 'MONDAY';

  toggle = true;

  timeValue: any;
  venueValue: any;

  currentStudent: Student = {};
  teacher: Teacher = {};
  teacherId: number;

  student: Student = {
    firstName: '',
    lastName: '',
    email: '',
    phoneNumber: '',
    profilePic: ''
  }

  currentUser: any;
  dates: Date[];
  constructor(private tokenStorage: TokenStorageService,
              private studentService: StudentService,
              private teacherService: TeacherService,
              private dateService: DateService,
              private eventService: EventService,
              private utilityService: UtilityService,
              private matDialog: MatDialog,) { }

  ngOnInit(){
    this.currentUser = this.tokenStorage.getUser();
    if(this.isStudent()){
      this.getStudentByUsername(this.currentUser.username);
    }
    if(this.isTeacher()){
      this.getTeacherByUsername(this.currentUser.username);
    }
    //console.log(this.currentUser.username);
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

  getStudentByUsername(email: string): void{
    this.studentService.getByEmail(email).subscribe((data: Student) =>{
      this.student = data;
      this.studentId = data.id;
      localStorage.setItem('id', data.id);
      this.getStudentCourses(this.studentId);
      this.getEventsByStudentId(this.studentId);
    },
    (error: HttpErrorResponse) => {
      alert(error.message);
    })
  }

  getTeacherByUsername(email: string): void{
    this.teacherService.getByEmail(email).subscribe((data: Teacher) =>{
      this.teacher = data;
      this.teacherId = data.id;
      localStorage.setItem('id', data.id);
      //this.getStudentCourses(this.studentId);
      this.getEventsByCourseId(data.course.id);
    },
    (error: HttpErrorResponse) => {
      alert(error.message);
    })
  }

  getEventsByStudentId(studentId: number): void{
    {
      this.eventService.getByStudentId(studentId).subscribe((data: Event[]) =>{
        this.events = data;
        this.sortEvents();
      },
      (error: HttpErrorResponse) => {
        alert(error.message);
      })
    }
  }

  getEventsByCourseId(courseId: number): void{
    {
      this.eventService.getByCourseId(courseId).subscribe((data: Event[]) =>{
        this.events = data;
        this.sortEvents();
      },
      (error: HttpErrorResponse) => {
        alert(error.message);
      })
    }
  }

  editStudent(){
    const data = {
      firstName: this.student.firstName,
      lastName: this.student.lastName,
      email: this.student.email,
      phoneNumber: this.student.phoneNumber
    };
    this.studentService.update(this.studentId, data)
      .subscribe(
        {
          next: (res) => {
            console.log(res);
            window.location.reload();
            alert("Student edited");
          } ,
          error: e => console.error(e)
        }
      );
  }

  editTeacher(){
    const data = {
      profilePic: this.teacher.profilePic,
      firstName: this.teacher.firstName,
      lastName: this.teacher.lastName,
      email: this.teacher.email,
      phoneNumber: this.teacher.phoneNumber
    };
    this.teacherService.update(this.teacherId, data)
      .subscribe(
        {
          next: (res) => {
            window.location.reload();
            alert("Teacher edited");
          } ,
          error: e => console.error(e)
        }
      );
  }

  changeDay(day): void{
    this.selectedDay = day;
  }

  showId(){
    alert(this.studentId);
  }

  deleteCourse(studentId: number, courseId: number){
    this.studentService.deleteCourse(studentId, courseId).subscribe(
      () => {},
      err => {
        console.log(err);
      }
    );
    alert("Course deleted");
    window.location.reload();
  }

  sortEvents(){
    this.events.sort((a, b) => a.date.startTime.localeCompare(b.date.startTime));
  }

  download(id: string){
    var element = document.getElementById(id);
    var opt = {
      margin:       1,
      filename:     'schedule.pdf',
      image:        { type: 'jpeg', quality: 0.98 },
      html2canvas:  { scale: 2 },
      jsPDF:        { unit: 'in', format: 'letter', orientation: 'portrait' }
    };
    html2pdf().from(element).set(opt).save();
  }


  openDialog(templateRef) {
    this.dateService.getAll().subscribe((data: any) =>{
      this.dates = data;
      this.dates.sort((a, b) => a.dow.localeCompare(b.dow));
    },
    (error: HttpErrorResponse) => {
      alert(error.message);
    })
    let dialogRef = this.matDialog.open(templateRef, {
     width: '300px'
   });
  }

  createEvent(){
    const data = {
      course: {
        id: this.teacher.course.id
      },
      date: {
        id: this.timeValue
      },
      venue: this.venueValue,
    };
    this.eventService.createEvent(data).subscribe({
      next: (res) => {
      },
      error: (e) => console.error(e),
    });
    this.matDialog.closeAll();
    window.location.reload();
    alert("New event added");

  }

  deleteEvent(eventId: number){
    if(confirm("Do you want to delete event?")){
      this.eventService.deleteEvent(eventId).subscribe(
        (r) => {
          console.log(r);
        },
        err => {
          console.log(err);
        }
        );
        alert("Event deleted");
        window.location.reload();
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
