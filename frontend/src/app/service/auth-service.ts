import { HttpClient } from '@angular/common/http';
import { computed, inject, Service, signal } from '@angular/core';
import { catchError, Observable, of, tap } from 'rxjs';
import { CurrentUser } from '../model/current-user';
import { RegisterRequest } from '../model/register-request';
import { clearAuthToken, getAuthToken, setAuthToken } from '../auth/auth-token';

@Service()
export class AuthService {
  private readonly http = inject(HttpClient);
  readonly currentUser = signal<CurrentUser | null>(null);
  readonly isLoggedIn = computed(() => this.currentUser() !== null);
  readonly isAdmin = computed(() => this.hasPermission('ADD_ITEM'));

  constructor() {
    const token = getAuthToken();
    if (token) {
      this.fetchMe(token).subscribe({
        next: user => this.currentUser.set(user),
        error: () => this.logout(),
      });
    }
  }

  ensureUser(): Observable<CurrentUser | null> {
    const user = this.currentUser();
    if (user) {
      return of(user);
    }
    const token = getAuthToken();
    if (!token) {
      return of(null);
    }
    return this.fetchMe(token).pipe(
      tap(user => this.currentUser.set(user)),
      catchError(() => {
        this.logout();
        return of(null);
      }),
    );
  }

  hasPermission(permission: string): boolean {
    return this.currentUser()?.permissions.includes(permission) ?? false;
  }

  login(email: string, password: string): Observable<CurrentUser> {
    const token = toBase64(`${email.trim()}:${password}`);
    return this.fetchMe(token).pipe(
      tap(user => {
        setAuthToken(token);
        this.currentUser.set(user);
      }),
    );
  }

  // The email is the Basic login: after the customer changed it, the next requests must
  // use the new email (same password), otherwise the backend answers 401.
  changeEmail(newEmail: string): void {
    const token = getAuthToken();
    const user = this.currentUser();
    if (!token || !user) {
      return;
    }
    const credentials = fromBase64(token);
    const password = credentials.slice(credentials.indexOf(':') + 1);
    setAuthToken(toBase64(`${newEmail}:${password}`));
    this.currentUser.set({ ...user, email: newEmail });
  }

  register(request: RegisterRequest): Observable<unknown> {
    return this.http.post('/api/customers', request);
  }

  logout(): void {
    clearAuthToken();
    this.currentUser.set(null);
  }

  private fetchMe(token: string): Observable<CurrentUser> {
    return this.http.get<CurrentUser>('/api/auth/me', {
      headers: { Authorization: `Basic ${token}` },
    });
  }
}

function toBase64(value: string): string {
  return btoa(String.fromCharCode(...new TextEncoder().encode(value)));
}

function fromBase64(value: string): string {
  return new TextDecoder().decode(Uint8Array.from(atob(value), char => char.charCodeAt(0)));
}
