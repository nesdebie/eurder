import { Component, computed, inject, signal } from '@angular/core';
import { rxResource } from '@angular/core/rxjs-interop';
import { switchMap } from 'rxjs';
import {FormsModule} from '@angular/forms';
import { CurrencyPipe } from '@angular/common';
import { RouterLink } from '@angular/router';

import { AuthService } from '../service/auth-service';
import { ItemService } from '../service/item-service';
import { StockLevel } from '../model/item';
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

  protected readonly canViewInventory = computed(() => this.auth.hasPermission('VIEW_INVENTORY'));

  protected readonly stockFilter = signal<StockLevel | 'NONE'>('NONE');
  protected readonly stockFilters: { value: StockLevel | 'NONE'; label: string }[] = [
    { value: 'NONE', label: 'All' },
    { value: 'STOCK_LOW', label: 'Low' },
    { value: 'STOCK_MEDIUM', label: 'Medium' },
    { value: 'STOCK_HIGH', label: 'High' },
  ];

  protected readonly items = rxResource({
    params: () => ({ user: this.auth.currentUser(), urgency: this.stockFilter() }),
    stream: ({ params }) => this.auth.ensureUser().pipe(
      switchMap(user => user?.permissions.includes('VIEW_INVENTORY')
        ? this.itemService.getInventory(params.urgency === 'NONE' ? undefined : params.urgency)
        : this.itemService.getItems()),
    ),
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
