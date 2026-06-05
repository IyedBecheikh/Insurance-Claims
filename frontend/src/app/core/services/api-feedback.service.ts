import { Injectable } from '@angular/core';
import { MatSnackBar } from '@angular/material/snack-bar';
import { HttpErrorResponse } from '@angular/common/http';
import { ApiErrorResponse } from '../../shared/models/api-error.model';

@Injectable({
  providedIn: 'root'
})
export class ApiFeedbackService {
  constructor(private readonly snackBar: MatSnackBar) {}

  success(message: string): void {
    this.snackBar.open(message, 'Close', {
      duration: 3000,
      horizontalPosition: 'right',
      verticalPosition: 'top'
    });
  }

  error(error: unknown, fallback = 'Request failed'): void {
    const message = this.extractMessage(error) ?? fallback;

    this.snackBar.open(message, 'Close', {
      duration: 5000,
      panelClass: ['snackbar-error'],
      horizontalPosition: 'right',
      verticalPosition: 'top'
    });
  }

  private extractMessage(error: unknown): string | null {
    if (!(error instanceof HttpErrorResponse)) {
      return null;
    }

    const payload = error.error as ApiErrorResponse | undefined;
    return payload?.message ?? error.message ?? null;
  }
}
