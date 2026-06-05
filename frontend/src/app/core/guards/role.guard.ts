import { inject } from '@angular/core';
import { ActivatedRouteSnapshot, CanActivateFn, Router, UrlTree } from '@angular/router';
import { AuthStateService } from '../auth/auth-state.service';
import { Role } from '../../shared/models/role';

export const roleGuard: CanActivateFn = (
  route: ActivatedRouteSnapshot
): boolean | UrlTree => {
  const authState = inject(AuthStateService);
  const router = inject(Router);
  const expectedRoles = (route.data['roles'] as Role[] | undefined) ?? [];

  if (!authState.isAuthenticated) {
    return router.createUrlTree(['/login']);
  }

  if (!expectedRoles.length || (authState.user && expectedRoles.includes(authState.user.role))) {
    return true;
  }

  return router.createUrlTree(['/dashboard']);
};
