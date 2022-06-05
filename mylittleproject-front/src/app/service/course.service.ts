import { Student } from 'src/app/model/student.model';
import { Course } from './../model/course.model';
import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

const baseUrl = 'http://localhost:8080/api/courses';

@Injectable({
  providedIn: 'root'
})
export class CourseService {

  constructor(private http: HttpClient) { }

  getAll(): Observable<Course[]> {
    return this.http.get<Course[]>(`${baseUrl}`);
  }

  getByName(courseName: string): Observable<Course[]> {
    return this.http.get<Course[]>(`${baseUrl}?courseName=${courseName}`);
  }

  delete(id: number): Observable<Course> {
    return this.http.delete(`${baseUrl}/${id}`);
  }

  get(id: number): Observable<Course> {
    return this.http.get(`${baseUrl}/${id}`)
  }

  getCourseStudents(id: number): Observable<any> {
    return this.http.get(`${baseUrl}/${id}/students`);
  }

  create(data: any): Observable<any> {
    return this.http.post(baseUrl, data);
  }

  update(id: number, newCourse: any): Observable<Course> {
    return this.http.put(`${baseUrl}/${id}`, newCourse);
  }

  deleteAll(): Observable<any> {
    return this.http.delete(baseUrl);
  }
}
