import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { roleGuard } from '../../core/guards/role.guard';
import { ClientClaimsPageComponent } from './pages/client-claims-page/client-claims-page.component';
import { ReviewerClaimsPageComponent } from './pages/reviewer-claims-page/reviewer-claims-page.component';

const routes: Routes = [
  {
    path: 'my',
    component: ClientClaimsPageComponent,
    canActivate: [roleGuard],
    data: { roles: ['CLIENT'] }
  },
  {
    path: 'review',
    component: ReviewerClaimsPageComponent,
    canActivate: [roleGuard],
    data: { roles: ['ADMIN', 'AGENT'] }
  },
  {
    path: '',
    pathMatch: 'full',
    redirectTo: 'my'
  }
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class ClaimsRoutingModule {}
