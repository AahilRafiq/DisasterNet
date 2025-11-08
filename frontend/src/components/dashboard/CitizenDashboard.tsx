import React, { useState, useEffect } from 'react';
import { Card, CardContent, CardDescription, CardHeader, CardTitle } from '../ui/card';
import { Button } from '../ui/button';
import { Alert, AlertDescription } from '../ui/alert';
import { AlertTriangle, Plus, MapPin, HelpCircle } from 'lucide-react';
import { Alert as AlertType } from '../../types/alert';
import { ResourceRequest } from '../../types/request';
import { CreateRequestModal } from './CreateRequestModal';
import { AlertList } from './AlertList';
import { RequestList } from './RequestList';
import { demoAlerts } from '../../data/demoData';
import api, { fetchAlerts } from '../../lib/api';

export const CitizenDashboard: React.FC = () => {
  const [alerts, setAlerts] = useState<AlertType[]>([]);
  const [requests, setRequests] = useState<ResourceRequest[]>([]);
  const [showCreateRequest, setShowCreateRequest] = useState(false);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState('');

  useEffect(() => {
    fetchData();
  }, []);

  const fetchData = async () => {
    try {
      setLoading(true);
      const realAlerts = await fetchAlerts();
      setAlerts(realAlerts.length ? realAlerts : demoAlerts);
      // Requests still demo / placeholder until backend implemented
      // setRequests(demoRequests.filter(req => req.citizenId === 101));
    } catch (err) {
      setError('Failed to load data');
      setAlerts(demoAlerts);
    } finally {
      setLoading(false);
    }
  };

  const handleCreateRequest = async (requestData: any) => {
    try {
      // Make API call to create request
      const response = await api.post('/requests', requestData);
      console.log('Response:', response);
      
      // Backend generates ID and timestamps
      const newRequest = response.data;
      setRequests(prev => [newRequest, ...prev]);
      setShowCreateRequest(false);
    } catch (err) {
      setError('Failed to create request');
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
      <Card className="bg-gradient-to-r from-blue-50 to-indigo-50 border-blue-200">
        <CardHeader>
          <CardTitle className="flex items-center space-x-2">
            <HelpCircle className="h-5 w-5 text-blue-600" />
            <span>Welcome to DisasterNet</span>
          </CardTitle>
          <CardDescription>
            Stay informed about disasters in your area and request help when needed
          </CardDescription>
        </CardHeader>
      </Card>

      {/* Stats Cards */}
      <div className="grid grid-cols-1 md:grid-cols-2 gap-6">
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
            <CardTitle className="text-sm font-medium">My Requests</CardTitle>
            <MapPin className="h-4 w-4 text-muted-foreground" />
          </CardHeader>
          <CardContent>
            <div className="text-2xl font-bold">{requests.length}</div>
            <p className="text-xs text-muted-foreground">
              Your resource requests
            </p>
          </CardContent>
        </Card>
      </div>

      {/* Actions */}
      <div className="flex justify-between items-center">
        <h2 className="text-2xl font-bold">Your Dashboard</h2>
        <Button onClick={() => setShowCreateRequest(true)}>
          <Plus className="h-4 w-4 mr-2" />
          Request Help
        </Button>
      </div>

      {/* Alerts Section */}
      <div className="space-y-4">
        <h3 className="text-lg font-semibold">Nearby Alerts</h3>
        <AlertList alerts={alerts} />
      </div>

      {/* My Requests Section */}
      <div className="space-y-4">
        <h3 className="text-lg font-semibold">My Requests</h3>
        <RequestList requests={requests} />
      </div>

      {/* Create Request Modal */}
      {showCreateRequest && (
        <CreateRequestModal
          onClose={() => setShowCreateRequest(false)}
          onSubmit={handleCreateRequest}
        />
      )}
    </div>
  );
};
