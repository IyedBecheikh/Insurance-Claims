import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { map, Observable, switchMap } from 'rxjs';
import {
  AuthenticatedUser,
  AuthSession,
  LoginRequest,
  LoginResponse
} from '../../shared/models/auth.models';
import { EnvironmentService } from '../services/environment.service';
import { AuthStateService } from './auth-state.service';

@Injectable({
  providedIn: 'root'
})
export class AuthApiService {
  constructor(
    private readonly http: HttpClient,
    private readonly environment: EnvironmentService,
    private readonly authState: AuthStateService
  ) {}

  login(request: LoginRequest): Observable<AuthSession> {
    return this.http
      .post<LoginResponse>(`${this.environment.apiBaseUrl}/auth/login`, request)
      .pipe(
        switchMap((response) =>
          this.http.get<AuthenticatedUser>(`${this.environment.apiBaseUrl}/auth/me`, {
            headers: {
              Authorization: `Bearer ${response.accessToken}`
            }
          }).pipe(
            map((user) => ({
              token: response.accessToken,
              user,
              expiresAt: Date.now() + response.expiresIn * 1000
            }))
          )
        ),
        map((session) => {
          this.authState.setSession(session);
          return session;
        })
      );
  }
}
