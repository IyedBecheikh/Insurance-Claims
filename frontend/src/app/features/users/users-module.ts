import { NgModule } from '@angular/core';
import { SharedModule } from '../../shared/shared-module';
import { UsersPageComponent } from './pages/users-page/users-page.component';
import { UsersRoutingModule } from './users-routing-module';

@NgModule({
  declarations: [UsersPageComponent],
  imports: [SharedModule, UsersRoutingModule]
})
export class UsersModule {}
