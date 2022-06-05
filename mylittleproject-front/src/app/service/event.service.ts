import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs/internal/Observable';
import { Event } from '../model/event.model';

const baseUrl = 'http://localhost:8080/api/events';

@Injectable({
  providedIn: 'root'
})
export class EventService {

  constructor(private http: HttpClient) { }

  getByCourseId(id: number): Observable<any> {
    return this.http.get(`${baseUrl}/${id}`);
  }

  getByStudentId(id: number): Observable<any> {
    return this.http.get(`${baseUrl}/${id}/students`);
  }

  createEvent(event: Event): Observable<any> {
    return this.http.post(baseUrl, event);
  }

  createEventForCourse(courseId: number, event: Event): Observable<any> {
    return this.http.post(`${baseUrl}/${courseId}/`, event);
  }

  deleteEvent(eventId: number): Observable<any> {
    return this.http.delete(`${baseUrl}/${eventId}/`);
  }

}
