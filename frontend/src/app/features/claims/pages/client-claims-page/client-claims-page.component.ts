import { Component, inject, OnInit } from '@angular/core';
import { FormBuilder, Validators } from '@angular/forms';
import { finalize } from 'rxjs';
import { ApiFeedbackService } from '../../../../core/services/api-feedback.service';
import { ClaimsApiService } from '../../../../core/services/claims-api.service';
import { ContractsApiService } from '../../../../core/services/contracts-api.service';
import { Claim } from '../../../../shared/models/claim.model';
import { Contract } from '../../../../shared/models/contract.model';

@Component({
  selector: 'app-client-claims-page',
  templateUrl: './client-claims-page.component.html',
  styleUrl: './client-claims-page.component.scss',
  standalone: false
})
export class ClientClaimsPageComponent implements OnInit {
  private readonly formBuilder = inject(FormBuilder);

  readonly claimsColumns = ['claimNumber', 'medicalServiceDate', 'claimAmount', 'status'];
  readonly claimForm = this.formBuilder.nonNullable.group({
    contractId: ['', Validators.required],
    claimAmount: [0, [Validators.required, Validators.min(0.01)]],
    description: [''],
    medicalServiceDate: ['', Validators.required]
  });
  readonly documentForm = this.formBuilder.nonNullable.group({
    fileName: ['', Validators.required],
    fileType: ['application/pdf', Validators.required],
    filePath: ['', Validators.required],
    fileSize: [1, [Validators.required, Validators.min(1)]]
  });

  claims: Claim[] = [];
  contracts: Contract[] = [];
  selectedClaim: Claim | null = null;
  loading = true;
  savingClaim = false;
  savingDocument = false;

  constructor(
    private readonly claimsApi: ClaimsApiService,
    private readonly contractsApi: ContractsApiService,
    private readonly feedback: ApiFeedbackService
  ) {}

  ngOnInit(): void {
    this.load();
  }

  selectClaim(claim: Claim): void {
    this.selectedClaim = claim;
  }

  createClaim(): void {
    if (this.claimForm.invalid || this.savingClaim) {
      this.claimForm.markAllAsTouched();
      return;
    }

    this.savingClaim = true;
    this.claimsApi
      .create(this.claimForm.getRawValue())
      .pipe(finalize(() => (this.savingClaim = false)))
      .subscribe({
        next: (claim) => {
          this.feedback.success('Draft claim created.');
          this.claimForm.reset({
            contractId: '',
            claimAmount: 0,
            description: '',
            medicalServiceDate: ''
          });
          this.load(claim.id);
        },
        error: (error) => this.feedback.error(error, 'Unable to create claim')
      });
  }

  addDocument(): void {
    if (!this.selectedClaim || this.documentForm.invalid || this.savingDocument) {
      this.documentForm.markAllAsTouched();
      return;
    }

    this.savingDocument = true;
    this.claimsApi
      .addDocument(this.selectedClaim.id, this.documentForm.getRawValue())
      .pipe(finalize(() => (this.savingDocument = false)))
      .subscribe({
        next: () => {
          this.feedback.success('Document metadata added.');
          this.documentForm.reset({
            fileName: '',
            fileType: 'application/pdf',
            filePath: '',
            fileSize: 1
          });
          this.load(this.selectedClaim?.id);
        },
        error: (error) => this.feedback.error(error, 'Unable to add document')
      });
  }

  submitClaim(): void {
    if (!this.selectedClaim) {
      return;
    }

    this.claimsApi.submit(this.selectedClaim.id).subscribe({
      next: () => {
        this.feedback.success('Claim submitted.');
        this.load(this.selectedClaim?.id);
      },
      error: (error) => this.feedback.error(error, 'Unable to submit claim')
    });
  }

  private load(selectedId?: string): void {
    this.loading = true;
    this.contractsApi.findOwn().subscribe((contracts) => (this.contracts = contracts));
    this.claimsApi.findOwnClaims().subscribe({
      next: (claims) => {
        this.claims = claims;
        this.selectedClaim = claims.find((claim) => claim.id === selectedId) ?? claims[0] ?? null;
        this.loading = false;
      },
      error: (error) => {
        this.feedback.error(error, 'Unable to load claims');
        this.loading = false;
      }
    });
  }
}
