export interface Item {
  id: string,
  name: string,
  description: string,
  price: number
  amount: number
}

export type StockLevel = 'STOCK_LOW' | 'STOCK_MEDIUM' | 'STOCK_HIGH';
