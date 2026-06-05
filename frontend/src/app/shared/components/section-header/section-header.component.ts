import { Component, Input } from '@angular/core';

@Component({
  selector: 'app-section-header',
  templateUrl: './section-header.component.html',
  styleUrl: './section-header.component.scss',
  standalone: false
})
export class SectionHeaderComponent {
  @Input({ required: true }) title = '';
  @Input() subtitle = '';
}
