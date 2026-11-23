import { Component, inject, signal } from '@angular/core';
import { HttpErrorResponse } from '@angular/common/http';
import { NonNullableFormBuilder, ReactiveFormsModule, Validators } from '@angular/forms';
import { RouterLink } from '@angular/router';

import { AuthService } from '../service/auth-service';
import { messagesFrom } from '../common/http-errors';
import { Customer } from '../model/customer';
import { CustomerService } from '../service/customer-service';

@Component({
  imports: [ReactiveFormsModule, RouterLink],
  selector: 'app-profile',
  styleUrls: ['../auth/auth-form.css', './edit-profile.css'],
  templateUrl: './edit-profile.html',
})
export class EditProfile {
  private readonly customerService = inject(CustomerService);
  private readonly auth = inject(AuthService);
  private readonly fb = inject(NonNullableFormBuilder);

  // Same structure and rules as UpdateCustomerRequest on the backend
  protected readonly form = this.fb.group({
    fullName: this.fb.group({
      firstName: ['', [Validators.required, Validators.maxLength(255)]],
      lastName: ['', [Validators.required, Validators.maxLength(255)]],
    }),
    email: ['', [Validators.required, Validators.email, Validators.maxLength(255)]],
    phoneNumber: ['', [Validators.required, Validators.maxLength(50)]],
    address: this.fb.group({
      streetName: ['', [Validators.required, Validators.maxLength(255)]],
      streetNumber: ['', [Validators.required, Validators.maxLength(20)]],
      postalCode: ['', [Validators.required, Validators.maxLength(20)]],
      city: ['', [Validators.required, Validators.maxLength(255)]],
    }),
  });

  protected readonly loading = signal(true);
  protected readonly saving = signal(false);
  protected readonly saved = signal(false);
  protected readonly errors = signal<string[]>([]);

  constructor() {
    // Prefills the form with GET /customers/me
    this.customerService.getMyProfile().subscribe({
      next: customer => {
        this.fill(customer);
        this.loading.set(false);
      },
      error: (e: HttpErrorResponse) => {
        this.loading.set(false);
        this.errors.set(messagesFrom(e));
      },
    });
  }

  protected save(): void {
    if (this.form.invalid) {
      this.form.markAllAsTouched();
      return;
    }
    this.saving.set(true);
    this.saved.set(false);
    this.errors.set([]);

    this.customerService.updateMyProfile(this.form.getRawValue()).subscribe({
      next: customer => {
        // The email is the login: keep the stored credentials in line with it
        this.auth.changeEmail(customer.email);
        this.fill(customer);
        this.saving.set(false);
        this.saved.set(true);
      },
      error: (e: HttpErrorResponse) => {
        this.saving.set(false);
        this.errors.set(messagesFrom(e));
      },
    });
  }

  private fill(customer: Customer): void {
    this.form.reset({
      fullName: customer.fullName,
      email: customer.email,
      phoneNumber: customer.phone,
      address: customer.address,
    });
  }
}
