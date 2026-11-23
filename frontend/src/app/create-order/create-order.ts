import { Component, computed, inject, signal } from '@angular/core';
import { CurrencyPipe, DatePipe } from '@angular/common';
import { HttpErrorResponse } from '@angular/common/http';
import { RouterLink } from '@angular/router';
import { rxResource } from '@angular/core/rxjs-interop';

import { OrderService } from '../service/order-service';
import { ItemService } from '../service/item-service';
import { messagesFrom } from '../common/http-errors';
import { Order } from '../model/order';

@Component({
  imports: [CurrencyPipe, DatePipe, RouterLink],
  selector: 'app-order',
  styleUrl: './create-order.css',
  templateUrl: './create-order.html',
})
export class CreateOrder {
  protected readonly order = inject(OrderService);
  private readonly itemService = inject(ItemService);

  private readonly items = rxResource({
    stream: () => this.itemService.getItems(),
    defaultValue: [],
  });

  protected readonly rows = computed(() => {
    const names = new Map(this.items.value().map(item => [item.id, item.name]));
    return this.order.lines().map(line => ({
      ...line,
      name: names.get(line.id) ?? (this.items.isLoading() ? 'Loading…' : 'Unknown item'),
    }));
  });

  protected readonly ordering = signal(false);
  protected readonly placedOrder = signal<Order | null>(null);
  protected readonly errors = signal<string[]>([]);

  protected remove(id: string): void {
    this.order.remove(id);
  }

  protected placeOrder(): void {
    this.ordering.set(true);
    this.errors.set([]);
    this.order.placeOrder().subscribe({
      next: order => {
        this.ordering.set(false);
        this.placedOrder.set(order);
      },
      error: (e: HttpErrorResponse) => {
        this.ordering.set(false);
        this.errors.set(messagesFrom(e));
      },
    });
  }
}
