import api from './api';

export interface LoginRequest {
  email: string;
  password: string;
}

export interface LoginResponse {
  token: string;
  email: string;
  name: string;
}

export interface RegisterRequest {
  name: string;
  email: string;
  password: string;
}

export interface RegisterResponse {
  id: number;
  name: string;
  email: string;
}

export const authService = {
  async login(credentials: LoginRequest): Promise<LoginResponse> {
    const response = await api.post('/users/login', credentials);
    return response.data as LoginResponse;
  },

  async register(userData: RegisterRequest): Promise<RegisterResponse> {
    const response = await api.post('/users/register', userData);
    return response.data as RegisterResponse;
  }
}; 