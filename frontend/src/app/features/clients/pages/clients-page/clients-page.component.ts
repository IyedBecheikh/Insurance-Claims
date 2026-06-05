import { Component, inject, OnInit } from '@angular/core';
import { FormBuilder, Validators } from '@angular/forms';
import { finalize } from 'rxjs';
import { ApiFeedbackService } from '../../../../core/services/api-feedback.service';
import { ClientsApiService } from '../../../../core/services/clients-api.service';
import { UsersApiService } from '../../../../core/services/users-api.service';
import { Client, SaveClientRequest } from '../../../../shared/models/client.model';
import { User } from '../../../../shared/models/user.model';

@Component({
  selector: 'app-clients-page',
  templateUrl: './clients-page.component.html',
  styleUrl: './clients-page.component.scss',
  standalone: false
})
export class ClientsPageComponent implements OnInit {
  private readonly formBuilder = inject(FormBuilder);

  readonly displayedColumns = ['name', 'nationalId', 'phone', 'registrationDate'];
  readonly form = this.formBuilder.nonNullable.group({
    userId: [''],
    firstName: ['', Validators.required],
    lastName: ['', Validators.required],
    phone: ['', Validators.required],
    address: [''],
    nationalId: ['', Validators.required],
    dateOfBirth: ['', Validators.required]
  });

  users: User[] = [];
  clients: Client[] = [];
  selectedClient: Client | null = null;
  loading = true;
  saving = false;
  editMode = false;

  constructor(
    private readonly clientsApi: ClientsApiService,
    private readonly usersApi: UsersApiService,
    private readonly feedback: ApiFeedbackService
  ) {}

  ngOnInit(): void {
    this.load();
  }

  selectClient(client: Client): void {
    this.selectedClient = client;
    this.editMode = true;
    this.form.patchValue({
      userId: client.userId,
      firstName: client.firstName,
      lastName: client.lastName,
      phone: client.phone,
      address: client.address ?? '',
      nationalId: client.nationalId,
      dateOfBirth: client.dateOfBirth
    });
  }

  prepareCreate(): void {
    this.editMode = false;
    this.selectedClient = null;
    this.form.reset({
      userId: '',
      firstName: '',
      lastName: '',
      phone: '',
      address: '',
      nationalId: '',
      dateOfBirth: ''
    });
  }

  submit(): void {
    if (this.form.invalid || this.saving) {
      this.form.markAllAsTouched();
      return;
    }

    const request = this.form.getRawValue() as SaveClientRequest;
    this.saving = true;

    const operation = this.editMode && this.selectedClient
      ? this.clientsApi.update(this.selectedClient.id, request)
      : this.clientsApi.create(request);

    operation.pipe(finalize(() => (this.saving = false))).subscribe({
      next: (client) => {
        this.feedback.success(`Client ${this.editMode ? 'updated' : 'created'}.`);
        this.load(client.id);
      },
      error: (error) => this.feedback.error(error, 'Unable to save client')
    });
  }

  private load(selectedId?: string): void {
    this.loading = true;
    this.prepareCreate();
    this.usersApi.findAll().subscribe((users) => {
      this.users = users.filter((user) => user.role === 'CLIENT');
    });

    this.clientsApi.findAll().subscribe({
      next: (clients) => {
        this.clients = clients;
        if (selectedId) {
          const selected = clients.find((client) => client.id === selectedId);
          if (selected) {
            this.selectClient(selected);
          }
        }
        this.loading = false;
      },
      error: (error) => {
        this.feedback.error(error, 'Unable to load clients');
        this.loading = false;
      }
    });
  }
}
