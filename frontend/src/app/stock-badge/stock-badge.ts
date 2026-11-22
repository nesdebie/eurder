import { Component, computed, input } from '@angular/core';

@Component({
  selector: 'app-stock-badge',
  styleUrl: './stock-badge.css',
  template: `<span class="overlay-stock" [class]="level()">{{ label() }}</span>`,
})
export class StockBadge {
  readonly amount = input.required<number>();

  protected readonly level = computed(() => {
    const amount = this.amount();
    if (amount >= 10) return 'high';
    if (amount >= 5) return 'medium';
    return 'low';
  });

  protected readonly label = computed(() => {
    const amount = this.amount();
    if (amount >= 10) return 'In stock';
    if (amount >= 5) return `${amount} left in stock`;
    if (amount > 0) return `Only ${amount} left!`;
    return 'Out of stock';
  });
}
