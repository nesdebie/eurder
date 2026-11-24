import { Component, inject, input } from '@angular/core';
import { HttpErrorResponse } from '@angular/common/http';
import { RouterLink } from '@angular/router';
import { rxResource } from '@angular/core/rxjs-interop';

import { CustomerService } from '../service/customer-service';

@Component({
  imports: [RouterLink],
  selector: 'app-customer-detail',
  styleUrl: './customer-detail.css',
  templateUrl: './customer-detail.html',
})
export class CustomerDetail {
  private readonly customerService = inject(CustomerService);

  readonly id = input.required<string>();

  protected readonly customer = rxResource({
    params: () => this.id(),
    stream: ({ params: id }) => this.customerService.getCustomer(id),
  });

  protected notFound(): boolean {
    const error = this.customer.error() as HttpErrorResponse | undefined;
    return error?.status === 404 || error?.status === 400;
  }
}
