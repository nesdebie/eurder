// One line of POST /orders = EurderSingleItemRequest on the backend
export interface OrderLine {
  id: string;      // id of the item
  amount: number;  // quantity, at least 1
}

// Body of POST /orders = EurderItemsRequest
export interface OrderRequest {
  items: OrderLine[];
}

// Response of POST /orders = EurderDto
export interface Order {
  id: string;
  totalPrice: number;
  items: {
    itemId: string;
    itemName: string;
    unitPrice: number;
    amount: number;
    totalPrice: number;
    shippingDate: string;
  }[];
}
