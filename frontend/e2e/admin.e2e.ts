import { expect, test } from '@playwright/test';
import { ADMIN, mockApi } from './support/mock-api';

test.describe('Inventory (admin)', () => {
  test.beforeEach(async ({ page }) => {
    await mockApi(page, { signedInAs: ADMIN });
    await page.goto('/');
  });

  test('sees every item, out of stock included, lowest stock first', async ({ page }) => {
    await expect(page.locator('.overlay-text')).toHaveText(['Headset', 'Screen 27 inch', 'Webcam HD', 'Mechanical keyboard']);
  });

  test('filters the inventory by stock level', async ({ page }) => {
    const request = page.waitForRequest(/\/api\/items\/inventory\?urgency=STOCK_LOW$/);
    await page.getByLabel('Filter by stock level').selectOption('STOCK_LOW');
    await request;

    await expect(page.locator('.overlay-text')).toHaveText(['Headset', 'Screen 27 inch']);
  });

  test('has no cart', async ({ page }) => {
    await expect(page.locator('a[href="/order"]')).toHaveCount(0);
  });

  test('opens the form to create an item', async ({ page }) => {
    await page.getByRole('link', { name: 'Add an item' }).click();

    await expect(page).toHaveURL('/items/new');
    await expect(page.getByRole('heading', { name: 'New item' })).toBeVisible();
  });
});
