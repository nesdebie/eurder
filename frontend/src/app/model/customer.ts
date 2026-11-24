export interface Customer {
  id: string;
  fullName: { firstName: string; lastName: string };
  email: string;
  phone: string;
  address: { streetName: string; streetNumber: string; postalCode: string; city: string };
}

export interface UpdateCustomerRequest {
  fullName: { firstName: string; lastName: string };
  email: string;
  phoneNumber: string;
  address: { streetName: string; streetNumber: string; postalCode: string; city: string };
}
