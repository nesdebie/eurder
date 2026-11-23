import { Component, inject } from '@angular/core';
import { Router, RouterLink, RouterLinkActive } from '@angular/router';
import { AuthService } from '../../auth/auth-service';
import { OrderService } from '../../service/order-service';

@Component({
  imports: [RouterLink, RouterLinkActive],
  selector: 'app-header',
  styleUrl: './header.css',
  templateUrl: './header.html',
})
export class Header {
  protected readonly auth = inject(AuthService);
  protected readonly order = inject(OrderService);
  private readonly router = inject(Router);

  protected logout(): void {
    this.order.clear();
    this.auth.logout();
    this.router.navigateByUrl('/');
  }
}
