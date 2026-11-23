import { EnvironmentProviders, Provider } from '@angular/core';
import { provideHttpClient } from '@angular/common/http';
import { provideHttpClientTesting } from '@angular/common/http/testing';
import { provideRouter } from '@angular/router';

// What almost every component of the app needs in a unit test:
// - HttpClient, but intercepted: no real request leaves the test (answer them with HttpTestingController)
// - the router, for routerLink / ActivatedRoute
export function provideTestDefaults(): (Provider | EnvironmentProviders)[] {
  return [provideHttpClient(), provideHttpClientTesting(), provideRouter([])];
}
