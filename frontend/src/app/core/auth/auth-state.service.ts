import { Injectable } from '@angular/core';
import { BehaviorSubject } from 'rxjs';
import { AuthenticatedUser, AuthSession } from '../../shared/models/auth.models';
import { Role } from '../../shared/models/role';

const SESSION_KEY = 'insurance-claims.session';

@Injectable({
  providedIn: 'root'
})
export class AuthStateService {
  private readonly sessionSubject = new BehaviorSubject<AuthSession | null>(this.readSession());

  readonly session$ = this.sessionSubject.asObservable();

  get session(): AuthSession | null {
    return this.sessionSubject.value;
  }

  get isAuthenticated(): boolean {
    return !!this.session?.token;
  }

  get token(): string | null {
    return this.session?.token ?? null;
  }

  get user(): AuthenticatedUser | null {
    return this.session?.user ?? null;
  }

  setSession(session: AuthSession): void {
    localStorage.setItem(SESSION_KEY, JSON.stringify(session));
    this.sessionSubject.next(session);
  }

  clearSession(): void {
    localStorage.removeItem(SESSION_KEY);
    this.sessionSubject.next(null);
  }

  hasRole(role: Role): boolean {
    return this.user?.role === role;
  }

  private readSession(): AuthSession | null {
    const raw = localStorage.getItem(SESSION_KEY);

    if (!raw) {
      return null;
    }

    try {
      return JSON.parse(raw) as AuthSession;
    } catch {
      localStorage.removeItem(SESSION_KEY);
      return null;
    }
  }
}
