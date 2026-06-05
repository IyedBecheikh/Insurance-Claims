import { TestBed } from '@angular/core/testing';
import { AuthStateService } from './auth-state.service';

describe('AuthStateService', () => {
  let service: AuthStateService;

  beforeEach(() => {
    localStorage.clear();
    TestBed.configureTestingModule({});
    service = TestBed.inject(AuthStateService);
  });

  it('persists and restores the current session', () => {
    const session = {
      token: 'token-123',
      expiresAt: Date.now() + 3600,
      user: {
        id: 'user-1',
        email: 'admin@insurance.local',
        role: 'ADMIN' as const
      }
    };

    service.setSession(session);

    const restored = new AuthStateService();

    expect(restored.isAuthenticated).toBeTrue();
    expect(restored.token).toBe('token-123');
    expect(restored.user?.role).toBe('ADMIN');
  });

  it('clears the session state and storage', () => {
    service.setSession({
      token: 'token-123',
      expiresAt: Date.now() + 3600,
      user: {
        id: 'user-1',
        email: 'client@insurance.local',
        role: 'CLIENT'
      }
    });

    service.clearSession();

    expect(service.isAuthenticated).toBeFalse();
    expect(localStorage.getItem('insurance-claims.session')).toBeNull();
  });
});
