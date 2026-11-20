import { Service, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import {environment} from '../../environments/environment'
import {map, Observable} from 'rxjs';
import { Item } from '../model/item'

@Service()
export class ItemService {
  private readonly http = inject(HttpClient);
  private readonly petsUrl = `${environment.backendUrl}/items`;

  getItems(): Observable<Item[]> {
    return this.http.get<Item[]>(this.itemsUrl)
      .pipe(map(items => items.sort((a, b) => a.name.localeCompare(b.name))));
  }
}
