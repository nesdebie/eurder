import { inject } from '@angular/core';
import { map } from 'rxjs';
import { CanActivateFn, Router } from '@angular/router';
import { AuthService } from '../service/auth-service';
import { getAuthToken } from './auth-token';

export const authGuard: CanActivateFn = (_route, state) => {
  if (getAuthToken()) {
    return true;
  }
  return inject(Router).createUrlTree(['/login'], { queryParams: { returnUrl: state.url } });
};


export function permissionGuard(permission: string): CanActivateFn {
  return (_route, state) => {
    const router = inject(Router);
    return inject(AuthService).ensureUser().pipe(
      map(user => {
        if (!user) {
          return router.createUrlTree(['/login'], { queryParams: { returnUrl: state.url } });
        }
        return user.permissions.includes(permission) ? true : router.createUrlTree(['/']);
      }),
    );
  };
}
