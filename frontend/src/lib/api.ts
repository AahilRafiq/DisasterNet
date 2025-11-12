import axios from 'axios';
import { Alert as AlertType } from '../types/alert';

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

export async function createNewAlert(alert: AlertType) {
    const payload = {
        message: alert.message,
        severity: alert.severity,
        location: alert.location,
        distance: 1000
    };
    const { data } = await api.post('/alerts/new', payload);
    return data;
}

// Fetch open resource requests for volunteers
export async function fetchOpenRequests() {
  const { data } = await api.get('/requests/open');
  const list = Array.isArray(data) ? data : [];
  return list.map((r: any) => {
    // Normalize possible ObjectId shapes to string id
    let id: string = r.id;
    if (!id && r._id) {
      if (typeof r._id === 'string') id = r._id;
      else if (r._id.$oid) id = r._id.$oid;
    }

    // Ensure location exists
    const location = r.location || { type: 'Point', coordinates: [0, 0] };

    return {
      id,
      type: (r.type || 'other').toLowerCase(),
      status: (r.status || 'pending').toLowerCase(),
      description: r.description || '',
      citizenId: r.citizenId ?? undefined,
      assignedVolunteerId: r.assignedVolunteerId ?? undefined,
      location,
      createdAt: r.createdAt || undefined,
    } as import('../types/request').ResourceRequest;
  });
}

export default api;
