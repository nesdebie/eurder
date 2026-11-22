import { Component, inject, signal } from '@angular/core';
import { CurrencyPipe } from '@angular/common';
import { HttpErrorResponse } from '@angular/common/http';
import { Router, RouterLink } from '@angular/router';
import { AuthService } from '../../auth/auth-service';
import { CartService } from '../../service/cart-service';
import { messagesFrom } from '../../common/http-errors';
import { Order } from '../../model/order';

@Component({
  imports: [CurrencyPipe, RouterLink],
  selector: 'app-header',
  styleUrl: './header.css',
  templateUrl: './header.html',
})
export class Header {
  protected readonly auth = inject(AuthService);
  protected readonly cart = inject(CartService);
  private readonly router = inject(Router);

  protected readonly ordering = signal(false);
  protected readonly placedOrder = signal<Order | null>(null);
  protected readonly orderError = signal<string | null>(null);

  protected placeOrder(): void {
    this.ordering.set(true);
    this.orderError.set(null);
    this.cart.placeOrder().subscribe({
      next: order => {
        this.ordering.set(false);
        this.placedOrder.set(order);
      },
      error: (e: HttpErrorResponse) => {
        this.ordering.set(false);
        this.orderError.set(messagesFrom(e).join(' '));
      },
    });
  }

  protected dismiss(): void {
    this.placedOrder.set(null);
    this.orderError.set(null);
  }

  protected logout(): void {
    this.cart.clear();
    this.dismiss();
    this.auth.logout();
    this.router.navigateByUrl('/');
  }
}
