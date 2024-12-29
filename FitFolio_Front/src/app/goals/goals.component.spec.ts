import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ChooseGoalComponent } from './goals.component';

describe('GoalsComponent', () => {
  let component: ChooseGoalComponent;
  let fixture: ComponentFixture<ChooseGoalComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [ChooseGoalComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(ChooseGoalComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
