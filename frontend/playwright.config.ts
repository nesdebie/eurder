import { defineConfig, devices } from '@playwright/test';

// End-to-end tests: a real browser clicks through the app.
// Run them with: npm run e2e        (headless)
//                npm run e2e:ui     (Playwright's UI, to watch and debug the tests)
export default defineConfig({
  testDir: './e2e',
  testMatch: '**/*.e2e.ts',   // not *.spec.ts: those are the Vitest unit tests (ng test)
  fullyParallel: true,
  retries: process.env['CI'] ? 2 : 0,
  reporter: [['list'], ['html', { open: 'never' }]],

  use: {
    baseURL: 'http://localhost:4300',
    trace: 'on-first-retry',     // on a failed retry, keeps a step-by-step trace to replay
    screenshot: 'only-on-failure',
  },

  // Every test runs on a desktop browser and on a phone-sized one
  projects: [
    { name: 'desktop', use: { ...devices['Desktop Chrome'] } },
    { name: 'mobile', use: { ...devices['Pixel 7'] } },
  ],

  // Starts the Angular app before the tests (port 4300, so your own `npm start` on 4200 can keep running).
  // The backend is NOT needed: the tests answer the /api requests themselves (see e2e/support/mock-api.ts).
  webServer: {
    command: 'npx ng serve --port 4300',
    url: 'http://localhost:4300',
    reuseExistingServer: !process.env['CI'],
    timeout: 120_000,
  },
});
