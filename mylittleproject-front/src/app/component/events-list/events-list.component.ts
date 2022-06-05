import { EventService } from './../../service/event.service';
import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpErrorResponse } from '@angular/common/http';

@Component({
  selector: 'app-events-list',
  templateUrl: './events-list.component.html',
  styleUrls: ['./events-list.component.scss']
})
export class EventsListComponent implements OnInit {

  events: Event[];
  courseId: number;
  selectedDay: 'MONDAY';

  constructor(private eventService: EventService, private route: ActivatedRoute) { }

  ngOnInit(): void {
    this.courseId = +this.route.snapshot.params.id;
    this.getByCourseId(this.courseId);
    //this.getByCourseId(this.studentId);
  }


  getByStudentId(studentId: number): void{
    {
      this.eventService.getByStudentId(studentId).subscribe((data: Event[]) =>{
        this.events = data;
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

  changeDay(day): void{
    this.selectedDay = day;
  }

}
