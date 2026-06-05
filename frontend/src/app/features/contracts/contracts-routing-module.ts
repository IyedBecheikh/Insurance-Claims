import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { roleGuard } from '../../core/guards/role.guard';
import { ContractsPageComponent } from './pages/contracts-page/contracts-page.component';

const routes: Routes = [
  {
    path: '',
    component: ContractsPageComponent,
    canActivate: [roleGuard],
    data: { roles: ['ADMIN'] }
  }
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class ContractsRoutingModule {}
