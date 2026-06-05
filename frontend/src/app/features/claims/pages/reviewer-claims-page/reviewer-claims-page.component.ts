import { Component, inject, OnInit } from '@angular/core';
import { FormBuilder } from '@angular/forms';
import { ApiFeedbackService } from '../../../../core/services/api-feedback.service';
import { ClaimsApiService } from '../../../../core/services/claims-api.service';
import { Claim, ClaimStatus } from '../../../../shared/models/claim.model';

@Component({
  selector: 'app-reviewer-claims-page',
  templateUrl: './reviewer-claims-page.component.html',
  styleUrl: './reviewer-claims-page.component.scss',
  standalone: false
})
export class ReviewerClaimsPageComponent implements OnInit {
  private readonly formBuilder = inject(FormBuilder);

  readonly statusOptions: Array<ClaimStatus | ''> = ['', 'SUBMITTED', 'UNDER_REVIEW', 'APPROVED', 'REJECTED', 'PAID'];
  readonly columns = ['claimNumber', 'clientId', 'claimAmount', 'status', 'actions'];
  readonly filtersForm = this.formBuilder.nonNullable.group({
    status: ['' as ClaimStatus | ''],
    clientId: [''],
    claimNumber: ['']
  });

  claims: Claim[] = [];
  selectedClaim: Claim | null = null;
  loading = true;

  constructor(
    private readonly claimsApi: ClaimsApiService,
    private readonly feedback: ApiFeedbackService
  ) {}

  ngOnInit(): void {
    this.load();
  }

  selectClaim(claim: Claim): void {
    this.selectedClaim = claim;
  }

  applyFilters(): void {
    this.load();
  }

  trigger(action: 'start' | 'approve' | 'reject' | 'pay', claim: Claim): void {
    const operation =
      action === 'start'
        ? this.claimsApi.startReview(claim.id)
        : action === 'approve'
          ? this.claimsApi.approve(claim.id)
          : action === 'reject'
            ? this.claimsApi.reject(claim.id)
            : this.claimsApi.pay(claim.id);

    operation.subscribe({
      next: () => {
        this.feedback.success('Claim workflow updated.');
        this.load(claim.id);
      },
      error: (error) => this.feedback.error(error, 'Unable to update claim')
    });
  }

  private load(selectedId?: string): void {
    this.loading = true;
    this.claimsApi.findReviewClaims(this.filtersForm.getRawValue()).subscribe({
      next: (claims) => {
        this.claims = claims;
        this.selectedClaim = claims.find((claim) => claim.id === selectedId) ?? claims[0] ?? null;
        this.loading = false;
      },
      error: (error) => {
        this.feedback.error(error, 'Unable to load review claims');
        this.loading = false;
      }
    });
  }
}
