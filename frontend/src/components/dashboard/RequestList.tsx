import React from 'react';
import { Card, CardContent, CardDescription, CardHeader, CardTitle } from '../ui/card';
import { Button } from '../ui/button';
import { Badge } from '../ui/badge';
import { MapPin, Clock, User, CheckCircle } from 'lucide-react';
import { ResourceRequest } from '../../types/request';

interface RequestListProps {
  requests: ResourceRequest[];
  showAssignButton?: boolean;
  onAssign?: (requestId: string) => void;
}

export const RequestList: React.FC<RequestListProps> = ({ 
  requests, 
  showAssignButton = false, 
  onAssign 
}) => {
  const getStatusColor = (status: string) => {
    switch (status) {
      case 'pending':
        return 'bg-yellow-100 text-yellow-800 border-yellow-200';
      case 'assigned':
        return 'bg-blue-100 text-blue-800 border-blue-200';
      case 'completed':
        return 'bg-green-100 text-green-800 border-green-200';
      default:
        return 'bg-gray-100 text-gray-800 border-gray-200';
    }
  };

  const getTypeIcon = (type: string) => {
    switch (type) {
      case 'food':
        return '🍽️';
      case 'water':
        return '💧';
      case 'shelter':
        return '🏠';
      case 'medical':
        return '🏥';
      default:
        return '📦';
    }
  };

  if (requests.length === 0) {
    return (
      <Card>
        <CardContent className="flex flex-col items-center justify-center py-8">
          <CheckCircle className="h-12 w-12 text-gray-400 mb-4" />
          <p className="text-gray-500 text-center">
            No requests found
          </p>
        </CardContent>
      </Card>
    );
  }

  return (
    <div className="space-y-4">
      {requests.map((request) => (
        <Card className="border-l-4 border-l-green-500">
          <CardHeader className="pb-3">
            <div className="flex items-start justify-between">
              <div className="flex items-center space-x-2">
                <span className="text-2xl">{getTypeIcon(request.type)}</span>
                <div>
                  <CardTitle className="text-lg capitalize">{request.type}</CardTitle>
                  <CardDescription className="mt-1">{request.description}</CardDescription>
                </div>
              </div>
              <div className="flex items-center space-x-2">
                <Badge className={getStatusColor(request.status)}>
                  {request.status.toUpperCase()}
                </Badge>
                {showAssignButton && request.status === 'pending' && (
                  <Button 
                    size="sm" 
                    // onClick={() => onAssign?.(request.id)}
                  >
                    Assign to Me
                  </Button>
                )}
              </div>
            </div>
          </CardHeader>
          <CardContent className="pt-0">
            <div className="flex items-center space-x-4 text-sm text-gray-600">
              <div className="flex items-center space-x-1">
                <MapPin className="h-4 w-4" />
                <span>
                  {request.location.coordinates[1].toFixed(4)}, {request.location.coordinates[0].toFixed(4)}
                </span>
              </div>
              {/* {request.assignedVolunteerId && (
                <div className="flex items-center space-x-1">
                  <User className="h-4 w-4" />
                  <span>Assigned to Volunteer #{request.assignedVolunteerId}</span>
                </div>
              )} */}
            </div>
          </CardContent>
        </Card>
      ))}
    </div>
  );
};
