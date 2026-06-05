export type ClaimStatus = 'DRAFT' | 'SUBMITTED' | 'UNDER_REVIEW' | 'APPROVED' | 'REJECTED' | 'PAID';

export interface ClaimDocument {
  id: string;
  claimId: string;
  fileName: string;
  fileType: string;
  filePath: string;
  fileSize: number;
  uploadedAt: string;
}

export interface ClaimComment {
  id: string;
  claimId: string;
  authorId: string;
  comment: string;
  createdAt: string;
}

export interface Claim {
  id: string;
  clientId: string;
  contractId: string;
  claimNumber: string;
  claimAmount: number;
  reimbursementAmount: number | null;
  status: ClaimStatus;
  description: string | null;
  medicalServiceDate: string;
  submittedAt: string | null;
  reviewedAt: string | null;
  reviewedBy: string | null;
  documents: ClaimDocument[];
  comments: ClaimComment[];
}

export interface CreateClaimRequest {
  contractId: string;
  claimAmount: number;
  description: string;
  medicalServiceDate: string;
}

export interface AddClaimDocumentRequest {
  fileName: string;
  fileType: string;
  filePath: string;
  fileSize: number;
}
