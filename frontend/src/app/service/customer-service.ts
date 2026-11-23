import { HttpClient } from '@angular/common/http';
import { inject, Service } from '@angular/core';
import { map, Observable } from 'rxjs';
import { Customer, UpdateCustomerRequest } from '../model/customer';

@Service()
export class CustomerService {
  private readonly http = inject(HttpClient);
  private readonly customersUrl = '/api/customers';
  private readonly meUrl = `${this.customersUrl}/me`;

  getAllCustomers(): Observable<Customer[]> {
    return this.http.get<Customer[]>(this.customersUrl).pipe(
      map(customers => customers.sort((a, b) =>
        a.fullName.lastName.localeCompare(b.fullName.lastName)
        || a.fullName.firstName.localeCompare(b.fullName.firstName))),
    );
  }

  getCustomer(id: string): Observable<Customer> {
    return this.http.get<Customer>(`${this.customersUrl}/${id}`);
  }

  getMyProfile(): Observable<Customer> {
    return this.http.get<Customer>(this.meUrl);
  }

  updateMyProfile(request: UpdateCustomerRequest): Observable<Customer> {
    return this.http.put<Customer>(this.meUrl, request);
  }
}
