import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs/internal/Observable';
const baseUrl = 'http://localhost:8080/api/dates';

@Injectable({
  providedIn: 'root'
})

export class DateService {

  constructor(private http: HttpClient) { }

  getAll(): Observable<Date[]> {
    return this.http.get<Date[]>(`${baseUrl}`);
  }
}
