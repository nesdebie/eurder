import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpTestingController } from '@angular/common/http/testing';
import { provideTestDefaults } from '../../testing/test-providers';
import { EditProfile } from './edit-profile';

describe('EditProfile', () => {
  let component: EditProfile;
  let fixture: ComponentFixture<EditProfile>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [EditProfile],
      providers: [provideTestDefaults()],
    }).compileComponents();

    fixture = TestBed.createComponent(EditProfile);
    component = fixture.componentInstance;

    TestBed.inject(HttpTestingController).expectOne('/api/customers/me').flush({
      id: '1',
      fullName: { firstName: 'Jane', lastName: 'Doe' },
      email: 'jane@example.com',
      phone: '+32 470 00 00 00',
      address: { streetName: 'Rue Neuve', streetNumber: '1', postalCode: '1000', city: 'Brussels' },
    });
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
