import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { ProductDComponent } from './product-d.component';

describe('ProductDComponent', () => {
  let component: ProductDComponent;
  let fixture: ComponentFixture<ProductDComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ ProductDComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(ProductDComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
