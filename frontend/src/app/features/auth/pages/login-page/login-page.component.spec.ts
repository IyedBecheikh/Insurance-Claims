import { ComponentFixture, TestBed } from '@angular/core/testing';
import { of } from 'rxjs';
import { Router } from '@angular/router';
import { SharedModule } from '../../../../shared/shared-module';
import { LoginPageComponent } from './login-page.component';
import { AuthApiService } from '../../../../core/auth/auth-api.service';
import { ApiFeedbackService } from '../../../../core/services/api-feedback.service';

describe('LoginPageComponent', () => {
  let fixture: ComponentFixture<LoginPageComponent>;
  let component: LoginPageComponent;
  let authApi: jasmine.SpyObj<AuthApiService>;
  let router: jasmine.SpyObj<Router>;

  beforeEach(async () => {
    authApi = jasmine.createSpyObj<AuthApiService>('AuthApiService', ['login']);
    router = jasmine.createSpyObj<Router>('Router', ['navigate']);
    router.navigate.and.returnValue(Promise.resolve(true));

    await TestBed.configureTestingModule({
      declarations: [LoginPageComponent],
      imports: [SharedModule],
      providers: [
        { provide: AuthApiService, useValue: authApi },
        { provide: ApiFeedbackService, useValue: jasmine.createSpyObj('ApiFeedbackService', ['success', 'error']) },
        { provide: Router, useValue: router }
      ]
    }).compileComponents();

    fixture = TestBed.createComponent(LoginPageComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('submits credentials and redirects to dashboard after login', () => {
    authApi.login.and.returnValue(
      of({
        token: 'abc',
        expiresAt: Date.now() + 1000,
        user: {
          id: '1',
          email: 'admin@insurance.local',
          role: 'ADMIN'
        }
      })
    );

    component.submit();

    expect(authApi.login).toHaveBeenCalledWith({
      email: 'admin@insurance.local',
      password: 'Password123!'
    });
    expect(router.navigate).toHaveBeenCalledWith(['/dashboard']);
  });
});
