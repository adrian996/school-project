import { UtilityService } from './../../service/utility.service';
import { StudentService } from './../../service/student.service';
import { Component, OnInit } from '@angular/core';
import { Student } from 'src/app/model/student.model';
import { HttpErrorResponse } from '@angular/common/http';
import { MatDialog } from '@angular/material/dialog';

@Component({
  selector: 'app-students-list',
  templateUrl: './students-list.component.html',
  styleUrls: ['./students-list.component.scss']
})
export class StudentsListComponent implements OnInit {
  p: number = 1;

  students: Student[];
  lastName: '';
  toggle = true;

  student: Student = {
    firstName: '',
    lastName: '',
    email: '',
    phoneNumber: ''
  }

  constructor(private studentService: StudentService,
              private matDialog: MatDialog,
              private utilityService: UtilityService) { }

  ngOnInit(): void {
    this.getAllStudents();
  }

  getAllStudents(): void{
    {
      this.studentService.getAll().subscribe((data: Student[]) =>{
        this.students = data;
        this.sortByName();
      },
      (error: HttpErrorResponse) => {
        alert(error.message);
      })
    }
  }

  openDialog(templateRef, student) {
    this.student = student;
    let dialogRef = this.matDialog.open(templateRef, {
     width: '300px'
   });
  }

  saveStudent(): void{
    const data = {
      firstName: this.student.firstName,
      lastName: this.student.lastName,
      email: this.student.email,
      phoneNumber: this.student.phoneNumber
    };

    this.studentService.create(data).subscribe({
      next: (res) => {
        console.log(res);
        //this.submitted = true;
      },
      error: (e) => console.error(e),
    });
    alert("New student added");
  }

  editStudent(){
    const data = {
      firstName: this.student.firstName,
      lastName: this.student.lastName,
      email: this.student.email,
      phoneNumber: this.student.phoneNumber
    };
    this.studentService.update(this.student.id, data)
      .subscribe(
        {
          next: (res) => {
            console.log(res);
            alert("Student edited");
            this.getAllStudents();
            this.matDialog.closeAll();
          } ,
          error: e => console.error(e)
        }
      );
  }

  getByLastName(): void{
    {
      this.studentService.getByLastName(this.lastName).subscribe((data: Student[]) =>{
        this.students = data;
        //this.sortStudentsByLastName();
        console.log(this.lastName);
      },
      (error: HttpErrorResponse) => {
        alert(error.message);
      })
    }
  }

  sortByName(){
    this.students.sort((a, b) =>
      this.toggle ?
        a.lastName.toLowerCase().localeCompare(b.lastName.toLowerCase()) :
        b.lastName.toLowerCase().localeCompare(a.lastName.toLowerCase()));
    this.toggle = !this.toggle;
  }

  deleteStudent(id: any): void{
    {
      this.studentService.delete(id).subscribe(
        (r) => {
          console.log(r);
        },
        err => {
          console.log(err);
        }
      );
      alert("Course with id " + id + " deleted");
      this.getAllStudents();
    }
  }

  deleteAllStudents(): void{
    this.studentService.deleteAll().subscribe(
      (r) => {
        console.log(r);
      },
      err => {
        console.log(err);
      }
      );
    alert("Students deleted");
    this.getAllStudents();
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
