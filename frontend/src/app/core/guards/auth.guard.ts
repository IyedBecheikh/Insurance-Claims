import { inject } from '@angular/core';
import { CanActivateFn, Router, UrlTree } from '@angular/router';
import { AuthStateService } from '../auth/auth-state.service';

export const authGuard: CanActivateFn = (): boolean | UrlTree => {
  const authState = inject(AuthStateService);
  const router = inject(Router);

  return authState.isAuthenticated ? true : router.createUrlTree(['/login']);
};
