import { expect, test } from '@playwright/test';
import { CUSTOMER, ITEMS, mockApi } from './support/mock-api';

test.describe('Order (customer)', () => {
  test.beforeEach(async ({ page }) => {
    await mockApi(page, { signedInAs: CUSTOMER });
  });

  test('adds items, removes one, then places the order', async ({ page }) => {
    let sentOrder: unknown;
    await page.route('**/api/orders', route => {
      sentOrder = route.request().postDataJSON();
      route.fulfill({
        status: 201,
        json: {
          id: 'order-1',
          totalPrice: 179.98,
          items: [{ itemId: ITEMS[0].id, itemName: 'Mechanical keyboard', unitPrice: 89.99, amount: 2, totalPrice: 179.98, shippingDate: '2026-11-24' }],
        },
      });
    });

    await page.goto(`/items/${ITEMS[0].id}`);
    await page.getByLabel('Quantity').fill('2');
    await page.getByRole('button', { name: 'Add to cart' }).click();
    await page.goto(`/items/${ITEMS[1].id}`);
    await page.getByRole('button', { name: 'Add to cart' }).click();
    await expect(page.locator('.item-count')).toHaveText('3');

    await page.locator('a[href="/order"]').click();
    await expect(page.locator('tbody a')).toHaveText(['Mechanical keyboard', 'Webcam HD']);
    await page.getByRole('button', { name: 'Remove Webcam HD' }).click();
    await expect(page.locator('tbody a')).toHaveText(['Mechanical keyboard']);

    await page.getByRole('button', { name: 'Place order' }).click();
    await expect(page.getByRole('heading', { name: 'Thank you for your order!' })).toBeVisible();
    expect(sentOrder).toEqual({ items: [{ id: ITEMS[0].id, amount: 2 }] });
    await expect(page.locator('.item-count')).toHaveCount(0);
  });

  test('refuses a quantity of 0', async ({ page }) => {
    await page.goto(`/items/${ITEMS[0].id}`);
    await page.getByLabel('Quantity').fill('0');
    await page.getByRole('button', { name: 'Add to cart' }).click();

    await expect(page.getByText('Enter a whole number of at least 1.')).toBeVisible();
    await expect(page.locator('.item-count')).toHaveCount(0);
  });

  test('cannot open the admin pages', async ({ page }) => {
    await page.goto('/customers');

    await expect(page).toHaveURL('/');
  });
});
