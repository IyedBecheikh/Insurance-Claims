import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ReactiveFormsModule } from '@angular/forms';
import { RouterModule } from '@angular/router';
import { MatButtonModule } from '@angular/material/button';
import { MatCardModule } from '@angular/material/card';
import { MatChipsModule } from '@angular/material/chips';
import { MatDividerModule } from '@angular/material/divider';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatIconModule } from '@angular/material/icon';
import { MatInputModule } from '@angular/material/input';
import { MatListModule } from '@angular/material/list';
import { MatProgressSpinnerModule } from '@angular/material/progress-spinner';
import { MatSelectModule } from '@angular/material/select';
import { MatSidenavModule } from '@angular/material/sidenav';
import { MatSnackBarModule } from '@angular/material/snack-bar';
import { MatTableModule } from '@angular/material/table';
import { MatToolbarModule } from '@angular/material/toolbar';
import { MatTooltipModule } from '@angular/material/tooltip';
import { StatusBadgeComponent } from './components/status-badge/status-badge.component';
import { StatCardComponent } from './components/stat-card/stat-card.component';
import { SectionHeaderComponent } from './components/section-header/section-header.component';

const MATERIAL_MODULES = [
  MatButtonModule,
  MatCardModule,
  MatChipsModule,
  MatDividerModule,
  MatFormFieldModule,
  MatIconModule,
  MatInputModule,
  MatListModule,
  MatProgressSpinnerModule,
  MatSelectModule,
  MatSidenavModule,
  MatSnackBarModule,
  MatTableModule,
  MatToolbarModule,
  MatTooltipModule
];

@NgModule({
  declarations: [StatusBadgeComponent, StatCardComponent, SectionHeaderComponent],
  imports: [CommonModule, ReactiveFormsModule, RouterModule, ...MATERIAL_MODULES],
  exports: [
    CommonModule,
    ReactiveFormsModule,
    RouterModule,
    ...MATERIAL_MODULES,
    StatusBadgeComponent,
    StatCardComponent,
    SectionHeaderComponent
  ]
})
export class SharedModule {}
