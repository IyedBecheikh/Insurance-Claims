import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';
import { CreateUserRequest, User } from '../../shared/models/user.model';
import { EnvironmentService } from './environment.service';

@Injectable({
  providedIn: 'root'
})
export class UsersApiService {
  constructor(
    private readonly http: HttpClient,
    private readonly environment: EnvironmentService
  ) {}

  findAll(): Observable<User[]> {
    return this.http.get<User[]>(`${this.environment.apiBaseUrl}/users`);
  }

  findById(id: string): Observable<User> {
    return this.http.get<User>(`${this.environment.apiBaseUrl}/users/${id}`);
  }

  create(request: CreateUserRequest): Observable<User> {
    return this.http.post<User>(`${this.environment.apiBaseUrl}/users`, request);
  }

  updateEnabled(id: string, enabled: boolean): Observable<User> {
    const params = new HttpParams().set('enabled', String(enabled));
    return this.http.patch<User>(`${this.environment.apiBaseUrl}/users/${id}/enabled`, null, { params });
  }
}
