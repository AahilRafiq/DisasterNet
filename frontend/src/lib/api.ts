import axios from 'axios';

const API_BASE_URL = 'http://localhost:8080'; // Gateway URL

export const api = axios.create({
  baseURL: API_BASE_URL,
  withCredentials: true, // Important for cookie-based auth
});

// Request interceptor to add auth token if available
api.interceptors.request.use((config) => {
  const token = localStorage.getItem('authToken');
  if (token) {
    config.headers.Authorization = `Bearer ${token}`;
  }
  return config;
});

// Response interceptor to handle auth errors
api.interceptors.response.use(
  (response) => response,
  (error) => {
    if (error.response?.status === 401) {
      localStorage.removeItem('authToken');
      window.location.href = '/login';
    }
    return Promise.reject(error);
  }
);

// Fetch alerts from backend and normalize to frontend Alert shape
export async function fetchAlerts() {
  const { data } = await api.get('/alerts');
  // Normalize severity to lowercase for UI styling consistency
  return (Array.isArray(data) ? data : []).map((a: any) => ({
    id: a.id,
    message: a.message,
    severity: (a.severity || 'low').toLowerCase(),
    location: a.location || { type: 'Point', coordinates: [0, 0] },
    distance: a.distance ?? undefined,
    createdAt: a.createdAt || new Date().toISOString(),
  }));
}

export default api;
