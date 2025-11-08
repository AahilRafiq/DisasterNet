import React, { useState } from 'react';
import { Button } from '../ui/button';
import { Input } from '../ui/input';
import { Card, CardContent, CardDescription, CardHeader, CardTitle } from '../ui/card';
import { Alert, AlertDescription } from '../ui/alert';
import { AlertTriangle, X } from 'lucide-react';
import { Alert as AlertType, CreateAlertRequest } from '../../types/alert';

interface CreateAlertModalProps {
  onClose: () => void;
  onSubmit: (alert: AlertType) => void;
}

export const CreateAlertModal: React.FC<CreateAlertModalProps> = ({ onClose, onSubmit }) => {
  const [formData, setFormData] = useState<CreateAlertRequest>({
    id: '',
    createdAt: '',
    message: '',
    severity: 'medium',
    location: {
      type: 'Point',
      coordinates: [0, 0] // Default coordinates
    }
  });
  const [error, setError] = useState('');
  const [loading, setLoading] = useState(false);

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();
    setLoading(true);
    setError('');

    try {
      // Get user's current location
      if (navigator.geolocation) {
        navigator.geolocation.getCurrentPosition(
          (position) => {
            const alertData = {
              id: '',
              createdAt: '',
              message: formData.message,
              severity: formData.severity,
              location: {
                type: 'Point',
                coordinates: [position.coords.longitude, position.coords.latitude]
              }
            };
            onSubmit(alertData);
          },
          () => {
            // Fallback to default coordinates if geolocation fails
            const alertData = {
              id: '',
              createdAt: '',
              message: formData.message,
              severity: formData.severity,
              location: formData.location
            };
            onSubmit(alertData);
          }
        );
      } else {
        // Fallback if geolocation is not supported
        const alertData = {
          id: '',
          createdAt: '',
          message: formData.message,
          severity: formData.severity,
          location: formData.location
        };
        onSubmit(alertData);
      }
    } catch (err) {
      setError('Failed to create alert');
    } finally {
      setLoading(false);
    }
  };

  const handleChange = (e: React.ChangeEvent<HTMLInputElement | HTMLSelectElement | HTMLTextAreaElement>) => {
    setFormData({
      ...formData,
      [e.target.name]: e.target.value,
    });
  };

  return (
    <div className="fixed inset-0 bg-black bg-opacity-50 flex items-center justify-center p-4 z-50">
      <Card className="w-full max-w-md">
        <CardHeader className="flex flex-row items-center justify-between space-y-0 pb-4">
          <div>
            <CardTitle className="text-xl">Create Alert</CardTitle>
            <CardDescription>Broadcast a disaster alert to the community</CardDescription>
          </div>
          <Button variant="ghost" size="icon" onClick={onClose}>
            <X className="h-4 w-4" />
          </Button>
        </CardHeader>
        <CardContent>
          <form onSubmit={handleSubmit} className="space-y-4">
            {error && (
              <Alert variant="destructive">
                <AlertTriangle className="h-4 w-4" />
                <AlertDescription>{error}</AlertDescription>
              </Alert>
            )}
            
            <div className="space-y-2">
              <label htmlFor="message" className="text-sm font-medium">
                Alert Message
              </label>
              <textarea
                id="message"
                name="message"
                placeholder="Describe the disaster or emergency..."
                value={formData.message}
                onChange={handleChange}
                className="flex min-h-[80px] w-full rounded-md border border-input bg-background px-3 py-2 text-sm ring-offset-background placeholder:text-muted-foreground focus-visible:outline-none focus-visible:ring-2 focus-visible:ring-ring focus-visible:ring-offset-2 disabled:cursor-not-allowed disabled:opacity-50"
                required
              />
            </div>
            
            <div className="space-y-2">
              <label htmlFor="severity" className="text-sm font-medium">
                Severity Level
              </label>
              <select
                id="severity"
                name="severity"
                value={formData.severity}
                onChange={handleChange}
                className="flex h-10 w-full rounded-md border border-input bg-background px-3 py-2 text-sm ring-offset-background focus-visible:outline-none focus-visible:ring-2 focus-visible:ring-ring focus-visible:ring-offset-2"
                required
              >
                <option value="low">Low</option>
                <option value="medium">Medium</option>
                <option value="high">High</option>
                <option value="critical">Critical</option>
              </select>
            </div>
            
            <div className="flex space-x-2">
              <Button type="button" variant="outline" onClick={onClose} className="flex-1">
                Cancel
              </Button>
              <Button type="submit" disabled={loading} className="flex-1">
                {loading ? 'Creating...' : 'Create Alert'}
              </Button>
            </div>
          </form>
        </CardContent>
      </Card>
    </div>
  );
};
