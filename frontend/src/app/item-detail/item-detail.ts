import { Component, inject, input, signal } from '@angular/core';
import { CurrencyPipe } from '@angular/common';
import { HttpErrorResponse } from '@angular/common/http';
import { AbstractControl, NonNullableFormBuilder, ReactiveFormsModule, ValidationErrors, Validators } from '@angular/forms';
import { RouterLink } from '@angular/router';
import { rxResource } from '@angular/core/rxjs-interop';

import { AuthService } from '../auth/auth-service';
import { CartService } from '../service/cart-service';
import { messagesFrom } from '../common/http-errors';
import { Item } from '../model/item';
import { ItemService } from '../service/item-service';
import { StockBadge } from '../stock-badge/stock-badge';

@Component({
  imports: [CurrencyPipe, ReactiveFormsModule, RouterLink, StockBadge],
  selector: 'app-item-detail',
  styleUrl: './item-detail.css',
  templateUrl: './item-detail.html',
})
export class ItemDetail {
  private readonly itemService = inject(ItemService);
  private readonly cart = inject(CartService);
  private readonly fb = inject(NonNullableFormBuilder);
  protected readonly auth = inject(AuthService);

  readonly id = input.required<string>();

  protected readonly item = rxResource({
    params: () => this.id(),
    stream: ({ params: id }) => this.itemService.getItem(id),
  });

  protected readonly cartForm = this.fb.group({
    amount: [1, [Validators.required, Validators.min(1), integer]],
  });
  protected readonly addedMessage = signal<string | null>(null);

  protected addToCart(item: Item): void {
    if (this.cartForm.invalid) {
      this.cartForm.markAllAsTouched();
      return;
    }
    const { amount } = this.cartForm.getRawValue();
    this.cart.add(item.id, amount);
    this.addedMessage.set(`${amount} × ${item.name} added to your cart.`);
    this.cartForm.reset({ amount: 1 });
  }

  protected readonly editing = signal(false);
  protected readonly saving = signal(false);
  protected readonly editErrors = signal<string[]>([]);

  protected readonly editForm = this.fb.group({
    name: ['', [Validators.required, Validators.maxLength(50)]],
    description: ['', [Validators.required, Validators.maxLength(255)]],
    price: [0, [Validators.required, Validators.min(0.01)]],
    amount: [1, [Validators.required, Validators.min(1), integer]],
  });

  protected startEditing(item: Item): void {
    this.editForm.reset({
      name: item.name,
      description: item.description,
      price: item.price,
      amount: Math.max(item.amount, 1),
    });
    this.editErrors.set([]);
    this.editing.set(true);
  }

  protected cancelEditing(): void {
    this.editing.set(false);
  }

  protected save(item: Item): void {
    if (this.editForm.invalid) {
      this.editForm.markAllAsTouched();
      return;
    }
    this.saving.set(true);
    this.editErrors.set([]);

    this.itemService.updateItem(item.id, this.editForm.getRawValue()).subscribe({
      next: updated => {
        this.item.set(updated);   // shows the new values without reloading
        this.saving.set(false);
        this.editing.set(false);
      },
      error: (e: HttpErrorResponse) => {
        this.saving.set(false);
        this.editErrors.set(messagesFrom(e));
      },
    });
  }
}

// Refuses 1.5, accepts 1, 2, 3...
function integer(control: AbstractControl): ValidationErrors | null {
  return Number.isInteger(Number(control.value)) ? null : { integer: true };
}
