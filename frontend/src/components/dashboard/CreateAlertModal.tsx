import React, { useState } from 'react';
import { Button } from '../ui/button';
import { Input } from '../ui/input';
import { Card, CardContent, CardDescription, CardHeader, CardTitle } from '../ui/card';
import { Alert, AlertDescription } from '../ui/alert';
import { AlertTriangle, X, Loader2, MapPin } from 'lucide-react'; // Added Loader2 and MapPin
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
    const [submitLoading, setSubmitLoading] = useState(false);
    const [geoLoading, setGeoLoading] = useState(false); // Loading state for geolocation

    // This function now handles both simple (message) and nested (coordinates) state
    const handleChange = (e: React.ChangeEvent<HTMLInputElement | HTMLSelectElement | HTMLTextAreaElement>) => {
        const { name, value } = e.target;

        if (name === 'longitude' || name === 'latitude') {
            const newCoords = [...formData.location.coordinates];
            const coordIndex = name === 'longitude' ? 0 : 1;

            // Update the correct index in the coordinates array
            newCoords[coordIndex] = parseFloat(value) || 0; // Parse as number, default to 0

            setFormData(prev => ({
                ...prev,
                location: {
                    ...prev.location,
                    coordinates: newCoords
                }
            }));
        } else {
            // Handle simple fields like message and severity
            setFormData(prev => ({
                ...prev,
                [name]: value,
            }));
        }
    };

    // Dedicated function to get user's location
    const handleGeolocate = () => {
        if (!navigator.geolocation) {
            setError('Geolocation is not supported by your browser.');
            return;
        }

        setGeoLoading(true);
        setError('');

        navigator.geolocation.getCurrentPosition(
            (position) => {
                setFormData(prev => ({
                    ...prev,
                    location: {
                        ...prev.location,
                        coordinates: [position.coords.longitude, position.coords.latitude]
                    }
                }));
                setGeoLoading(false);
            },
            () => {
                setError('Unable to retrieve your location. Please enter it manually.');
                setGeoLoading(false);
            }
        );
    };

    // Simplified submit handler
    const handleSubmit = (e: React.FormEvent) => {
        e.preventDefault();
        setSubmitLoading(true);
        setError('');

        try {
            // Create the alert data directly from the form state
            const alertData = {
                id: '',
                createdAt: '',
                message: formData.message,
                severity: formData.severity,
                location: {
                    type: 'Point',
                    coordinates: formData.location.coordinates // Uses manually entered or geolocated coords
                }
            };

            // onSubmit is the handleCreateAlert function from the parent
            onSubmit(alertData);
            // The parent component (AdminDashboard) will handle closing the modal

        } catch (err) {
            setError('Failed to create alert. Please try again.');
        } finally {
            setSubmitLoading(false);
        }
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

                        {/* --- NEW LOCATION INPUTS --- */}
                        <div className="space-y-2">
                            <label className="text-sm font-medium">Location</label>
                            <div className="flex space-x-2">
                                <div className="flex-1 space-y-1">
                                    <label htmlFor="longitude" className="text-xs text-muted-foreground">Longitude</label>
                                    <Input
                                        type="number"
                                        id="longitude"
                                        name="longitude"
                                        value={formData.location.coordinates[0]}
                                        onChange={handleChange}
                                        placeholder="e.g., -74.0060"
                                        step="any" // Allows for decimal values
                                    />
                                </div>
                                <div className="flex-1 space-y-1">
                                    <label htmlFor="latitude" className="text-xs text-muted-foreground">Latitude</label>
                                    <Input
                                        type="number"
                                        id="latitude"
                                        name="latitude"
                                        value={formData.location.coordinates[1]}
                                        onChange={handleChange}
                                        placeholder="e.g., 40.7128"
                                        step="any"
                                    />
                                </div>
                            </div>
                        </div>
                        {/* --- END NEW SECTION --- */}

                        <div className="flex space-x-2">
                            <Button type="button" variant="outline" onClick={onClose} className="flex-1">
                                Cancel
                            </Button>
                            <Button type="submit" disabled={submitLoading} className="flex-1">
                                {submitLoading ? <Loader2 className="h-4 w-4 animate-spin" /> : 'Create Alert'}
                            </Button>
                        </div>
                    </form>
                </CardContent>
            </Card>
        </div>
    );
};