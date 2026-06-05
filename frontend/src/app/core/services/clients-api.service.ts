import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Client, SaveClientRequest } from '../../shared/models/client.model';
import { EnvironmentService } from './environment.service';

@Injectable({
  providedIn: 'root'
})
export class ClientsApiService {
  constructor(
    private readonly http: HttpClient,
    private readonly environment: EnvironmentService
  ) {}

  findAll(): Observable<Client[]> {
    return this.http.get<Client[]>(`${this.environment.apiBaseUrl}/clients`);
  }

  findById(id: string): Observable<Client> {
    return this.http.get<Client>(`${this.environment.apiBaseUrl}/clients/${id}`);
  }

  create(request: SaveClientRequest): Observable<Client> {
    return this.http.post<Client>(`${this.environment.apiBaseUrl}/clients`, request);
  }

  update(id: string, request: SaveClientRequest): Observable<Client> {
    return this.http.put<Client>(`${this.environment.apiBaseUrl}/clients/${id}`, request);
  }
}
