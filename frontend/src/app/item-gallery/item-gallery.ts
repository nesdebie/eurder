import { Component, computed, inject, signal } from '@angular/core';
import { rxResource } from '@angular/core/rxjs-interop';
import {FormsModule} from '@angular/forms';
import { CurrencyPipe } from '@angular/common';

import { ItemService } from '../service/item-service';

@Component({
  imports: [
    FormsModule,
    CurrencyPipe
  ],
  selector: 'app-item-gallery',
  styleUrl: './item-gallery.css',
  templateUrl: './item-gallery.html',
})
export class ItemGallery {
  private readonly itemService = inject(ItemService);
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
