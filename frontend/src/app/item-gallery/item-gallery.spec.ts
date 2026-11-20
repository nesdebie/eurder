import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ItemGallery } from './item-gallery';

describe('ItemGallery', () => {
  let component: ItemGallery;
  let fixture: ComponentFixture<ItemGallery>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [ItemGallery],
    }).compileComponents();

    fixture = TestBed.createComponent(ItemGallery);
    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
