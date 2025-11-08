import React, { useState } from 'react';
import { useAuth } from '../../contexts/AuthContext';
import { Button } from '../ui/button';
import { Input } from '../ui/input';
import { Card, CardContent, CardDescription, CardHeader, CardTitle } from '../ui/card';
import { Alert, AlertDescription } from '../ui/alert';
import { AlertCircle } from 'lucide-react';

interface RegisterFormProps {
  onToggleMode: () => void;
}

export const RegisterForm: React.FC<RegisterFormProps> = ({ onToggleMode }) => {
  const [formData, setFormData] = useState({
    name: '',
    email: '',
    phone: '',
    password: '',
    confirmPassword: '',
    role: 'citizen' as 'citizen' | 'volunteer' | 'authority',
    location: {
      type: 'Point',
      coordinates: [0, 0] // Default coordinates
    }
  });
  const [error, setError] = useState('');
  const [loading, setLoading] = useState(false);
  const [locationError, setLocationError] = useState('');
  const { register } = useAuth();

  const getCurrentLocation = (): Promise<{ longitude: number; latitude: number }> => {
    return new Promise((resolve, reject) => {
      if (!navigator.geolocation) {
        reject(new Error('Geolocation is not supported by this browser.'));
        return;
      }

      navigator.geolocation.getCurrentPosition(
        (position) => {
          resolve({
            longitude: position.coords.longitude,
            latitude: position.coords.latitude
          });
        },
        (error) => {
          reject(new Error('Unable to retrieve your location. Please allow location access or enter coordinates manually.'));
        },
        {
          enableHighAccuracy: true,
          timeout: 10000,
          maximumAge: 300000 // 5 minutes
        }
      );
    });
  };

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();
    setLoading(true);
    setError('');
    setLocationError('');

    if (formData.password !== formData.confirmPassword) {
      setError('Passwords do not match');
      setLoading(false);
      return;
    }

    try {
      // Get user's current location
      const location = await getCurrentLocation();
      const userData = {
        name: formData.name,
        email: formData.email,
        phone: formData.phone,
        password: formData.password,
        role: formData.role,
        location: {
          type: 'Point',
          coordinates: [location.longitude, location.latitude]
        }
      };

      await register(userData);
    } catch (err) {
      if (err instanceof Error && err.message.includes('location')) {
        setLocationError(err.message);
      } else {
        setError('Registration failed. Please try again.');
      }
    } finally {
      setLoading(false);
    }
  };

  const handleChange = (e: React.ChangeEvent<HTMLInputElement | HTMLSelectElement>) => {
    setFormData({
      ...formData,
      [e.target.name]: e.target.value,
    });
  };

  return (
    <Card className="w-full max-w-md mx-auto">
      <CardHeader className="space-y-1">
        <CardTitle className="text-2xl font-bold text-center">Sign Up</CardTitle>
        <CardDescription className="text-center">
          Create an account to join the disaster management platform
        </CardDescription>
      </CardHeader>
      <CardContent>
        <form onSubmit={handleSubmit} className="space-y-4">
          {error && (
            <Alert variant="destructive">
              <AlertCircle className="h-4 w-4" />
              <AlertDescription>{error}</AlertDescription>
            </Alert>
          )}
          
          {locationError && (
            <Alert variant="destructive">
              <AlertCircle className="h-4 w-4" />
              <AlertDescription>{locationError}</AlertDescription>
            </Alert>
          )}
          
          <div className="space-y-2">
            <label htmlFor="name" className="text-sm font-medium">
              Full Name
            </label>
            <Input
              id="name"
              name="name"
              type="text"
              placeholder="Enter your full name"
              value={formData.name}
              onChange={handleChange}
              required
            />
          </div>
          
          <div className="space-y-2">
            <label htmlFor="email" className="text-sm font-medium">
              Email
            </label>
            <Input
              id="email"
              name="email"
              type="email"
              placeholder="Enter your email"
              value={formData.email}
              onChange={handleChange}
              required
            />
          </div>
          
          <div className="space-y-2">
            <label htmlFor="phone" className="text-sm font-medium">
              Phone Number
            </label>
            <Input
              id="phone"
              name="phone"
              type="tel"
              placeholder="Enter your phone number"
              value={formData.phone}
              onChange={handleChange}
              required
            />
          </div>
          
          <div className="space-y-2">
            <label htmlFor="role" className="text-sm font-medium">
              Role
            </label>
            <select
              id="role"
              name="role"
              value={formData.role}
              onChange={handleChange}
              className="flex h-10 w-full rounded-md border border-input bg-background px-3 py-2 text-sm ring-offset-background focus-visible:outline-none focus-visible:ring-2 focus-visible:ring-ring focus-visible:ring-offset-2"
              required
            >
              <option value="citizen">Citizen</option>
              <option value="volunteer">Volunteer</option>
              <option value="authority">Authority/Admin</option>
            </select>
          </div>
          
          <div className="space-y-2">
            <label htmlFor="password" className="text-sm font-medium">
              Password
            </label>
            <Input
              id="password"
              name="password"
              type="password"
              placeholder="Enter your password"
              value={formData.password}
              onChange={handleChange}
              required
            />
          </div>
          
          <div className="space-y-2">
            <label htmlFor="confirmPassword" className="text-sm font-medium">
              Confirm Password
            </label>
            <Input
              id="confirmPassword"
              name="confirmPassword"
              type="password"
              placeholder="Confirm your password"
              value={formData.confirmPassword}
              onChange={handleChange}
              required
            />
          </div>
          
          <div className="space-y-2">
            <label className="text-sm font-medium">
              Location (Required for emergency services)
            </label>
            <div className="text-xs text-gray-600 mb-2">
              We need your location to provide accurate disaster alerts and coordinate help.
            </div>
            <div className="grid grid-cols-2 gap-2">
              <div>
                <label htmlFor="latitude" className="text-xs text-gray-500">
                  Latitude
                </label>
                <Input
                  id="latitude"
                  name="latitude"
                  type="number"
                  step="any"
                  placeholder="e.g., 40.7128"
                  value={formData.location.coordinates[1]}
                  onChange={(e) => {
                    const newCoords = [...formData.location.coordinates];
                    newCoords[1] = parseFloat(e.target.value) || 0;
                    setFormData({
                      ...formData,
                      location: {
                        ...formData.location,
                        coordinates: newCoords
                      }
                    });
                  }}
                />
              </div>
              <div>
                <label htmlFor="longitude" className="text-xs text-gray-500">
                  Longitude
                </label>
                <Input
                  id="longitude"
                  name="longitude"
                  type="number"
                  step="any"
                  placeholder="e.g., -74.0060"
                  value={formData.location.coordinates[0]}
                  onChange={(e) => {
                    const newCoords = [...formData.location.coordinates];
                    newCoords[0] = parseFloat(e.target.value) || 0;
                    setFormData({
                      ...formData,
                      location: {
                        ...formData.location,
                        coordinates: newCoords
                      }
                    });
                  }}
                />
              </div>
            </div>
            <div className="flex items-center justify-between">
              <div className="text-xs text-blue-600">
                💡 We'll try to get your location automatically, but you can also enter it manually above.
              </div>
              <Button
                type="button"
                variant="outline"
                size="sm"
                onClick={async () => {
                  try {
                    const location = await getCurrentLocation();
                    setFormData({
                      ...formData,
                      location: {
                        type: 'Point',
                        coordinates: [location.longitude, location.latitude]
                      }
                    });
                    setLocationError('');
                  } catch (err) {
                    if (err instanceof Error) {
                      setLocationError(err.message);
                    }
                  }
                }}
              >
                📍 Get My Location
              </Button>
            </div>
          </div>
          
          <Button type="submit" className="w-full" disabled={loading}>
            {loading ? 'Creating Account...' : 'Sign Up'}
          </Button>
        </form>
        
        <div className="mt-4 text-center">
          <p className="text-sm text-muted-foreground">
            Already have an account?{' '}
            <button
              type="button"
              onClick={onToggleMode}
              className="text-primary hover:underline"
            >
              Sign in
            </button>
          </p>
        </div>
      </CardContent>
    </Card>
  );
};
