import { Component, Input } from '@angular/core';

@Component({
  selector: 'app-status-badge',
  templateUrl: './status-badge.component.html',
  styleUrl: './status-badge.component.scss',
  standalone: false
})
export class StatusBadgeComponent {
  @Input({ required: true }) value = '';

  get tone(): string {
    switch (this.value) {
      case 'ACTIVE':
      case 'APPROVED':
      case 'PAID':
        return 'status-positive';
      case 'UNDER_REVIEW':
      case 'SUBMITTED':
        return 'status-attention';
      case 'SUSPENDED':
      case 'REJECTED':
        return 'status-negative';
      default:
        return 'status-neutral';
    }
  }
}
