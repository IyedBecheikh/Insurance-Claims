import { TestBed } from '@angular/core/testing';
import { Router } from '@angular/router';
import { roleGuard } from './role.guard';
import { AuthStateService } from '../auth/auth-state.service';

describe('roleGuard', () => {
  let authState: AuthStateService;
  let router: jasmine.SpyObj<Router>;

  beforeEach(() => {
    router = jasmine.createSpyObj<Router>('Router', ['createUrlTree']);
    router.createUrlTree.and.returnValue({} as never);

    TestBed.configureTestingModule({
      providers: [{ provide: Router, useValue: router }]
    });

    authState = TestBed.inject(AuthStateService);
    authState.clearSession();
  });

  it('redirects unauthenticated visitors to login', () => {
    const result = TestBed.runInInjectionContext(() =>
      roleGuard({ data: { roles: ['ADMIN'] } } as never, {} as never)
    );

    expect(router.createUrlTree).toHaveBeenCalledWith(['/login']);
    expect(result).toBe(router.createUrlTree.calls.mostRecent().returnValue);
  });

  it('allows authenticated users with the expected role', () => {
    authState.setSession({
      token: 'abc',
      expiresAt: Date.now() + 1000,
      user: {
        id: '1',
        email: 'admin@insurance.local',
        role: 'ADMIN'
      }
    });

    const result = TestBed.runInInjectionContext(() =>
      roleGuard({ data: { roles: ['ADMIN'] } } as never, {} as never)
    );

    expect(result).toBeTrue();
  });
});
