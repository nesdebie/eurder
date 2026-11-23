import { HttpClient } from '@angular/common/http';
import { inject, Service } from '@angular/core';
import { map, Observable } from 'rxjs';
import { Customer, UpdateCustomerRequest } from '../model/customer';

@Service()
export class CustomerService {
  private readonly http = inject(HttpClient);
  private readonly customersUrl = '/api/customers';
  private readonly meUrl = `${this.customersUrl}/me`;

  // Admin only (VIEW_CUSTOMERS): every customer, sorted by last name then first name
  getAllCustomers(): Observable<Customer[]> {
    return this.http.get<Customer[]>(this.customersUrl).pipe(
      map(customers => customers.sort((a, b) =>
        a.fullName.lastName.localeCompare(b.fullName.lastName)
        || a.fullName.firstName.localeCompare(b.fullName.firstName))),
    );
  }

  // The signed-in customer's own profile ("/me": never someone else's)
  getMyProfile(): Observable<Customer> {
    return this.http.get<Customer>(this.meUrl);
  }

  updateMyProfile(request: UpdateCustomerRequest): Observable<Customer> {
    return this.http.put<Customer>(this.meUrl, request);
  }
}
