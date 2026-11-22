import { HttpInterceptorFn } from '@angular/common/http';
import { getAuthToken } from './auth-token';

export const authInterceptor: HttpInterceptorFn = (request, next) => {
  const token = getAuthToken();

  let headers = request.headers.set('X-Requested-With', 'XMLHttpRequest');

  if (token && !headers.has('Authorization')) {
    headers = headers.set('Authorization', `Basic ${token}`);
  }
  return next(request.clone({ headers }));
};
