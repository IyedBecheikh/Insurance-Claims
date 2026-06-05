import { NgModule } from '@angular/core';
import { SharedModule } from '../../shared/shared-module';
import { ContractsRoutingModule } from './contracts-routing-module';
import { ContractsPageComponent } from './pages/contracts-page/contracts-page.component';

@NgModule({
  declarations: [ContractsPageComponent],
  imports: [SharedModule, ContractsRoutingModule]
})
export class ContractsModule {}
