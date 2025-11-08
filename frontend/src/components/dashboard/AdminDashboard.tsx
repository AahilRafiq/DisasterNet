import React, { useState, useEffect } from 'react';
import { Card, CardContent, CardHeader, CardTitle } from '../ui/card';
import { Button } from '../ui/button';
import { Alert, AlertDescription } from '../ui/alert';
import { AlertTriangle, Plus, MapPin, Users } from 'lucide-react';
import { Alert as AlertType } from '../../types/alert';
import { ResourceRequest } from '../../types/request';
import { CreateAlertModal } from './CreateAlertModal';
import { AlertList } from './AlertList';
import { RequestList } from './RequestList';
import { demoAlerts, demoRequests } from '../../data/demoData';
import { fetchAlerts } from '../../lib/api';

export const AdminDashboard: React.FC = () => {
  const [alerts, setAlerts] = useState<AlertType[]>([]);
  const [requests, setRequests] = useState<ResourceRequest[]>([]);
  const [showCreateAlert, setShowCreateAlert] = useState(false);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState('');

  useEffect(() => {
    fetchData();
  }, []);

  const fetchData = async () => {
    try {
      setLoading(true);
      // Fetch real alerts
      const realAlerts = await fetchAlerts();
      setAlerts(realAlerts.length ? realAlerts : demoAlerts); // fallback if empty
      // Requests still demo for now
      setRequests(demoRequests);
    } catch (err) {
      setError('Failed to load data');
      setAlerts(demoAlerts); // fallback on error
      setRequests(demoRequests);
    } finally {
      setLoading(false);
    }
  };

  const handleCreateAlert = async (alertData: any) => {
    try {
      // TODO: Replace with real API call (e.g., await api.post('/alerts/new', payload))
      const newAlert: AlertType = {
        id: Date.now().toString(),
        message: alertData.message,
        severity: alertData.severity.toLowerCase(),
        location: alertData.location,
        createdAt: new Date().toISOString()
      };
      setAlerts(prev => [newAlert, ...prev]);
      setShowCreateAlert(false);
    } catch (err) {
      setError('Failed to create alert');
    }
  };

  if (loading) {
    return (
      <div className="flex items-center justify-center h-64">
        <div className="text-center">
          <div className="animate-spin rounded-full h-8 w-8 border-b-2 border-primary mx-auto"></div>
          <p className="mt-2 text-gray-600">Loading dashboard...</p>
        </div>
      </div>
    );
  }

  return (
    <div className="space-y-6">
      {error && (
        <Alert variant="destructive">
          <AlertTriangle className="h-4 w-4" />
          <AlertDescription>{error}</AlertDescription>
        </Alert>
      )}

      {/* Stats Cards */}
      <div className="grid grid-cols-1 md:grid-cols-3 gap-6">
        <Card>
          <CardHeader className="flex flex-row items-center justify-between space-y-0 pb-2">
            <CardTitle className="text-sm font-medium">Active Alerts</CardTitle>
            <AlertTriangle className="h-4 w-4 text-muted-foreground" />
          </CardHeader>
          <CardContent>
            <div className="text-2xl font-bold">{alerts.length}</div>
            <p className="text-xs text-muted-foreground">
              Currently active disaster alerts
            </p>
          </CardContent>
        </Card>

        <Card>
          <CardHeader className="flex flex-row items-center justify-between space-y-0 pb-2">
            <CardTitle className="text-sm font-medium">Open Requests</CardTitle>
            <Users className="h-4 w-4 text-muted-foreground" />
          </CardHeader>
          <CardContent>
            <div className="text-2xl font-bold">{requests.filter(r => r.status === 'pending').length}</div>
            <p className="text-xs text-muted-foreground">
              Pending resource requests
            </p>
          </CardContent>
        </Card>

        <Card>
          <CardHeader className="flex flex-row items-center justify-between space-y-0 pb-2">
            <CardTitle className="text-sm font-medium">Completed</CardTitle>
            <MapPin className="h-4 w-4 text-muted-foreground" />
          </CardHeader>
          <CardContent>
            <div className="text-2xl font-bold">{requests.filter(r => r.status === 'completed').length}</div>
            <p className="text-xs text-muted-foreground">
              Successfully resolved requests
            </p>
          </CardContent>
        </Card>
      </div>

      {/* Actions */}
      <div className="flex justify-between items-center">
        <h2 className="text-2xl font-bold">Dashboard</h2>
        <Button onClick={() => setShowCreateAlert(true)}>
          <Plus className="h-4 w-4 mr-2" />
          Create Alert
        </Button>
      </div>

      {/* Alerts Section */}
      <div className="space-y-4">
        <h3 className="text-lg font-semibold">Recent Alerts</h3>
        <AlertList alerts={alerts} />
      </div>

      {/* Requests Section */}
      <div className="space-y-4">
        <h3 className="text-lg font-semibold">Resource Requests</h3>
        <RequestList requests={requests} />
      </div>

      {/* Create Alert Modal */}
      {showCreateAlert && (
        <CreateAlertModal
          onClose={() => setShowCreateAlert(false)}
          onSubmit={handleCreateAlert}
        />
      )}
    </div>
  );
};
