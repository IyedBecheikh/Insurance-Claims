export type ContractStatus = 'ACTIVE' | 'EXPIRED' | 'SUSPENDED';
export type ContractType = 'HEALTH';

export interface Contract {
  id: string;
  clientId: string;
  contractNumber: string;
  type: ContractType;
  startDate: string;
  endDate: string;
  coverageLimit: number;
  reimbursementRate: number;
  status: ContractStatus;
}

export interface SaveContractRequest {
  clientId: string;
  contractNumber: string;
  type: ContractType;
  startDate: string;
  endDate: string;
  coverageLimit: number;
  reimbursementRate: number;
  status?: ContractStatus;
}
