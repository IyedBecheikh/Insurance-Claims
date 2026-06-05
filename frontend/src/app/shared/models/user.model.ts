import { Role } from './role';

export interface User {
  id: string;
  email: string;
  role: Role;
  enabled: boolean;
  createdAt: string;
}

export interface CreateUserRequest {
  email: string;
  password: string;
  role: Role;
  enabled: boolean;
}
