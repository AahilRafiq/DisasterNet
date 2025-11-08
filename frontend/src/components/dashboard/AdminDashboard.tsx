import React, { useState, useEffect } from 'react';
import { Card, CardContent, CardHeader, CardTitle } from '../ui/card';
import { Button } from '../ui/button';
import { Alert, AlertDescription } from '../ui/alert';
import { AlertTriangle, Plus } from 'lucide-react';
import { Alert as AlertType } from '../../types/alert';
import { CreateAlertModal } from './CreateAlertModal';
import { AlertList } from './AlertList';
import { demoAlerts } from '../../data/demoData';
import { fetchAlerts } from '../../lib/api';

export const AdminDashboard: React.FC = () => {
  const [alerts, setAlerts] = useState<AlertType[]>([]);
  const [showCreateAlert, setShowCreateAlert] = useState(false);
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
    } catch (err) {
      setError('Failed to load alerts');
      setAlerts(demoAlerts);
    } finally {
      setLoading(false);
    }
  };

  const handleCreateAlert = async (alertData: any) => {
    try {
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

  const handleDeleteAlert = (id: string) => {
    setAlerts(prev => prev.filter(a => a.id !== id));
  };

  if (loading) {
    return (
      <div className="flex items-center justify-center h-64">
        <div className="text-center">
          <div className="animate-spin rounded-full h-8 w-8 border-b-2 border-primary mx-auto"></div>
          <p className="mt-2 text-gray-600">Loading alerts...</p>
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

      <div className="flex justify-between items-center">
        <h2 className="text-2xl font-bold flex items-center space-x-2">
          <AlertTriangle className="h-5 w-5 text-red-600" />
          <span>Manage Alerts</span>
        </h2>
        <Button onClick={() => setShowCreateAlert(true)}>
          <Plus className="h-4 w-4 mr-2" />
          Create Alert
        </Button>
      </div>

      <div className="space-y-4">
        <h3 className="text-lg font-semibold">Active / Recent Alerts</h3>
        <AlertList alerts={alerts} onDelete={handleDeleteAlert} />
      </div>

      {showCreateAlert && (
        <CreateAlertModal
          onClose={() => setShowCreateAlert(false)}
          onSubmit={handleCreateAlert}
        />
      )}
    </div>
  );
};
