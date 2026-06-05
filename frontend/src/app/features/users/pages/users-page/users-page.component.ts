import { Component, inject, OnInit } from '@angular/core';
import { FormBuilder, Validators } from '@angular/forms';
import { finalize } from 'rxjs';
import { ApiFeedbackService } from '../../../../core/services/api-feedback.service';
import { UsersApiService } from '../../../../core/services/users-api.service';
import { Role } from '../../../../shared/models/role';
import { User } from '../../../../shared/models/user.model';

@Component({
  selector: 'app-users-page',
  templateUrl: './users-page.component.html',
  styleUrl: './users-page.component.scss',
  standalone: false
})
export class UsersPageComponent implements OnInit {
  private readonly formBuilder = inject(FormBuilder);

  readonly roleOptions: Role[] = ['ADMIN', 'AGENT', 'CLIENT'];
  readonly displayedColumns = ['email', 'role', 'enabled', 'createdAt', 'actions'];
  readonly form = this.formBuilder.nonNullable.group({
    email: ['', [Validators.required, Validators.email]],
    password: ['', [Validators.required, Validators.minLength(8)]],
    role: ['CLIENT' as Role, Validators.required],
    enabled: [true]
  });

  users: User[] = [];
  selectedUser: User | null = null;
  loading = true;
  saving = false;

  constructor(
    private readonly usersApi: UsersApiService,
    private readonly feedback: ApiFeedbackService
  ) {}

  ngOnInit(): void {
    this.loadUsers();
  }

  selectUser(user: User): void {
    this.selectedUser = user;
  }

  submit(): void {
    if (this.form.invalid || this.saving) {
      this.form.markAllAsTouched();
      return;
    }

    this.saving = true;
    this.usersApi
      .create(this.form.getRawValue())
      .pipe(finalize(() => (this.saving = false)))
      .subscribe({
        next: (user) => {
          this.feedback.success('User created.');
          this.form.reset({ email: '', password: '', role: 'CLIENT', enabled: true });
          this.loadUsers(user.id);
        },
        error: (error) => this.feedback.error(error, 'Unable to create user')
      });
  }

  toggleEnabled(user: User): void {
    this.usersApi.updateEnabled(user.id, !user.enabled).subscribe({
      next: (updated) => {
        this.feedback.success(`User ${updated.enabled ? 'enabled' : 'disabled'}.`);
        this.loadUsers(updated.id);
      },
      error: (error) => this.feedback.error(error, 'Unable to update user')
    });
  }

  private loadUsers(selectedUserId?: string): void {
    this.loading = true;
    this.usersApi.findAll().subscribe({
      next: (users) => {
        this.users = users;
        this.selectedUser = users.find((user) => user.id === selectedUserId) ?? users[0] ?? null;
        this.loading = false;
      },
      error: (error) => {
        this.feedback.error(error, 'Unable to load users');
        this.loading = false;
      }
    });
  }
}
