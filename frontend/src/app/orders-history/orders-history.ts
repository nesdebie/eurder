import { Component, inject } from '@angular/core';
import { CurrencyPipe, DatePipe } from '@angular/common';
import { RouterLink } from '@angular/router';
import { rxResource } from '@angular/core/rxjs-interop';

import { OrderService } from '../service/order-service';

@Component({
  imports: [CurrencyPipe, DatePipe, RouterLink],
  selector: 'app-orders-history',
  styleUrl: './orders-history.css',
  templateUrl: './orders-history.html',
})
export class OrdersHistory {
  private readonly orderService = inject(OrderService);

  protected readonly report = rxResource({
    stream: () => this.orderService.getOrdersHistory(),
  });
}
