import { Component } from '@angular/core';
import { Router } from '@angular/router';
import { AuthStateService } from '../../core/auth/auth-state.service';
import { Role } from '../../shared/models/role';

interface NavItem {
  label: string;
  icon: string;
  route: string;
  roles: Role[];
}

@Component({
  selector: 'app-shell',
  templateUrl: './app-shell.component.html',
  styleUrl: './app-shell.component.scss',
  standalone: false
})
export class AppShellComponent {
  readonly navItems: NavItem[] = [
    {
      label: 'Dashboard',
      icon: 'space_dashboard',
      route: '/dashboard',
      roles: ['ADMIN', 'AGENT', 'CLIENT']
    },
    { label: 'Users', icon: 'manage_accounts', route: '/users', roles: ['ADMIN'] },
    { label: 'Clients', icon: 'groups', route: '/clients', roles: ['ADMIN'] },
    { label: 'Contracts', icon: 'description', route: '/contracts', roles: ['ADMIN'] },
    { label: 'My Claims', icon: 'folder_shared', route: '/claims/my', roles: ['CLIENT'] },
    { label: 'Review Claims', icon: 'fact_check', route: '/claims/review', roles: ['ADMIN', 'AGENT'] }
  ];

  constructor(
    public readonly authState: AuthStateService,
    private readonly router: Router
  ) {}

  get visibleItems(): NavItem[] {
    const role = this.authState.user?.role;
    return this.navItems.filter((item) => (role ? item.roles.includes(role) : false));
  }

  logout(): void {
    this.authState.clearSession();
    void this.router.navigate(['/login']);
  }
}
