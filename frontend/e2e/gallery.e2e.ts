import { expect, test } from '@playwright/test';
import { mockApi } from './support/mock-api';

test.describe('Gallery (visitor)', () => {
  test.beforeEach(async ({ page }) => {
    await mockApi(page);
    await page.goto('/');
  });

  test('shows the items in stock only', async ({ page }) => {
    await expect(page.locator('.overlay-text')).toHaveText(['Mechanical keyboard', 'Screen 27 inch', 'Webcam HD']);
    await expect(page.getByText('Headset')).toHaveCount(0);
  });

  test('filters on the name', async ({ page }) => {
    await page.getByPlaceholder('Filter by Name').fill('cam');

    await expect(page.locator('.overlay-text')).toHaveText(['Webcam HD']);
  });

  test('opens the details of an item', async ({ page }) => {
    await page.getByText('Webcam HD').click();

    await expect(page).toHaveURL(/\/items\/a1b2c3d4-0000-4000-8000-000000000002$/);
    await expect(page.getByRole('heading', { level: 1 })).toHaveText('Webcam HD');
    await expect(page.getByText('Full HD 1080p.')).toBeVisible();
  });

  test('asks visitors to sign in before ordering', async ({ page }) => {
    await page.getByText('Webcam HD').click();

    await expect(page.getByRole('button', { name: 'Add to cart' })).toHaveCount(0);
    await expect(page.getByRole('link', { name: 'Sign in' }).last()).toBeVisible();
  });
});
