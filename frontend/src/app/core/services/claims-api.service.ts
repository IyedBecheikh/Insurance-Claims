import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';
import {
  AddClaimDocumentRequest,
  Claim,
  ClaimStatus,
  CreateClaimRequest
} from '../../shared/models/claim.model';
import { EnvironmentService } from './environment.service';

@Injectable({
  providedIn: 'root'
})
export class ClaimsApiService {
  constructor(
    private readonly http: HttpClient,
    private readonly environment: EnvironmentService
  ) {}

  findOwnClaims(): Observable<Claim[]> {
    return this.http.get<Claim[]>(`${this.environment.apiBaseUrl}/claims/my`);
  }

  findOwnClaim(id: string): Observable<Claim> {
    return this.http.get<Claim>(`${this.environment.apiBaseUrl}/claims/my/${id}`);
  }

  create(request: CreateClaimRequest): Observable<Claim> {
    return this.http.post<Claim>(`${this.environment.apiBaseUrl}/claims`, request);
  }

  addDocument(id: string, request: AddClaimDocumentRequest): Observable<Claim> {
    return this.http.post<Claim>(`${this.environment.apiBaseUrl}/claims/${id}/documents`, request);
  }

  submit(id: string): Observable<Claim> {
    return this.http.post<Claim>(`${this.environment.apiBaseUrl}/claims/${id}/submit`, {});
  }

  findReviewClaims(filters: {
    status?: ClaimStatus | '';
    clientId?: string;
    claimNumber?: string;
  }): Observable<Claim[]> {
    let params = new HttpParams();

    if (filters.status) {
      params = params.set('status', filters.status);
    }

    if (filters.clientId) {
      params = params.set('clientId', filters.clientId);
    }

    if (filters.claimNumber) {
      params = params.set('claimNumber', filters.claimNumber);
    }

    return this.http.get<Claim[]>(`${this.environment.apiBaseUrl}/claims`, { params });
  }

  findReviewClaim(id: string): Observable<Claim> {
    return this.http.get<Claim>(`${this.environment.apiBaseUrl}/claims/${id}`);
  }

  startReview(id: string): Observable<Claim> {
    return this.http.patch<Claim>(`${this.environment.apiBaseUrl}/claims/${id}/start-review`, {});
  }

  approve(id: string): Observable<Claim> {
    return this.http.patch<Claim>(`${this.environment.apiBaseUrl}/claims/${id}/approve`, {});
  }

  reject(id: string): Observable<Claim> {
    return this.http.patch<Claim>(`${this.environment.apiBaseUrl}/claims/${id}/reject`, {});
  }

  pay(id: string): Observable<Claim> {
    return this.http.patch<Claim>(`${this.environment.apiBaseUrl}/claims/${id}/pay`, {});
  }
}
