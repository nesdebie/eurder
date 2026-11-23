import { Service, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { map, Observable } from 'rxjs';
import { Item, StockLevel } from '../model/item';
import { ItemRequest } from '../model/item-request';

@Service()
export class ItemService {
  private readonly http = inject(HttpClient);
  private readonly itemsUrl = `/api/items`;

  getItems(): Observable<Item[]> {
    return this.http.get<Item[]>(this.itemsUrl)
      .pipe(map(items => items.sort((a, b) => a.name.localeCompare(b.name))));
  }

  getInventory(urgency?: StockLevel): Observable<Item[]> {
    const params: Record<string, string> = urgency ? { urgency } : {};
    return this.http.get<Item[]>(`${this.itemsUrl}/inventory`, { params });
  }

  getItem(id: string, includeOutOfStock = false): Observable<Item | undefined> {
    const items = includeOutOfStock ? this.getInventory() : this.getItems();
    return items.pipe(map(items => items.find(item => item.id === id)));
  }

  createItem(request: ItemRequest): Observable<Item> {
    return this.http.post<Item>(this.itemsUrl, request);
  }

  updateItem(id: string, request: ItemRequest): Observable<Item> {
    return this.http.put<Item>(`${this.itemsUrl}/${id}/update`, request);
  }
}
