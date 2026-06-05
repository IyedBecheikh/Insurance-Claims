import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Contract, SaveContractRequest } from '../../shared/models/contract.model';
import { EnvironmentService } from './environment.service';

@Injectable({
  providedIn: 'root'
})
export class ContractsApiService {
  constructor(
    private readonly http: HttpClient,
    private readonly environment: EnvironmentService
  ) {}

  findAll(): Observable<Contract[]> {
    return this.http.get<Contract[]>(`${this.environment.apiBaseUrl}/contracts`);
  }

  findOwn(): Observable<Contract[]> {
    return this.http.get<Contract[]>(`${this.environment.apiBaseUrl}/contracts/my`);
  }

  findById(id: string): Observable<Contract> {
    return this.http.get<Contract>(`${this.environment.apiBaseUrl}/contracts/${id}`);
  }

  create(request: SaveContractRequest): Observable<Contract> {
    return this.http.post<Contract>(`${this.environment.apiBaseUrl}/contracts`, request);
  }

  update(id: string, request: SaveContractRequest): Observable<Contract> {
    return this.http.put<Contract>(`${this.environment.apiBaseUrl}/contracts/${id}`, request);
  }

  activate(id: string): Observable<Contract> {
    return this.http.patch<Contract>(`${this.environment.apiBaseUrl}/contracts/${id}/activate`, null);
  }

  suspend(id: string): Observable<Contract> {
    return this.http.patch<Contract>(`${this.environment.apiBaseUrl}/contracts/${id}/suspend`, null);
  }
}
