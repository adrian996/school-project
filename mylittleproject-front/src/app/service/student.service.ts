import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { Student } from '../model/student.model';

const baseUrl = 'http://localhost:8080/api/students';

@Injectable({
  providedIn: 'root'
})
export class StudentService {

  constructor(private http: HttpClient) { }

  getAll(): Observable<Student[]> {
    return this.http.get<Student[]>(baseUrl);
  }

  get(id: any): Observable<Student> {
    return this.http.get(`${baseUrl}/${id}`)
  }

  getByLastName(lastName: string): Observable<Student[]> {
    return this.http.get<Student[]>(`${baseUrl}?studentName=${lastName}`);
  }

  getByEmail(email: string): Observable<Student> {
    return this.http.get<Student>(`${baseUrl}/findstudent?email=${email}`);
  }

  getStudentCourses(id: number): Observable<any> {
    return this.http.get(`${baseUrl}/${id}/courses`);
  }

  addCourseToStudent(studentId: number, courseId: number): Observable<any>{
    return this.http.put(`${baseUrl}/${studentId}/${courseId}`, null);
  }

  create(data: any): Observable<any> {
    return this.http.post(baseUrl, data);
  }

  update(id: any, newStudent: any): Observable<Student> {
    return this.http.put(`${baseUrl}/${id}`, newStudent);
  }

  delete(id: number): Observable<Student> {
    return this.http.delete(`${baseUrl}/${id}`);
  }

  deleteCourse(studentId: number, courseId: number): Observable<Student> {
    return this.http.delete(`${baseUrl}/${studentId}/${courseId}`);
  }

  deleteAll(): Observable<any> {
    return this.http.delete(baseUrl);
  }


}
