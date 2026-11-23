import { HttpClient } from '@angular/common/http';
import { inject, Service } from '@angular/core';
import { Observable } from 'rxjs';
import { Customer, UpdateCustomerRequest } from '../model/customer';

// The signed-in customer's own profile ("/me": never someone else's)
@Service()
export class CustomerService {
  private readonly http = inject(HttpClient);
  private readonly meUrl = '/api/customers/me';

  getMyProfile(): Observable<Customer> {
    return this.http.get<Customer>(this.meUrl);
  }

  updateMyProfile(request: UpdateCustomerRequest): Observable<Customer> {
    return this.http.put<Customer>(this.meUrl, request);
  }
}
