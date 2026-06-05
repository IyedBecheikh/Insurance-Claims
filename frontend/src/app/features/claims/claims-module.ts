import { NgModule } from '@angular/core';
import { SharedModule } from '../../shared/shared-module';
import { ClaimsRoutingModule } from './claims-routing-module';
import { ClientClaimsPageComponent } from './pages/client-claims-page/client-claims-page.component';
import { ReviewerClaimsPageComponent } from './pages/reviewer-claims-page/reviewer-claims-page.component';

@NgModule({
  declarations: [ClientClaimsPageComponent, ReviewerClaimsPageComponent],
  imports: [SharedModule, ClaimsRoutingModule]
})
export class ClaimsModule {}
