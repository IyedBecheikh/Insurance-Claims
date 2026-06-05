import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { authGuard } from './core/guards/auth.guard';
import { AppShellComponent } from './layout/app-shell/app-shell.component';

const routes: Routes = [
  {
    path: 'login',
    loadChildren: () => import('./features/auth/auth-module').then((m) => m.AuthModule)
  },
  {
    path: '',
    component: AppShellComponent,
    canActivate: [authGuard],
    children: [
      {
        path: 'dashboard',
        loadChildren: () =>
          import('./features/dashboard/dashboard-module').then((m) => m.DashboardModule)
      },
      {
        path: 'users',
        loadChildren: () => import('./features/users/users-module').then((m) => m.UsersModule)
      },
      {
        path: 'clients',
        loadChildren: () =>
          import('./features/clients/clients-module').then((m) => m.ClientsModule)
      },
      {
        path: 'contracts',
        loadChildren: () =>
          import('./features/contracts/contracts-module').then((m) => m.ContractsModule)
      },
      {
        path: 'claims',
        loadChildren: () => import('./features/claims/claims-module').then((m) => m.ClaimsModule)
      },
      {
        path: '',
        pathMatch: 'full',
        redirectTo: 'dashboard'
      }
    ]
  },
  {
    path: '**',
    redirectTo: 'login'
  }
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule {}
