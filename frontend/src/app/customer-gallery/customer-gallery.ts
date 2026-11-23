import { Component, computed, inject, signal } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { rxResource } from '@angular/core/rxjs-interop';

import { CustomerService } from '../service/customer-service';

@Component({
  imports: [FormsModule],
  selector: 'app-customer-gallery',
  styleUrl: './customer-gallery.css',
  templateUrl: './customer-gallery.html',
})
export class CustomerGallery {
  private readonly customerService = inject(CustomerService);
  protected readonly searchText = signal<string | undefined>(undefined);

  protected readonly customers = rxResource({
    stream: () => this.customerService.getAllCustomers(),
    defaultValue: [],
  });

  protected readonly filteredCustomers = computed(() => {
    const toFind = this.searchText()?.trim().toLowerCase();
    const customers = this.customers.value();

    if (!toFind)
      return customers;

    return customers.filter(customer => customer.email.toLowerCase().includes(toFind));
  });

  protected initials(firstName: string, lastName: string): string {
    return (firstName.charAt(0) + lastName.charAt(0)).toUpperCase();
  }
}
