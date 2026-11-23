import { HttpErrorResponse } from '@angular/common/http';
import { Component, inject, signal } from '@angular/core';
import { NonNullableFormBuilder, ReactiveFormsModule, Validators } from '@angular/forms';
import { Router, RouterLink } from '@angular/router';
import { switchMap } from 'rxjs';
import { AuthService } from '../../service/auth-service';

@Component({
  imports: [ReactiveFormsModule, RouterLink],
  selector: 'app-register',
  styleUrl: '../auth-form.css',
  templateUrl: './register.html',
})
export class Register {
  private readonly auth = inject(AuthService);
  private readonly router = inject(Router);
  private readonly fb = inject(NonNullableFormBuilder);

  // Same structure and rules as CreateCustomerRequest on the backend
  protected readonly form = this.fb.group({
    fullName: this.fb.group({
      firstName: ['', Validators.required],
      lastName: ['', Validators.required],
    }),
    email: ['', [Validators.required, Validators.email]],
    password: ['', [Validators.required, Validators.minLength(8), Validators.maxLength(72)]],
    phoneNumber: ['', Validators.required],
    address: this.fb.group({
      streetName: ['', Validators.required],
      streetNumber: ['', Validators.required],
      postalCode: ['', Validators.required],
      city: ['', Validators.required],
    }),
  });
  protected readonly errors = signal<string[]>([]);
  protected readonly loading = signal(false);

  protected submit(): void {
    if (this.form.invalid) {
      this.form.markAllAsTouched();
      return;
    }
    this.loading.set(true);
    this.errors.set([]);

    const request = this.form.getRawValue();
    this.auth.register(request).pipe(
      // Registration succeeded: sign in straight away with the same credentials
      switchMap(() => this.auth.login(request.email, request.password)),
    ).subscribe({
      next: () => this.router.navigateByUrl('/'),
      error: (e: HttpErrorResponse) => {
        this.loading.set(false);
        this.errors.set(messagesFrom(e));
      },
    });
  }
}

// 400: { errors: { "email": ["Email must be valid"], ... } } — 409: { detail: "..." }
function messagesFrom(e: HttpErrorResponse): string[] {
  if (e.status === 400 && e.error?.errors) {
    return Object.values(e.error.errors as Record<string, string[]>).flat();
  }
  if (e.error?.detail) {
    return [e.error.detail];
  }
  return ['Could not reach the server. Is the backend running?'];
}
