import { UtilityService } from './../../service/utility.service';
import { MatDialog, MatDialogModule } from '@angular/material/dialog';
import { TeacherService } from './../../service/teacher.service';
import { Component, OnInit } from '@angular/core';
import { Teacher } from 'src/app/model/teacher.model';
import { HttpErrorResponse } from '@angular/common/http';

@Component({
  selector: 'app-teachers-list',
  templateUrl: './teachers-list.component.html',
  styleUrls: ['./teachers-list.component.scss']
})
export class TeachersListComponent implements OnInit {
  p: number = 1;

  teachers: Teacher[];
  lastName: '';
  toggle = true;
  teacher: Teacher = {
    firstName: '',
    lastName: '',
    email: '',
    phoneNumber: ''
  }

  constructor(private teacherService: TeacherService,
              private matDialog: MatDialog,
              private utilityService: UtilityService) { }

  ngOnInit(): void {
    this.getAllTeachers();
  }

  saveTeacher(): void{
    const data = {
      firstName: this.teacher.firstName,
      lastName: this.teacher.lastName,
      email: this.teacher.email,
      phoneNumber: +this.teacher.phoneNumber
    };

    this.teacherService.create(data).subscribe({
      next: (res) => {
        console.log(res);
      },
      error: (e) => console.error(e),
    });
    this.matDialog.closeAll();
    alert("New teacher added");
    this.getAllTeachers();
  }

  openDialog(templateRef, teacher) {
    this.teacher = teacher;
    let dialogRef = this.matDialog.open(templateRef, {
     width: '300px'
   });
  }

  editTeacher(){
    const data = {
      firstName: this.teacher.firstName,
      lastName: this.teacher.lastName,
      email: this.teacher.email,
      phoneNumber: +this.teacher.phoneNumber
    };
    console.log(this.teacher);
    this.teacherService.update(this.teacher.id, data)
      .subscribe(
        {
          next: (res) => {
            console.log(res);
            alert("Teacher edited");
            this.getAllTeachers();
            this.matDialog.closeAll();
          } ,
          error: e => console.error(e)
        }
      );
  }

  getAllTeachers(): void{
    this.teacherService.getAll().subscribe((data: Teacher[]) =>{
      this.teachers = data;
    },
    (error: HttpErrorResponse) => {
      alert(error.message);
    })
  }

  deleteAllTeachers(): void{
    this.teacherService.deleteAll().subscribe(
      (r) => {
        console.log(r);
      },
      err => {
        console.log(err);
      }
      );
      alert("All teachers deleted");
    this.getAllTeachers();
  }

  deleteTeacher(id: any): void{
    if(confirm("Do you want to delete teacher " + id)){
    this.teacherService.delete(id).subscribe(
      (r) => {
        console.log(r);
      },
      err => {
        console.log(err);
      }
      );
      alert("Teacher deleted");
      this.getAllTeachers();
    }
  }

  getByLastName(): void{
    {
      this.teacherService.getByLastName(this.lastName).subscribe((data: Teacher[]) =>{
        this.teachers = data;
        //this.sortStudentsByLastName();
        console.log(this.lastName);
      },
      (error: HttpErrorResponse) => {
        alert(error.message);
      })
    }
  }

  sortByName(){
    this.teachers.sort((a, b) =>
      this.toggle ?
        a.lastName.toLowerCase().localeCompare(b.lastName.toLowerCase()) :
        b.lastName.toLowerCase().localeCompare(a.lastName.toLowerCase()));
    this.toggle = !this.toggle;
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
