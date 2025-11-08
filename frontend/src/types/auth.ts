export interface User {
  id: number;
  name: string;
  email: string;
  phone: string;
  role: 'citizen' | 'volunteer' | 'authority';
  location?: {
    type: string;
    coordinates: number[];
  };
}

export interface LoginRequest {
  email: string;
  password: string;
}

export interface RegisterRequest {
  name: string;
  email: string;
  phone: string;
  password: string;
  role: 'citizen' | 'volunteer' | 'authority';
  location?: {
    type: string;
    coordinates: number[];
  };
}

export interface AuthContextType {
  user: User | null;
  login: (credentials: LoginRequest) => Promise<void>;
  register: (userData: RegisterRequest) => Promise<void>;
  logout: () => void;
  loading: boolean;
}



