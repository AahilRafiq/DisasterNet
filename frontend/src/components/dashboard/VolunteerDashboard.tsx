import React, { useState, useEffect } from 'react';
import { Card, CardContent, CardHeader, CardTitle } from '../ui/card';
import { Button } from '../ui/button';
import { Alert, AlertDescription } from '../ui/alert';
import { AlertTriangle, Users, MapPin, CheckCircle } from 'lucide-react';
import { Alert as AlertType } from '../../types/alert';
import { ResourceRequest } from '../../types/request';
import { AlertList } from './AlertList';
import { RequestList } from './RequestList';
import { demoAlerts } from '../../data/demoData';
import api, { fetchAlerts, fetchOpenRequests } from '../../lib/api';
import { useAuth } from '../../contexts/AuthContext';

export const VolunteerDashboard: React.FC = () => {
  const { user } = useAuth();
  const [alerts, setAlerts] = useState<AlertType[]>([]);
  const [requests, setRequests] = useState<ResourceRequest[]>([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState('');

  useEffect(() => {
    fetchData();
    // eslint-disable-next-line react-hooks/exhaustive-deps
  }, []);

  const fetchData = async () => {
    try {
      setLoading(true);
      const realAlerts = await fetchAlerts();
      setAlerts(realAlerts.length ? realAlerts : demoAlerts);

      // Load open (pending) requests from backend
      const openRequests = await fetchOpenRequests();
      setRequests(openRequests);
    } catch (err) {
      setError('Failed to load data');
      setAlerts(demoAlerts);
      setRequests([]);
    } finally {
      setLoading(false);
    }
  };

  const handleAssignRequest = async (requestId: string) => {
    console.log("this is",requests);
      try {
      if (!user) throw new Error('Not authenticated');
      // Only volunteers and authorities can assign; basic guard on client
      if (user.role !== 'volunteer' && user.role !== 'authority') {
        throw new Error('Insufficient permissions');
      }

      // Send volunteerId as query param as expected by backend
      await api.patch(`/requests/${requestId}/assign`, null, {
        params: { volunteerId: user.id },
      });

      // Refresh list (assigned request will no longer be in open list)
      await fetchData();
    } catch (err) {
      setError('Failed to assign request');
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

      {/* Welcome Message */}
      <Card className="bg-gradient-to-r from-green-50 to-emerald-50 border-green-200">
        <CardHeader>
          <CardTitle className="flex items-center space-x-2">
            <Users className="h-5 w-5 text-green-600" />
            <span>Volunteer Dashboard</span>
          </CardTitle>
        </CardHeader>
      </Card>

      {/* Stats Cards */}
      <div className="grid grid-cols-1 md:grid-cols-3 gap-6">
        <Card>
          <CardHeader className="flex flex-row items-center justify-between space-y-0 pb-2">
            <CardTitle className="text-sm font-medium">Nearby Alerts</CardTitle>
            <AlertTriangle className="h-4 w-4 text-muted-foreground" />
          </CardHeader>
          <CardContent>
            <div className="text-2xl font-bold">{alerts.length}</div>
            <p className="text-xs text-muted-foreground">
              Active alerts in your area
            </p>
          </CardContent>
        </Card>

        <Card>
          <CardHeader className="flex flex-row items-center justify-between space-y-0 pb-2">
            <CardTitle className="text-sm font-medium">Open Requests</CardTitle>
            <MapPin className="h-4 w-4 text-muted-foreground" />
          </CardHeader>
          <CardContent>
            <div className="text-2xl font-bold">{requests.filter(r => r.status === 'pending').length}</div>
            <p className="text-xs text-muted-foreground">
              Requests needing help
            </p>
          </CardContent>
        </Card>
      </div>

      {/* Alerts Section */}
      <div className="space-y-4">
        <h3 className="text-lg font-semibold">Nearby Alerts</h3>
        <AlertList alerts={alerts} />
      </div>

      {/* Requests Section */}
      <div className="space-y-4">
        <h3 className="text-lg font-semibold">Available Requests</h3>
        <RequestList 
          requests={requests} 
          showAssignButton={true}
          onAssign={handleAssignRequest}
        />
      </div>
    </div>
  );
};
