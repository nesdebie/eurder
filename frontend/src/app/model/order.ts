export interface OrderLine {
  id: string;
  amount: number;
}

export interface OrderRequest {
  items: OrderLine[];
}

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
