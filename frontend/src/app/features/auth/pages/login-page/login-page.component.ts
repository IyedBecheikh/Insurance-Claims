import { Component, inject } from '@angular/core';
import { FormBuilder, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { finalize } from 'rxjs';
import { AuthApiService } from '../../../../core/auth/auth-api.service';
import { ApiFeedbackService } from '../../../../core/services/api-feedback.service';
import { Role } from '../../../../shared/models/role';

@Component({
  selector: 'app-login-page',
  templateUrl: './login-page.component.html',
  styleUrl: './login-page.component.scss',
  standalone: false
})
export class LoginPageComponent {
  private readonly formBuilder = inject(FormBuilder);

  readonly form = this.formBuilder.nonNullable.group({
    email: ['admin@insurance.local', [Validators.required, Validators.email]],
    password: ['Password123!', [Validators.required]]
  });

  submitting = false;

  constructor(
    private readonly authApi: AuthApiService,
    private readonly feedback: ApiFeedbackService,
    private readonly router: Router
  ) {}

  submit(): void {
    if (this.form.invalid || this.submitting) {
      this.form.markAllAsTouched();
      return;
    }

    this.submitting = true;

    this.authApi
      .login(this.form.getRawValue())
      .pipe(finalize(() => (this.submitting = false)))
      .subscribe({
        next: ({ user }) => {
          this.feedback.success('Signed in successfully.');
          void this.router.navigate([this.routeForRole(user.role)]);
        },
        error: (error) => this.feedback.error(error, 'Login failed')
      });
  }

  private routeForRole(role: Role): string {
    switch (role) {
      case 'ADMIN':
      case 'AGENT':
      case 'CLIENT':
        return '/dashboard';
    }
  }
}
