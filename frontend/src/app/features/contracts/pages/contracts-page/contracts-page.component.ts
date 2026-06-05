import { Component, inject, OnInit } from '@angular/core';
import { FormBuilder, Validators } from '@angular/forms';
import { finalize } from 'rxjs';
import { ApiFeedbackService } from '../../../../core/services/api-feedback.service';
import { ClientsApiService } from '../../../../core/services/clients-api.service';
import { ContractsApiService } from '../../../../core/services/contracts-api.service';
import { Client } from '../../../../shared/models/client.model';
import { Contract, ContractStatus, ContractType, SaveContractRequest } from '../../../../shared/models/contract.model';

@Component({
  selector: 'app-contracts-page',
  templateUrl: './contracts-page.component.html',
  styleUrl: './contracts-page.component.scss',
  standalone: false
})
export class ContractsPageComponent implements OnInit {
  private readonly formBuilder = inject(FormBuilder);

  readonly displayedColumns = ['contractNumber', 'clientId', 'type', 'status', 'actions'];
  readonly typeOptions: ContractType[] = ['HEALTH'];
  readonly form = this.formBuilder.nonNullable.group({
    clientId: ['', Validators.required],
    contractNumber: ['', Validators.required],
    type: ['HEALTH' as ContractType, Validators.required],
    startDate: ['', Validators.required],
    endDate: ['', Validators.required],
    coverageLimit: [0, [Validators.required, Validators.min(0.01)]],
    reimbursementRate: [0.8, [Validators.required, Validators.min(0), Validators.max(1)]]
  });

  clients: Client[] = [];
  contracts: Contract[] = [];
  selectedContract: Contract | null = null;
  editMode = false;
  loading = true;
  saving = false;

  constructor(
    private readonly contractsApi: ContractsApiService,
    private readonly clientsApi: ClientsApiService,
    private readonly feedback: ApiFeedbackService
  ) {}

  ngOnInit(): void {
    this.load();
  }

  selectContract(contract: Contract): void {
    this.selectedContract = contract;
    this.editMode = true;
    this.form.patchValue({
      clientId: contract.clientId,
      contractNumber: contract.contractNumber,
      type: contract.type,
      startDate: contract.startDate,
      endDate: contract.endDate,
      coverageLimit: contract.coverageLimit,
      reimbursementRate: contract.reimbursementRate
    });
  }

  prepareCreate(): void {
    this.editMode = false;
    this.selectedContract = null;
    this.form.reset({
      clientId: '',
      contractNumber: '',
      type: 'HEALTH',
      startDate: '',
      endDate: '',
      coverageLimit: 0,
      reimbursementRate: 0.8
    });
  }

  submit(): void {
    if (this.form.invalid || this.saving) {
      this.form.markAllAsTouched();
      return;
    }

    const payload = this.form.getRawValue() as SaveContractRequest;
    this.saving = true;

    const operation = this.editMode && this.selectedContract
      ? this.contractsApi.update(this.selectedContract.id, payload)
      : this.contractsApi.create({ ...payload, status: 'ACTIVE' });

    operation.pipe(finalize(() => (this.saving = false))).subscribe({
      next: (contract) => {
        this.feedback.success(`Contract ${this.editMode ? 'updated' : 'created'}.`);
        this.load(contract.id);
      },
      error: (error) => this.feedback.error(error, 'Unable to save contract')
    });
  }

  changeStatus(contract: Contract, status: ContractStatus): void {
    const operation = status === 'ACTIVE'
      ? this.contractsApi.activate(contract.id)
      : this.contractsApi.suspend(contract.id);

    operation.subscribe({
      next: () => {
        this.feedback.success(`Contract ${status === 'ACTIVE' ? 'activated' : 'suspended'}.`);
        this.load(contract.id);
      },
      error: (error) => this.feedback.error(error, 'Unable to update contract')
    });
  }

  private load(selectedId?: string): void {
    this.loading = true;
    this.prepareCreate();
    this.clientsApi.findAll().subscribe((clients) => (this.clients = clients));
    this.contractsApi.findAll().subscribe({
      next: (contracts) => {
        this.contracts = contracts;
        if (selectedId) {
          const selected = contracts.find((contract) => contract.id === selectedId);
          if (selected) {
            this.selectContract(selected);
          }
        }
        this.loading = false;
      },
      error: (error) => {
        this.feedback.error(error, 'Unable to load contracts');
        this.loading = false;
      }
    });
  }
}
