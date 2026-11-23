import { Routes } from '@angular/router';
import { ItemGallery } from './item-gallery/item-gallery';
import { ItemDetail } from './item-detail/item-detail';
import { ItemCreate } from './item-create/item-create';
import { permissionGuard } from './auth/auth-guard';
import { Login } from './auth/login/login';
import { Register } from './auth/register/register';

export const routes: Routes = [
  { path: '', component: ItemGallery, title: 'Örder' },
  { path: 'items/new', component: ItemCreate, title: 'New item - Örder', canActivate: [permissionGuard('ADD_ITEM')] },
  { path: 'items/:id', component: ItemDetail, title: 'Item - Örder' },
  { path: 'login', component: Login, title: 'Sign in - Örder' },
  { path: 'register', component: Register, title: 'Create an account - Örder' },
  { path: '**', redirectTo: '' },
];
