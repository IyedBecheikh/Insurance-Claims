import { Component, OnInit } from '@angular/core';
import { forkJoin } from 'rxjs';
import { AuthStateService } from '../../../../core/auth/auth-state.service';
import { ClaimsApiService } from '../../../../core/services/claims-api.service';
import { ClientsApiService } from '../../../../core/services/clients-api.service';
import { ContractsApiService } from '../../../../core/services/contracts-api.service';
import { UsersApiService } from '../../../../core/services/users-api.service';
import { Claim } from '../../../../shared/models/claim.model';
import { Contract } from '../../../../shared/models/contract.model';
import { Client } from '../../../../shared/models/client.model';
import { User } from '../../../../shared/models/user.model';
import { Role } from '../../../../shared/models/role';

@Component({
  selector: 'app-dashboard-page',
  templateUrl: './dashboard-page.component.html',
  styleUrl: './dashboard-page.component.scss',
  standalone: false
})
export class DashboardPageComponent implements OnInit {
  loading = true;
  users: User[] = [];
  clients: Client[] = [];
  contracts: Contract[] = [];
  claims: Claim[] = [];

  constructor(
    public readonly authState: AuthStateService,
    private readonly usersApi: UsersApiService,
    private readonly clientsApi: ClientsApiService,
    private readonly contractsApi: ContractsApiService,
    private readonly claimsApi: ClaimsApiService
  ) {}

  ngOnInit(): void {
    const role = this.authState.user?.role;

    const request =
      role === 'ADMIN'
        ? forkJoin({
            users: this.usersApi.findAll(),
            clients: this.clientsApi.findAll(),
            contracts: this.contractsApi.findAll(),
            claims: this.claimsApi.findReviewClaims({})
          })
        : role === 'AGENT'
          ? forkJoin({
              users: [this.users],
              clients: [this.clients],
              contracts: [this.contracts],
              claims: this.claimsApi.findReviewClaims({})
            })
          : forkJoin({
              users: [this.users],
              clients: [this.clients],
              contracts: this.contractsApi.findOwn(),
              claims: this.claimsApi.findOwnClaims()
            });

    request.subscribe({
      next: ({ users, clients, contracts, claims }) => {
        this.users = users;
        this.clients = clients;
        this.contracts = contracts;
        this.claims = claims;
        this.loading = false;
      },
      error: () => {
        this.loading = false;
      }
    });
  }

  get role(): Role | null {
    return this.authState.user?.role ?? null;
  }

  get dashboardTitle(): string {
    switch (this.role) {
      case 'ADMIN':
        return 'Administration dashboard';
      case 'AGENT':
        return 'Reviewer dashboard';
      case 'CLIENT':
        return 'Client dashboard';
      default:
        return 'Dashboard';
    }
  }

  get dashboardSubtitle(): string {
    switch (this.role) {
      case 'ADMIN':
        return 'Current system activity across users, clients, contracts, and claims.';
      case 'AGENT':
        return 'Focus on the review queue and claims needing workflow decisions.';
      case 'CLIENT':
        return 'Track your claims and move drafts through submission.';
      default:
        return '';
    }
  }

  get claimsInReview(): number {
    return this.claims.filter((claim) => claim.status === 'UNDER_REVIEW').length;
  }

  get readyToActCount(): number {
    return this.claims.filter(
      (claim) => claim.status === 'SUBMITTED' || claim.status === 'APPROVED'
    ).length;
  }
}
