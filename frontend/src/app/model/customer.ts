// Response of GET/PUT /customers/me (= CustomerDto on the backend)
export interface Customer {
  fullName: { firstName: string; lastName: string };
  email: string;
  phone: string;
  address: { streetName: string; streetNumber: string; postalCode: string; city: string };
}

// Body of PUT /customers/me (= UpdateCustomerRequest): everything except id and password
export interface UpdateCustomerRequest {
  fullName: { firstName: string; lastName: string };
  email: string;
  phoneNumber: string;
  address: { streetName: string; streetNumber: string; postalCode: string; city: string };
}
