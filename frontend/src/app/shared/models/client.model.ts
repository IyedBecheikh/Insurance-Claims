export interface Client {
  id: string;
  userId: string;
  firstName: string;
  lastName: string;
  phone: string;
  address: string | null;
  nationalId: string;
  dateOfBirth: string;
  registrationDate: string;
}

export interface SaveClientRequest {
  userId?: string;
  firstName: string;
  lastName: string;
  phone: string;
  address: string;
  nationalId: string;
  dateOfBirth: string;
}
