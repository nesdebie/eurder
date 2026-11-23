import { Page } from '@playwright/test';

// The e2e tests don't need the backend: every /api request is answered here,
// so the tests are fast and always find the same data.

export interface TestItem {
  id: string;
  name: string;
  description: string;
  price: number;
  amount: number;
}

export const ITEMS: TestItem[] = [
  { id: 'a1b2c3d4-0000-4000-8000-000000000001', name: 'Mechanical keyboard', description: 'Red switches.', price: 89.99, amount: 12 },
  { id: 'a1b2c3d4-0000-4000-8000-000000000002', name: 'Webcam HD', description: 'Full HD 1080p.', price: 39, amount: 7 },
  { id: 'a1b2c3d4-0000-4000-8000-000000000003', name: 'Screen 27 inch', description: 'IPS panel.', price: 249, amount: 3 },
  { id: 'a1b2c3d4-0000-4000-8000-000000000004', name: 'Headset', description: 'Bluetooth.', price: 59.9, amount: 0 },
];

// The permissions each role gets from GET /auth/me (same as Role.java on the backend)
export const CUSTOMER = ['EURDER_ITEMS', 'REEURDER_ITEMS', 'VIEW_EURDERS_REPORT', 'MANAGE_PROFILE'];
export const ADMIN = ['ADD_ITEM', 'UPDATE_ITEM', 'VIEW_CUSTOMERS', 'VIEW_CUSTOMER', 'VIEW_INVENTORY', 'VIEW_SHIPPING_TODAY'];

const STOCK_LEVELS: Record<string, (amount: number) => boolean> = {
  STOCK_LOW: amount => amount < 5,
  STOCK_MEDIUM: amount => amount >= 5 && amount < 10,
  STOCK_HIGH: amount => amount >= 10,
};

// Answers the item requests like the backend does.
// signedInAs: the permissions of the signed-in user; leave it out for a visitor.
export async function mockApi(page: Page, options: { signedInAs?: string[]; items?: TestItem[] } = {}): Promise<void> {
  const items = options.items ?? ITEMS;

  if (options.signedInAs) {
    const permissions = options.signedInAs;
    // The app reads its Basic credentials from sessionStorage: put fake ones there before it starts
    await page.addInitScript(() => sessionStorage.setItem('eurder-auth', btoa('test@example.com:password123')));
    await page.route('**/api/auth/me', route => route.fulfill({ json: { email: 'test@example.com', permissions } }));
  }

  // GET /items: only what is in stock
  await page.route('**/api/items', route => route.fulfill({ json: items.filter(item => item.amount > 0) }));

  // GET /items/inventory?urgency=...: everything (admin), lowest stock first, optionally one stock level
  await page.route(/\/api\/items\/inventory(\?.*)?$/, route => {
    const urgency = new URL(route.request().url()).searchParams.get('urgency');
    const keep = urgency ? STOCK_LEVELS[urgency] : () => true;
    route.fulfill({ json: items.filter(item => keep(item.amount)).sort((a, b) => a.amount - b.amount) });
  });
}
