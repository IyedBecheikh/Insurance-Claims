import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { roleGuard } from '../../core/guards/role.guard';
import { ClientsPageComponent } from './pages/clients-page/clients-page.component';

const routes: Routes = [
  {
    path: '',
    component: ClientsPageComponent,
    canActivate: [roleGuard],
    data: { roles: ['ADMIN'] }
  }
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class ClientsRoutingModule {}
