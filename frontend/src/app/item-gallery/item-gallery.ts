import { Component, computed, inject, signal } from '@angular/core';
import { rxResource } from '@angular/core/rxjs-interop';
import {FormsModule} from '@angular/forms';
import { CurrencyPipe } from '@angular/common';
import { RouterLink } from '@angular/router';

import { AuthService } from '../service/auth-service';
import { ItemService } from '../service/item-service';
import { StockBadge } from '../stock-badge/stock-badge';

@Component({
  imports: [
    FormsModule,
    CurrencyPipe,
    RouterLink,
    StockBadge
  ],
  selector: 'app-item-gallery',
  styleUrl: './item-gallery.css',
  templateUrl: './item-gallery.html',
})
export class ItemGallery {
  private readonly itemService = inject(ItemService);
  protected readonly auth = inject(AuthService);
  protected searchText = signal<string | undefined>(undefined);

  protected readonly items = rxResource({
    stream: () => this.itemService.getItems(),
    defaultValue: [],
  });
  protected readonly filteredItems = computed(() => {
    const toFind = this.searchText()?.trim().toLowerCase();
    const items = this.items.value();

    if (!toFind)
      return items;

    return items.filter(item => item.name.toLowerCase().includes(toFind));
  });
}
