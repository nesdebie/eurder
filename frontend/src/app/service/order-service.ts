import { HttpClient } from '@angular/common/http';
import { computed, inject, Service, signal } from '@angular/core';
import { Observable, tap } from 'rxjs';
import { Order, OrderLine, OrdersReport } from '../model/order';

const CART_KEY = 'eurder-cart';

@Service()
export class OrderService {
  private readonly http = inject(HttpClient);

  readonly lines = signal<OrderLine[]>(readCart());
  readonly count = computed(() => this.lines().reduce((sum, line) => sum + line.amount, 0));

  add(id: string, amount: number): void {
    const lines = this.lines();
    const existing = lines.find(line => line.id === id);
    this.save(existing
      ? lines.map(line => line === existing ? { ...line, amount: line.amount + amount } : line)
      : [...lines, { id, amount }]);
  }

  remove(id: string): void {
    this.save(this.lines().filter(line => line.id !== id));
  }

  placeOrder(): Observable<Order> {
    return this.http.post<Order>('/api/orders', { items: this.lines() }).pipe(
      tap(() => this.clear()),
    );
  }

  getOrdersHistory(): Observable<OrdersReport> {
    return this.http.get<OrdersReport>('/api/orders/report');
  }

  clear(): void {
    this.save([]);
  }

  private save(lines: OrderLine[]): void {
    this.lines.set(lines);
    localStorage.setItem(CART_KEY, JSON.stringify(lines));
  }
}

function readCart(): OrderLine[] {
  try {
    return JSON.parse(localStorage.getItem(CART_KEY) ?? '[]');
  } catch {
    return [];
  }
}
