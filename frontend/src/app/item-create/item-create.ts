import { Component, inject, signal } from '@angular/core';
import { HttpErrorResponse } from '@angular/common/http';
import { AbstractControl, NonNullableFormBuilder, ReactiveFormsModule, ValidationErrors, Validators } from '@angular/forms';
import { Router, RouterLink } from '@angular/router';

import { messagesFrom } from '../common/http-errors';
import { ItemService } from '../service/item-service';

@Component({
  imports: [ReactiveFormsModule, RouterLink],
  selector: 'app-item-create',
  styleUrl: './item-create.css',
  templateUrl: './item-create.html',
})
export class ItemCreate {
  private readonly itemService = inject(ItemService);
  private readonly router = inject(Router);

  // Same rules as AddOrUpdateItemRequest on the backend
  protected readonly form = inject(NonNullableFormBuilder).group({
    name: ['', [Validators.required, Validators.maxLength(50)]],
    description: ['', [Validators.required, Validators.maxLength(255)]],
    price: [null as number | null, [Validators.required, Validators.min(0.01)]],
    amount: [1, [Validators.required, Validators.min(1), integer]],
  });
  protected readonly saving = signal(false);
  protected readonly errors = signal<string[]>([]);

  protected create(): void {
    if (this.form.invalid) {
      this.form.markAllAsTouched();
      return;
    }
    this.saving.set(true);
    this.errors.set([]);

    const { name, description, price, amount } = this.form.getRawValue();
    this.itemService.createItem({ name, description, price: price!, amount }).subscribe({
      // Created: open the page of the new item
      next: item => this.router.navigate(['/items', item.id]),
      error: (e: HttpErrorResponse) => {
        this.saving.set(false);
        this.errors.set(messagesFrom(e));
      },
    });
  }
}

// Refuses 1.5, accepts 1, 2, 3...
function integer(control: AbstractControl): ValidationErrors | null {
  return Number.isInteger(Number(control.value)) ? null : { integer: true };
}
