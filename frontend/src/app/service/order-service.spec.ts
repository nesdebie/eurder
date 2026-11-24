import { TestBed } from '@angular/core/testing';
import { HttpTestingController } from '@angular/common/http/testing';
import { provideTestDefaults } from '../../testing/test-providers';
import { OrderService } from './order-service';

describe('OrderService (cart)', () => {
  let service: OrderService;
  let http: HttpTestingController;

  beforeEach(() => {
    localStorage.clear();
    TestBed.configureTestingModule({ providers: [provideTestDefaults()] });
    service = TestBed.inject(OrderService);
    http = TestBed.inject(HttpTestingController);
  });

  afterEach(() => http.verify());

  it('adds up the amounts when the same item is added twice', () => {
    service.add('keyboard', 2);
    service.add('keyboard', 1);
    service.add('screen', 1);

    expect(service.lines()).toEqual([
      { id: 'keyboard', amount: 3 },
      { id: 'screen', amount: 1 },
    ]);
    expect(service.count()).toBe(4);
  });

  it('removes a whole line', () => {
    service.add('keyboard', 2);
    service.add('screen', 1);

    service.remove('keyboard');

    expect(service.lines()).toEqual([{ id: 'screen', amount: 1 }]);
  });

  it('keeps the cart in localStorage', () => {
    service.add('keyboard', 2);

    expect(JSON.parse(localStorage.getItem('eurder-cart')!)).toEqual([{ id: 'keyboard', amount: 2 }]);
  });

  it('sends the whole cart with POST /orders, then empties it', () => {
    service.add('keyboard', 2);
    let placed = false;

    service.placeOrder().subscribe(() => (placed = true));

    const request = http.expectOne('/api/orders');
    expect(request.request.method).toBe('POST');
    expect(request.request.body).toEqual({ items: [{ id: 'keyboard', amount: 2 }] });

    request.flush({ id: 'order-1', items: [], totalPrice: 179.98 });

    expect(placed).toBe(true);
    expect(service.count()).toBe(0);
  });

  it('keeps the cart when the order fails', () => {
    service.add('keyboard', 2);

    service.placeOrder().subscribe({ error: () => {} });
    http.expectOne('/api/orders').flush({ detail: 'Item keyboard is out of stock.' }, { status: 400, statusText: 'Bad Request' });

    expect(service.count()).toBe(2);
  });
});
