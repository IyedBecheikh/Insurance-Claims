import { Role } from './role';

export interface LoginRequest {
  email: string;
  password: string;
}

export interface LoginResponse {
  accessToken: string;
  tokenType: string;
  role: Role;
  expiresIn: number;
}

export interface AuthenticatedUser {
  id: string;
  email: string;
  role: Role;
}

export interface AuthSession {
  token: string;
  user: AuthenticatedUser;
  expiresAt: number;
}
