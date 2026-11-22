import { Routes } from '@angular/router';
import { ItemGallery } from './item-gallery/item-gallery';
import { Login } from './auth/login/login';
import { Register } from './auth/register/register';

export const routes: Routes = [
  { path: '', component: ItemGallery, title: 'Örder' },
  { path: 'login', component: Login, title: 'Sign in – Örder' },
  { path: 'register', component: Register, title: 'Create an account – Örder' },
  { path: '**', redirectTo: '' },
];
