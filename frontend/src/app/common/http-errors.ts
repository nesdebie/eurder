import { HttpErrorResponse } from '@angular/common/http';

export function messagesFrom(e: HttpErrorResponse): string[] {
  if (e.status === 400 && e.error?.errors) {
    return Object.values(e.error.errors as Record<string, string[]>).flat();
  }
  if (e.status === 401) {
    return ['Your session has expired, please sign in again.'];
  }
  if (e.error?.detail) {
    return [e.error.detail];
  }
  return ['Could not reach the server. Is the backend running?'];
}
