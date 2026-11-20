import { Component } from '@angular/core';

@Component({
  imports: [],
  selector: 'app-footer',
  templateUrl: './footer.html',
})
export class Footer {
  year = new Date().getFullYear();
}
