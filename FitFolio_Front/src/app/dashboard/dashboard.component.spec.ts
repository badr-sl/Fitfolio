import { ComponentFixture, TestBed } from '@angular/core/testing';
import { DashboardComponent } from './dashboard.component';
import { By } from '@angular/platform-browser';

describe('DashboardComponent', () => {
  let component: DashboardComponent;
  let fixture: ComponentFixture<DashboardComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [DashboardComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(DashboardComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should display the correct user name', () => {
    component.userName = 'John Doe';
    fixture.detectChanges();
    const userNameElement = fixture.debugElement.query(By.css('h3')).nativeElement;
    expect(userNameElement.textContent).toContain('John Doe');
  });

  it('should display the correct BMI value', () => {
    component.bmiValue = 22.5;
    fixture.detectChanges();
    const bmiValueElement = fixture.debugElement.query(By.css('.bmi-value')).nativeElement;
    expect(bmiValueElement.textContent).toBe('22.5');
  });

  it('should display the correct BMI status', () => {
    component.bmiStatus = 'Overweight';
    fixture.detectChanges();
    const bmiStatusElement = fixture.debugElement.query(By.css('.bmi-status p')).nativeElement;
    expect(bmiStatusElement.textContent).toBe('Overweight');
  });

  it('should display the correct water intake', () => {
    component.waterIntake = 3.5;
    fixture.detectChanges();
    const waterIntakeElement = fixture.debugElement.query(By.css('.water-intake-card .card-text')).nativeElement;
    expect(waterIntakeElement.textContent).toContain('3.5 Liters');
  });

  it('should display the correct sleep time', () => {
    component.sleepTime = '7h 45m';
    fixture.detectChanges();
    const sleepTimeElement = fixture.debugElement.query(By.css('.sleep-card .card-text')).nativeElement;
    expect(sleepTimeElement.textContent).toBe('7h 45m');
  });

  it('should display the correct calories consumed and remaining', () => {
    component.caloriesConsumed = 850;
    component.caloriesLeft = 150;
    fixture.detectChanges();
    const caloriesConsumedElement = fixture.debugElement.query(By.css('.calories-card .card-text')).nativeElement;
    const caloriesLeftElement = fixture.debugElement.query(By.css('.remaining-calories')).nativeElement;
    expect(caloriesConsumedElement.textContent).toContain('850 kCal');
    expect(caloriesLeftElement.textContent).toBe('150 kCal left');
  });
});
