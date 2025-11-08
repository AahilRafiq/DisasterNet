import React, { createContext, useContext, useState, useEffect } from 'react';
import { User, LoginRequest, RegisterRequest, AuthContextType } from '../types/auth';
import api from '../lib/api';
import { demoUsers } from '../data/demoData';

const AuthContext = createContext<AuthContextType | undefined>(undefined);

export const useAuth = () => {
  const context = useContext(AuthContext);
  if (context === undefined) {
    throw new Error('useAuth must be used within an AuthProvider');
  }
  return context;
};

export const AuthProvider: React.FC<{ children: React.ReactNode }> = ({ children }) => {
  const [user, setUser] = useState<User | null>(null);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    const token = localStorage.getItem('authToken');
    setLoading(false);
  }, []);

  const login = async (credentials: LoginRequest) => {
    try {
      // Demo login path
      const demoUser = Object.values(demoUsers).find(u => u.email === credentials.email);
      if (demoUser && credentials.password === 'demo123') {
        localStorage.setItem('authToken', 'authenticated');
        setUser(demoUser);
        return;
      }

      // Real API login
      const response = await api.post('/auth/signin', credentials);
      if (response.status === 200) {
        const userResponse = response.data;
        localStorage.setItem('authToken', 'authenticated');
        setUser({
          id: userResponse.id,
          name: userResponse.name,
          email: userResponse.email,
          phone: userResponse.phone || '',
          role: userResponse.role,
          location: userResponse.location,
        });
        return;
      }

      throw new Error('Login failed');
    } catch (error) {
      throw new Error('Login failed');
    }
  };

  const register = async (userData: RegisterRequest) => {
    try {
      const response = await api.post('/auth/signup', userData);
      if (response.status === 200) {
        const userResponse = response.data;
        localStorage.setItem('authToken', 'authenticated');
        setUser({
          id: userResponse.id,
          name: userResponse.name,
          email: userResponse.email,
          phone: userResponse.phone,
          role: userResponse.role,
          location: userResponse.location,
        });
      }
    } catch (error) {
      console.error('Registration error:', error);
      throw new Error('Registration failed');
    }
  };

  const logout = () => {
    localStorage.removeItem('authToken');
    setUser(null);
  };

  const value: AuthContextType = { user, login, register, logout, loading };

  return (
    <AuthContext.Provider value={value}>
      {children}
    </AuthContext.Provider>
  );
};
