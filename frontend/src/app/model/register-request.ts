export interface RegisterRequest {
  fullName: { firstName: string; lastName: string };
  email: string;
  password: string;
  phoneNumber: string;
  address: { streetName: string; streetNumber: string; postalCode: string; city: string };
}
