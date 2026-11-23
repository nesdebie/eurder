import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpTestingController } from '@angular/common/http/testing';
import { provideTestDefaults } from '../../testing/test-providers';
import { ItemGallery } from './item-gallery';

describe('ItemGallery', () => {
  let fixture: ComponentFixture<ItemGallery>;
  let page: HTMLElement;

  beforeEach(async () => {
    sessionStorage.clear();
    await TestBed.configureTestingModule({
      imports: [ItemGallery],
      providers: [provideTestDefaults()],
    }).compileComponents();

    fixture = TestBed.createComponent(ItemGallery);
    page = fixture.nativeElement;
    fixture.detectChanges();

    TestBed.inject(HttpTestingController).expectOne('/api/items').flush([
      { id: '1', name: 'Mechanical keyboard', description: '', price: 89.99, amount: 12 },
      { id: '2', name: 'Webcam HD', description: '', price: 39, amount: 3 },
    ]);
    await fixture.whenStable();
  });

  function itemNames(): string[] {
    return Array.from(page.querySelectorAll('.overlay-text')).map(element => element.textContent!.trim());
  }

  it('shows every item', () => {
    expect(itemNames()).toEqual(['Mechanical keyboard', 'Webcam HD']);
  });

  it('filters on the name, without caring about capitals', async () => {
    const search = page.querySelector<HTMLInputElement>('#name')!;
    search.value = 'WEBCAM';
    search.dispatchEvent(new Event('input'));
    await fixture.whenStable();

    expect(itemNames()).toEqual(['Webcam HD']);
  });

  it('hides the admin tools from visitors', () => {
    expect(page.querySelector('#stock-filter')).toBeNull();
    expect(page.querySelector('.add-item')).toBeNull();
  });
});
