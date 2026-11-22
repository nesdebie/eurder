// Same fields and rules as AddOrUpdateItemRequest on the backend
export interface ItemRequest {
  name: string;
  description: string;
  price: number;
  amount: number;
}
