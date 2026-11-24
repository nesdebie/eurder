import { ComponentFixture, TestBed } from '@angular/core/testing';
import { StockBadge } from './stock-badge';

describe('StockBadge', () => {
  let fixture: ComponentFixture<StockBadge>;

  beforeEach(() => {
    fixture = TestBed.createComponent(StockBadge);
  });

  async function render(amount: number): Promise<HTMLElement> {
    fixture.componentRef.setInput('amount', amount);
    await fixture.whenStable();
    return fixture.nativeElement.querySelector('.overlay-stock');
  }

  it('shows "In stock" from 10 units', async () => {
    const badge = await render(10);
    expect(badge.textContent).toBe('In stock');
    expect(badge.classList).toContain('high');
  });

  it('shows how many are left between 5 and 9 units', async () => {
    const badge = await render(7);
    expect(badge.textContent).toBe('7 left in stock');
    expect(badge.classList).toContain('medium');
  });

  it('warns below 5 units', async () => {
    const badge = await render(3);
    expect(badge.textContent).toBe('Only 3 left!');
    expect(badge.classList).toContain('low');
  });

  it('keeps the "low" style when the stock is empty', async () => {
    const badge = await render(0);
    expect(badge.classList).toContain('low');
  });
});
